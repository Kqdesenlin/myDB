package com.domain.Entity.bTree;

import java.util.Iterator;
import java.util.logging.Logger;

/**
 * @author xumg
 * @create 2020-10-15 9:30
 */
public class Menu {

    public static Logger logger = Logger.getLogger("log_" + Menu.class.getSimpleName());
    public static void main(String[] args) {
        BTree<Integer,String> tree=new BTree<Integer,String>(3);
        for(int i=1;i<15;i++){
//            int ran=new Random().nextInt(10000);
            Entry<Integer,String> en=new Entry<>(i,String.valueOf(i+"第" + i + "个节点"));
            tree.addNode(en);
        }
        Iterator<Entry<Integer,String>> iterator = tree.iterator();
        while (iterator.hasNext()) {
            logger.info(iterator.next().toString());
        }
    }
}
