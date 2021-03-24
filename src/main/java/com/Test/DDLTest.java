package com.Test;

import com.domain.repository.TableConstant;
import com.domain.Entity.*;
import com.domain.Entity.result.OperateResult;
import com.domain.Entity.result.ResultCode;
import com.domain.Entity.result.SelectResult;
import com.Infrastructure.TableInfo.TableInfo;
import com.domain.event.DDLOperate;
import com.domain.event.DMLOperate;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DDLTest {
    public static String tableName = "test_t";
    public static Logger logger = Logger.getLogger("log_ddl");

    public static void main(String[] args)throws Exception{
        String sql = "insert into S(var1,var2,var3,var4) values('S8','Dong',18,'F');";
        Statement statement =  CCJSqlParserUtil.parse(sql);
        if (statement instanceof Insert) {
            Insert insert = (Insert) statement;
        }


    }

    public static boolean createTable(){
//        CreateEntity createEntity = new CreateEntity();
//        createEntity.setTableName(tableName);
//        Map<String,String> rules = new HashMap<>();
//        rules.put("StringColumn","String");
//        rules.put("IntColumn","Int");
//        rules.put("DoubleColumn","Double");
//        rules.put("CharColumn","Char");
//        createEntity.setRules(rules);
//        DDLOperate ddlOperate = new DDLOperate();
//        OperateResult operateResult = ddlOperate.create(createEntity);
//        System.out.println(operateResult.getInfo());
//        if (operateResult.getCode() == ResultCode.ok){
//            TableInfo tableInfo = TableConstant.tableMap.get(tableName);
//            System.out.println("建表成功");
//            System.out.println(tableInfo.toString());
//            return true;
//        }
//        System.out.println("建表失败");
        return false;
    }

    public static boolean insertItems(){
//        InsertEntity insertEntity = new InsertEntity();
//        insertEntity.setTableName(tableName);
//
//        Map<String,String> map = new HashMap<>();
//        map.put("StringColumn","stringTestabcdefg");
//        map.put("IntColumn","987654321");
//        map.put("DoubleColumn","12345.6789");
//        map.put("CharColumn","c");
//        insertEntity.setItems(map);
//        DMLOperate dmlOperate = new DMLOperate();
//        OperateResult operateResult = dmlOperate.insert(insertEntity);
//        if(ResultCode.ok == operateResult.getCode()){
//            System.out.println("插入成功");
//            return true;
//        }
//        System.out.println(operateResult.getInfo());
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
        SelectResult selectResult = dmlOperate.selectTotalTable(selectEntity);
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
