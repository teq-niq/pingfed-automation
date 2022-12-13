package admin;

import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public class JsonTransformer {

	private TransformBean[] transformBeans;

	public void setTransformBeans(TransformBean... transformBeans) {
		this.transformBeans = transformBeans;

	}

	public void process(JsonNode responseBody) {
		if(transformBeans!=null)
		{
			for (TransformBean transformBean : transformBeans) {
				// why waste processing if its null
				if (transformBean.getResponseTypeTransformer() != null) {
					String xPath = transformBean.getxPath();
					// xPath=xPath.replaceAll("\\", "/");
					if (xPath.startsWith("/") || xPath.endsWith("/")) {
						throw new RuntimeException("found invalid xpath:" + xPath + " (showuld not start or end with /)");
					}
					JsonNode currentParentNode = responseBody;
					String[] xPathArray = xPath.split("/");

					drill(currentParentNode, xPathArray, transformBean.getResponseTypeTransformer());
				}

			}
		}
		
	}

	private void drill(JsonNode currentParentNode, String[] xpathArray,
			Function<String, String> responseTypeTransformer) {
		if (currentParentNode != null) {
			if(xpathArray.length>=1)
			{
				String xPathNode=xpathArray[0];
				if (xPathNode.equals("*")) {
					processStarXpath(currentParentNode, xpathArray, responseTypeTransformer);
				} else {
					processNonStarXPath(currentParentNode, xpathArray, responseTypeTransformer, xPathNode);

				}
			}
		
		}

	}

	private void processNonStarXPath(JsonNode currentParentNode, String[] xpathArray,
			Function<String, String> responseTypeTransformer, String xPathNode) {
		JsonNode childNode = currentParentNode.get(xPathNode);
		if (childNode != null) {
			if (childNode instanceof ValueNode && (!(childNode instanceof POJONode))) {
				// no need to drill more
				// just change the node val here if the xpath is over
				if (xpathArray.length == 1) {
					// as of now we are only considerng text nodes
					if (childNode instanceof TextNode) {
						TextNode textNode = (TextNode) childNode;

						String textNodeText = textNode.asText();
						if (responseTypeTransformer != null) {
							if (currentParentNode instanceof ObjectNode) {
								ObjectNode currentParentObjectNode = (ObjectNode) currentParentNode;
								String modified = responseTypeTransformer.apply(textNodeText);
								currentParentObjectNode.put(xPathNode, modified);

							} else {
								throw new RuntimeException(
										"unexpected type of " + currentParentNode.getClass().getName());
							}

						}

					}
				}
			} else if (childNode instanceof ArrayNode||childNode instanceof ObjectNode) {

				String[] newXpathArray = new String[xpathArray.length - 1];
				System.arraycopy(xpathArray, 1, newXpathArray, 0, newXpathArray.length);
				// use childNode todrill more
				drill(childNode, newXpathArray, responseTypeTransformer);

			}

			
			else
			{
				throw new RuntimeException("not implemented for "+childNode.getClass().getName());
			}
		}
	}

	private void processStarXpath(JsonNode currentParentNode, String[] xpathArray,
			Function<String, String> responseTypeTransformer) {
		if (currentParentNode instanceof ArrayNode) {
			ArrayNode currentParentArrayNode = (ArrayNode) currentParentNode;
			for (int i = 0; i < currentParentArrayNode.size(); i++) {
				JsonNode arrayRow = currentParentArrayNode.get(i);
				if (arrayRow != null) {
					if (arrayRow instanceof ValueNode && (!(arrayRow instanceof POJONode))) {
						// no need to drill more
						// just change the node val here if the xpath is over
						if (xpathArray.length == 1) {
							// as of now we are only considerng text nodes
							if (arrayRow instanceof TextNode) {
								TextNode textNode = (TextNode) arrayRow;

								String textNodeText = textNode.asText();
								if (responseTypeTransformer != null) {

									
									String modified = responseTypeTransformer.apply(textNodeText);
									currentParentArrayNode.set(i, modified);

								}

							}
						}
					} 
					else if (arrayRow instanceof ObjectNode||arrayRow instanceof ArrayNode) {
						//no need to set back in array
						String[] newXpathArray = new String[xpathArray.length - 1];
						System.arraycopy(xpathArray, 1, newXpathArray, 0, newXpathArray.length);
						// use childNode todrill more
						drill(arrayRow, newXpathArray, responseTypeTransformer);
						
					}
					
					else {
						throw new RuntimeException("not implemented for "+arrayRow.getClass().getName());
					}
				}
			}
		} else {
			throw new RuntimeException("* path but not arraynode");
		}
	}

}
