package com.domain;

import com.BTree.BTree;
import com.Constant.TableConstant;
import com.Infrastructure.Entity.CreateEntity;

import com.Infrastructure.Entity.OperateResult;
import com.Infrastructure.Entity.ResultCode;
import com.Infrastructure.Enum.ColumnTypeEnums;
import com.Infrastructure.TableInfo.TableInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String,String> tableRules = createEntity.getRules();
        //判断是否已存在
        if (checkOperate.ifTableExists(createEntity.getTableName())){
            rtn = OperateResult.error("表已存在");
            return rtn;
        }
        //判断规则是否合法
        rtn = checkOperate.ifRulesLegal(createEntity);
        if (ResultCode.ok != rtn.getCode()){
            return rtn;
        }
        List<String> rulesOrder = new ArrayList<>(tableRules.keySet());
        TableInfo newTable = new TableInfo(new BTree<Integer,List<String>>(),tableRules,rulesOrder);
        TableConstant.tableMap.put(tableName,newTable);
        return rtn;

    }

    public OperateResult createTempTable(){

    }

}
