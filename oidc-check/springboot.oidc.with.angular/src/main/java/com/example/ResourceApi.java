package com.example;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/*
 * All our resource api is internal.
 * We have avoided external resource server
 */
@RestController
public class ResourceApi {
	@RequestMapping(path = "/secured", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> secured()
	{
		Map<String,Object> data= new HashMap<>();
		data.put("sensitivedata", true);
		return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/unsecured", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> unsecured()
	{
		Map<String,Object> data= new HashMap<>();
		data.put("normaldata", true);
		return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
	}

}
