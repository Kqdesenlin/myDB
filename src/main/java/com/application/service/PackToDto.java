package com.application.service;

import com.domain.Entity.result.OperateResult;
import com.interfaces.dto.ResultDto;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:把operateResult转换成dto
 */
public class PackToDto {

    public static ResultDto ResultToDto(OperateResult operateResult) {
        ResultDto resultDto = new ResultDto(operateResult.getInfo()
                ,operateResult.getCode().getResultCode(),operateResult.getRtn().toString());
        return resultDto;
    }
}
