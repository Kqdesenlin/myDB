package com.Infrastructure.TableInfo;

import com.domain.Entity.bTree.BTree;
import lombok.AllArgsConstructor;
import lombok.Data;

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
    public AtomicInteger primaryKey = new AtomicInteger(1);

    public TableInfo(BTree<Integer, List<String>> bTree, List<ColumnInfo> columnInfoList, List<String> rulesOrder) {
        this.bTree = bTree;
        this.columnInfoList = columnInfoList;
        this.rulesOrder = rulesOrder;
    }
    public TableInfo(String tableName,BTree<Integer, List<String>> bTree, List<ColumnInfo> columnInfoList, List<String> rulesOrder) {
        this.tableName = tableName;
        this.bTree = bTree;
        this.columnInfoList = columnInfoList;
        this.rulesOrder = rulesOrder;
    }

    @Override
    public TableInfo clone() throws CloneNotSupportedException {
        return (TableInfo)super.clone();
    }
}
