package com.ecmoho.base.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 插入数据data
 */
@Repository("spiderDataDao")
public class SpiderDataDao {
    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    public void addData(Map<String,String> dataMap,String tableName){
        StringBuffer keysb=new StringBuffer("");
        StringBuffer valuesb=new StringBuffer("");
        if(dataMap!=null&&dataMap.size()>0){
            for(Map.Entry<String, String> entry:dataMap.entrySet()){
                keysb.append(entry.getKey()+",");
                valuesb.append("'"+entry.getValue()+"',");
            }
        }else{
            return;
        }
        String sql="insert into "+"spider."+tableName+" ("+keysb.substring(0, keysb.length()-1)+") values ("+valuesb.substring(0, valuesb.length()-1)+")";
	    	  System.out.println(sql);
        jdbcTemplate.update(sql);
    };
    public int[]  addListData(final List<Map<String,String>> dataList,String tableName){
        StringBuffer keysb=new StringBuffer("");
        StringBuffer valuesb=new StringBuffer("");
        if(dataList!=null&&dataList.size()>0){
            Map<String,String> dataMap=dataList.get(0);
            for(Map.Entry<String, String> entry:dataMap.entrySet()){
                keysb.append(entry.getKey()+",");
                valuesb.append("?,");
//	    			  valuesb.append("'"+entry.getValue()+"',");
            }
        }else{
            return null;
        }
        String keyStr=keysb.substring(0, keysb.length()-1);
        String valueStr=valuesb.substring(0, valuesb.length()-1);
        final String[] keyArr=keyStr.split(",");
        String insertSql="insert into "+"spider."+tableName+" ("+keyStr+") values ("+valueStr+")";
//	         System.out.println(insertSql);
        int[] updateCounts = jdbcTemplate.batchUpdate(
                insertSql,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        for(int j=0;j<keyArr.length;j++){
                            String value=dataList.get(i).get(keyArr[j]);
                            if("log_at".equalsIgnoreCase(keyArr[j])){
                                ps.setTimestamp(j+1,Timestamp.valueOf(value));
                            }else {
                                ps.setString(j+1, value);
                            }
                        }
                    }
                    public int getBatchSize() {
                        return dataList.size();
                    }
                }
        );

        return updateCounts;
    };
    public void executeSql(String sql){
        jdbcTemplate.execute(sql);
    }
}
