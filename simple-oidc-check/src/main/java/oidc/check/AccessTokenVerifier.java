package oidc.check;

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

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import oidc.check.others.ClientBuilder;

public class AccessTokenVerifier {
	
	private  final String jwksUriEndpoint;
	
	public AccessTokenVerifier(String jwksUriEndpoint) {
		super();
		this.jwksUriEndpoint = jwksUriEndpoint;
	}

	

	public boolean verifyAccessToken(String accessToken) throws Exception{
		 
		ObjectMapper mapper= new ObjectMapper();
		System.out.println("accessToken="+accessToken);
		int firstDotPos = accessToken.indexOf('.');
		String header=accessToken.substring(0, firstDotPos);
		
		byte[] decoded = java.util.Base64.getUrlDecoder().decode(header);
		String decodedHeader = new String(decoded);
		System.out.println("decoded header="+decodedHeader);
		 ObjectNode decodedHeaderAsObjectNode = mapper.readValue(decodedHeader, ObjectNode.class);
		 String algInHedaer = getTextFieldFromObjectNode(decodedHeaderAsObjectNode, "alg");
		 String kidInHedaer = getTextFieldFromObjectNode(decodedHeaderAsObjectNode, "kid");
		 boolean  verified=false;
		 verified=confirmed( mapper, kidInHedaer, accessToken);
		
		
		return verified;
	}
	
	private boolean confirmed(ObjectMapper mapper, String kidInHedaer, String accessToken)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
		
		HttpGet request = new HttpGet(jwksUriEndpoint);
		 CloseableHttpClient client = ClientBuilder.buildClient();
		boolean confirmed=client.execute(request, response -> {
			return confirmInternal(mapper, kidInHedaer, accessToken, response);
		});
		return confirmed;
	}

	private Boolean confirmInternal(ObjectMapper mapper, String kidInHedaer, String accessToken,
			ClassicHttpResponse response)
			throws IOException, ParseException, JsonProcessingException, JsonMappingException {
		boolean verified=false;
		int statusCode = response.getCode();
		System.out.println("statusCode="+statusCode);
		HttpEntity entity = response.getEntity();
		String string = EntityUtils.toString(entity);
		System.out.println("jwkscontent="+string);

		ObjectNode readValue = mapper.readValue(string, ObjectNode.class);
		ArrayNode keys = (ArrayNode) readValue.get("keys");
		for (int i = 0, size=keys.size(); i < size; i++) {
			ObjectNode jwk = (ObjectNode) keys.get(i);
			String kty = getTextFieldFromObjectNode(jwk, "kty");//RSA or EC
			String kid = getTextFieldFromObjectNode(jwk, "kid");
			String use = getTextFieldFromObjectNode(jwk, "use");
			String n = getTextFieldFromObjectNode(jwk, "n");
			String e = getTextFieldFromObjectNode(jwk, "e");
			if(kid.equals(kidInHedaer))
			{
				//not checking alg for now with algInHeder
				try {
					verified=verify(e, n, accessToken);
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
				break;
			}
		}
		return verified;
	}
	
	private static boolean verify(String eOrExponent, String nOrModulus, String jwt)
			throws Exception {
		String signedData = jwt.substring(0, jwt.lastIndexOf("."));
		String signatureB64u = jwt.substring(jwt.lastIndexOf(".")+1,jwt.length());
		byte signature[] = Base64.getUrlDecoder().decode(signatureB64u);
		PublicKey pubKey = getPblicKey(eOrExponent, nOrModulus);
		Signature sig = Signature.getInstance("SHA256withRSA");
		sig.initVerify(pubKey);
		sig.update(signedData.getBytes());
		boolean isVerified = sig.verify(signature);
		return isVerified;
	}
	
	private static PublicKey getPblicKey(String eOrExponent, String nOrModulus)
			throws Exception {
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
	
	private String getTextFieldFromObjectNode(ObjectNode jwk, String fieldName) {
		String ret=null;
		if(jwk!=null)
		{
			JsonNode val = jwk.get(fieldName);
			if(val!=null)
			{
				ret=val.asText();
			}
		}
		return ret;
	}

}
