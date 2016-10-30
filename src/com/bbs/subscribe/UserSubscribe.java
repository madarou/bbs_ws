package com.bbs.subscribe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.bbs.util.QueryUtils;

import freemarker.template.Configuration;
/**
 * 
 * <p> 邮件订阅. </p>
 * 
 * created on 2014-10-19-下午9:29:46
 * @author  fannairu
 * 
 * @version 1.0
 */
public class UserSubscribe implements Runnable{
	private	Logger logger=Logger.getLogger(SubscribeJob.class);
	private String hostname; 
	private QueryUtils query=new QueryUtils();
	private Map subscribermap;
	private int pagesize;
	private JavaMailSender mailSender;
	private String mailsubject;
	public UserSubscribe(JavaMailSender mailSender,String hostname,Map subscribermap,int pagesize,String mailsubject){
		this.mailSender=mailSender;
		this.hostname=hostname;
		this.subscribermap=subscribermap;
		this.pagesize=pagesize;
		this.mailsubject=mailsubject;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Object timeto=subscribermap.get("timeto");
		Object source=subscribermap.get("source");
		Object keylist=subscribermap.get("keylist");
		Object email=subscribermap.get("email");
		int timeto_int=0;
		if(timeto!=null){
			timeto_int=(int)timeto;
		}
		
		String content=getUserJobList(pagesize,keylist.toString(),timeto_int,source.toString());
		
		if(content!=null){
			MimeMessage mailMessage = mailSender.createMimeMessage();
			MimeMessageHelper mail = new MimeMessageHelper(mailMessage,"UTF-8"); 
			try {  		
				   Map para=new HashMap();
				   para.put("joblist", content);
				   para.put("unsubscribe", "<a href=\""+subscribermap.get("unsuburl")+"\">退订</a>");
				   content=PageTemplate.getTemplate("mailTemplate.ftl", para);
					System.out.println(content);

				   mail.setTo(email.toString());  
				   mail.setFrom(((JavaMailSenderImpl)mailSender).getUsername()); 
				   mail.setSubject(mailsubject);  
				   mail.setText(content,true);  
				   mailSender.send(mailMessage);  
				   logger.info(email+" send successful!");
			} catch (Exception e) {  
				   logger.info(e);  
				   logger.info(email+" send successful!");
			}
		}
		
			   
	}
	public String getUserJobList(int pagesize,String keylist,int timeto,String source){
		StringBuilder content=new StringBuilder();
		if(source!=null&&source.equals("ALL")){
			source=null;
		}
		Map results=query.query(0, pagesize*10, keylist, source, 1, timeto, null,"BOTH");	
		String title;
		String jobsource;
		String id;
		Set<String> titles=new HashSet();
		int i=pagesize;
		if(results!=null){
			List<Map> jobs=(List<Map>)results.get("jobs");
			int seq=1;
			String url;
			for(Map job:jobs){
				title=job.get("title").toString();
				if(i>0){
					if(!titles.contains(title)){
						titles.add(title);
						i--;
						jobsource=job.get("source").toString();
						id=job.get("id").toString();
						url=hostname+"/jobpost.html?source="+jobsource+"&id="+id;
						content.append("<tr class=\"job\">");
						content.append("<td class=\"title\" width=\"80%;\">");
						content.append("<a href='");
						content.append(url);
						content.append("' style=\"color:#428bca;font-size:16px;\">"+title+"</a>");
						content.append("<p style=\"padding-top:5px;font-size: 12px;color: #999;margin: 0;\">来自：");
						content.append(typeToName(jobsource));
						content.append("</p></td>");
						content.append("<td  width=\"20%;\">");
						content.append("<a style=\"height: 60px;width: 60px;line-height: 60px;text-align: center;background: #eee;float: right;color: #666;text-decoration: none;\" href=\"");
						content.append(url);
						content.append("\">详情</a>");
						content.append("</td></tr>");
					}
				}
				else{
					break;
				}
				
				
			}
			
			return content.toString();
		}

		return null;
	}
	
	private String typeToName(String type){
		switch(type){
			case "FDU":return "复旦日月光华";
			case "NJU":return "南大小百合";
			case "BYR":return "北邮人";
			case "ECNU":return "爱在华师";
			case "ECUST":return "华理梅陇客栈";
			case "PKU":return "北大未名";
			case "QINGHUA":return "清华水木";
			case "SJ":return "交大饮水思源";
			default: return "复旦日月光华";
		}
		
	}
}
