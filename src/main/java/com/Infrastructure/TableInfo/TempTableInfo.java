package com.Infrastructure.TableInfo;

import com.domain.Entity.bTree.BTree;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:临时表信息
 */

@Data
@AllArgsConstructor
public class TempTableInfo {

    //存放数据的b树
    BTree<Integer, List<String>> bTree;
    //存放的数据的每一列的约束
    Map<String,String> rules;
    //存放数据的属性的顺序
    List<String> ruleOrder;
    //驱动表(可选)
    String driveTable;
    //被驱动表(可选)
    String drivenTable;
    //自增主键
    AtomicInteger primaryKey = new AtomicInteger(1);


    public TempTableInfo(BTree<Integer, List<String>> bTree, Map<String, String> newRules, List<String> newTableRuleOrder, String driveTable, String drivenTable) {
        this.bTree = bTree;
        this.rules = newRules;
        this.ruleOrder = newTableRuleOrder;
        this.driveTable = driveTable;
        this.drivenTable = drivenTable;

    }
}
