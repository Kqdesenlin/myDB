package Antler4Demo;


import org.antlr.v4.runtime.*;


public class Main {

    public static void run(String expr) throws Exception{
        ANTLRInputStream in = new ANTLRInputStream(expr);
        DemoLexer lexer = new DemoLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DemoParser parser = new DemoParser(tokens);
        parser.prog();
    }

    public static void main(String[] args) throws Exception{
        String[] testStr = {"2","a+b+3","(a-b)+3","a+(b*3"};
        for (String s :testStr){
            System.out.println("Input expr:"+s);
            run(s);
        }
    }
}
