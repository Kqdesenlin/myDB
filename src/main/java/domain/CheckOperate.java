package domain;

import Constant.TableConstant;
import Infrastructure.Entity.CreateEntity;
import Infrastructure.Entity.InsertEntity;
import Infrastructure.Entity.OperateResult;
import Infrastructure.Enum.ColumnTypeEnums;

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
        for (String type : createEntity.getRules().keySet()){
            if (!ColumnTypeEnums.ifContains(type)){
                return OperateResult.error("创建关键字不存在");
            }
        }
        return OperateResult.ok("关键词合法");
    }

    /**
     * 判断插入规则是否合法
     * @param insertEntity
     * @return
     */
    public OperateResult ifInsertItemsLegal(InsertEntity insertEntity){
        return null;
    }

    public boolean ifInt(String Int){
        String pattern = "[+-]?[0-9]+";
        return pattern.matches(Int);
    }
}
