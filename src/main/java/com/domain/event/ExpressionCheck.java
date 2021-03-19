package com.domain.event;

import com.domain.Entity.bTree.Entry;
import com.domain.Entity.ComplexSelectEntity;
import com.domain.Entity.result.OperateResult;

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