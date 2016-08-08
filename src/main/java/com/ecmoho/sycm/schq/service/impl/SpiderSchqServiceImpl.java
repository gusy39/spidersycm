package com.ecmoho.sycm.schq.service.impl;

import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.base.dao.SpiderAccountDao;
import com.ecmoho.sycm.schq.exploration.SchqExploration;
import com.ecmoho.base.model.SpiderSchqAcount;
import com.ecmoho.base.model.SpiderSchqChild;
import com.ecmoho.sycm.schq.processor.SchqProcessor;
import com.ecmoho.sycm.schq.service.SpiderSchqService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import us.codecraft.webmagic.Spider;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by meidejing on 2016/6/16.
 */
@Service("spiderSchqServiceImpl")
public class SpiderSchqServiceImpl implements SpiderSchqService {
    @Resource
    private SpiderAccountDao spiderAccountDao;
    @Resource(name="schqHeaderBean")
    private HeaderBean schqHeaderBean;
    //开始抓取
    public void startGrap(WebApplicationContext wac, int accountid, int accountChildid, String dateStr) {

        SpiderSchqAcount spiderSchqAccount = spiderAccountDao.getSpiderSchqAccount(accountid);
        SpiderSchqChild spiderSchqChild= spiderAccountDao.getSpiderSchqChildById(accountChildid);
        String refer_cookie = spiderSchqAccount.getRefferCookie();
        schqHeaderBean.setCookie(refer_cookie);
        String explorationBean = spiderSchqChild.getExplorationBean();
        String processorBean = spiderSchqChild.getProcessorBean();
        SchqExploration schqExploration= (SchqExploration) wac.getBean(explorationBean);
        SchqProcessor schqProcessor= (SchqProcessor) wac.getBean(processorBean);
        List<HashMap<String, String>> urlList = schqExploration.getUrlList(spiderSchqAccount, spiderSchqChild, dateStr);
        System.out.println("urlList.size():" + urlList.size());
        for (int j = 0; j < urlList.size(); j++) {
            Map<String, String> map = urlList.get(j);
            schqHeaderBean.setUrlMap(map);
            try {
                int rest=(new Random().nextInt(5)+15)* 1000;
                Thread.sleep(rest);
                System.out.println("休息时间："+rest+"s");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Spider.create(schqProcessor).addUrl(map.get("targetUrl")).run();
        }
    }
}
