package com.example.util;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import com.example.config.CurrentSettings;
import com.example.config.Settings;
import com.example.oidc.principal.OidcPrincipal;
import com.example.oidc.principal.accesstoken.AccessTokenData;
import com.example.oidc.principal.accesstoken.AccessTokenHeader;
import com.example.oidc.principal.accesstoken.AccessTokenPayload;
import com.example.oidc.principal.idtoken.IdTokenData;
import com.example.oidc.principal.idtoken.IdTokenHeader;
import com.example.oidc.principal.idtoken.IdTokenPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/*
 * Only checking the signature
 * Not checking claims or expiration.
 * Enhance accordingly if needed
 * 
 * No optimisations to avoid hitting jwksUriEndpoint each time
 */
public class AccessTokenVerifier {
	private final static Logger logger = Logger.getLogger(AccessTokenVerifier.class.getName());
	private static final Settings[] settingsArr=CurrentSettings.authorizationCodeDefaultSettings;
	/*
	 * This method needs enhancement. Its should cache jwks and introspection if
	 * used
	 * 
	 */

	public boolean verifyTokensUsingJwks(OidcPrincipal principal) throws Exception {
		logger.log(Level.FINE, "****verifying");
		boolean verifiedAccessToken = false;
		Settings settings = settingsArr[principal.getSettingsIndex()];
		AccessTokenData accessTokenData = principal.getAccessTokenData();
		if (accessTokenData != null) {
				verifiedAccessToken = verifyAccessTokenUsingJwks(settings.getJwksUriEndpoint(), accessTokenData);

		}
		boolean verifiedIdToken = false;
		IdTokenData idTokenData = principal.getIdTokenData();
		if (idTokenData != null) {
			verifiedIdToken = verifyIdTokenUsingJwks(settings.getJwksUriEndpoint(), idTokenData);

		}

		logger.log(Level.FINE, "*****verifiedAccessToken=" + verifiedAccessToken+",verifiedIdToken="+verifiedIdToken);
		return verifiedAccessToken && verifiedIdToken;
	}

	public boolean verifyAccessTokenUsingJwks(String jwksUriEndpoint, AccessTokenData accessTokenData)
			throws JsonProcessingException, JsonMappingException, KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, IOException {
		boolean verified = false;
		if (jwksUriEndpoint != null && accessTokenData != null && accessTokenData.getRaw()!=null) {
			
			AccessTokenPayload payload = accessTokenData.getPayload();
			if(payload.getExp()!=null)
			{
				//exp is always expected
				if(payload.getExp()*1000>System.currentTimeMillis())
				{
					//not yet expired
					AccessTokenHeader header = accessTokenData.getHeader();
					if(header!=null)
					{
						// we are testing relying on kid alone. ignoring algInHeader for now
						verified = confirmed(header.getKid(), accessTokenData.getRaw(), jwksUriEndpoint);
					}
					else
					{
						//till we use opaque its false
					}

					
				}
				else
				{
					//its not verified even without using jwks
				}
			}
			else
			{
				//cant use exp
				//till we use opaque its false
			}
			
			
		}
		return verified;
	}
	
	public boolean verifyIdTokenUsingJwks(String jwksUriEndpoint, IdTokenData tokenData)
			throws JsonProcessingException, JsonMappingException, KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, IOException {
		boolean verified = false;
		if(tokenData.getRaw()!=null)
		{
			if (jwksUriEndpoint != null && tokenData != null ) {
				
				IdTokenPayload payload = tokenData.getPayload();
				if(payload.getExp()!=null)
				{
					//exp is always expected
					if(payload.getExp()*1000>System.currentTimeMillis())
					{
						//not yet expired
						IdTokenHeader header = tokenData.getHeader();
						if(header!=null)
						{
							// we are testing relying on kid alone. ignoring algInHeader for now
							verified = confirmed(header.getKid(), tokenData.getRaw(), jwksUriEndpoint);
						}
						else
						{
							//treat as false
						}

						
					}
					else
					{
						//its not verified even without using jwks
					}
				}
				
				
			}
			else
			{
				verified=true;//means didnt get an id token thats all
			}
		}

		return verified;
	}

