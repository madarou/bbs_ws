package com.bbs.mapper;

import java.util.List;
import java.util.Map;

public interface SubscribeMapper {
	public int addSubscribe(Map para);
	public void updateSubscribe(Map para);
	public List<Map> getValideSubscribe();
	public Map getSubscribes(Map para);
	public int changeMail(Map para);
	public int validateMail(Map para);
	public int enableSubs(Map para);

}
