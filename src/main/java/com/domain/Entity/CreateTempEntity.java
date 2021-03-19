package com.domain.Entity;

import lombok.Data;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:创建虚表实体
 */
@Data
public class CreateTempEntity {

    List<String> tableList;
    SubSelect subSelect;
    boolean ifSubSelect = false;
}
