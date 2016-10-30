package com.bbs.mapper;

import java.util.List;
import java.util.Map;

import com.bbs.bean.User;

public interface UserMapper {
	public User getUserInfo(Map para);
	public int addUser(User user);
	public int addFavorite(Map para);
	public List getJobFavList(Map para);
	public Map getJobFavCnt(Map para);
	public int removeFavorite(Map para);
	public int updatekey(Map para);

}
