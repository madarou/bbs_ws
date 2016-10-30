package com.bbs.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.bbs.bean.QueryResult;
import com.bbs.mapper.JobsMapper;
import com.bbs.mapper.UserMapper;
import com.bbs.util.Constants.JobType;
import com.bbs.util.QueryUtils;

@Path("jobs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Jobs {
	private @Context HttpServletRequest request;
	@Autowired
    private JobsMapper jobsDao;
	@Autowired
    private UserMapper userDao;
	@GET
	@Path("getlist")
	public Map getJobList(@QueryParam("start")int start,@QueryParam("end")int end,@QueryParam("source") String source,@QueryParam("jobtype") String jobtype){
		Map reMap=new HashMap();
		Map<String,Object> para=new HashMap();
		para.put("start", start);
		para.put("end", (end-start));
		para.put("source", source);
		if(jobtype==null||jobtype.equals("null")){
			jobtype=JobType.ALL.name();
		}
		if(JobType.valueOf(jobtype)==JobType.FULL){
			jobtype=JobType.PART.name();
		}
		else if(JobType.valueOf(jobtype)==JobType.PART){
			jobtype=JobType.FULL.name();
		}
		para.put("jobtype", jobtype.toUpperCase());
		Object user=request.getSession().getAttribute("user");
		if(user==null){//not login
			reMap.put("joblist", jobsDao.getJobsList(para));
		}
		else{//login
			para.put("uid", ((com.bbs.bean.User)user).getUid());
			reMap.put("joblist", jobsDao.getUserJobsList(para));
		}
		
		reMap.putAll(jobsDao.getJobsCnt(para));
		return reMap;
	}
	
	@GET
	@Path("getcontent")
	public Map getJobContent(@QueryParam("source") String source,@QueryParam("id") String id){
		Map<String,Object> para=new HashMap();
		para.put("source", source);
		para.put("id", id);
		return jobsDao.getJobDetail(para);
	}
	@GET
	@Path("search")
	public Map search(@QueryParam("key") String key,@QueryParam("source") String source,@QueryParam("page") int page,@QueryParam("pageSize") int pageSize,@QueryParam("orderBy") String orderBy,@QueryParam("timeto") String timeto,@QueryParam("jobtype") String jobtype) throws UnsupportedEncodingException{
		if(key==null){
			return null;
		}
		Map<String,Object> para=new HashMap();
		Map reMap=new HashMap();
		String decodekey=URLDecoder.decode(key,"UTF-8");
		decodekey= ClientUtils.escapeQueryChars(decodekey);
		QueryUtils q=new QueryUtils();
		int timeto_int,orderby_int;
		if(timeto!=null)
			timeto_int=Integer.valueOf(timeto);
		else
			timeto_int=-1;
	
		if(orderBy!=null)
			orderby_int=Integer.valueOf(orderBy);
		else
			orderby_int=1;

		Object user=request.getSession().getAttribute("user");
		Set<String> favset=null;
		if(user!=null){//login
			favset=jobsDao.getUserSavedJobIds(((com.bbs.bean.User)user).getUid());
		}
		return q.query(page, pageSize, decodekey, source, orderby_int, timeto_int,favset,jobtype);
	}
}
