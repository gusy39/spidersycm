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
 * 市场行情商品店铺榜产品粒度
 */
@Component("schqSpdpbCpldProcessor")
public class SchqSpdpbCpldProcessor extends  SchqProcessor{
	

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
		       //��Ʒ���̰�_��Ʒ����_������Ʒ��
		       case "spdpb-cpld-rxsp":
				       spiderSchqDataDao.addListData(dataList, "sycm_commodityshop_product_hotcommodityranking");
			           break;
			   //��Ʒ���̰�_��Ʒ����_������Ʒ�� 
		       case "spdpb-cpld-llsp":
				      spiderSchqDataDao.addListData(dataList, "sycm_commodityshop_product_flowcommodityranking");
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
		   dataMap.put("brandId", urlMap.get("brandId"));
		   dataMap.put("brandName", urlMap.get("brandName"));
		   dataMap.put("modelId", urlMap.get("modelId"));
		   dataMap.put("modelName", urlMap.get("modelName"));
		   dataMap.put("spuId", urlMap.get("spuId"));
		   dataMap.put("device", urlMap.get("device"));
		   dataMap.put("seller", urlMap.get("seller"));
		   dataMap.put("log_at", urlMap.get("log_at"));
		   return dataMap;
     }
}
