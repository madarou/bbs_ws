package com.bbs.bean;

import java.util.List;

public class QueryResult {

	private long numFound;//结果总数
	private int page;//当前页
	private String key;
	private List<JobDoc> jobs;
	
	public long getNumFound() {
		return numFound;
	}
	public void setNumFound(long numFound) {
		this.numFound = numFound;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public List<JobDoc> getJobs() {
		return jobs;
	}
	public void setJobs(List<JobDoc> jobs) {
		this.jobs = jobs;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
