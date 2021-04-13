package com.domain.event;

import com.Infrastructure.TableInfo.TableInfo;
import com.domain.Entity.bTree.BTree;
import com.domain.Entity.bTree.Entry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/4/13
 * @description: 对传入的table进行limit操作，最终返回一个过滤之后的table
 */
public class LimitOperate {

    /**
     * 暂时采用删除的方式，进行过滤
     * 未来考虑根据limit中数据量的大小
     * 考虑删除，或者时新建的操作
     *
     * n,m n+1  -- m
     */
    public List<List<String>> limit(List<List<String>> selectResult, int end) {
        return limit(selectResult,0,end);
    }

    public List<List<String>> limit(List<List<String>> selectResult,int begin, int end) {
        end = begin + end;
        List<List<String>> limitResult = new ArrayList<>();
        for (int var1 = 0; var1 < selectResult.size();++var1) {
            if ((var1>=begin)&&(var1<end)) {
                limitResult.add(selectResult.get(var1));
            }
        }
        return limitResult;
    }

    public TableInfo limit(TableInfo table,int end) {
        return limit(table,0,end);
    }

    public TableInfo limit(TableInfo table, int begin, int end) {
        end = begin + end;
        BTree<Integer, List<String>> bTree = table.getBTree();
        Iterator<Entry<Integer, List<String>>> iterator = bTree.iterator();
        List<Integer> limitFilterPK = new ArrayList<>();
        //查找需要去除的key
        while (iterator.hasNext()) {
            Entry<Integer,List<String>> entry = iterator.next();
            int index = entry.getKey();
            if (index <=begin || index > end) {
                limitFilterPK.add(index);
            }
        }

        for (Integer filterPK : limitFilterPK) {
            bTree.delete(filterPK);
        }
        return table;
    }

    public TableInfo limitByCreate(TableInfo table) {
        return null;
    }

    public TableInfo limitByDelete(TableInfo table) {
        return null;
    }

}
