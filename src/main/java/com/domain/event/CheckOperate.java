package com.domain.event;

import com.Infrastructure.TableInfo.ColumnInfo;
import com.domain.Entity.common.ColumnInfoEntity;
import com.domain.Entity.enums.ColumnSpecsEnums;
import com.domain.Entity.enums.ColumnTypeLengthEnums;
import com.domain.repository.TableConstant;
import com.domain.Entity.InsertEntity;
import com.domain.Entity.result.OperateResult;
import com.domain.Entity.enums.ColumnTypeEnums;
import com.domain.Entity.bTree.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * 校验操作
 */
public class CheckOperate {
    /**
     * 是否存在对应名称的表
     * @param tableName
     * @return
     */

    private static Logger logger = Logger.getLogger("log_check");

    public boolean ifTableExists(String tableName){
        return TableConstant.tableMap.containsKey(tableName);
    }
    /**
     * 判断建表规则是否合法
     * @param
     * @return
     */
    public OperateResult ifCreateColumnLegal(List<ColumnInfoEntity> columnInfoEntities){
        //校验的同时，把对象重新装配，在返回的时候添加进去，避免重新装配
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        for (ColumnInfoEntity columnInfoEntity : columnInfoEntities) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setColumnName(columnInfoEntity.getColumnName());
            //关键词的类型校验
            String dataType = columnInfoEntity.getDataType();
            if (!ColumnTypeEnums.ifContains(dataType)) {
                return OperateResult.error("关键词校验未通过," + dataType + "不存在");
            }
            columnInfo.setColumnType(dataType);
            //关键词的参数校验，如varchar，char等
            int arguments = Integer.parseInt(columnInfoEntity.getColumnArguments());
            if (ColumnTypeLengthEnums.ifContains(dataType) && ColumnTypeLengthEnums.findType(dataType).getLength() < arguments) {
                return OperateResult.error("关键词校验未通过," + dataType + "超过最大长度");
            }
            columnInfo.setColumnArgument(arguments);
            //特殊条件，如not null,unique校验
            List<String> dataSpecies = columnInfoEntity.getColumnSpecs();
            for (int var1 = 0; var1<dataSpecies.size();var1++) {
                String first = dataSpecies.get(var1);
                ColumnSpecsEnums columnSpecsEnums = ColumnSpecsEnums.findTypeByFirst(first);
                if (null == columnSpecsEnums) {
                    return OperateResult.error("关键词校验未通过," + first + "未知条件");
                }
                int length = columnSpecsEnums.getLength();
                if ((var1 + length)>dataSpecies.size()) {
                    return OperateResult.error("关键词校验未通过," +first + "未知条件" );
                }
                StringBuilder sb = new StringBuilder();
                for (int var2 = 0;var2<length;var2++) {
                    sb.append(dataSpecies.get(var1+var2));
                }
                if (!columnSpecsEnums.getTotal().equals(sb.toString())) {
                    return OperateResult.error("关键词校验未通过," + sb.toString() + "未知条件");
                }
                columnInfo.addSpecs(columnSpecsEnums);
            }
            columnInfoList.add(columnInfo);
        }
        return OperateResult.ok("关键词校验通过",columnInfoList);
    }

    /**
     * 判断插入规则是否合法
     * @param insertEntity
     * @return
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
     * @param insertEntity
     * @return
     */
    public OperateResult ifInsertItemsLegal(InsertEntity insertEntity){
//        Map<String,String> insertItems = insertEntity.getItems();
//        String tableName = insertEntity.getTableName();
//        Map<String,String> tableRules = TableConstant.getTableByName(tableName).
//        for (Map.Entry<String,String> entry : insertItems.entrySet()){
//            String rule = tableRules.get(entry.getKey());
//            ColumnTypeEnums columnTypeEnums = ColumnTypeEnums.findType(rule);
//            if (ColumnTypeEnums.Known.equals(columnTypeEnums)){
//                return OperateResult.error("参数校验未通过," + "非法参数类型:" + rule);
//            }
//            if (!checkColumnType(columnTypeEnums,entry.getValue())){
//                return OperateResult.error("参数校验未通过," + "未匹配参数:" + entry.getValue());
//            }
//        }
        return OperateResult.ok("参数校验通过");
    }

    public OperateResult checkDeleteResultLegal(Entry<Integer, List<String>> deleteEntity){
        if (null == deleteEntity.getKey()){
            return OperateResult.error("删除校验未通过," + "无匹配删除值");
        }
        return OperateResult.ok("删除校验通过");
    }



    public boolean checkColumnType(ColumnTypeEnums columnTypeEnums,String insertItem){
        boolean flag = true;
        switch (columnTypeEnums){
            case Int:
                flag = ifInt(insertItem);
                break;
            case Char:
                flag = ifChar(insertItem);
                break;
            case Double:
                flag = ifDouble(insertItem);
                break;
            case VarChar:
                flag = ifString(insertItem);
                break;
        }
        return flag;
    }

    public boolean ifInt(String IntType){
        String pattern = "[+-]?[0-9]+";
        return Pattern.matches(pattern,IntType);
    }

    public boolean ifDouble(String DoubleType){
        String pattern = "[+-]?[0-9]+(\\.[0-9]+)";
        return Pattern.matches(pattern,DoubleType);
    }

    public boolean ifChar(String CharType){
        String pattern = "[a-z|A-Z]";
        return Pattern.matches(pattern,CharType);
    }

    public boolean ifString(String StringType){
        String pattern = "[a-z|A-Z]+";
        return Pattern.matches(pattern,StringType);
    }
}
