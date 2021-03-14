package domain;

import Constant.TableConstant;
import Infrastructure.Entity.CreateEntity;

import Infrastructure.Entity.OperateResult;
import Infrastructure.Enum.ColumnTypeEnums;

public class DDLOperate {
    /**
     * 是否存在对应名称的表
     * @param tableName
     * @return
     */
    public boolean ifTableExists(String tableName){
        return TableConstant.tableMap.containsKey(tableName);
    }
    public  OperateResult createTable(CreateEntity createEntity){
        String tableName = createEntity.getTableName();
        //判断是否已存在
        if (ifTableExists(tableName)){
            return OperateResult.error("表已存在");
        }
        //判断规则是否合法
        if (ifRulesLegal(createEntity).code == 1)
    }

    public OperateResult ifRulesLegal(CreateEntity createEntity){
        for (String type : createEntity.getRules().keySet()){
            if (!ColumnTypeEnums.ifContains(type)){
                return OperateResult.error("创建关键字不存在");
            }
        }
        return OperateResult.ok("关键词合法");
    }
}
