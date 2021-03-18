package domain;

import Constant.TableConstant;
import Infrastructure.Entity.CreateEntity;
import Infrastructure.Entity.InsertEntity;
import Infrastructure.Entity.OperateResult;
import Infrastructure.Enum.ColumnTypeEnums;
import Infrastructure.Service.TypeConverUtils;

import java.util.List;
import java.util.Map;
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
     * @param createEntity
     * @return
     */
    public OperateResult ifRulesLegal(CreateEntity createEntity){
        for (Map.Entry<String,String> entry : createEntity.getRules().entrySet()){
            String type = entry.getValue();
            if (!ColumnTypeEnums.ifContains(type)){
                return OperateResult.error("创建关键字 " + type + " 不存在");
            }
        }
        return OperateResult.ok("关键词校验通过");
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
        Map<String,String> insertItems = insertEntity.getItems();
        String tableName = insertEntity.getTableName();
        Map<String,String> tableRules = TableConstant.getTableByName(tableName).getRules();
        for (Map.Entry<String,String> entry : insertItems.entrySet()){
            String rule = tableRules.get(entry.getKey());
            ColumnTypeEnums columnTypeEnums = ColumnTypeEnums.findType(rule);
            if (ColumnTypeEnums.Known.equals(columnTypeEnums)){
                return OperateResult.error("参数校验未通过," + "非法参数类型:" + rule);
            }
            if (!checkColumnType(columnTypeEnums,entry.getValue())){
                return OperateResult.error("参数校验未通过," + "未匹配参数:" + entry.getValue());
            }
        }
        return OperateResult.ok("参数校验通过");
    }

    public OperateResult checkDeleteResultLegal(BTree.Entry<Integer, List<String>> deleteEntity){
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
            case String:
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
