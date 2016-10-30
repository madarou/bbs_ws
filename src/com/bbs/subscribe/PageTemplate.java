package com.bbs.subscribe;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class PageTemplate {
	
	public static String getTemplate(String pagename,Map para) throws IOException, TemplateException{
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);
		cfg.setClassForTemplateLoading(PageTemplate.class, "../templates");
		Template temp = cfg.getTemplate(pagename);
		StringWriter sw = new StringWriter(); 
		temp.process(para, sw);
		return sw.toString();
	}

	/**
	 * @param args
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, TemplateException {
		// TODO Auto-generated method stub
		Map para=new HashMap();
		para.put("joblist", "joblist");
		getTemplate("mailTemplate.ftl",para);
	}

}
