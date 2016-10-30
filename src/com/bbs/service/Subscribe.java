package com.bbs.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.bbs.mapper.SubscribeMapper;
import com.bbs.subscribe.MailValidate;
import com.bbs.util.Constants;
import com.bbs.util.DES;
import com.bbs.util.Encrypyt;
import com.bbs.util.UnSubscribeUrl;

@Path("subscribe")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Subscribe {
	
	@Autowired
	private SubscribeMapper subscribeDao;
	@Autowired
	private MailValidate mailvalidate;
	private @Context HttpServletRequest request;
	private @Context ServletConfig sc;
	@POST
	@Path("addsubscribe")
	public Map addSubscribe(Map para){
		Map statues=new HashMap();
		Object user=request.getSession().getAttribute("user");
		if(user==null){
			statues.put("statues", Constants.NOTLOGIN);	
			return statues;
		}
		else{
			long uid=((com.bbs.bean.User)user).getUid();
			String type=((com.bbs.bean.User)user).getType();
			String unsuburl=UnSubscribeUrl.gen(sc.getInitParameter("servername"), uid, type);
			para.put("uid", uid);
			para.put("type",type);
			para.put("unsuburl",unsuburl);
			Map subdata=subscribeDao.getSubscribes(para);

			if(subdata==null){//new 
				try {
					mailvalidate.genMail(sc.getInitParameter("servername"), uid,type, para.get("email").toString());
					subscribeDao.addSubscribe(para);
					statues.put("statues", Constants.SUCCESS);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					statues.put("statues", Constants.FAIL);
				}
			}
			else{//update
				Object email_db=subdata.get("email");
				if(email_db!=null&&email_db.toString().equals(para.get("email").toString())){//not modify email
					para.put("email", null);
					subscribeDao.updateSubscribe(para);
					statues.put("statues", Constants.SUCCESS);
				}
				else{
					para.put("verify", "N");
					subscribeDao.updateSubscribe(para);
					statues.put("statues", Constants.SUCCESS);
					
					try {
						mailvalidate.genMail(sc.getInitParameter("servername"), uid,type, para.get("email").toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				
			}
				
		}
		return statues;
	}
	
	@GET
	@Path("getsubscribes")
	public Map getSubscribes(){
		Map statues=new HashMap();
		Object user=request.getSession().getAttribute("user");
		if(user==null){
			statues.put("statues", Constants.NOTLOGIN);	
			return statues;
		}
		else{
			statues.put("statues", Constants.SUCCESS);
			Map para=new HashMap();
			para.put("uid", ((com.bbs.bean.User)user).getUid());
			para.put("type", ((com.bbs.bean.User)user).getType());
			statues.put("result",subscribeDao.getSubscribes(para));
		}
		return statues;
	}
	
	@GET
	@Path("verify")
	public void verify(@Context HttpServletRequest request,@Context HttpServletResponse response) throws Exception{
		int statues;
		long nowtime=System.currentTimeMillis();
		String encryptstr=request.getParameter("encryptstr");
		String keystr=request.getParameter("key");
		byte []key=DES.hexStringToBytes(keystr);
		byte []encryptData=DES.hexStringToBytes(encryptstr);
		byte[] data_byte=DES.decrypt(encryptData, key);
		String data=new String(data_byte);
		
		String tmp[]=data.split(";");
		
		if(tmp!=null&&tmp.length==4){//参数合法 
			String email=tmp[0];
			long uid=Long.parseLong(tmp[1]);
			String type=tmp[2];
			long time=Long.parseLong(tmp[3]);
			if((nowtime-time)>Constants.TIMEOUT){//链接超时
				statues=Constants.TIMEOUT;
			}
			else{
				Map para=new HashMap();
				para.put("uid", uid);
				para.put("type", type);
				para.put("email", email);
				subscribeDao.validateMail(para);
				statues=Constants.SUCCESS;
			}
		}
		else{//参数不合法
			statues=Constants.URL_INVALID;
		}
		request.setAttribute("statues", statues);
		request.getRequestDispatcher("/emailvalidate.jsp").forward(request, response);
	}
	@POST
	@Path("enable")
	public Map enable(Map para){
		Map statues=new HashMap();
		Object user=request.getSession().getAttribute("user");
		if(user==null){
			statues.put("statues", Constants.NOTLOGIN);	
			return statues;
		}
		else{
			para.put("uid", ((com.bbs.bean.User)user).getUid());
			subscribeDao.enableSubs(para);
			statues.put("statues", Constants.SUCCESS);
		}
		return statues;
	}	
	
	@GET
	@Path("unsubscribe")
	public void Unsubscribe(@Context HttpServletRequest request,@Context HttpServletResponse response) throws ServletException, IOException{
		String data=request.getParameter("data");
		try {
			data=Encrypyt.de(data);
			String []array=data.split(";");
			if(array.length>1){
				Map para=new HashMap();
				para.put("uid", array[0]);
				para.put("type", array[1]);
				para.put("enable", "N");
				subscribeDao.enableSubs(para);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.getRequestDispatcher("/unsubscript.jsp").forward(request, response);
	}
}
