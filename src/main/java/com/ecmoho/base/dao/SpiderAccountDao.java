package com.ecmoho.base.dao;

import com.ecmoho.base.model.SpiderSchqAcount;
import com.ecmoho.base.model.SpiderSchqChild;

import java.util.List;

/**
 * Created by gusy on 2016/6/16.
 */
public interface SpiderAccountDao {
    public List<SpiderSchqAcount> getAllSpiderSchqAccount();
    public SpiderSchqAcount getSpiderSchqAccount(int id);
    public List<SpiderSchqChild> getAllSpiderSchqChild();
    public SpiderSchqChild getSpiderSchqChildById(int id);
    public SpiderSchqChild getSpiderSchqChildByChildAccount(String childAccount);
}
