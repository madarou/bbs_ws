package com.bbs.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bbs.bean.User;
import com.bbs.mapper.UserMapper;
import com.bbs.util.Constants;
import com.bbs.util.Encrypyt;
import com.bbs.util.RenrenConfig;
import com.renren.api.AuthorizationException;
import com.renren.api.RennClient;
import com.renren.api.client.RenrenApiClient;
import com.renren.api.client.RenrenApiConfig;
import com.renren.api.client.utils.HttpURLUtils;
import com.renren.api.service.Image;

/**
 * Servlet implementation class RenrenLoginServlet
 */
@WebServlet("/rr_login")
public class RenrenLoginServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RenrenLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String code = request.getParameter("code");
		if (code == null || code.length() == 0) {
			//缺乏有效参数，跳转到登录页去
			response.sendRedirect("/login?type=null");
			return;
		}
		//到人人网的OAuth 2.0的token endpoint用code换取access token
		String rrOAuthTokenEndpoint = "https://graph.renren.com/oauth/token";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("client_id", RenrenConfig.configure.API_KEY);
		parameters.put("client_secret", RenrenConfig.configure.APP_SECRET);
		parameters.put("redirect_uri", RenrenConfig.configure.REDIRECT_URI);
                              //这个redirect_uri要和之前传给authorization endpoint的值一样
		parameters.put("grant_type", "authorization_code");
		parameters.put("code", code);
		String tokenResult = HttpURLUtils.doPost(rrOAuthTokenEndpoint, parameters);
		ObjectMapper objectMapper = new ObjectMapper();
		Map tokenJson=objectMapper.readValue(tokenResult, Map.class);
		if (tokenJson != null) {
			String accessToken = (String) tokenJson.get("access_token");
			Long expiresIn = Long.parseLong(tokenJson.get("expires_in").toString());//距离过期时的时间段（秒数）
			long currentTime = System.currentTimeMillis() / 1000;
			long expiresTime = currentTime + expiresIn;//即将过期的时间点（秒数）
			Map usermap=(Map)tokenJson.get("user");
			long rrUid = Long.parseLong(usermap.get("id").toString());
			String name=(String)usermap.get("name");
			List<Map> avatars=(List)usermap.get("avatar");
			//调用人人网API获得用户信息
			
			//RennClient client = new RennClient(RenrenConfig.configure.API_KEY, RenrenConfig.configure.APP_SECRET);
			try {
				//client.authorizeWithAccessToken(accessToken);
				//client.getUserService().getUserLogin().getAvatar();
				ApplicationContext applicationcontext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
				UserMapper userDao=(UserMapper)applicationcontext.getBean("userDao");
				Map<String, Object> para = new HashMap<>();
				para.put("uid", rrUid);
				para.put("type", Constants.LoginType.Type_Renren.toString());
				User user = userDao.getUserInfo(para);
				//User user;
				if (user == null) {
					//在帐号关联表里没有记录，用户是第一次来；为这个用户创建一个User对象
					System.out.println("New renren user");
					User newUser = new User();
					newUser.setUsername(name);
					newUser.setHeadurl(avatars.get(avatars.size()-1).get("url").toString());
					newUser.setUid(rrUid);
					newUser.setType(Constants.LoginType.Type_Renren.toString());
					newUser.setAccesstoken(accessToken);
					userDao.addUser(newUser);
					user=newUser;
				}
				
				//将用户身份信息保存在会话里
				request.getSession().setAttribute("user", user);
				
				Cookie cookie = new Cookie("id",Encrypyt.en(String.valueOf(rrUid) + "," +
						Constants.LoginType.Type_Renren.toString()));   
				cookie.setMaxAge(100*24*60*60);   
				cookie.setPath("/");
				response.addCookie(cookie);
				//已登录，跳转到个人主页
				Object urlpara=request.getSession().getAttribute("para");
				if(urlpara!=null&&!urlpara.equals("")){
					response.sendRedirect("index.html?"+urlpara);
				}
				else{
					response.sendRedirect("index.html");
				}
				return;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		response.sendRedirect("login?type=renren");
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
