package com.ecmoho.test;

import com.ecmoho.base.Util.DateUtil;
import com.ecmoho.base.dao.SpiderAccountDao;
import com.ecmoho.promotion.service.PromotionService;
import com.ecmoho.base.model.SpiderSchqAcount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by meidejing on 2016/6/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring.xml","classpath:spring/spring_mybatis.xml"})
public class TestSpider {
    @Resource
    private SpiderAccountDao spiderAccountDao;
    @Resource(name = "subwayServiceImpl")
    private PromotionService subwayService;
    @Resource(name = "taobaoCustomersServiceImpl")
    private PromotionService taobaoCustomersService;
    @Resource(name = "majibaoServiceImpl")
    private PromotionService majibaoService;
    @Resource(name = "productBaoServiceImpl")
    private PromotionService productBaoService;
    @Resource(name = "diamondBoothServiceImpl")
    private PromotionService diamondService;
    @Resource(name = "accountantServiceImpl")
    private PromotionService accountantService;
    @Test
    //直通车
    public void testSubWay(){
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();

        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList){
            if ("1".equals(spiderSchqAcount.getPromotionSubwayFlag())) {
                subwayService.startGrap(spiderSchqAcount.getSid(), "2016-08-08","week");
            }
        }
    }
    //麻吉宝
    @Test
    public void testMajibao(){
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList){
            if ("1".equals(spiderSchqAcount.getPromotionMajibaoFlag())) {
                majibaoService.startGrap(spiderSchqAcount.getSid(), "2016-08-08","week");
            }
        }
    }
    //钻展
    @Test
    public void testDiamond(){
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList) {
            if ("1".equalsIgnoreCase(spiderSchqAcount.getPromotionDiamondFlag())&&spiderSchqAcount.getSid()==102) {
                diamondService.startGrap(spiderSchqAcount.getSid(), "2016-09-01","day");
            }
        }
    }
    //账房
    @Test
    public void testAccountantService(){
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList) {
            if ("1".equalsIgnoreCase(spiderSchqAcount.getPromotionAccountantFlag())&&spiderSchqAcount.getSid()>=129) {
                accountantService.startGrap(spiderSchqAcount.getSid(), "2016-08-24","month");
            }
        }
    }
    //品销宝
    @Test
    public void testProductBaoService(){
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList) {
            if ("1".equalsIgnoreCase(spiderSchqAcount.getPromotionProductFlag())) {
                productBaoService.startGrap(spiderSchqAcount.getSid(), "2016-08-08","week");
            }
        }
    }
    //淘宝客
    @Test
    public void testTaobaoCustomersService(){
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList) {
            if ("1".equalsIgnoreCase(spiderSchqAcount.getPromotionTaobaocustomersFlag())&&spiderSchqAcount.getSid()==120) {
                taobaoCustomersService.startGrap(spiderSchqAcount.getSid(), "2016-08-26","week");
            }
        }
    }
}
