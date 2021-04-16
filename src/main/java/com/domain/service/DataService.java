package com.domain.service;

/**
 * @author: zhangQY
 * @date: 2021/4/9
 * @description:
 */

import com.Infrastructure.TableInfo.ColumnInfo;
import com.Infrastructure.TableInfo.TableInfo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.application.dto.FakeNode;
import com.domain.repository.TableConstant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataService {

    public List<FakeNode> getTableInfoDto() {
        List<FakeNode> list = new ArrayList<>();
        for (Map.Entry<String, TableInfo> entry : TableConstant.tableMap.entrySet()){
            FakeNode tempNode = new FakeNode();
            List<FakeNode> tableChild = new ArrayList<>();

            String tableName = entry.getKey();
            TableInfo tableInfo = entry.getValue();
            tempNode.setName(tableName);
            //添加column
            if (null != tableInfo.getColumnInfoList()) {
                FakeNode columnNode = new FakeNode();
                columnNode.setName("column");
                List<FakeNode> columnList = getColumnInfoDto(tableInfo.getColumnInfoList());
                columnNode.setChildren(columnList);
                tableChild.add(columnNode);
            }
            //添加index

            tempNode.setChildren(tableChild);
            list.add(tempNode);
        }
        return list;
    }

    public JSONArray getTableAndColumn() {
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, TableInfo> entry : TableConstant.tableMap.entrySet()) {
            String tableName = entry.getKey();
            TableInfo tableInfo = entry.getValue();
            List<ColumnInfo> columnInfoList = tableInfo.getColumnInfoList();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",tableName);
            jsonObject.put("columns",columnInfoList);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public List<FakeNode> getColumnInfoDto(List<ColumnInfo> columnInfoList) {
        List<FakeNode> rtn = new ArrayList<>();
        for (ColumnInfo column: columnInfoList) {
            FakeNode columnNode = new FakeNode();
            columnNode.setName(column.getColumnName());
            List<FakeNode> columnInfo = getParameterInfoDto(column);
            columnNode.setChildren(columnInfo);
            rtn.add(columnNode);
        }
        return rtn;
    }

    public List<FakeNode> getParameterInfoDto(ColumnInfo columnInfo) {
        List<FakeNode> rtn = new ArrayList<>();
        FakeNode columnName = new FakeNode("name:"+columnInfo.getColumnName(),null);
        rtn.add(columnName);
        FakeNode columnType = new FakeNode("type:"+columnInfo.getColumnType() ,null);
        rtn.add(columnType);
        FakeNode columnNull = new FakeNode("notNull:"+(columnInfo.isNotNull()?"true":"false"),null);
        rtn.add(columnNull);
        return rtn;
    }
}
