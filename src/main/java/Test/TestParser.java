package Test;

import junit.framework.Test;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import java.util.logging.Logger;

public class TestParser {

    private static Logger logger = Logger.getLogger("log_" + TestParser.class.getName());

    public static void main(String[] args)throws Exception{
        String sql = "select * from t_test;";
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (statement instanceof Select){
            logger.info("this sql is select");
            return;
        }
        if (statement instanceof Insert){
            logger.info("this sql is insert");
            return;
        }
        if (statement instanceof Update){
            logger.info("this sql is update");
            return;
        }
        if (statement instanceof Delete){
            logger.info("this sql is delete");
            return;
        }
    }
}
