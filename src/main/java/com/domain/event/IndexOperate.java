package com.domain.event;

import com.Infrastructure.IndexInfo.IndexInfo;
import com.Infrastructure.TableInfo.TableInfo;
import com.Infrastructure.Visitor.IndexVisitor.IndexExpressionVisitorWithRtn;
import com.domain.Entity.bTree.BTree;
import com.domain.Entity.bTree.Entry;
import net.sf.jsqlparser.expression.Expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhangQY
 * @date: 2021/4/25
 * @description:
 */
public class IndexOperate {

    /**
     * 返回indexlist的集合
     * 如果为空，说明索引查找效率低于全表
     * 如果不为空，则使用对应的索引
     * @param tableInfo
     * @param expression
     * @return
     */
    public List<String> indexMatch(TableInfo tableInfo, Expression expression) {
        List<List<String>> indexAfterRule = indexMatchOnRule(tableInfo,expression);
        if (null == indexAfterRule || indexAfterRule.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> indexAfterCost = indexMatchOnCost(tableInfo,expression,indexAfterRule);
        if (null == indexAfterCost || indexAfterCost.isEmpty()) {
            return new ArrayList<>();
        }
        return null;
    }

    /**
     * 基于规则的索引分析
     * 1. 查询的字段需要有索引
     * 2. 索引在>,<,=,<=,>=,between,is null,like,
     * 3. 索引不用于or, not in
     * 4. 索引在like上不属于最左匹配
     *
     * 最终返回所有可能使用的索引组合
     * @param tableInfo
     * @param expression
     * @return
     */
    public List<List<String>> indexMatchOnRule(TableInfo tableInfo, Expression expression) {
        List<String> columnOrder = tableInfo.getRulesOrder();
        List<IndexInfo> indexInfoList = tableInfo.getIndexInfos();
        BTree<Integer,List<String>> bTree = tableInfo.getBTree();
        Iterator<Entry<Integer,List<String>>> iterator = bTree.iterator();
        if (null != expression && iterator.hasNext() && !indexInfoList.isEmpty()) {
            List<String> indexNameList = indexInfoList.stream().map(IndexInfo::getIndexName).collect(Collectors.toList());
            IndexExpressionVisitorWithRtn visitor = new IndexExpressionVisitorWithRtn(indexNameList);
            expression.accept(visitor);
            return visitor.getRtn();
        }
        return null;
    }

    /**
     * 基于成本的索引分析
     *
     * 根据给定的所有索引使用规则
     * 简单的索引存在的数量的思路
     * 1. 如果是单一区间
     * 直接去索引里面查寻对应的值，如果在很下面的叶子节点找到，说明找了很久，值基本上唯一
     * 如果在上面的root节点就找到了，说明这个值可能很好找，说明索引重复的较多，需要回表的较多
     * 则，查到的需要回表的索引id数，为tree的高度减去节点的高度
     * 同时，在b树上查找到的时间，定位高度/2
     * 基本大体思路为 tree的高度，减去找到节点的位置的高度，然后乘上，就是最终索引查找的个数
     * 最终的成本是，个数*0.2 + 个数*0.2 = 0.4*个数
     *
     * 2. 如果是多区间
     * 则，根据多区间的间隔点，如 >=1 and <=2
     * 如果是一个索引，a>=1 and a<=2
     * 在索引中，预先添加，最左和最右的值
     * 模糊判断，区间和区间之间的大小
     * 根据所占比例*总条数，返回总数，总数*在索引中查找每条预估需要0.2的陈本
     * 然后，每条id在主表中对剩余的条件，判断，需要0.2的成本
     * 因此，最终需要，预估条数*0.4
     *
     * @param tableInfo
     * @param expression
     * @return
     */
    public List<String> indexMatchOnCost(TableInfo tableInfo, Expression expression,List<List<String>> lists) {
        if (null == lists || lists.isEmpty()) {
            return new ArrayList<>();
        }
        return null;
    }

    /**
     * 通过索引查询
     * @param tableInfo
     * @param expression
     * @param indexInfoList
     * @return
     */
    public TableInfo searchByIndex(TableInfo tableInfo, Expression expression,List<IndexInfo> indexInfoList) {
        return null;
    }
}
