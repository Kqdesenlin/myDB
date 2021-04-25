package com.Infrastructure.IndexInfo;

import com.Infrastructure.TableInfo.ColumnInfo;
import com.domain.Entity.bTree.BTree;
import com.domain.Entity.enums.IndexTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: zhangQY
 * @date: 2021/4/23
 * @description:
 */
@Data
@AllArgsConstructor
public class IndexInfo implements Cloneable{

    String indexName;

    String tableName;

    BTree<Integer, List<String>> bTree;

    List<ColumnInfo> columnInfoList;

    List<String> rulesOrder;

    IndexTypeEnums indexType;

    public AtomicInteger primaryKey = new AtomicInteger(1);

    public IndexInfo() {}

    public IndexInfo(BTree<Integer, List<String>> bTree, List<ColumnInfo> columnInfoList, List<String> rulesOrder) {
        this.bTree = bTree;
        this.columnInfoList = columnInfoList;
        this.rulesOrder = rulesOrder;
    }
    public IndexInfo(String indexName,String tableName,BTree<Integer, List<String>> bTree, List<ColumnInfo> columnInfoList, List<String> rulesOrder,IndexTypeEnums indexType) {
        this.indexName = indexName;
        this.tableName = tableName;
        this.bTree = bTree;
        this.columnInfoList = columnInfoList;
        this.rulesOrder = rulesOrder;
        this.indexType = indexType;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
