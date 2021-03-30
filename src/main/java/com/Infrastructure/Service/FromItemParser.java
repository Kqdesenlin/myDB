package com.Infrastructure.Service;

import com.Infrastructure.TableInfo.TableInfo;
import com.Infrastructure.Visitor.FromItemVisitorWithRtn;
import net.sf.jsqlparser.statement.select.FromItem;

/**
 * @author: zhangQY
 * @date: 2021/3/29
 * @description:
 */
public class FromItemParser {

    /**
     * 将fromitem转化为tableInfo
     * @param fromItem
     * @return
     */
    public static TableInfo fromItemToTable(FromItem fromItem) {
        FromItemVisitorWithRtn visitorWithRtn = new FromItemVisitorWithRtn();
        fromItem.accept(visitorWithRtn);
        return visitorWithRtn.getTableInfo();
    }
}
