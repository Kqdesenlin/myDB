package Test;

import Constant.TableConstant;
import Infrastructure.Entity.CreateEntity;
import Infrastructure.Entity.OperateResult;
import Infrastructure.Entity.ResultCode;
import Infrastructure.TableInfo.TableInfo;
import domain.DDLOperate;

import java.util.HashMap;
import java.util.Map;

public class DDLTest {

    public static void main(String[] args)throws Exception{
        if (!createTable()){
            return;
        }

    }

    public static boolean createTable(){
        String tableName = "test_t";
        CreateEntity createEntity = new CreateEntity();
        createEntity.setTableName(tableName);
        Map<String,String> rules = new HashMap<>();
        rules.put("StringColumn","String");
        rules.put("IntColumn","Int");
        rules.put("DoubleColumn","Double");
        rules.put("CharColumn","Char");
        createEntity.setRules(rules);
        DDLOperate ddlOperate = new DDLOperate();
        OperateResult operateResult = ddlOperate.createTable(createEntity);
        System.out.println(operateResult.info);
        if (operateResult.code == ResultCode.ok){
            TableInfo tableInfo = TableConstant.tableMap.get(tableName);
            System.out.println("建表成功");
            System.out.println(tableInfo.toString());
            return true;
        }
        System.out.println("建表失败");
        return false;
    }

    public static boolean insertItems(){
        return false;
    }
}
