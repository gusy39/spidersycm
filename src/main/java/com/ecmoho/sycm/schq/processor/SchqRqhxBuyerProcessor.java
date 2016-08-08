package com.ecmoho.sycm.schq.processor;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;










import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.sycm.schq.exploration.SchqExploration;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
/**
 * 
 * @author gusy
 * 市场行情
 */
@Component("schqRqhxBuyerProcessor")
public class SchqRqhxBuyerProcessor extends  SchqProcessor{
	
	@Override
	public Site getSite() {
		return super.getSite();
	}
    @Override
	public void process(Page page) {
           
	   String jsonStr=page.getJson().toString();
       System.out.println(jsonStr);
       JSONObject finalTargetJsonObject=JSON.parseObject(jsonStr);
       List<Map<String,String>> dataList= new ArrayList<Map<String,String>>();
	   Map<String,String> dataMap=null;
	   JSONArray jsonArray=null;
	   JSONObject dataJsonObject=null;
	   Map<String,String> urlMap=schqHeaderBean.getUrlMap();
	 //'rqhx-buyer-mjrq','rqhx-buyer-zyfb','rqhx-buyer-sffb'
	   switch (urlMap.get("childAccount")) {
		   case "rqhx-buyer-mjrq"://��Ⱥ����_�����Ⱥ_�����Ⱥ����
			   jsonArray=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONArray("data");
			   JSONObject propertyTypeObject=null;
			   JSONArray propertyJosnArray=null;
			   
			   for(int i=0;jsonArray!=null&&i<jsonArray.size();i++){
				   propertyTypeObject=jsonArray.getJSONObject(i);
				   String propertyName=propertyTypeObject.getString("name");
				   propertyJosnArray=propertyTypeObject.getJSONArray("list");
				   for(int j=0;propertyJosnArray!=null&&j<propertyJosnArray.size();j++){
					   dataJsonObject=propertyJosnArray.getJSONObject(j);
					   dataMap=getDataMap(dataMap, urlMap);
					   dataMap.put("name",dataJsonObject.getString("name"));
					   dataMap.put("proportion",dataJsonObject.getString("proportion"));
					   dataMap.put("propertyName",propertyName);
					   dataList.add(dataMap);
				   }
			   }
			   spiderSchqDataDao.addListData(dataList, "sycm_crowdportrait_buyerportrait_buyerdetail");
			   break;
		   case "rqhx-buyer-zyfb"://��Ⱥ����_�����Ⱥ_�������ְҵ�ֲ�
			   jsonArray=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONArray("list");
			   for(int i=0;jsonArray!=null&&i<jsonArray.size();i++){
				   dataJsonObject=jsonArray.getJSONObject(i);
				   dataMap=getDataMap(dataMap, urlMap);
				   dataMap.put("proportion", dataJsonObject.getString("proportion"));
				   dataMap.put("name", dataJsonObject.getString("name"));
			       dataList.add(dataMap);
			   }
			   spiderSchqDataDao.addListData(dataList, "sycm_crowdportrait_buyerportrait_jobpercent");
			   break;
		   case "rqhx-buyer-sffb"://��Ⱥ����_�����Ⱥ_�������ʡ�ݷֲ�����
			   jsonArray=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONArray("list");
			   for(int i=0;jsonArray!=null&&i<jsonArray.size();i++){
				   dataJsonObject=jsonArray.getJSONObject(i);
				   dataMap=getDataMap(dataMap, urlMap);
				   dataMap.put("proportion", dataJsonObject.getString("proportion"));
				   dataMap.put("name", dataJsonObject.getString("name"));
				   dataMap.put("price", dataJsonObject.getString("price"));
			       dataList.add(dataMap);
			   }
			   spiderSchqDataDao.addListData(dataList, "sycm_crowdportrait_buyerportrait_provincepercent");
			   break;
		   default:
			  break;
		}
    	
	}
    
    public static Map<String,String> getDataMap(Map<String,String> dataMap,Map<String,String> urlMap){
 	   dataMap=new HashMap<String,String>();
		   dataMap.put("accountid",urlMap.get("accountid"));
		   dataMap.put("create_at", urlMap.get("create_at"));
		   dataMap.put("level", urlMap.get("level"));
		   dataMap.put("item1", urlMap.get("item1"));
		   dataMap.put("item2", urlMap.get("item2"));
		   dataMap.put("item3", urlMap.get("item3"));
		   dataMap.put("device", urlMap.get("device"));
		   dataMap.put("seller", urlMap.get("seller"));
		   dataMap.put("log_at", urlMap.get("log_at"));
		   return dataMap;
 }
}
