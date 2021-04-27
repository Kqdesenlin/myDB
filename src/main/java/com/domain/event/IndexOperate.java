package com.domain.event;

import com.Infrastructure.IndexInfo.IndexInfo;
import com.Infrastructure.IndexInfo.IndexValues;
import com.Infrastructure.Service.TypeConverUtils;
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
 * 索引思路，再次修改，索引只使用一个
 * 因为多个索引的使用，本质上很难提高查询速度
 * 例如，总行数为2000行，第一个索引条件，回表200行，而第二个索引条件，本质上
 * 也只是回表200行，如果要使用两个索引，就需要对这200和200取交集，而
 * 在不确定的情况下，取交集返回的行数不确定，同时，计算交集也需要很多时间
 * 因此，在一般情况下，不考虑使用多个索引
 * @author: zhangQY
 * @date: 2021/4/25
 * @description:
 *
 */
public class IndexOperate {

    /**
     * 返回indexlist的集合
     * 如果为空，说明索引查找效率低于全表
     * 如果不为空，则使用对应的索引
     * @param tableInfo tableInfo
     * @param expression expression
     * @return indexInfo
     */
    public IndexInfo indexMatch(TableInfo tableInfo, Expression expression) {
        List<List<IndexValues>> indexAfterRule = indexMatchOnRule(tableInfo,expression);
        if (null == indexAfterRule || indexAfterRule.isEmpty()) {
            return null;
        }
        List<IndexValues> indexAfterCost = indexMatchOnCost(tableInfo,expression,indexAfterRule);
        if (null == indexAfterCost || indexAfterCost.isEmpty()) {
            return null;
        }
        return indexAfterCost.get(0).getIndexInfo();
    }

    /**
     * 2021/4/26 索引思路再次修改
     * 之前将单一索引和组合索引的思路分开思考
     * 现在将两个混合思考
     * 组合索引，本质上视作 分集 例如 组合索引 id,custId,posId
     * 看作三个索引 id,  (id,custId),(id,custId,posId)
     * 而单一索引，例如 id,看作id
     * 因此
     * 在and or 这种二元判断的时候
     * 首先，获取左边的索引，例如获取了一个id
     * 这时候再获取右边，如果右边获取的索引，第一个是custId
     * 将两个整合在一起，例如id,custId,这个在索引表中，则说明可以使用索引
     * 如果是id,posId这种不在索引集中的，则只计算其中
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
    public List<List<IndexValues>> indexMatchOnRule(TableInfo tableInfo, Expression expression) {
        List<String> columnOrder = tableInfo.getRulesOrder();
        List<IndexInfo> indexInfoList = tableInfo.getIndexInfos();
        BTree<Integer,List<String>> bTree = tableInfo.getBTree();
        Iterator<Entry<Integer,List<String>>> iterator = bTree.iterator();
        if (null != expression && iterator.hasNext() && !indexInfoList.isEmpty()) {
            IndexExpressionVisitorWithRtn visitor = new IndexExpressionVisitorWithRtn(indexInfoList);
            expression.accept(visitor);
            return visitor.getRtn();
        }
        return null;
    }

    /**
     * 基于成本的索引分析
     *
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
    public List<IndexValues> indexMatchOnCost(TableInfo tableInfo, Expression expression,List<List<IndexValues>> lists) {
        if (null == lists || lists.isEmpty()) {
            return new ArrayList<>();
        }
        double totalNumber = 100;
        double eachCalculate = 0.2;
        double times = totalNumber * eachCalculate;
        List<IndexValues> finalIndex = new ArrayList<>();
        //计算所有索引
        //1 2 3, 4, 5 6
        //-> 1,12,123,4,5,56
        for (IndexInfo indexInfo : tableInfo.getIndexInfos()) {

            //1 2 3-> 1, 12, 123
            List<String> tempIndexs = indexInfo.getRulesOrder();
            List<String> tempString = new ArrayList<>();
            List<List<String>> tableIndexLists = new ArrayList<>();
            for (String s : tempIndexs) {
                tempString.add(s);
                List<String> temptemp = new ArrayList<>(tempString);
                tableIndexLists.add(temptemp);
            }
            for (List<IndexValues> indexToCost : lists) {
                List<String> tempIndexNameList = indexToCost.stream().map(IndexValues::getIndexName).collect(Collectors.toList());
                indexToCost.forEach(indexValues -> {
                    indexValues.setIndexInfo(indexInfo);
                });
                if (tableIndexLists.contains(tempIndexNameList)) {
                    int rtnCount = 0;
                    String cutLeftString = "";
                    String cutRightString = "";
                    for (IndexValues values : indexToCost) {
                        cutLeftString = cutLeftString + (values.getKeyPoint().get(0));
                        cutRightString = cutRightString + values.getKeyPoint().get(1);
                    }
                    String min = indexInfo.getMin();
                    String max = indexInfo.getMax();
                    double rate = TypeConverUtils.StringRate(cutLeftString, cutRightString, min, max);

                    if (times > (rate * totalNumber * eachCalculate * 2)) {
                        times = rate * totalNumber * eachCalculate * 2;
                        finalIndex = indexToCost;
                    }
                }
            }
        }
        return finalIndex;
    }

    /**
     * 通过索引查询
     * @param tableInfo tableInfo
     * @param expression expression
     * @param indexInfo index
     * @return tableInfo
     */
    public TableInfo searchByIndex(TableInfo tableInfo, Expression expression,IndexInfo indexInfo) {
        List<Integer> pkList = WhereOperate.indexWhere(indexInfo,expression);
        return WhereOperate.whereFiterPK(tableInfo,expression,pkList);
    }
}
