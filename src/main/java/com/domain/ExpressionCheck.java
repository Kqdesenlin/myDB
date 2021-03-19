package domain;

import BTree.Entry;
import Infrastructure.Entity.ComplexSelectEntity;
import Infrastructure.Entity.OperateResult;

import java.util.List;
import java.util.Map;

public class ExpressionCheck {

    private Map<String,String> rules;

    private ComplexSelectEntity selectEntity;

    public ExpressionCheck(Map<String,String> rules,ComplexSelectEntity complexSelectEntity){
        this.rules = rules;
        this.selectEntity = complexSelectEntity;
    }

    public OperateResult main(Entry<Integer, List<String>> entry){
        return null;
    }
}