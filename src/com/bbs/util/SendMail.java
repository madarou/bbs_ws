package com.bbs.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.bbs.subscribe.UserSubscribe;

public class SendMail {
	 // 邮箱服务器
    private String host = "smtp.exmail.qq.com";
    // 这个是你的邮箱用户名
    private String username = "jobs@4jobs.me";
    // 你的邮箱密码
    private String password = "seed2014";
    
    private String mail_head_name = "Hi,";
    
    private String mail_subject = "Password Reset";
    private String body;
    private String personalName = "administrator";
	public void send(String link,String mail_to){

		
		body="Hi,"+mail_to.split("@")[0]; 
		body+=":\n    Please click the following link to reset you password:\n    ";
		body+=link;
		body+="\n    Best Regards!";
		Properties props = new Properties();
        Session session = null;
        try {
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");
            session = Session.getDefaultInstance(props, new Email_Autherticator());
            // 设置session,和邮件服务器进行通讯。
            MimeMessage message = new MimeMessage(session);
            message.setSubject(mail_subject,"UTF-8"); // 设置邮件主题
            message.setText(body,"UTF-8"); // 设置邮件正文
            message.setHeader(mail_head_name, "alex"); // 设置邮件标题
            message.setSentDate(new Date()); // 设置邮件发送日期
            Address address = new InternetAddress(username, personalName);
            message.setFrom(address); // 设置邮件发送者的地址
            Address toAddress = new InternetAddress(mail_to); // 设置邮件接收方的地址
            message.addRecipient(Message.RecipientType.TO, toAddress);
            Transport.send(message); // 发送邮件
            System.out.println("send ok!");

        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public class Email_Autherticator extends Authenticator{
		 public Email_Autherticator()
	     {
	         super();
	     }

	     public Email_Autherticator(String user, String pwd)
	     {
	         super();
	         username = user;
	         password = pwd;
	     }

	     public PasswordAuthentication getPasswordAuthentication()
	     {
	         return new PasswordAuthentication(username, password);
	     }
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//new SendMail().send(new UserSubscribe("http://localhost").getUserJobList(10, "java", 7, null),"583798151@qq.com");
	}

}
