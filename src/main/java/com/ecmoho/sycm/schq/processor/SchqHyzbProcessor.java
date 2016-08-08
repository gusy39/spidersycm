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
 * @author gusy
 * 行业直播
 */
@Component("schqHyzbProcessor")
public class SchqHyzbProcessor extends SchqProcessor {

    @Override
    public Site getSite() {
        return super.getSite();
    }

    @Override
    public void process(Page page) {

        String jsonStr = page.getJson().toString();
        System.out.println("hyzb��" + jsonStr);
        Map<String, String> urlMap = schqHeaderBean.getUrlMap();
        Map<String, String> dataMap = new HashMap<String, String>();
        System.out.println("accountid:" + urlMap.get("accountid"));
        dataMap.put("accountid", urlMap.get("accountid"));
        dataMap.put("create_at", urlMap.get("create_at"));
        dataMap.put("level", urlMap.get("level"));
        dataMap.put("item1", urlMap.get("item1"));
        dataMap.put("item2", urlMap.get("item2"));
        dataMap.put("item3", urlMap.get("item3"));
        dataMap.put("device", urlMap.get("device"));
        dataMap.put("timeslotType", urlMap.get("timeslotType"));
        dataMap.put("log_at", urlMap.get("log_at"));
        JSONObject finalTargetJsonObject = JSON.parseObject(jsonStr);
        JSONObject jsonObject = finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getJSONObject("data");
        JSONArray payAmtList = jsonObject.getJSONArray("payAmtList");
        JSONArray paySubOrderCntList = jsonObject.getJSONArray("paySubOrderCntList");
        String updateTime = finalTargetJsonObject.getJSONObject("content").getJSONObject("data").getString("updateTime");
        dataMap.put("updateTime", updateTime);

        for (int a = 0; a < payAmtList.size(); a++) {
            dataMap.put("timeslot" + a, payAmtList.getString(a));
        }
        spiderSchqDataDao.addData(dataMap, "sycm_industry_payamtlist");
        dataMap.remove("payamt");
        for (int b = 0; b < paySubOrderCntList.size(); b++) {
            dataMap.put("timeslot" + b, paySubOrderCntList.getString(b));
        }
        spiderSchqDataDao.addData(dataMap, "sycm_industry_paysubordercntlist");


    }
}
