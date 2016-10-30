package weibo4j.util;

public class Constants {
	public enum JobType{
		FULL,PART,BOTH
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
