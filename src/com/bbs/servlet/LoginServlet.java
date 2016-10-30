package com.bbs.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weibo4j.Oauth;
import weibo4j.model.WeiboException;

import com.bbs.util.Constants;
import com.bbs.util.Encrypyt;
import com.bbs.util.RenrenConfig;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//检测是否登陆或者是否有登陆cookies
		String logintype=request.getParameter("type");
		String page=request.getParameter("page");
		String jobid=request.getParameter("jobid");
		String para="";
		if(page!=null){
			para="page="+page;
		}
		if(jobid!=null){
			if(para.equals("")){
				para="jobid="+jobid;
			}
			else{
				para+="&jobid="+jobid;
			}
		}
		request.getSession().setAttribute("para", para);
		Object user=request.getSession().getAttribute("user");
		if(user!=null){
			if(!para.equals("")){
				response.sendRedirect("index.html?"+para);
			}
			else{
				response.sendRedirect("index.html");
			}		
			return;
		}
		
		Cookie[] cookies=request.getCookies();
		if(cookies!=null){
			for(Cookie c:cookies){
				if(c.getName().equals("id")){
					if(c.getValue()!=null){
						if(!para.equals("")){
							response.sendRedirect("index.html?"+para);
						}
						else{
							response.sendRedirect("index.html");
						}
						return;
					}
					
				}
			}
		}
		

		if(logintype==null||logintype.equals("null")){
			response.sendRedirect("error.html");
		}
		else if(logintype.equals(Constants.LoginType.Type_Renren.toString())){
			String url="https://graph.renren.com/oauth/authorize?client_id=%s&response_type=code&scope=publish_feed&redirect_uri=%s&display=page";
			response.sendRedirect(String.format(url, RenrenConfig.configure.APP_ID,URLEncoder.encode(RenrenConfig.configure.REDIRECT_URI, "UTF-8")));
		}
		else if(logintype.equals(Constants.LoginType.Type_Weibo.toString())){
			Oauth oauth = new Oauth();
			try {
				String redirectUrl = oauth.authorize("code","","");
				response.sendRedirect(redirectUrl);
			} catch (WeiboException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

}
