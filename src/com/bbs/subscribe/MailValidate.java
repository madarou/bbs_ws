package com.bbs.subscribe;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.bbs.util.DES;

/**
 * 
 * <p> 邮箱认证. </p>
 * 
 * created on 2014-10-19-下午9:31:25
 * @author  fannairu
 * 
 * @version 1.0
 */
public class MailValidate {
	private JavaMailSender mailSender; 
	public void genMail(String hostname,long uid,String type,String mail) throws Exception{
		long time=System.currentTimeMillis();
		String data=mail+";"+uid+";"+type+";"+time;
		byte[] key = DES.initSecretKey();		
		byte[] encryptData = DES.encrypt(data.getBytes(), key);
		String senddata=DES.bytesToHexString(encryptData);
		String key_str=DES.bytesToHexString(key);
		String link=hostname+"/rest/subscribe/verify?encryptstr="+senddata+"&key="+key_str;
		String msg="  Please click the following link to verify your email:\n";
		msg+="    "+link;
		msg+="\n  Notify: The link will be validate for only 24 hours!";
		SimpleMailMessage message=new SimpleMailMessage();
		message.setTo(mail);
		message.setFrom(((JavaMailSenderImpl)mailSender).getUsername());
		message.setText(msg);
		message.setSubject("E-mail verify!"); 
		mailSender.send(message);

	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
}