	public boolean verifyAccessTokenUsingJwks(String jwksUriEndpoint, String accessToken)
			throws JsonProcessingException, JsonMappingException, KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, IOException {
		boolean verified = false;
		if (jwksUriEndpoint != null && accessToken != null) {
			String decodedPayloadString = JwtAccessTokenUtil.getTokenDecodedPayload(accessToken);
			ObjectNode decodedPayloadAsObjectNode = ObjectMapperHolder.mapper.readValue(decodedPayloadString,
					ObjectNode.class);
			Long exp = JsonUtils.getLongFieldFromObjectNode(decodedPayloadAsObjectNode, "exp");
			//exp is always expected
			if( exp*1000>System.currentTimeMillis())
			{
				String decodedHeaderString = JwtAccessTokenUtil.getTokenDecodedHeader(accessToken);
				ObjectNode decodedHedaerAsObjectNode = ObjectMapperHolder.mapper.readValue(decodedHeaderString,
						ObjectNode.class);
				String algInHeader = JsonUtils.getTextFieldFromObjectNode(decodedHedaerAsObjectNode, "alg");
				String kidInHeader = JsonUtils.getTextFieldFromObjectNode(decodedHedaerAsObjectNode, "kid");
				// we are testing relying on kid alone. ignoring algInHeader for now
				verified = confirmed(kidInHeader, accessToken, jwksUriEndpoint);
			}
			
		}
		return verified;
	}

	private boolean confirmed(String kidInHedaer, String accessToken, String jwksUriEndpoint)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {

		HttpGet request = new HttpGet(jwksUriEndpoint);
		CloseableHttpClient client = ClientBuilder.buildClient();
		boolean confirmed = client.execute(request, response -> {
			return confirmInternal(kidInHedaer, accessToken, response);
		});
		return confirmed;
	}

	private Boolean confirmInternal(String kidInHedaer, String accessToken, ClassicHttpResponse response)
			throws IOException, ParseException, JsonProcessingException, JsonMappingException {
		boolean verified = false;
		int statusCode = response.getCode();
		logger.log(Level.FINE, "statusCode=" + statusCode);
		HttpEntity entity = response.getEntity();
		String string = EntityUtils.toString(entity);
		logger.log(Level.FINE, "jwkscontent=" + string);

		ObjectNode readValue = ObjectMapperHolder.mapper.readValue(string, ObjectNode.class);
		ArrayNode keys = (ArrayNode) readValue.get("keys");
		for (int i = 0, size = keys.size(); i < size; i++) {
			ObjectNode jwk = (ObjectNode) keys.get(i);
			String kty = JsonUtils.getTextFieldFromObjectNode(jwk, "kty");// RSA or EC
			String kid = JsonUtils.getTextFieldFromObjectNode(jwk, "kid");
			String use = JsonUtils.getTextFieldFromObjectNode(jwk, "use");
			String n = JsonUtils.getTextFieldFromObjectNode(jwk, "n");
			String e = JsonUtils.getTextFieldFromObjectNode(jwk, "e");
			if (kid.equals(kidInHedaer)) {
				// jwk can be cached if we have a strategy to cache it
				// not checking alg for now with algInHeder
				try {
					verified = verify(e, n, accessToken);
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
				break;
			}
		}
		return verified;
	}

	private static boolean verify(String eOrExponent, String nOrModulus, String jwt) throws Exception {
		String signedData = jwt.substring(0, jwt.lastIndexOf("."));
		String signatureB64u = jwt.substring(jwt.lastIndexOf(".") + 1, jwt.length());
		byte signature[] = Base64.getUrlDecoder().decode(signatureB64u);
		PublicKey pubKey = getPblicKey(eOrExponent, nOrModulus);
		Signature sig = Signature.getInstance("SHA256withRSA");
		sig.initVerify(pubKey);
		sig.update(signedData.getBytes());
		boolean isVerified = sig.verify(signature);
		return isVerified;
	}

	private static PublicKey getPblicKey(String eOrExponent, String nOrModulus) throws Exception {
		Decoder decoder = Base64.getUrlDecoder();
		byte[] expBytes = decoder.decode(eOrExponent);
		byte[] modBytes = decoder.decode(nOrModulus);

		BigInteger modules = new BigInteger(1, modBytes);
		BigInteger exponent = new BigInteger(1, expBytes);
		KeyFactory factory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modules, exponent);
		PublicKey pubKey = factory.generatePublic(pubSpec);
		return pubKey;
	}

}
