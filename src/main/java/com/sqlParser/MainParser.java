package sqlParser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;

import java.util.logging.Logger;

public class MainParser {

    private static Logger logger = Logger.getLogger("log_" + MainParser.class.getName());

    public void entrance(String sql){
        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        logger.info(sql);
        if (statement instanceof Select){
//            logger.info("this sql is select");
            selectParser((Select) statement);
        }
        if (statement instanceof Insert){
//            logger.info("this sql is insert");
            insertParser((Insert) statement);
        }
        if (statement instanceof Update){
//            logger.info("this sql is update");
            updateParser((Update) statement);
        }
        if (statement instanceof Delete){
//            logger.info("this sql is delete");
            deleteParser((Delete) statement);
        }
//    public void mainParsing(String sql)throws Exception{
//        Statement statement = CCJSqlParserUtil.parse(sql);

//        Select selectStatement = (Select) statement;
//
//        //获取select和from之间的items
//        List<SelectItem> selectItems = plainSelect.getSelectItems();
//        System.out.println(selectItems.toString());
//        //获取表名
//        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
//        List<String> tables = tablesNamesFinder.getTableList(selectStatement);
//        System.out.println(tables.toString());
//        Expression where = plainSelect.getWhere();
//        whereJudge(where);

    }

    public void whereJudge(Expression where){
        if(where instanceof AndExpression){
            AndExpression andExpression = (AndExpression) where;
            Expression leftExpression = andExpression.getLeftExpression();
            Expression rightExpression = andExpression.getRightExpression();
            whereJudge(leftExpression);
            whereJudge(rightExpression);
        } else if(where instanceof EqualsTo){
            System.out.println(where.toString());
        }
    }

    /**
     * 查找解析
     * @param select
     */
    public void selectParser(Select select){
        PlainSelect plainSelect = (PlainSelect)select.getSelectBody();
        FromItem fromItem = plainSelect.getFromItem();

        if (fromItem instanceof Table){
            logger.info("this is table");
        }else if (fromItem instanceof SubSelect){
            logger.info("this is subselect");
        }
    }

    /**
     * 插入解析
     * @param insert
     */
    public void insertParser(Insert insert){

    }

    /**
     * 更新解析
     * @param update
     */
    public void updateParser(Update update){

    }

    /**
     * 删除解析
     * @param delete
     */
    public void deleteParser(Delete delete){

    }


}
