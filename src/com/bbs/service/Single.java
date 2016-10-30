package com.bbs.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bbs.mapper.JobsMapper;
import com.bbs.mapper.SingleMapper;

@Path("/single")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Single {
	private @Context ServletContext context;
	private @Context HttpServletResponse response;
	@Autowired
    private SingleMapper singleDao;
	
	@GET
	@Path("getlist")
	public Map getSingleList(@QueryParam("start")int start,@QueryParam("end")int end,@QueryParam("source") String source){
		Map<String,Object> para=new HashMap();
		Map<String,Object> reMap=new HashMap();
		para.put("start", start);
		para.put("end", (end-start));
		para.put("source", source);
		reMap.put("result", singleDao.getSingleList(para));
		reMap.putAll(singleDao.getSingleCnt(para));
		return reMap;
	}
	
	@GET
	@Path("getlistwithcontent")
	public List getSingleListWithContent(@QueryParam("start")int start,@QueryParam("end")int end,@QueryParam("source") String source){
		Map<String,Object> para=new HashMap();
		para.put("start", start);
		para.put("end", (end-start));
		para.put("source", source);
		List<Map> singlelist=singleDao.getSingleListWithContent(para);
		
		
		for(Map single:singlelist){
			if(single.get("pic_tag").toString().equals("Y")){
				single.put("piclist", singleDao.getSinglePicAddr(single));
			}
		}
		return singlelist;
	}
	@GET
	@Path("img/{source}/{id}/{picid}")
	@Produces("image/jpg")
	public Response getImage(@PathParam("id") String id,@PathParam("source") String source,@PathParam("picid") String picid){

		Map<String,String> para=new HashMap();
		para.put("id", id);
		para.put("source", source);
		para.put("picid", picid);
		Map picmap=singleDao.getSinglePic(para);
		if(picmap==null){
			return Response.noContent().build();
		}
		else{
			String filename=System.currentTimeMillis()+".jpg";
			return Response.ok(picmap.get("content")).header("Content-Disposition", "attachment;filename="+filename).build();
		}
		
	}
	
	@GET
	@Path("getMessage")
	public Map getMessage(@QueryParam("id") String id,@QueryParam("source") String source){

		Map<String,String> para=new HashMap();
		para.put("id", id);
		para.put("source", source);
		return singleDao.getMessage(para);
	}

	@GET
	@Path("getHtml")
	@Produces(MediaType.TEXT_HTML)
	public String getHtml(@QueryParam("id") String id,@QueryParam("source") String source){

		Map<String,String> para=new HashMap();
		para.put("id", id);
		para.put("source", source);
		return (String)singleDao.getMessage(para).get("content");
	}
	
	@GET
	@Path("search")
	public List search(@QueryParam("keywordlist") String keywordlist){
		return singleDao.search(Arrays.asList(keywordlist.split(",")));
	}
}
