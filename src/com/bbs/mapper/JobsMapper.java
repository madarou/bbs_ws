package com.bbs.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface JobsMapper {
	public List getJobsList(Map para);
	public List getUserJobsList(Map para);
	public Map getJobsCnt(Map para);
	public Map getJobDetail(Map para);
	public List search(Map para);
	public List loginSearch(Map para);
	public Map searchcount(Map para);
	public Set getUserSavedJobIds(long uid);

}
