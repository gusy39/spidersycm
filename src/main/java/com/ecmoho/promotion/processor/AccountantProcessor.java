package com.ecmoho.promotion.processor;

import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.base.dao.SpiderDataDao;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/6.
 */
@Component("accountantProcessor")
public class AccountantProcessor implements PageProcessor {
    @Resource(name = "accountantHeaderBean")
    private HeaderBean accountantHeaderBean;
    @Resource(name = "spiderDataDao")
    protected SpiderDataDao spiderPromotionDataDao;
    private Site site = Site.me().setTimeOut(15000).setRetryTimes(3).setSleepTime(3000);

    @Override
    public void process(Page page) {
        Map<String, String> urlMap = accountantHeaderBean.getUrlMap();
        String resultStr = page.getHtml().toString();
        if (resultStr != null && !"".equals(resultStr)) {
            Document doc = Jsoup.parse(resultStr);
            Element element = doc.getElementById("srcFollow");
            Elements linksElements = element.getElementsByTag("tr");
            List<Map<String, String>> list = new ArrayList<>();
            if (linksElements != null && linksElements.size() > 0) {
                for (Element ele : linksElements) {
                    Map<String, String> map = new HashMap<>();
                    Elements td1 = ele.getElementsByTag("td");
                    for (int i = 0; i < td1.size(); i++) {
                        Element eletd = td1.get(i);
                        map.put(i + "", eletd.text());
                        System.out.println("td: " + eletd.text());
                    }
                    list.add(map);
                }
                for (Map<String, String> map : list) {
                    Map<String, String> dataMap = new HashMap<String, String>();
                    dataMap.put("expenditure_categories", map.get("1"));
                    dataMap.put("expenditure_items", map.get("2"));
                    dataMap.put("expenditure_month", map.get("3"));
                    dataMap.put("payment_month", map.get("4"));
                    dataMap.put("transactions_num_month", map.get("5"));
                    dataMap.put("accumulated_unpaid", map.get("6"));
                    dataMap.put("log_at", urlMap.get("log_at"));
                    dataMap.put("create_at", urlMap.get("create_at"));
                    dataMap.put("accountid", urlMap.get("accountid"));
                    dataMap.put("grapfrequency", urlMap.get("grapfrequency"));
                    spiderPromotionDataDao.addData(dataMap, "promotion_accountant_report");
                }
            } else {
                System.out.println("结果为空");
            }
        }
    }

    @Override
    public Site getSite() {
        System.out.println("asdada:" + accountantHeaderBean.getCookie());
        if (!"".equals(accountantHeaderBean.getUserAgent()) && accountantHeaderBean.getUserAgent() != null) {
            site.addHeader("user-agent", accountantHeaderBean.getUserAgent());
        }
        if (!"".equals(accountantHeaderBean.getAccept()) && accountantHeaderBean.getAccept() != null) {
            site.addHeader("accept", accountantHeaderBean.getAccept());
        }
        if (!"".equals(accountantHeaderBean.getAcceptLanguage()) && accountantHeaderBean.getAcceptLanguage() != null) {
            site.addHeader("accept-language", accountantHeaderBean.getAcceptLanguage());
        }
        if (!"".equals(accountantHeaderBean.getOrgin()) && accountantHeaderBean.getOrgin() != null) {
            site.addHeader("origin", accountantHeaderBean.getOrgin());
        }
        if (!"".equals(accountantHeaderBean.getReferer()) && accountantHeaderBean.getReferer() != null) {
            site.addHeader("referer", accountantHeaderBean.getReferer());
        }
        if (!"".equals(accountantHeaderBean.getCookie()) && accountantHeaderBean.getCookie() != null) {
            site.addHeader("cookie", accountantHeaderBean.getCookie());
        }
        return site;
    }
}
