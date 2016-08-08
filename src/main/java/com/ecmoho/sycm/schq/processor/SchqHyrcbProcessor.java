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
 * 市场行情行业热词榜
 */
@Component("schqHyrcbProcessor")
public class SchqHyrcbProcessor extends  SchqProcessor{

	@Override
	public Site getSite() {
		return super.getSite();
	}
    @Override
	public void process(Page page) {
		System.out.println(page.getJson());
	   String jsonStr=page.getJson().toString();
       Map<String,String> urlMap=schqHeaderBean.getUrlMap();
	   String childAccount=urlMap.get("childAccount");
       JSONObject jsonHotObject=null;
       JSONObject jsonSoarObject=null;
       Map<String,String> dataMap=null;
       List<Map<String, String>>  dataHotList=new ArrayList<Map<String,String>>();
       List<Map<String, String>>  dataSoarList=new ArrayList<Map<String,String>>();
       JSONArray jsonHotArray=JSON.parseObject(jsonStr).getJSONObject("content").getJSONObject("data").getJSONArray("hotList");
       JSONArray jsonSoarArray=JSON.parseObject(jsonStr).getJSONObject("content").getJSONObject("data").getJSONArray("soarList");
       for(int i=0;jsonHotArray!=null&&i<(jsonHotArray.size()>20?20:jsonHotArray.size());i++){
    	   jsonHotObject=jsonHotArray.getJSONObject(i);
    	   dataMap=getDataMap(urlMap);
    	   for(String  key:jsonHotObject.keySet()){
    		   dataMap.put(key,jsonHotObject.getString(key));
    	   }
    	   dataHotList.add(dataMap);
       }
       for(int j=0;jsonSoarArray!=null&&j<(jsonSoarArray.size()>20?20:jsonSoarArray.size());j++){
    	   jsonSoarObject=jsonHotArray.getJSONObject(j);
    	   dataMap=getDataMap(urlMap);
    	   for(String  key:jsonSoarObject.keySet()){
    		   dataMap.put(key,jsonSoarObject.getString(key));
    	   }
    	   dataSoarList.add(dataMap);
       }	   
     //'sscfx-hyrcb-rmssc','sscfx-hyrcb-rmcwc','sscfx-hyrcb-rmhxc','sscfx-hyrcb-rmppc','sscfx-hyrcb-rmxsc'
        switch (childAccount) {
			case "sscfx-hyrcb-rmssc"://搜索词分析-行业热词榜-热门搜索词
				spiderSchqDataDao.addListData(dataHotList, "sycm_industryhotwords_hotsearchwordsranking");
				spiderSchqDataDao.addListData(dataSoarList, "sycm_industryhotwords_hotsearchwordssoarranking");
				break;
			case "sscfx-hyrcb-rmcwc"://搜索词分析-行业热词榜-热门长尾词
				spiderSchqDataDao.addListData(dataHotList, "sycm_industryhotwords_hottailwordsranking");
				spiderSchqDataDao.addListData(dataSoarList, "sycm_industryhotwords_hottailwordssoarranking");
				break;
			case "sscfx-hyrcb-rmhxc"://搜索词分析-行业热词榜-热门核心词
				spiderSchqDataDao.addListData(dataHotList, "sycm_industryhotwords_hotcorewordsranking");
				spiderSchqDataDao.addListData(dataSoarList, "sycm_industryhotwords_hotcorewordssoarranking");
				break;
			case "sscfx-hyrcb-rmppc"://搜索词分析-行业热词榜-热门品牌词
				spiderSchqDataDao.addListData(dataHotList, "sycm_industryhotwords_hotbrandwordsranking");
				spiderSchqDataDao.addListData(dataSoarList, "sycm_industryhotwords_hotbrandwordssoarranking");
				break;
			case "sscfx-hyrcb-rmxsc"://搜索词分析-行业热词榜-热门修饰词
				spiderSchqDataDao.addListData(dataHotList, "sycm_industryhotwords_hotattrwordsranking");
				spiderSchqDataDao.addListData(dataSoarList, "sycm_industryhotwords_hotattrwordssoarranking");
				break;		
			default:
				break;
		}
      
	}
  //��ȡ���ݸ�����Ϣ
    public static Map<String,String> getDataMap(Map<String,String> urlMap){
 	       HashMap<String,String> dataMap=new HashMap<String,String>();

 	       dataMap.put("accountid",urlMap.get("accountid"));
 		   dataMap.put("create_at", urlMap.get("create_at"));
 		   dataMap.put("level", urlMap.get("level"));
 		   dataMap.put("item1", urlMap.get("item1"));
 		   dataMap.put("item2", urlMap.get("item2"));
 		   dataMap.put("item3", urlMap.get("item3"));
 		   dataMap.put("device", urlMap.get("device"));
 		   dataMap.put("log_at", urlMap.get("log_at"));
		   return dataMap;
     }
}
