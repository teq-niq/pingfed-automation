package com.example.util;

import java.net.URLEncoder;

public class Encoder {
	public static String encode(String string) {
		//return URLEncoder.encode(string, Charset.defaultCharset());
		return URLEncoder.encode(string);
	//	return string;
	}

}
