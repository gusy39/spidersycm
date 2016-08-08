package com.ecmoho.promotion.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.base.Util.StringUtil;
import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.base.dao.SpiderDataDao;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by meidejing on 2016/6/29.
 * 直通车URL获取结果解析
 */
@Component("subWayProcessor")
public class SubWayProcessor implements PageProcessor{
    private Site site=Site.me().setTimeOut(15000).setRetryTimes(3).setSleepTime(3000);
    @Resource(name = "subWayHeaderBean")
    private HeaderBean subWayHeaderBean;
    @Resource(name="spiderDataDao")
    protected SpiderDataDao spiderPromotionDataDao;

    @Override
    public Site getSite() {

        if(!"".equals(subWayHeaderBean.getUserAgent())&&subWayHeaderBean.getUserAgent()!=null){
            site.addHeader("user-agent", subWayHeaderBean.getUserAgent());
        }
        if(!"".equals(subWayHeaderBean.getAccept())&&subWayHeaderBean.getAccept()!=null){
            site.addHeader("accept", subWayHeaderBean.getAccept());
        }
        if(!"".equals(subWayHeaderBean.getAcceptLanguage())&&subWayHeaderBean.getAcceptLanguage()!=null){
            site.addHeader("accept-language", subWayHeaderBean.getAcceptLanguage());
        }
        if(!"".equals(subWayHeaderBean.getOrgin())&&subWayHeaderBean.getOrgin()!=null){
            site.addHeader("origin", subWayHeaderBean.getOrgin());
        }
        if(!"".equals(subWayHeaderBean.getReferer())&&subWayHeaderBean.getReferer()!=null){
            site.addHeader("referer", subWayHeaderBean.getReferer());
        }
        if(!"".equals(subWayHeaderBean.getCookie())&&subWayHeaderBean.getCookie()!=null){
            site.addHeader("cookie", subWayHeaderBean.getCookie());
        }
        return site;
    }
    @Override
    public void process(Page page) {
        System.out.println(page.getJson());
        String resultStr=page.getJson().toString();
        Map<String, String> urlMap = subWayHeaderBean.getUrlMap();
        Map<String,String> dataMap=null;

        if("isshop".equalsIgnoreCase(urlMap.get("urlType"))){
            JSONArray resultJsonArray = JSON.parseObject(resultStr).getJSONArray("result");
            if(resultJsonArray!=null&&resultJsonArray.size()>0) {
                JSONObject resultJsonObject = JSON.parseObject(resultStr).getJSONArray("result").getJSONObject(0);
                dataMap = new HashMap<String, String>();
                for (String key : resultJsonObject.keySet()) {
                    dataMap.put(key, resultJsonObject.getString(key));
                    dataMap.put("log_at", urlMap.get("log_at"));
                    dataMap.put("create_at", urlMap.get("create_at"));
                    dataMap.put("accountid", urlMap.get("accountid"));
                    dataMap.put("grapfrequency",urlMap.get("grapfrequency"));
                }
                spiderPromotionDataDao.addData(dataMap, "promotion_subway_report");
            }
        }else if ("network".equalsIgnoreCase(urlMap.get("urlType"))) {
            JSONArray resultJsonArray = JSON.parseObject(resultStr).getJSONArray("result");
            //点击量
            String pcinsideclick = "";
            String pcoutsideclick = "";
            String mobileinsideclick = "";
            String mobileoutsideclick = "";
            //展现量
            String pcinsideimpression = "";
            String pcoutsideimpression = "";
            String mobileinsideimpression = "";
            String moblieoutsideimpression = "";
            //总成交金额
            String pcinsidetransactiontotal = "";
            String pcoutsidetransactiontotal = "";
            String mobileinsidetransactiontotal = "";
            String moblieoutsidetransactiontotal = "";
            if (resultJsonArray!=null&&resultJsonArray.size()>0) {
                for (int i = 0; i < resultJsonArray.size(); i++) {
                    switch (i) {
                        case 0:
                            pcinsideclick = StringUtil.isNullString(resultJsonArray.getJSONObject(i).getString("click"));
                            pcinsideimpression = StringUtil.isNullString(resultJsonArray.getJSONObject(i).getString("impression"));
                            pcinsidetransactiontotal = StringUtil.isNullString(resultJsonArray.getJSONObject(i).getString("transactiontotal"));
                            break;
                        case 1:
                            pcoutsideclick = StringUtil.isNullString(resultJsonArray.getJSONObject(i).getString("click"));
                            pcoutsideimpression = StringUtil.isNullString(resultJsonArray.getJSONObject(i).getString("impression"));
                            pcoutsidetransactiontotal = StringUtil.isNullString(resultJsonArray.getJSONObject(i).getString("transactiontotal"));
                            break;
                        case 2:
                            mobileinsideclick = StringUtil.isNullString(resultJsonArray.getJSONObject(i).getString("click"));
                            mobileinsideimpression = StringUtil.isNullString(resultJsonArray.getJSONObject(i).getString("impression"));
                            mobileinsidetransactiontotal = StringUtil.isNullString(resultJsonArray.getJSONObject(i).getString("transactiontotal"));
                            break;
                        case 3:
                            mobileoutsideclick = StringUtil.isNullString(resultJsonArray.getJSONObject(i).getString("click"));
                            moblieoutsideimpression = StringUtil.isNullString(resultJsonArray.getJSONObject(i).getString("impression"));
                            moblieoutsidetransactiontotal = StringUtil.isNullString(resultJsonArray.getJSONObject(i).getString("transactiontotal"));
                            break;
                    }
                }

                String sql = "update spider.promotion_subway_report set " +
                        " pcinsideclick='" + pcinsideclick + "',pcoutsideclick='" + pcoutsideclick + "',mobileinsideclick='" + mobileinsideclick + "',mobileoutsideclick='" + mobileoutsideclick + "'," +
                        " pcinsideimpression='" + pcinsideimpression + "',pcoutsideimpression='" + pcoutsideimpression + "',mobileinsideimpression='" + mobileinsideimpression + "',moblieoutsideimpression='" + moblieoutsideimpression + "'," +
                        " pcinsidetransactiontotal='" + pcinsidetransactiontotal + "',pcoutsidetransactiontotal='" + pcoutsidetransactiontotal + "',mobileinsidetransactiontotal='" + mobileinsidetransactiontotal + "',moblieoutsidetransactiontotal='" + moblieoutsidetransactiontotal + "' " +
                        " where log_at='" + urlMap.get("log_at") + "'::timestamp and accountid='"+urlMap.get("accountid")+"'";
                System.out.println(sql);
                spiderPromotionDataDao.executeSql(sql);
            }
        }
    }


}
