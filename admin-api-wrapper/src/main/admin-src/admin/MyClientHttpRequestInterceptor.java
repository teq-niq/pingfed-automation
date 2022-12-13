package admin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;

import com.example.pingfedadmin.invoker.CustomInstantDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;


public class MyClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	private ObjectMapper mapper= buildObjectMapper();
	JsonTransformer requestProcessor= new JsonTransformer();
	JsonTransformer responseProcessor= new JsonTransformer();

	private ObjectMapper buildObjectMapper() {
		
		 ObjectMapper objectMapper = new ObjectMapper();

		 ThreeTenModule module = new ThreeTenModule();
		 module.addDeserializer(OffsetDateTime.class, CustomInstantDeserializer.OFFSET_DATE_TIME);
		 objectMapper.registerModule(module);
		return objectMapper;
	}


	public void setResponseTransformBeans(TransformBean... transformBeans) {
		this.responseProcessor.setTransformBeans(transformBeans);
		
	}
	
	public void setRequestTransformBeans(TransformBean... transformBeans) {
		this.requestProcessor.setTransformBeans(transformBeans);
		
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		System.out.println("req="+new String(body));
		JsonNode requestBody = mapper.readTree(body);
		if(requestBody!=null)
		{
			requestProcessor.process(requestBody);
		}
		

		 ClientHttpResponse actual = execution.execute(request, mapper.writeValueAsBytes(requestBody));
		 InputStream responseBodyAsStream = actual.getBody();
		 JsonNode responseBody =  mapper.readTree(responseBodyAsStream);
		 ClientHttpResponse response=null;
		 if(responseBody!=null)
		 {
			 responseProcessor.process(responseBody);

			 
				
				response= newResponse(actual, responseBody);
		 }
		


		
		return response;
	}


	private ClientHttpResponse newResponse(ClientHttpResponse actual, JsonNode responseBody) {
		return new ClientHttpResponse() {

			@Override
			public InputStream getBody() throws IOException {
				
				return new ByteArrayInputStream(mapper.writeValueAsBytes(responseBody));
			}

			@Override
			public HttpHeaders getHeaders() {
				
				return actual.getHeaders();
			}

			@Override
			public HttpStatus getStatusCode() throws IOException {
				
				return actual.getStatusCode();
			}

			@Override
			public int getRawStatusCode() throws IOException {
				
				return actual.getRawStatusCode();
			}

			@Override
			public String getStatusText() throws IOException {
				
				return actual.getStatusText();
			}

			@Override
			public void close() {
				
				
			}};
	}

	
	

}




