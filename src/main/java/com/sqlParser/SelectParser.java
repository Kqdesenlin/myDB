package sqlParser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:解析select
 */
public class SelectParser {

    public List<String> getSelectItems(String sql)throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        List<SelectItem> selectItems = plain.getSelectItems();
        List<String> strItems = new ArrayList<String>();
        if (null != selectItems) {
            for (SelectItem selectItem : selectItems) {
                strItems.add(selectItem.toString());
            }
        }
        return strItems;
    }

    public List<String> getSelectTables(String sql)throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        Select selectStatement = (Select) statement;
        PlainSelect plain = (PlainSelect) selectStatement.getSelectBody();
        FromItem fromItem = plain.getFromItem();
        if (fromItem instanceof Table){
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            return tablesNamesFinder.getTableList(selectStatement);
        }
        else if (fromItem instanceof SubSelect){
            return null;
        }
        return null;
    }

    public List<String> getJoinTables(String sql)throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        Select selectStatement = (Select) statement;
        PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();
        List<Join> joinList = plainSelect.getJoins();
        List<String> tableWithJoin = new ArrayList<>();
        if (null != joinList) {
            for (Join join : joinList) {
                join.setLeft(true);
                tableWithJoin.add(join.toString());
            }
            return tableWithJoin;
        }
        return null;
    }
}
