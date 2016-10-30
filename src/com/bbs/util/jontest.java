package com.bbs.util;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class jontest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		String json="{\"token_type\":\"bearer\",\"expires_in\":2593659,\"refresh_token\":\"271771|0.H0KZD1o5DGito86Ua9GkKqGXXK4KSNJn.222074709.1410758046956\",\"user\":{\"id\":222074709,\"name\":\"范乃如\",\"avatar\":[{\"type\":\"avatar\",\"url\":\"http://hdn.xnimg.cn/photos/hdn321/20110508/1505/head_9m0W_94763i019118.jpg\"},{\"type\":\"tiny\",\"url\":\"http://hdn.xnimg.cn/photos/hdn321/20110508/1505/tiny_QSXd_94769p019118.jpg\"},{\"type\":\"main\",\"url\":\"http://hdn.xnimg.cn/photos/hdn321/20110508/1505/main_dC7Q_94763i019118.jpg\"},{\"type\":\"large\",\"url\":\"http://hdn.xnimg.cn/photos/hdn321/20110508/1505/large_2S30_95255o019118.jpg\"}]},\"access_token\":\"271771|6.274c436831d7533dac6e20e7600b2e14.2592000.1413900000-222074709\"}";
		ObjectMapper objectMapper = new ObjectMapper();
		Map obj=objectMapper.readValue(json, Map.class);
		Map usr=(Map)obj.get("user");
		System.out.println(usr.get("name"));
	}

}
