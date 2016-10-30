package com.bbs.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import weibo4j.Oauth;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;

import com.bbs.mapper.UserMapper;
import com.bbs.util.Constants;
import com.bbs.util.Encrypyt;

@Path("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class User {
	@Autowired
    private UserMapper userDao;
	private @Context HttpServletRequest request;
	
	@POST
	@Path("addJobFav")
	public Map addToFavorite(Map para){
		Map statues=new HashMap();
		try{
			Object user=request.getSession().getAttribute("user");
			if(user==null){
				statues.put("statues", Constants.NOTLOGIN);	
				return statues;
			}
			else{
				para.put("uid", ((com.bbs.bean.User)user).getUid());
				userDao.addFavorite(para);
			}
			statues.put("statues", Constants.SUCCESS);
		}catch(Exception e){
			e.printStackTrace();
			statues.put("statues", Constants.FAIL);
		}
		return statues;
	}
	
	@GET
	@Path("getJobFavList")
	public Map getJobFavList(@QueryParam("start") int start,@QueryParam("end") int end){
		Map reMap=new HashMap();
		Map para=new HashMap();
		
		Object user=request.getSession().getAttribute("user");
		reMap.put("statues", Constants.FAIL);
		if(user!=null){
			para.put("start", start);
			para.put("end", (end-start));
			para.put("uid", ((com.bbs.bean.User)user).getUid());

			reMap.put("favlist", userDao.getJobFavList(para));
			reMap.put("statues", Constants.SUCCESS);
			reMap.putAll(userDao.getJobFavCnt(para));
		}
		else{ 
			reMap.put("statues", Constants.NOTLOGIN);
		}
		
		return reMap;
	}
	
	@POST
	@Path("removeJobFav")
	public Map removeJobFav(Map para){
		Map statues=new HashMap();
		try{
			Object user=request.getSession().getAttribute("user");
			if(user==null){
				statues.put("statues", Constants.NOTLOGIN);
				return statues;
			}
			else{
				
				para.put("uid", ((com.bbs.bean.User)user).getUid());
				userDao.removeFavorite(para);
			}
			statues.put("statues", Constants.SUCCESS);
		}catch(Exception e){
			e.printStackTrace();
			statues.put("statues", Constants.FAIL);
		}
		return statues;
	}
	
	@GET
	@Path("getUserInfo")
	public Map getUserInfo() {
		Object user=request.getSession().getAttribute("user");
		if(user==null){
			Cookie[] cookies=request.getCookies();
			if(cookies != null) {
				for(Cookie c:cookies){
					if(c.getName().equals("id")){
						String[] strings = null;
						try {
							strings = Encrypyt.de(c.getValue()).split(",");
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if(strings != null && strings.length >= 2) {
							String id = strings[0];
							String type = strings[1];
							Map para = new HashMap();
							para.put("uid", Long.parseLong(id));
							para.put("type", type);
							user=userDao.getUserInfo(para);
							request.getSession().setAttribute("user", user);
						}

					}
				}
			}
		}
		Map remap=new HashMap();
		if(user!=null){
			com.bbs.bean.User tmpuser=(com.bbs.bean.User)user;
			remap.put("username", tmpuser.getUsername());
			remap.put("headurl", tmpuser.getHeadurl());
			remap.put("uid", tmpuser.getUid());
			remap.put("type", tmpuser.getType());
			remap.put("searchkey", tmpuser.getSearchkey());
		}
		return remap;
	}
	
	@POST
	@Path("updateJobKeys")
	public Map updateJobKeys(Map para){
		Map reMap=new HashMap();
		reMap.put("statues", Constants.FAIL);
		Object user=request.getSession().getAttribute("user");
		if(user==null){
			reMap.put("statues", Constants.NOTLOGIN);
		}
		else{
			para.put("uid", ((com.bbs.bean.User)user).getUid());
			userDao.updatekey(para);
			reMap.put("statues", Constants.SUCCESS);
		}
		return reMap;	
	}
}
