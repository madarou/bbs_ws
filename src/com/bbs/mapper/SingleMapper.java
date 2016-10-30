package com.bbs.mapper;

import java.util.List;
import java.util.Map;

public interface SingleMapper {
	public Map getSinglePic(Map para);
	public List getSingleList(Map para);
	public List getSingleListWithContent(Map para);
	public Map getSingleCnt(Map para);
	public List getSinglePicAddr(Map para);
	public Map getMessage(Map para);
	public List search(List para);

}
