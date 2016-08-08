package com.ecmoho.sycm.schq.processor;





import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.base.Util.StringUtil;
import com.ecmoho.sycm.schq.exploration.SchqExploration;
import com.ecmoho.sycm.schq.exploration.SchqHydpExploration;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
/**
 * 
 * @author gusy
 * 市场行情行业大盘
 */
@Component("schqHydpProcessor")
public class SchqHydpProcessor extends SchqProcessor{
	

	@Override
	public Site getSite() {
		return super.getSite();
	}
	
    @Override
	public void process(Page page) {
		String jsonStr = page.getJson().toString();
//	   
		System.out.println("hydp:" + jsonStr);

		Map<String, String> urlMap = schqHeaderBean.getUrlMap();
		Map<String, String> dataMap = new HashMap<String, String>();

		dataMap.put("accountid", urlMap.get("accountid"));
		dataMap.put("create_at", urlMap.get("create_at"));
		dataMap.put("level", urlMap.get("level"));
		dataMap.put("item1", urlMap.get("item1"));
		dataMap.put("item2", urlMap.get("item2"));
		dataMap.put("item3", urlMap.get("item3"));
		dataMap.put("device", urlMap.get("device"));
		dataMap.put("seller", urlMap.get("seller"));
		dataMap.put("log_at", urlMap.get("log_at"));
		//�������ɹ�
		JSONObject finalTargetJsonObject = JSON.parseObject(jsonStr);
		JSONArray jsonArray = finalTargetJsonObject.getJSONObject("content").getJSONArray("data");
		//	   JSONObject.toJSON(dataMap);
		JSONObject dataJsonObject = null;
		for (int i = 0; i < jsonArray.size(); i++) {
			dataJsonObject = jsonArray.getJSONObject(i);
			String indexCode = dataJsonObject.getString("indexCode");
			String currentValue = dataJsonObject.getString("currentValue");
			dataMap.put(indexCode, currentValue);
		}
		spiderSchqDataDao.addData(dataMap, "sycm_industry_report");

	}
}
