package com.Test.BTreeTest;

import com.domain.Entity.bTree.BTree;
import com.domain.Entity.bTree.Entry;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: zhangQY
 * @date: 2021/4/25
 * @description:
 */
@Slf4j
public class SameEntryTest {

    public static void main(String... args)throws Exception {
        BTree<String,Integer> bTree = new BTree<>();
        bTree.addNode(new Entry<>("aaa",1));
        bTree.addNode(new Entry<>("aab",2));
        bTree.addNode(new Entry<>("aaa",3));
        log.info("");
    }
}
