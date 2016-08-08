package com.ecmoho.promotion.service;

import org.springframework.web.context.WebApplicationContext;

/**
 * Created by meidejing on 2016/6/29.
 */
public interface PromotionService {
    /**
     * 
     * @param accountid
     * @param dateStr
     * @param grapfrequency
     */
    public void startGrap(int accountid, String dateStr,String grapfrequency);
}
