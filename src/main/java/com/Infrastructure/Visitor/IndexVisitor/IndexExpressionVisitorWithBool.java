package com.Infrastructure.Visitor.IndexVisitor;
import com.Infrastructure.IndexInfo.IndexColumnResult;
import com.Infrastructure.TableInfo.ColumnValueInfo;
import com.Infrastructure.Visitor.BinaryExpressionParser;
import lombok.Data;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/3/25
 * @description:
 */
@Data
public class IndexExpressionVisitorWithBool implements ExpressionVisitor {

    private ColumnValueInfo columnValueInfo;

    private boolean ifPass;

    public IndexExpressionVisitorWithBool(ColumnValueInfo columnValueInfo) {
        this.columnValueInfo = columnValueInfo;
        this.ifPass = true;
    }


    //解析到是一个括号(插入语)
    @Override
    public void visit(Parenthesis parenthesis) {
        parenthesis.getExpression().accept(this);
    }


    @Override
    public void visit(EqualsTo equalsTo) {
        List<String> leftAndRight = BinaryExpressionParser.parser(equalsTo,columnValueInfo);
        String left = leftAndRight.get(0);
        String right = leftAndRight.get(1);
        if (left.equals(right)) {
            this.ifPass = true;
        } else {
            this.ifPass = false;
        }
    }

    @Override
    public void visit(SubSelect subSelect) {

    }

    @Override
    public void visit(BitwiseRightShift aThis) {

    }

    @Override
    public void visit(BitwiseLeftShift aThis) {

    }

    @Override
    public void visit(NullValue nullValue) {

    }

    @Override
    public void visit(Function function) {

    }

