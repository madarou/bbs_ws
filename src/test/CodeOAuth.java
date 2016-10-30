package test;

import weibo4j.Account;
import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.http.AccessToken;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;
public class CodeOAuth {

	/**
	 * @param args
	 * @throws WeiboException 
	 * @throws JSONException 
	 */
	public static void main(String[] args) throws WeiboException, JSONException {
		// TODO Auto-generated method stub
		
		Oauth oauth2 = new Oauth();
		AccessToken accessTokenObj = oauth2.getAccessTokenByCode("8a7bf687d9e2440342f45a954bccb888") ;
		String accessToken =accessTokenObj.getAccessToken();
		oauth2.setToken(accessToken);
		
		Account account = new Account() ;
        account.client.setToken(accessToken) ;
        JSONObject uidJson = account.getUid() ;
        String uid = uidJson.getString("uid") ;
       
        Users users = new Users() ;
        users.client.setToken(accessToken) ;
        User weiboUser = users.showUserById(uid) ;
        String username = weiboUser.getName() ;
        String screenName = weiboUser.getScreenName() ;
        System.out.println(uid);
        System.out.println(username);
        System.out.println(screenName);
        System.out.println(        weiboUser.getAvatarLarge());
	}

}
