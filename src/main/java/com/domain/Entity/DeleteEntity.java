package com.domain.Entity;

import com.Infrastructure.TableInfo.TableInfo;
import lombok.Data;
import net.sf.jsqlparser.expression.Expression;

@Data
public class DeleteEntity {
    TableInfo tableInfo;
    Expression expression;
}
