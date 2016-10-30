package com.bbs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class RenrenConfig {
	public String APP_ID;
	public String API_KEY;
	public String APP_SECRET;
	public String REDIRECT_URI;
	public static RenrenConfig configure=new RenrenConfig();
	private RenrenConfig(){
		InputStream inputstream=null;
		Properties p=null;
		try {
			inputstream = Thread.currentThread().getContextClassLoader().getResourceAsStream("renrenconfig.properties");
			p = new Properties();
			p.load(inputstream);
		
			APP_ID=objtostr(p.get("APP_ID"));
			API_KEY=objtostr(p.get("API_KEY"));
			APP_SECRET=objtostr(p.get("APP_SECRET"));
			REDIRECT_URI=objtostr(p.get("REDIRECT_URI"));
			
			System.out.println("APP_ID:"+APP_ID);
			System.out.println("API_KEY:"+API_KEY);
			System.out.println("APP_SECRET:"+APP_SECRET);
			System.out.println("REDIRECT_URI:"+REDIRECT_URI);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(inputstream!=null)
				    inputstream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}
	public static String objtostr(Object obj){
		return obj==null?"":obj.toString();
	}
	

}
