package Infrastructure.Entity;

import lombok.Data;

/**
 * 表达式内容
 *
 */
@Data
public class ExpressionEntity {
    String leftExpression;
    String symbol;
    String rightExpression;
}
