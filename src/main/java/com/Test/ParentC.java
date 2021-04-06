package com.Test;

public class ParentC {

    public static void main(String[] args){
        Thread thread = new Thread() {
            public void run() {
                pong();
            }
        };
        thread.run();
        System.out.println("ping");
    }

    static void pong(){
        System.out.println("pong");
    }
}
