package com.domain.Entity;

import com.Infrastructure.TableInfo.TableInfo;
import lombok.Data;
import net.sf.jsqlparser.expression.Expression;

import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/4/7
 * @description:
 */
@Data
public class UpdateEntity {

    private TableInfo tableInfo;

    private List<String> columnList;

    private List<Expression> expressionList;

    private Expression expression;

}
