package com.Infrastructure.Entity;

import lombok.Data;

import java.util.List;

/**
 * 复杂查询实体
 * 包含条件查询
 */
@Data
public class ComplexSelectEntity extends SelectEntity{
    List<ExpressionEntity> entityList;
}
