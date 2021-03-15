package domain;

import Constant.TableConstant;
import Infrastructure.Entity.CreateEntity;
import Infrastructure.Entity.InsertEntity;
import Infrastructure.Entity.OperateResult;
import Infrastructure.Enum.ColumnTypeEnums;

import java.util.Map;
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
    public OperateResult ifInsertItemsLegal(InsertEntity insertEntity){
        Map<String,String> insertItems = insertEntity.getItems();
        for (Map.Entry<String,String> entry : insertItems.entrySet()){
            ColumnTypeEnums columnTypeEnums = ColumnTypeEnums.findType(entry.getKey());
            if (ColumnTypeEnums.Known.equals(columnTypeEnums)){
                return OperateResult.error("创建关键字不存在");
            }
            if (!checkColumnType(columnTypeEnums,entry.getValue())){
                return OperateResult.error("参数不符合规范");
            }
        }
        return OperateResult.ok("参数校验通过");
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
