package com.example.misc.beans;

public class Response {
	private final int statusCode;
	private final String response;
	public Response(int statusCode, String response) {
		super();
		this.statusCode = statusCode;
		this.response = response;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public String getResponse() {
		return response;
	}
	

}
