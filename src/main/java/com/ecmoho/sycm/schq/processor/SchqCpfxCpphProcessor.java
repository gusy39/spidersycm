package com.ecmoho.sycm.schq.processor;



import java.util.HashMap;
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
 * 市场行情产品分析产品排行
 */
@Component("schqCpfxCpphProcessor")
public class SchqCpfxCpphProcessor extends  SchqProcessor{

	@Override
	public Site getSite() {
		return super.getSite();
	}
    @Override
	public void process(Page page) {
           
	   String jsonStr=page.getJson().toString();
       System.out.println(jsonStr);
       JSONObject finalTargetJsonObject=JSON.parseObject(jsonStr);
	   JSONArray jsonArray=finalTargetJsonObject.getJSONObject("content").getJSONArray("data");
	   Map<String,String> dataMap=new HashMap<String,String>();
	   Map<String,String> urlMap=schqHeaderBean.getUrlMap();
	   dataMap.put("accountid",urlMap.get("accountid"));
	   dataMap.put("create_at", urlMap.get("create_at"));
	   dataMap.put("level", urlMap.get("level"));
	   dataMap.put("item1", urlMap.get("item1"));
	   dataMap.put("item2", urlMap.get("item2"));
	   dataMap.put("item3", urlMap.get("item3"));
	   dataMap.put("device", urlMap.get("device"));
	   dataMap.put("seller", urlMap.get("seller"));
	   dataMap.put("log_at", urlMap.get("log_at"));
	   for(int i=0;i<jsonArray.size();i++){
		   JSONObject dataJsonObject=jsonArray.getJSONObject(i);
		   for(String key:dataJsonObject.keySet()){
			   dataMap.put(key, dataJsonObject.getString(key));
		   }
		   spiderSchqDataDao.addData(dataMap, "sycm_product_ranking_hotproduct");
	   }
	   
    	
	}
}
