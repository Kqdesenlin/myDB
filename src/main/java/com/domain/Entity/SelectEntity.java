package com.domain.Entity;

import com.Infrastructure.TableInfo.TableInfo;
import lombok.Data;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.List;

@Data
public class SelectEntity {
    TableInfo tableInfo;
    Expression whereExpression;
    List<SelectItem> selectItemList;
}
