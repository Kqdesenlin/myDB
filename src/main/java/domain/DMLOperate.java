package domain;

import Constant.TableConstant;
import Infrastructure.Entity.InsertEntity;
import Infrastructure.Entity.OperateResult;

import java.util.List;

public class DMLOperate {

    private CheckOperate checkOperate = new CheckOperate();
    public List<Object> selectSingleTable(String tableName, List<String> selectItmes){


    }

    public OperateResult insert(InsertEntity insertEntity) {
        if (!checkOperate.ifTableExists(insertEntity.getTableName())){
            return OperateResult.error("插入表不存在");
        }


    }
}
