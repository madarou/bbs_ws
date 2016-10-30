package com.bbs.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bbs.bean.User;
import com.bbs.mapper.UserMapper;
import com.bbs.util.Constants;
import com.bbs.util.Encrypyt;

import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;

/**
 * Servlet implementation class WeiboLoginServlet
 */
@WebServlet("/wb_login")
public class WeiboLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeiboLoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code = request.getParameter("code");
		
		if(code == null || code.isEmpty()) {
			String error = request.getParameter("error_description");
			if(error == null || error.isEmpty()) 
				error = request.getParameter("error");
			response.sendRedirect("index.html");
			return;
		}
		
		// Get access token
		Oauth oauth = new Oauth();
		AccessToken token = null;
		try{
			token = oauth.getAccessTokenByCode(code);
		} catch (WeiboException e) {
			if(401 == e.getStatusCode()){
				System.err.println("Unable to get the access token.");
			} else {
				e.printStackTrace();
			}
			response.sendRedirect("index.html");
			return;
		}
		if(token == null) {
			response.sendRedirect("index.html");
			return;
		} else {
			System.out.println(token);
		}
		
		// Get user info
		String accessTokenStr = token.getAccessToken();
		String uidStr = token.getUid();
		Users um = new Users();
		um.client.setToken(accessTokenStr);
		weibo4j.model.User weiboUser = null;
		try {
			weiboUser = um.showUserById(uidStr);
			//Log.logInfo(user.toString());
			System.out.println(weiboUser.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
		if(weiboUser != null) {
			try {
				ApplicationContext applicationcontext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
				UserMapper userDao=(UserMapper)applicationcontext.getBean("userDao");
				Map<String, Object> para = new HashMap<>();
				para.put("uid", uidStr);
				para.put("type", Constants.LoginType.Type_Weibo.toString());
				User user = userDao.getUserInfo(para);
				long uid = 0;
				try {
					uid = Long.parseLong(uidStr);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//User user;
				if (user == null && uid > 0) {
					//在帐号关联表里没有记录，用户是第一次来；为这个用户创建一个User对象
					System.out.println("New weibo user");
					User newUser = new User();
					newUser.setUsername(weiboUser.getName());
					newUser.setHeadurl(weiboUser.getAvatarLarge());
					newUser.setUid(uid);
					newUser.setType(Constants.LoginType.Type_Weibo.toString());
					newUser.setAccesstoken(accessTokenStr);
					userDao.addUser(newUser);
					user=newUser;
				}
				
				//将用户身份信息保存在会话里
				request.getSession().setAttribute("user", user);
				
				Cookie cookie = new Cookie("id",Encrypyt.en(String.valueOf(uidStr) + "," +
						Constants.LoginType.Type_Weibo.toString()));   
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
				e.printStackTrace();
			}
		}
		response.sendRedirect("login?type=weibo");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
