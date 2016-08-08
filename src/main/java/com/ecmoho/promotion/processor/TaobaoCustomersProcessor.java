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
 * 淘宝客URL获取结果解析
 */
@Component("taobaoCustomersProcessor")
public class TaobaoCustomersProcessor implements PageProcessor{
    private Site site=Site.me().setTimeOut(15000).setRetryTimes(3).setSleepTime(3000);
    @Resource(name = "taobaoCustomersHeaderBean")
    private HeaderBean taobaoCustomersHeaderBean;
    @Resource(name="spiderDataDao")
    protected SpiderDataDao spiderPromotionDataDao;

    @Override
    public Site getSite() {

        if(!"".equals(taobaoCustomersHeaderBean.getUserAgent())&&taobaoCustomersHeaderBean.getUserAgent()!=null){
            site.addHeader("user-agent", taobaoCustomersHeaderBean.getUserAgent());
        }
        if(!"".equals(taobaoCustomersHeaderBean.getAccept())&&taobaoCustomersHeaderBean.getAccept()!=null){
            site.addHeader("accept", taobaoCustomersHeaderBean.getAccept());
        }
        if(!"".equals(taobaoCustomersHeaderBean.getAcceptLanguage())&&taobaoCustomersHeaderBean.getAcceptLanguage()!=null){
            site.addHeader("accept-language", taobaoCustomersHeaderBean.getAcceptLanguage());
        }
        if(!"".equals(taobaoCustomersHeaderBean.getOrgin())&&taobaoCustomersHeaderBean.getOrgin()!=null){
            site.addHeader("origin", taobaoCustomersHeaderBean.getOrgin());
        }
        if(!"".equals(taobaoCustomersHeaderBean.getReferer())&&taobaoCustomersHeaderBean.getReferer()!=null){
            site.addHeader("referer", taobaoCustomersHeaderBean.getReferer());
        }
        if(!"".equals(taobaoCustomersHeaderBean.getCookie())&&taobaoCustomersHeaderBean.getCookie()!=null){
            site.addHeader("cookie", taobaoCustomersHeaderBean.getCookie());
        }
        return site;
    }
    @Override
    public void process(Page page) {
//        System.out.println(page.getJson());
        String resultStr=page.getJson().toString();
        System.out.println("resultStr:"+resultStr);
        Map<String, String> urlMap = taobaoCustomersHeaderBean.getUrlMap();
        Map<String,String> dataMap=new HashMap<String,String>();
        JSONObject dataJsonObject=JSON.parseObject(resultStr).getJSONObject("data");

        if (dataJsonObject!=null&&!"null".equals(dataJsonObject.toString())&&!"".equals(dataJsonObject.toString())){
            JSONObject resultJsonObject = dataJsonObject.getJSONObject("totalData");
            for(String key:resultJsonObject.keySet()){
                dataMap.put(key, resultJsonObject.getString(key));
            }
            dataMap.put("log_at",urlMap.get("log_at"));
            dataMap.put("create_at",urlMap.get("create_at"));
            dataMap.put("accountid",urlMap.get("accountid"));
            dataMap.put("grapfrequency",urlMap.get("grapfrequency"));
            spiderPromotionDataDao.addData(dataMap,"promotion_taobaocustomers_overview");
        }

    }


}
