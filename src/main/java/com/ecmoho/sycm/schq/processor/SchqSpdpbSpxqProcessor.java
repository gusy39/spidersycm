package com.ecmoho.sycm.schq.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;

import java.util.*;

/**
 * @author zhangjr �г�����--��Ʒ���̰�--��Ʒ����
 */
@Component("schqSpdpbSpxqProcessor")
public class SchqSpdpbSpxqProcessor extends SchqProcessor {
    @Override
    public Site getSite() {
        Random random = new Random();
        return super.getSite().setSleepTime(10000+random.nextInt(5) * 1000);
    }

    @Override
    public void process(Page page) {
        System.out.println("hahahhaha");
        String jsonStr = page.getJson().toString();
        System.out.println(jsonStr);
        JSONObject finalTargetJsonObject = JSON.parseObject(jsonStr);
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        List<Map<String, String>> dataListpc = new ArrayList<Map<String, String>>();
        List<Map<String, String>> dataListwl = new ArrayList<Map<String, String>>();
        Map<String, String> dataMap = null;
        JSONArray dataJsonArray = null;
        JSONObject dataJsonObject = null;
        String device = "0";
        Map<String, String> urlMap = schqHeaderBean.getUrlMap();
        String childAccount = urlMap.get("childAccount");
        if (null == finalTargetJsonObject.getString("content")) {
            System.out.println("�ֱ������ˣ����Ժ�����");
        } else {
            if ("0".equals(finalTargetJsonObject.getJSONObject("content")
                    .getString("code"))) {
                if (childAccount.equals("spdpb-spxq-spqs")) {
                    dataJsonObject = finalTargetJsonObject.getJSONObject(
                            "content").getJSONObject("data");
                    dataMap = getDataMap(dataMap, urlMap);
                    dataMap.put("payOrdCntList",
                            dataJsonObject.getString("payOrdCntList"));
                    dataMap.put("payByrRateIndexList",
                            dataJsonObject.getString("payByrRateIndexList"));
                    dataMap.put("payItemQtyList",
                            dataJsonObject.getString("payItemQtyList"));
                    dataList.add(dataMap);
                } else if (childAccount.equals("spdpb-spxq-ylgjc")) {
                    dataJsonArray = finalTargetJsonObject
                            .getJSONObject("content").getJSONObject("data")
                            .getJSONArray("wlSeList");
                    for (int i = 0; i < dataJsonArray.size(); i++) {
                        dataJsonObject = dataJsonArray.getJSONObject(i);
                        dataMap = getDataMap(dataMap, urlMap);
                        for (String key : dataJsonObject.keySet()) {
                            dataMap.put(key, dataJsonObject.getString(key));
                        }
                        dataMap.put("SeList", "wlSeList");
                        dataListwl.add(dataMap);
                    }
                    dataJsonArray = finalTargetJsonObject
                            .getJSONObject("content").getJSONObject("data")
                            .getJSONArray("pcSeList");
                    for (int i = 0; i < dataJsonArray.size(); i++) {
                        dataJsonObject = dataJsonArray.getJSONObject(i);
                        dataMap = getDataMap(dataMap, urlMap);
                        for (String key : dataJsonObject.keySet()) {
                            dataMap.put(key, dataJsonObject.getString(key));
                        }
                        dataMap.put("SeList", "pcSeList");
                        dataListpc.add(dataMap);
                    }
                } else {
                    dataJsonArray = finalTargetJsonObject.getJSONObject(
                            "content").getJSONArray("data");
                    for (int i = 0; i < dataJsonArray.size(); i++) {
                        dataJsonObject = dataJsonArray.getJSONObject(i);
                        dataMap = getDataMap(dataMap, urlMap);
                        for (String key : dataJsonObject.keySet()) {
                            dataMap.put(key, dataJsonObject.getString(key));
                        }
                        device = dataMap.get("device");
                dataList.add(dataMap);
            }
        }
    } else {
        System.out.println("�������ˣ��ֶ���鿴��");
            }
        }

        switch (childAccount) {
            // ��Ʒ���̰�_��Ʒ����_��Ʒ����
            case "spdpb-spxq-spqs":
                spiderSchqDataDao.addListData(dataList, "commodityshop_detail_commoditytrend");
                break;
            // ��Ʒ���̰�_��Ʒ����_������Դ
            case "spdpb-spxq-llly":
                if (device.equals("1")) {
                    spiderSchqDataDao.addListData(dataList,
                            "commodityshop_detail_flowsources_pc");
                } else if (device.equals("2")) {
                    spiderSchqDataDao.addListData(dataList,
                            "commodityshop_detail_flowsources_wl");
                }
                break;
            // ��Ʒ���̰�_��Ʒ����_�����ؼ���
            case "spdpb-spxq-ylgjc":
                spiderSchqDataDao.addListData(dataListwl, "commodityshop_detail_keyword_wl");
                spiderSchqDataDao.addListData(dataListpc, "commodityshop_detail_keyword_pc");
                break;
            default:
                break;
        }
    }

    // ��ȡ���ݸ�����Ϣ
    public static Map<String, String> getDataMap(Map<String, String> dataMap,
                                                 Map<String, String> urlMap) {
        dataMap = new HashMap<String, String>();
        dataMap.put("accountid", urlMap.get("accountid"));
        dataMap.put("create_at", urlMap.get("create_at"));
        dataMap.put("level", urlMap.get("level"));
        dataMap.put("item1", urlMap.get("item1"));
        dataMap.put("item2", urlMap.get("item2"));
        dataMap.put("item3", urlMap.get("item3"));
        if (!"-1".equals(urlMap.get("device"))) {
            dataMap.put("device", urlMap.get("device"));
        }
        dataMap.put("itemTitle", urlMap.get("itemTitle"));
        dataMap.put("log_at", urlMap.get("log_at"));
        return dataMap;
    }
}
