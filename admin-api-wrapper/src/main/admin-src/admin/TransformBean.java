package admin;

import java.util.function.Function;

public class TransformBean {
	//xpath seperated by /
	//first not having /
	//our xpath doesnt cre about wherher it translates to array or objecrt node
	private String xPath;
	private Function<String, String> responseTypeTransformer;
	public TransformBean() {
		super();
		
	}
	public TransformBean(String xPath, Function<String, String> responseTypeTransformer) {
		super();
		this.xPath = xPath;
		this.responseTypeTransformer = responseTypeTransformer;
	}
	public String getxPath() {
		return xPath;
	}
	public void setxPath(String xPath) {
		this.xPath = xPath;
	}
	public Function<String, String> getResponseTypeTransformer() {
		return responseTypeTransformer;
	}
	public void setResponseTypeTransformer(Function<String, String> responseTypeTransformer) {
		this.responseTypeTransformer = responseTypeTransformer;
	}

}
