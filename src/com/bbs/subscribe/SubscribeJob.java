package com.bbs.subscribe;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.bbs.mapper.SubscribeMapper;
/**
 * 
 * <p> 邮件订阅--定时任务 </p>
 * 
 * created on 2014-10-19-下午9:30:50
 * @author  fannairu
 * 
 * @version 1.0
 */
public class SubscribeJob{
	private	Logger logger=Logger.getLogger(SubscribeJob.class);
	
	private SubscribeMapper subscribeDao;
	private JavaMailSender mailSender;
	private String mailsubject;
	public void execute(int threadcount,int pagesize,String hostname) {
		// TODO Auto-generated method stub
		logger.info("start send subscript email...");
		List<Map> subscribers=subscribeDao.getValideSubscribe();
		logger.info("subscribers count:"+subscribers.size());
		if(subscribers!=null&&subscribers.size()>0){
			ExecutorService executor=Executors.newFixedThreadPool(threadcount);
			for(Map subscriber:subscribers){
				executor.execute(new UserSubscribe(mailSender,hostname,subscriber,pagesize,mailsubject));
			}
			executor.shutdown();
			
			try {
				executor.awaitTermination(1000, TimeUnit.DAYS);
				logger.info("Email send over!");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void setSubscribeDao(SubscribeMapper subscribeDao) {
		this.subscribeDao = subscribeDao;
	}
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setMailsubject(String mailsubject) {
		this.mailsubject = mailsubject;
	}


}
