package com.bbs.util;

public class Constants {
	public static final int SUCCESS=1;
	public static final int FAIL=0;
	public static final int NOTLOGIN=-1;
	public static final int TIMEOUT=24*3600*1000;
	public static final int URL_INVALID=-1;
	public static final int URL_SUCCESS=1;
	public static final int URL_TIMEOUT=0;
	public enum JobType{
		FULL,PART,BOTH,ALL
	}
	public enum LoginType{
		Type_Renren("renren"),Type_Weibo("weibo");
		private String type;
		private LoginType(String type){
			this.type=type;
		}
		public String toString(){
			return type;
		}
	}

}
