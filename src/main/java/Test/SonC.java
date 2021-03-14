package Test;

import domain.CheckOperate;

public class SonC extends ParentC{
    public static void main(String[] args)throws Exception{
        CheckOperate checkOperate = new CheckOperate();
        System.out.println(checkOperate.ifInt("10"));
    }
}
