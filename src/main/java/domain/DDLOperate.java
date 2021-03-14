package domain;

import BTree.BTree;
import Constant.TableConstant;
import Infrastructure.Entity.CreateEntity;

import Infrastructure.Entity.OperateResult;
import Infrastructure.Entity.ResultCode;
import Infrastructure.Enum.ColumnTypeEnums;
import Infrastructure.TableInfo.TableInfo;

import java.util.HashMap;

public class DDLOperate {

    private CheckOperate checkOperate = new CheckOperate();

    /**
     * 创建表
     * @param createEntity
     * @return
     */
    public  OperateResult createTable(CreateEntity createEntity){
        OperateResult rtn = OperateResult.ok("建表成功");
        String tableName = createEntity.getTableName();
        //判断是否已存在
        if (checkOperate.ifTableExists(createEntity.getTableName())){
            rtn = OperateResult.error("表已存在");
            return rtn;
        }
        //判断规则是否合法
        rtn = checkOperate.ifRulesLegal(createEntity);
        if (ResultCode.ok != rtn.code){
            return rtn;
        }
        TableInfo newTable = new TableInfo(new BTree(),new HashMap<>());
        TableConstant.tableMap.put(tableName,newTable);
        return rtn;

    }

}
