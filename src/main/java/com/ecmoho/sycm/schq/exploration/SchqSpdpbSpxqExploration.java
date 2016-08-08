package com.ecmoho.sycm.schq.exploration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.base.Util.UrlUtil;

import com.ecmoho.base.model.SpiderSchqAcount;
import com.ecmoho.base.model.SpiderSchqChild;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author zhangjr �г�����--��Ʒ���̰�--��Ʒ����
 */
@Component("schqSpdpbSpxqExploration")
public class SchqSpdpbSpxqExploration extends SchqExploration {
    public List<HashMap<String, String>> getUrlList(SpiderSchqAcount spiderSchqAcount, SpiderSchqChild spiderSchqChild, String dateStr) {
        /**
         * childAccount:spdpb-spxq-spqs/spdpb-spxq-llly/spdpb-spxq-ylgjc
         */
        //��ȡitemId�ķ�Χ
        String[] childAccountArr2 = new String[]{"spdpb-hyld-rxsp", "spdpb-hyld-llsp", "spdpb-ppld-rxsp", "spdpb-ppld-llsp", "spdpb-cpld-rxsp", "spdpb-cpld-llsp"};
        // ��ȡ��ҵĿ¼
        List<HashMap<String, String>> hymlList = getHyml(schqHeaderBean);
        // ����URL������Ϣ
        List<HashMap<String, String>> urlList = new ArrayList<>();
        //�洢��ȡ��url��Ϣ������urlList����Ϊreturn
        //HashMap<String, String> urlMap = null;
        //�洢��ȡ��itemId
        List<HashMap<String, String>> itemBeanList = new ArrayList<>();
        //��Ϊurl����Ϣ����urlMap��
        int accountid = spiderSchqAcount.getSid();
        //��ȡ��ǰʱ��
        String nowDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String geturl = spiderSchqChild.getGeturl();
        String childAccount = spiderSchqChild.getChildAccount();
        //��ȡitemId�ķ���
        getItemId(dateStr, itemBeanList, childAccountArr2);
        //getItemIdTest(dateStr,itemBeanList,childAccountArr2);

        for (Map<String, String> mlMap : hymlList) {
            String targetUrl = geturl.replaceAll("##D##", dateStr)
                    .replaceAll("##CID##", mlMap.get("id"))
                    .replaceAll("\\|", "%7C");
            if (childAccount.equals("spdpb-spxq-ylgjc")) {
                for (int i = 0; i < itemBeanList.size(); i++) {
                    String finalUrl = targetUrl.replaceAll("##IID##",
                            itemBeanList.get(i).get("itemId"));
                    makeUrlList(mlMap, finalUrl, childAccount,
                            itemBeanList.get(i), -1, accountid, dateStr, nowDateStr, urlList);
                }
            } else if (childAccount.equals("spdpb-spxq-spqs")) {
                for (int i = 0; i < itemBeanList.size(); i++) {
                    String targetUrl1 = targetUrl.replaceAll("##IID##",
                            itemBeanList.get(i).get("itemId"));
                    for (int j = 0; j < 3; j++) {
                        String finalUrl = targetUrl1.replaceAll(
                                "##DE1##", j + "");
                        makeUrlList(mlMap, finalUrl, childAccount,
                                itemBeanList.get(i), j, accountid, dateStr, nowDateStr, urlList);
                    }
                }
            } else if (childAccount.equals("spdpb-spxq-llly")) {
                for (int i = 0; i < itemBeanList.size(); i++) {
                    String targetUrl1 = targetUrl.replaceAll("##IID##",
                            itemBeanList.get(i).get("itemId"));
                    for (int j = 1; j < 3; j++) {
                        String finalUrl = targetUrl1.replaceAll(
                                "##DE##", j + "");
                        makeUrlList(mlMap, finalUrl, childAccount,
                                itemBeanList.get(i), j, accountid, dateStr, nowDateStr, urlList);
                    }
                }
            }
        }
        return urlList;
    }

    private void makeUrlList(Map<String, String> mlMap, String finalUrl,
                             String childAccount, HashMap<String, String> item, int i, int accountid, String dateStr, String nowDateStr, List<HashMap<String, String>> urlList) {
        HashMap<String, String> urlMap = new HashMap<>();
        urlMap.put("childAccount", childAccount);
        urlMap.put("targetUrl", finalUrl);
        urlMap.put("accountid", accountid + "");
        urlMap.put("create_at", dateStr);
        urlMap.put("level", mlMap.get("level"));
        urlMap.put("item1", mlMap.get("item1"));
        urlMap.put("item2", mlMap.get("item2"));
        urlMap.put("item3", mlMap.get("item3"));
        if (i != -1) {
            urlMap.put("device", i + "");
        } else {
            urlMap.put("device", "-1");
        }
        urlMap.put("itemTitle", item.get("itemTitle"));
        urlMap.put("log_at", nowDateStr);
        urlList.add(urlMap);
    }

