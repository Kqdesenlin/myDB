package domain;

import BTree.Entry;
import Constant.TableConstant;
import Infrastructure.Entity.InsertEntity;
import Infrastructure.Entity.OperateResult;
import Infrastructure.Entity.ResultCode;
import Infrastructure.Entity.SelectEntity;
import Infrastructure.Service.TypeConverUtils;
import Infrastructure.TableInfo.TableInfo;

import java.util.List;

public class DMLOperate {

    private CheckOperate checkOperate = new CheckOperate();
    public List<Object> selectSingleTable(SelectEntity selectEntity){
        String tableName = selectEntity.getTableName();
        if (!checkOperate.ifTableExists(tableName)){
            return
        }
        return null;
    }


    public OperateResult insert(InsertEntity insertEntity) {
        String tableName = insertEntity.getTableName();
        if (!checkOperate.ifTableExists(tableName)){
            return OperateResult.error("插入表不存在");
        }
        if(ResultCode.ok.getResultCode()
                != checkOperate.ifInsertItemsLegal(insertEntity).code.getResultCode()){
            return OperateResult.error("参数校验失败");
        }
        TableInfo tableInfo = TableConstant.getTableByName(tableName);
        List<String> insertItems = TypeConverUtils.mapToListByListOrder(
                insertEntity.getItems(),tableInfo.getRulesOrder());
        Integer primaryKey = tableInfo.primaryKey.getAndIncrement();
        Entry<Integer,List<String>> insertEntry = new Entry<Integer,List<String>>(primaryKey,insertItems);
        tableInfo.getBTree().addNode(insertEntry);
        return OperateResult.ok("插入成功");
    }
}
