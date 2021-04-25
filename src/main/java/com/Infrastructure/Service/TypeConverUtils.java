package com.Infrastructure.Service;

import com.alibaba.fastjson.JSONObject;
import com.application.dto.SelectResultDto;
import com.domain.Entity.InsertEntity;
import com.domain.Entity.result.SelectResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypeConverUtils {


    //根据list对应map中k的顺序，顺序输出value
    public static List<String> mapToListByListOrder(InsertEntity insertEntity, List<String> list){
        List<String> rtn = new ArrayList<>();
        List<String> insertOrder = insertEntity.getColumnOrder();
        List<String> insertValue = insertEntity.getColumnValue();
        for (String s : list) {
            for (int var2 = 0; var2 < insertOrder.size(); ++var2) {
                if (s.equals(insertOrder.get(var2))) {
                    rtn.add(insertValue.get(var2));
                }
            }
        }
        return rtn;
    }


    public static SelectResultDto selectResultToDto(SelectResult result) {
        SelectResultDto selectDto = new SelectResultDto();
        List<String> columnlist = result.getRules();
        selectDto.setColumnTemplate(columnlist);
        List<JSONObject> tempList = new ArrayList<>();
        for (List<String> selectRow : result.getItems()) {
            JSONObject tempObj = new JSONObject();
            for (int var1 = 0; var1<selectRow.size();++var1) {
                tempObj.put(columnlist.get(var1),selectRow.get(var1));
            }
            tempList.add(tempObj);
        }
        selectDto.setRowTemplate(tempList);
        return selectDto;
    }

    //字符串大写转换
    public static String stringUpperConvert(String s){
        return s.toUpperCase();
    }

    /*
          插入的entity是 具体的值 : 对应的列
          需要返回是 具体的值 ： 对应列的类型
     */

    /**
     * 一个值具有三个属性，这一列(column)的对应的名字(name)
     * ，这一列对应的类型(type)如string,int,double等
     * ，这一列对应的值(value)具体的值如12345，abcd等
     * 当一个数据插入的时候，插入的是name:value，首先是value必须包含在内，同时不能为key，应为value会重复
     * 那么下一个key的选择只能在name和type之间，而一行多个列可以有重复的type，所以只能选择name作为key
     * 则，插入的map为，name:value
     * 而对其进行类型的校验时，首先需要吧name转换成type，之后才能对value进行校验
     * 当name转换成type之后，会出现，type:value的情况，是多对多的情况，所以这里不能使用map进行存储
     *
     * @date :2021/3/17
     * @param insertItems insertItem
     * @param tableName tableName
     * @return map
     */
    public static Map<String, String> valAndNameConvertToValAndType(Map<String,String> insertItems,String tableName) {

//        for(Map.Entry<String,String> entry : insertItems.entrySet()){
//            String RowName = entry.getValue();
//            TableInfo tableInfo = TableConstant.getTableByName(tableName);
//            Map<String,String> tableRules = tableInfo.getRules();
////            for (Map.Entry<String,String> ruleEntry : tableRules.entrySet()){
////
////            }
////            String ruleName =
//        }
//        Map<String,String> tableRules = TableConstant.getTableByName(tableName).getRules();
//        List<Map.Entry<String,String>> entryList = new ArrayList<>(insertItems.entrySet());
//        List<Map.Entry<String,String>> newEntryList = new ArrayList<>();
//        entryList.forEach(entry -> {
//            String ruleType = tableRules.get(entry.getKey());
//
//        });
//
//        return entryList.stream().collect(Collectors.toMap
//                (Map.Entry<String,String>::getKey,Map.Entry<String,String>::getValue));
//    }
        return null;
    }

    public static List<String> selectColumnValueFromGivenColumnInfo(List<String> parentOrders, List<String> values, List<String> needOrders) {
        List<String> rtnValues = new ArrayList<>();
        for (String needOrder : needOrders) {
            for (int var2 = 0; var2 < parentOrders.size(); ++var2) {
                if (needOrder.equals(parentOrders.get(var2))) {
                    rtnValues.add(values.get(var2));
                }
            }
        }
        return rtnValues;
    }
}