    /**
     * ��ȡitemId
     *
     * @param dateStr
     * @param itemBeanList
     * @param childAccountArr2
     */
    private void getItemId(String dateStr, List<HashMap<String, String>> itemBeanList, String[] childAccountArr2) {
        itemBeanList = new ArrayList<>();
        for (int c = 0; c < childAccountArr2.length; c++) {
            // ��ȡ��ҵĿ¼
            /**
             * ������id ����cateID��categoryID
             * */
            String childAccount = childAccountArr2[c];
            SpiderSchqChild spiderchild = spiderAccountDao.getSpiderSchqChildByChildAccount(childAccount);
            String geturl = spiderchild.getGeturl();
            List<HashMap<String, String>> hymlList = getHyml(schqHeaderBean);
            for (Map<String, String> mlMap : hymlList) {
                // ѭ���ն���Դ��0,�����նˣ�1��PC�ˣ�2�����߶ˣ�
                for (int j = 0; j <= 2; j++) {
                    // ѭ��֧�����ֶ����ͣ�0,��ʱ������ͼ��1��ʱ���ۼ�ͼ��
                    for (int k = -1; k <= 1; k++) {
                        if (childAccount.equals("spdpb-hyld-rxsp")
                                || childAccount.equals("spdpb-hyld-llsp")) {
                            String finalTargetUrl = geturl
                                    .replaceAll("##D##", dateStr)
                                    .replaceAll("##CID##", mlMap.get("id"))
                                    .replaceAll("##DE##", j + "")
                                    .replaceAll("##S##", k + "")
                                    .replaceAll("\\|", "%7C");
                            getItemIdByUrl(finalTargetUrl, itemBeanList);
                        } else {
                            Map<String, String> pplbMap = getPplb(schqHeaderBean, mlMap.get("id"), dateStr);
                            for (Entry<String, String> entry : pplbMap
                                    .entrySet()) {
                                String brandId = entry.getKey();// Ʒ��ID
                                if (childAccount.equals("spdpb-ppld-rxsp")
                                        || childAccount
                                        .equals("spdpb-ppld-llsp")) {
                                    HashMap<String, String> urlMap = new HashMap<String, String>();
                                    String finalTargetUrl = geturl
                                            .replaceAll("##D##", dateStr)
                                            .replaceAll("##CID##",
                                                    mlMap.get("id"))
                                            .replaceAll("##BID##", brandId)
                                            .replaceAll("##DE##", j + "")
                                            .replaceAll("##S##", k + "");
                                    getItemIdByUrl(finalTargetUrl, itemBeanList);
                                } else if (childAccount
                                        .equals("spdpb-cpld-rxsp")
                                        || childAccount
                                        .equals("spdpb-cpld-llsp")) {
                                    // ��ȡ��Ʒ����--Ʒ���б�--��Ʒ�б�
                                    List<HashMap<String, String>> cplbList = getCpfxCpxqModels(schqHeaderBean,
                                            mlMap.get("id"), brandId, dateStr);
                                    // ������Ʒ�б�
                                    for (int i = 0; cplbList != null
                                            && i < cplbList.size(); i++) {
                                        String modelId = cplbList.get(i).get(
                                                "modelId");
                                        String spuId = cplbList.get(i).get(
                                                "spuId");
                                        // Ʒ�Ʒ���_Ʒ������_Ʒ�Ƹſ�
                                        String finalTargetUrl = geturl
                                                .replaceAll("##BID##", brandId)
                                                .replaceAll("##CID##",
                                                        mlMap.get("id"))
                                                .replaceAll("##MID##", modelId)
                                                .replaceAll("##D##", dateStr)
                                                .replaceAll("##DE##", j + "")
                                                .replaceAll("##SID##", spuId)
                                                .replaceAll("##S##", k + "");
                                        getItemIdByUrl(finalTargetUrl, itemBeanList);
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    private void getItemIdByUrl(String finalTargetUrl, List<HashMap<String, String>> itemBeanList) {
        String response = UrlUtil.getUrlString(schqHeaderBean, finalTargetUrl,"GET");
        JSONObject jsonobject = JSONObject.parseObject(response).getJSONObject(
                "content");
        System.out.println("finalTargetUrl: " + finalTargetUrl);
        System.out.println("response: " + response);
        if (JSONObject.parseObject(response).getString("content") != null) {
            JSONObject obj = jsonobject.getJSONObject("data");
            JSONArray array = obj.getJSONArray("data");
            if (null != array && array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<>();
                    if (itemBeanList != null && itemBeanList.size() > 0) {
                        boolean flag = false;
                        for (int s = 0; s < itemBeanList.size(); s++) {
                            if (itemBeanList.get(s).equals(
                                    object.getString("itemId"))) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            map.put("itemId", object.getString("itemId"));
                            map.put("itemTitle", object.getString("itemTitle"));
                            itemBeanList.add(map);
                        }
                    } else if (itemBeanList == null || itemBeanList.size() == 0) {
                        map.put("itemId", object.getString("itemId"));
                        map.put("itemTitle", object.getString("itemTitle"));
                        itemBeanList.add(map);
                    }
                }
            }
        } else {
            System.out.println("��ȡ����ʧ�ܣ����Ժ�����");
        }
    }

    private void getItemIdTest(String dateStr, List<HashMap<String, String>> itemBeanList, String[] childAccountArr2) {
        itemBeanList = new ArrayList<>();
        for (int c = 0; c < childAccountArr2.length; c++) {
            // ��ȡ��ҵĿ¼
            /**
             * ������id ����cateID��categoryID
             * */
            String childAccount = childAccountArr2[c];
            SpiderSchqChild spiderchild = spiderAccountDao.getSpiderSchqChildByChildAccount(childAccount);
            String geturl = spiderchild.getGeturl();
            List<HashMap<String, String>> hymlList = getHyml(schqHeaderBean);
            Map<String, String> mlMap = hymlList.get(0);
            if (childAccount.equals("spdpb-hyld-rxsp")
                    || childAccount.equals("spdpb-hyld-llsp")) {
                String finalTargetUrl = geturl.replaceAll("##D##", dateStr)
                        .replaceAll("##CID##", mlMap.get("id"))
                        .replaceAll("##DE##", 0 + "").replaceAll("##S##", -1 + "")
                        .replaceAll("\\|", "%7C");
                getItemIdByUrl(finalTargetUrl, itemBeanList);
            } else {
                Map<String, String> pplbMap = getPplb(schqHeaderBean,
                        mlMap.get("id"), dateStr);
                for (Entry<String, String> entry : pplbMap.entrySet()) {
                    String brandId = entry.getKey();// Ʒ��ID
                    if (childAccount.equals("spdpb-ppld-rxsp")
                            || childAccount.equals("spdpb-ppld-llsp")) {
                        HashMap<String, String> urlMap = new HashMap<String, String>();
                        String finalTargetUrl = geturl.replaceAll("##D##", dateStr)
                                .replaceAll("##CID##", mlMap.get("id"))
                                .replaceAll("##BID##", brandId)
                                .replaceAll("##DE##", 0 + "")
                                .replaceAll("##S##", -1 + "");
                        getItemIdByUrl(finalTargetUrl, itemBeanList);
                    } else if (childAccount.equals("spdpb-cpld-rxsp")
                            || childAccount.equals("spdpb-cpld-llsp")) {
                        // ��ȡ��Ʒ����--Ʒ���б�--��Ʒ�б�
                        List<HashMap<String, String>> cplbList = getCpfxCpxqModels(schqHeaderBean, mlMap.get("id"),
                                brandId, dateStr);
                        // ������Ʒ�б�
                        for (int i = 0; cplbList != null && i < cplbList.size(); i++) {
                            String modelId = cplbList.get(i).get("modelId");
                            String spuId = cplbList.get(i).get("spuId");
                            // Ʒ�Ʒ���_Ʒ������_Ʒ�Ƹſ�
                            String finalTargetUrl = geturl
                                    .replaceAll("##BID##", brandId)
                                    .replaceAll("##CID##", mlMap.get("id"))
                                    .replaceAll("##MID##", modelId)
                                    .replaceAll("##D##", dateStr)
                                    .replaceAll("##DE##", 0 + "")
                                    .replaceAll("##SID##", spuId)
                                    .replaceAll("##S##", -1 + "");
                            getItemIdByUrl(finalTargetUrl, itemBeanList);
                        }
                    }
                }
            }
        }
    }
}