package com.bbs.security;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LoadAuthority {
	private static Logger logger=Logger.getLogger(LoadAuthority.class);
	public static List<Authority> LoadSecurity(String projectname) {
		logger.info("load authority");
		List<Authority> authoritys = new ArrayList();
		try (InputStream is = LoadAuthority.class.getClassLoader().getResourceAsStream("security.xml");) {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			NodeList nodeList = doc.getElementsByTagName("authority");
			String []tmpStrings;
			String uri;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				tmpStrings=node.getTextContent().split("=");
				String []roles=tmpStrings[1].trim().split(",");
				Set<String> set=new HashSet<>();
				uri=projectname+tmpStrings[0].trim();
				logger.info(uri);
				for(String tmp:roles){
					logger.info("   "+tmp.trim());
					set.add(tmp.trim());
				}
				authoritys.add(new Authority(uri,set));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authoritys;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoadAuthority.LoadSecurity("BBS");

	}

}
