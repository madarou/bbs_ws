package com.bbs.bean;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

public class JobDoc {
	@Field
	private String id;//seq
	@Field
	private String TITLE;
	@Field
	private String CONTENT;
	@Field
	private String SOURCE;
	@Field
	private Date TIME;

	public String getId() {
		return id;
	}
	@Field
	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return TITLE;
	}
	@Field
	public void setTitle(String title) {
		this.TITLE = title;
	}

	public String getContent() {
		return CONTENT;
	}
	@Field
	public void setContent(String content) {
		this.CONTENT = content;
	}

	public String getSource() {
		return SOURCE;
	}
	@Field
	public void setSource(String source) {
		this.SOURCE = source;
	}

	public Date getTime() {
		return TIME;
	}
	@Field
	public void setTime(Date time) {
		this.TIME = time;
	}
	public String toString(){
		return id+"\t"+TITLE+"\t"+TIME+"\t"+SOURCE;
	}
}
