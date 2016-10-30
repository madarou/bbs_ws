package com.bbs.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import com.bbs.bean.QueryResult;
import com.bbs.util.Constants.JobType;

public class QueryUtils {
	
	private final static String solrUrl="http://localhost/solr/";
	private final static int MaxPageSize=100; 
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 * @param page =页数
	 * @param pageSize =每页大小
	 * @param key =query
	 * @param source =ALL，QINGHUA，FDU，SJ，PKU
	 * @param orderBy =0 time,1 rel
	 * @param timeTo =-1,7,14,30 时间范围；-1无限制，7天内，2周内，一月内
	 * @return
	 */
	public Map query(int page,int pageSize,String key,String source,int orderBy,int timeTo,Set<String> favset,String jobType){
		Map result=new HashMap();
		try {
			HttpSolrServer server = new HttpSolrServer(solrUrl);	

			SolrQuery query=new SolrQuery();
				query.setQuery(key);
				int start=page*pageSize;
				query.setStart(start);
				pageSize=pageSize>MaxPageSize?MaxPageSize:pageSize;
		        query.setRows(pageSize);
		        if(orderBy==0)//搜索结果按照时间排序，默认相关度排序
		        	query.setSort("TIME", SolrQuery.ORDER.desc);
		        if(source!=null)
		        	query.addFilterQuery("SOURCE:"+source);
		        if(jobType==null){
		        	jobType=JobType.ALL.name();
		        }
		        if(jobType.equals(JobType.FULL.name()))
		        	query.addFilterQuery("JOBTYPE:FULL JOBTYPE:BOTH");
		        else if(jobType.equals(JobType.PART.name()))
		        	query.addFilterQuery("JOBTYPE:PART JOBTYPE:BOTH");
		        if(timeTo!=-1)//搜索时间范围
		        	query.addFilterQuery("TIME:[NOW/DAY-"+timeTo+"DAY TO *]");
		        
			QueryResponse response = server.query(query);

			List<Map> jobs = new LinkedList<Map>();
			SolrDocumentList sdl=response.getResults();
			Iterator<SolrDocument> it= sdl.iterator();
			Map jd;
			if(favset==null){
				while(it.hasNext()){
					SolrDocument sd=it.next();
					String id=(String)sd.getFirstValue("id");
					String title=(String)sd.getFirstValue("TITLE");
					String sou=(String)sd.getFirstValue("SOURCE");
					Date time=(Date)sd.getFirstValue("TIME");
					jd=new HashMap();
					jd.put("title", title);
					jd.put("source", sou);
					jd.put("id", id);
					jd.put("time", format.format(time));
					
					jobs.add(jd);
				}
			}
			else{
				while(it.hasNext()){
					SolrDocument sd=it.next();
					int id=Integer.parseInt(sd.getFirstValue("id").toString());
					String title=(String)sd.getFirstValue("TITLE");
					String sou=(String)sd.getFirstValue("SOURCE");
					Date time=(Date)sd.getFirstValue("TIME");
					jd=new HashMap();
					jd.put("title", title);
					jd.put("source", sou);
					jd.put("id", String.valueOf(id));
					jd.put("time", format.format(time));
					if(favset.contains(id)){
						jd.put("save", "Y");
					}
					else{
						jd.put("save", "N");
					}
					jobs.add(jd);
				}
			}
			
			long numFound = response.getResults().getNumFound();

			result.put("jobs", jobs);
			result.put("numFound", numFound);
			result.put("page", page);
			result.put("key", key);
				
			return result;
			
			
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public  static void main(String[] args){
		QueryUtils q=new QueryUtils();
		System.out.println(q.query(0, 10, "java", null, 1, 7,null,"FULL"));
	}
}