    @Override
    public void visit(SignedExpression signedExpression) {

    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {

    }

    @Override
    public void visit(JdbcNamedParameter jdbcNamedParameter) {

    }

    @Override
    public void visit(DoubleValue doubleValue) {

    }

    @Override
    public void visit(LongValue longValue) {

    }

    @Override
    public void visit(HexValue hexValue) {

    }

    @Override
    public void visit(DateValue dateValue) {

    }

    @Override
    public void visit(TimeValue timeValue) {

    }

    @Override
    public void visit(TimestampValue timestampValue) {

    }

    @Override
    public void visit(StringValue stringValue) {

    }

    @Override
    public void visit(Addition addition) {

    }

    @Override
    public void visit(Division division) {

    }

    @Override
    public void visit(IntegerDivision division) {

    }

    @Override
    public void visit(Multiplication multiplication) {

    }

    @Override
    public void visit(Subtraction subtraction) {

    }

    @Override
    public void visit(AndExpression andExpression) {
        List<Boolean> booleanList = BinaryExpressionParser.binaryMarkJudge(andExpression,this.columnValueInfo);
        this.ifPass = booleanList.get(0) && booleanList.get(1);
    }

    @Override
    public void visit(OrExpression orExpression) {
        List<Boolean> booleanList = BinaryExpressionParser.binaryMarkJudge(orExpression,this.columnValueInfo);
        this.ifPass = booleanList.get(0) || booleanList.get(1);
    }

    @Override
    public void visit(Between between) {

    }

    @Override
    public void visit(GreaterThan greaterThan) {
        IndexColumnResult leftAndRightAndIndex = BinaryExpressionParser.indexParser(greaterThan,columnValueInfo);
        if (leftAndRightAndIndex.isIfMark()) {
            this.ifPass = true;
            return;
        }
        String left = leftAndRightAndIndex.getLeft();
        String right = leftAndRightAndIndex.getRight();
        this.ifPass = left.compareTo(right) > 0;
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        IndexColumnResult leftAndRightAndIndex = BinaryExpressionParser.indexParser(greaterThanEquals,columnValueInfo);
        if (leftAndRightAndIndex.isIfMark()) {
            this.ifPass = true;
            return;
        }
        String left = leftAndRightAndIndex.getLeft();
        String right = leftAndRightAndIndex.getRight();
        this.ifPass = left.compareTo(right) >= 0;
    }

    @Override
    public void visit(InExpression inExpression) {

    }

    @Override
    public void visit(FullTextSearch fullTextSearch) {

    }

    @Override
    public void visit(IsNullExpression isNullExpression) {

    }

    @Override
    public void visit(IsBooleanExpression isBooleanExpression) {

    }

    @Override
    public void visit(LikeExpression likeExpression) {

    }

    @Override
    public void visit(MinorThan minorThan) {
        IndexColumnResult leftAndRightAndIndex = BinaryExpressionParser.indexParser(minorThan,columnValueInfo);
        if (leftAndRightAndIndex.isIfMark()) {
            this.ifPass = true;
            return;
        }
        String left = leftAndRightAndIndex.getLeft();
        String right = leftAndRightAndIndex.getRight();
        this.ifPass = left.compareTo(right) < 0;
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        IndexColumnResult leftAndRightAndIndex = BinaryExpressionParser.indexParser(minorThanEquals,columnValueInfo);
        if (leftAndRightAndIndex.isIfMark()) {
            this.ifPass = true;
            return;
        }
        String left = leftAndRightAndIndex.getLeft();
        String right = leftAndRightAndIndex.getRight();
        this.ifPass = left.compareTo(right) <= 0;
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        IndexColumnResult leftAndRightAndIndex = BinaryExpressionParser.indexParser(notEqualsTo,columnValueInfo);
        if (leftAndRightAndIndex.isIfMark()) {
            this.ifPass = true;
            return;
        }
        String left = leftAndRightAndIndex.getLeft();
        String right = leftAndRightAndIndex.getRight();
        this.ifPass = !left.equals(right);
    }

    @Override
    public void visit(Column tableColumn) {

    }

    @Override
    public void visit(CaseExpression caseExpression) {

    }

    @Override
    public void visit(WhenClause whenClause) {

    }

    @Override
    public void visit(ExistsExpression existsExpression) {

    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {

    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {

    }

    @Override
    public void visit(Concat concat) {

    }

    @Override
    public void visit(Matches matches) {

    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {

    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {

    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {

    }

    @Override
    public void visit(CastExpression cast) {

    }

    @Override
    public void visit(Modulo modulo) {

    }

    @Override
    public void visit(AnalyticExpression aexpr) {

    }

    @Override
    public void visit(ExtractExpression eexpr) {

    }

    @Override
    public void visit(IntervalExpression iexpr) {

    }

    @Override
    public void visit(OracleHierarchicalExpression oexpr) {

    }

    @Override
    public void visit(RegExpMatchOperator rexpr) {

    }

    @Override
    public void visit(JsonExpression jsonExpr) {

    }

    @Override
    public void visit(JsonOperator jsonExpr) {

    }

    @Override
    public void visit(RegExpMySQLOperator regExpMySQLOperator) {

    }

    @Override
    public void visit(UserVariable var) {

    }

    @Override
    public void visit(NumericBind bind) {

    }

    @Override
    public void visit(KeepExpression aexpr) {

    }

    @Override
    public void visit(MySQLGroupConcat groupConcat) {

    }

    @Override
    public void visit(ValueListExpression valueList) {

    }

    @Override
    public void visit(RowConstructor rowConstructor) {

    }

    @Override
    public void visit(OracleHint hint) {

    }

    @Override
    public void visit(TimeKeyExpression timeKeyExpression) {

    }

    @Override
    public void visit(DateTimeLiteralExpression literal) {

    }

    @Override
    public void visit(NotExpression aThis) {

    }

    @Override
    public void visit(NextValExpression aThis) {

    }

    @Override
    public void visit(CollateExpression aThis) {

    }

    @Override
    public void visit(SimilarToExpression aThis) {

    }

    @Override
    public void visit(ArrayExpression aThis) {

    }

    @Override
    public void visit(VariableAssignment aThis) {

    }

    @Override
    public void visit(XMLSerializeExpr aThis) {

    }
}
