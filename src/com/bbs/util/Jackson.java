package com.bbs.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Jackson{
	private static ObjectMapper objectMapper = new ObjectMapper();
	public  static Object  jsonToObject(String json,Class type) throws JsonParseException, JsonMappingException, IOException{
		return objectMapper.readValue(json,type);
	}

}
