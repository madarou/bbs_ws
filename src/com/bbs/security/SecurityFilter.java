package com.bbs.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SecurityFilter implements Filter {
	private List<Authority> securityList;
	private String projectname;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
  
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest)request;
		HttpSession session = req.getSession();
		String uriString=req.getRequestURI();
		//System.out.println(uriString);
		Set<String> roles=new HashSet();
		for(Authority au:securityList){
			if(uriString.startsWith(au.getUri())){
				roles.addAll(au.getAuth());
			}
		}
		if(roles.size()==0||uriString.equals(projectname)||uriString.equals(projectname+"/")){//默认权限，无限制
			chain.doFilter( request,response );  
		}
		else{
			String realrole=objtostr(session.getAttribute("role"));

			if(roles.contains(realrole)){
				chain.doFilter( request,response ); 
			}
			else{
				((HttpServletResponse)response).sendRedirect(projectname+"/login.html");
			}
		} 
	}

	@Override
	public void init(FilterConfig conf) throws ServletException {
		// TODO Auto-generated method stub
		projectname=conf.getInitParameter("projectname");
		securityList=LoadAuthority.LoadSecurity(projectname);
		 
	}
	private String objtostr(Object obj){
		return obj==null?"":obj.toString();
	}

}
