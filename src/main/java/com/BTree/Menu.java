package com.BTree;

/**
 * @author xumg
 * @create 2020-10-15 9:30
 */
public class Menu {
    public static void main(String[] args) {
        BTree<Integer,String> tree=new BTree();
        for(int i=0;i<15;i++){
//            int ran=new Random().nextInt(10000);
            Entry<Integer,String> en=new Entry<>(i,String.valueOf(i+"第" + i + "个节点"));
            tree.addNode(en);
        }
        System.out.println("----------------------");
        tree.outPut();
        System.out.println("----------------------");

        tree.delete(5);
        System.out.println("----------------------");
        tree.delete(7);
        System.out.println("----------------------");
        tree.delete(20);
    }
}
