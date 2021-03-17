package Test;

import Constant.TableConstant;
import Infrastructure.Entity.*;
import Infrastructure.TableInfo.TableInfo;
import com.google.common.collect.Lists;
import domain.DDLOperate;
import domain.DMLOperate;
import edu.emory.mathcs.backport.java.util.Arrays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DDLTest {
    public static String tableName = "test_t";
    Logger log = Logger.getLogger("log_ddl");

    public static void main(String[] args)throws Exception{
        if (!createTable()){
            return;
        }
        if (!insertItems()){
            return;
        }
        if (!selectItems()){
            return;
        }


    }

    public static boolean createTable(){
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
        InsertEntity insertEntity = new InsertEntity();
        insertEntity.setTableName(tableName);

        Map<String,String> map = new HashMap<>();
        map.put("StringColumn","stringTestabcdefg");
        map.put("IntColumn","987654321");
        map.put("DoubleColumn","12345.6789");
        map.put("CharColumn","c");
        insertEntity.setItems(map);
        DMLOperate dmlOperate = new DMLOperate();
        OperateResult operateResult = dmlOperate.insert(insertEntity);
        if(ResultCode.ok == operateResult.code){
            System.out.println("插入成功");
            return true;
        }
        System.out.println(operateResult.info);
        return false;
    }

    public static boolean selectItems(){
        SelectEntity selectEntity = new SelectEntity();
        selectEntity.setTableName(tableName);
        List<String> list = new ArrayList<>();
        list.add("StringColumn");
        list.add("IntColumn");
        list.add("DoubleColumn");
        selectEntity.setSelectItems(list);
        DMLOperate dmlOperate = new DMLOperate();
        SelectResult selectResult = dmlOperate.selectSingleTable(selectEntity);
        if (ResultCode.ok == selectResult.code){
            printSelectResult(selectResult);
            return true;
        }
        System.out.println(selectResult.info);
        return false;
    }

    public static void printSelectResult(SelectResult selectResult){
        List<String> rules = selectResult.getRules();
        List<List<String>> filtedItems = selectResult.getItems();
        for (String rule : rules){
            System.out.print(rule + " ");
        }
        System.out.println("");
        for (List<String> row : filtedItems){
            for(String item : row){
                System.out.print(item + " ");
            }
            System.out.println("");
        }
    }
}
