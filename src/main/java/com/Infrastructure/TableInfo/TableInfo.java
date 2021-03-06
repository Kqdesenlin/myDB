package com.Infrastructure.TableInfo;

import com.Infrastructure.IndexInfo.IndexInfo;
import com.domain.Entity.bTree.BTree;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 具体表信息
 */
@Data
@AllArgsConstructor
public class TableInfo implements Cloneable{
    String tableName;
    BTree<Integer,List<String>> bTree;
    List<ColumnInfo> columnInfoList;
    List<String> rulesOrder;
    List<IndexInfo> indexInfos;
    public AtomicInteger primaryKey = new AtomicInteger(1);

    public TableInfo() {}

    public TableInfo(BTree<Integer, List<String>> bTree, List<ColumnInfo> columnInfoList, List<String> rulesOrder) {
        this.bTree = bTree;
        this.columnInfoList = columnInfoList;
        this.rulesOrder = rulesOrder;
        this.indexInfos = new ArrayList<>();
    }
    public TableInfo(String tableName,BTree<Integer, List<String>> bTree, List<ColumnInfo> columnInfoList, List<String> rulesOrder) {
        this.tableName = tableName;
        this.bTree = bTree;
        this.columnInfoList = columnInfoList;
        this.rulesOrder = rulesOrder;
        this.indexInfos = new ArrayList<>();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
