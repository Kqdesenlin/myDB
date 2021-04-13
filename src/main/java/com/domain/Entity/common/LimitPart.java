package com.domain.Entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.jsqlparser.expression.Expression;

/**
 * @author: zhangQY
 * @date: 2021/4/13
 * @description: limit的包装类
 */
@Data
@AllArgsConstructor
public class LimitPart {
    //起始
    private Expression offset;
    //数量
    private Expression rowcount;
}
