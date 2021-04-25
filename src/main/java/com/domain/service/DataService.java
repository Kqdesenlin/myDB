package com.domain.service;

/**
 * @author: zhangQY
 * @date: 2021/4/9
 * @description:
 */

import com.Infrastructure.IndexInfo.IndexInfo;
import com.Infrastructure.TableInfo.ColumnInfo;
import com.Infrastructure.TableInfo.TableInfo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.application.dto.FakeNode;
import com.domain.repository.TableConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
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
            if (null != tableInfo.getIndexInfos()) {
                FakeNode indexNode = new FakeNode();
                indexNode.setName("index");
                List<FakeNode> indexList = getIndexInfoDto(tableInfo.getIndexInfos());
                indexNode.setChildren(indexList);
                tableChild.add(indexNode);
            }
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

    public JSONArray getTableAndIndex() {
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, TableInfo> entry : TableConstant.tableMap.entrySet()) {
            String tableName = entry.getKey();
            TableInfo tableInfo = entry.getValue();
            List<IndexInfo> indexInfoList = tableInfo.getIndexInfos();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",tableName);
            jsonObject.put("indexs",indexInfoList);
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

    public List<FakeNode> getIndexInfoDto(List<IndexInfo> indexInfoList) {
        List<FakeNode> rtn = new ArrayList<>();
        for (IndexInfo index : indexInfoList) {
            FakeNode indexNode = new FakeNode();
            indexNode.setName(index.getIndexName());
            List<FakeNode> indexInfo = getIndexParameterInfoDto(index);
            indexNode.setChildren(indexInfo);
            rtn.add(indexNode);
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

    public List<FakeNode> getIndexParameterInfoDto(IndexInfo indexInfo) {
        List<FakeNode> rtn = new ArrayList<>();
        FakeNode columnList = new FakeNode();
        columnList.setName("columns");
        List<FakeNode> columnNode = getIndexParameterColumnDto(indexInfo.getRulesOrder());
        columnList.setChildren(columnNode);
        rtn.add(columnList);
        FakeNode type = new FakeNode();
        type.setName("type");
        FakeNode typeNode = getIndexParameterTypeDto(indexInfo.getIndexType().getKey());
        List<FakeNode> fakeNodeList = new ArrayList<>();
        fakeNodeList.add(typeNode);
        type.setChildren(fakeNodeList);
        rtn.add(type);
        return rtn;
    }

    public List<FakeNode> getIndexParameterColumnDto(List<String> columnNames) {
        List<FakeNode> fakeNodeList = new ArrayList<>();
        for (String columnName : columnNames) {
            FakeNode fakeNode = new FakeNode();
            fakeNode.setName(columnName);
            fakeNodeList.add(fakeNode);
        }
        return fakeNodeList;
    }

    public FakeNode getIndexParameterTypeDto(String indexType) {
        FakeNode node = new FakeNode();
        node.setName(indexType);
        return node;
    }

    public File createSqlFile(String sql) {
        try {
            File file = new File("sql.txt");
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(sql);
            myWriter.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File("sql.txt");
    }
}
