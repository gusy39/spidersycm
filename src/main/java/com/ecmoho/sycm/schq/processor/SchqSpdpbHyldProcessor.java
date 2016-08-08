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
 * 市场行情商品店铺榜行业粒度
 */
@Component("schqSpdpbHyldProcessor")
public class SchqSpdpbHyldProcessor extends  SchqProcessor{


	@Override
	public Site getSite() {
		return super.getSite().setSleepTime(10000);
	}
    @Override
	public void process(Page page) {
    	
	   	
	 
    	   String jsonStr=page.getJson().toString();
           System.out.println(jsonStr);
	       JSONObject finalTargetJsonObject=JSON.parseObject(jsonStr);
	       List<Map<String,String>> dataList= new ArrayList<Map<String,String>>();
		   Map<String,String> dataMap=null;
		   JSONArray dataJsonArray=null;
		   JSONObject dataJsonObject=null;
		   Map<String,String> urlMap=schqHeaderBean.getUrlMap();
		   String childAccount=urlMap.get("childAccount");
		   dataJsonArray=finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONArray("data");
	       for(int i=0;i<dataJsonArray.size();i++){
	    	   dataJsonObject=dataJsonArray.getJSONObject(i);
	    	   dataMap=getDataMap(dataMap, urlMap);
	    	   for(String key:dataJsonObject.keySet()){
		    	   dataMap.put(key, dataJsonObject.getString(key));
		       }
	    	   dataList.add(dataMap);
	       }
		   switch (childAccount) {
		       //��Ʒ���̰�_��ҵ����_������Ʒ��
		       case "spdpb-hyld-rxsp":
				      spiderSchqDataDao.addListData(dataList, "sycm_commodityshop_industry_hotcommodityranking");
				      break;
			   //��Ʒ���̰�_��ҵ����_������Ʒ��     
		       case "spdpb-hyld-llsp":
				      spiderSchqDataDao.addListData(dataList, "sycm_commodityshop_industry_flowcommodityranking");
			          break;
			   //��Ʒ���̰�_��ҵ����_�������̰�     
		       case "spdpb-hyld-rxdp":
				      spiderSchqDataDao.addListData(dataList, "sycm_commodityshop_industry_hotshopranking");
			          break;
			   //��Ʒ���̰�_��ҵ����_�������̰�   
		       case "spdpb-hyld-lldp":
				       spiderSchqDataDao.addListData(dataList, "sycm_commodityshop_industry_flowshopranking");
			           break;
		       default:
			           break;
	        }
 
	}
    //��ȡ���ݸ�����Ϣ
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
