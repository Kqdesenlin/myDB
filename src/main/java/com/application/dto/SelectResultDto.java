package com.application.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/4/8
 * @description:
 */
@Data
public class SelectResultDto {
    List<String> columnTemplate;
    List<JSONObject> rowTemplate;
}
