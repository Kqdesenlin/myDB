package com.application.service;

import com.domain.Entity.CreateTempEntity;

import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description: 将sql解析结果包装成dml需要的实体
 */
public class PackToEntity {

    public static CreateTempEntity packFromToWhereToCreateTempEntity(List<String> tableList) {
        CreateTempEntity createTempEntity = new CreateTempEntity();
        createTempEntity.setTableList(tableList);
        return createTempEntity;
    }
}
