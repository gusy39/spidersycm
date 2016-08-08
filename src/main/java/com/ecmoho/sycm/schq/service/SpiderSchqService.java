package com.ecmoho.sycm.schq.service;

import org.springframework.web.context.WebApplicationContext;

/**
 * Created by meidejing on 2016/6/16.
 */
public interface SpiderSchqService {
    public void startGrap(WebApplicationContext wac, int accountid, int accountChildid, String dateStr);
}
