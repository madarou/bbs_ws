package com.bbs.util;

public class UnSubscribeUrl {
	
	public static String gen(String servername,long userid,String type){
		String data=userid+";"+type;
		try {
			String senddata=Encrypyt.en(data);
			return servername+"/rest/subscribe/unsubscribe?data="+senddata;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(UnSubscribeUrl.gen("http://localhost/BBS",222074709, "renren"));
	}

}
