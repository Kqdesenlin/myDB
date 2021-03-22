package com.domain.Entity.result;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 操作返回结果
 */
@Data
@AllArgsConstructor
public class OperateResult {
    //返回信息
    private String info;
    //判断是否成功的代码
    private ResultCode code;
    //额外需要返回的信息
    private Object rtn;

    public static OperateResult ok(String info){
        return new OperateResult(info,ResultCode.ok,null);
    }

    public static OperateResult ok(String info,Object rtn) {
        return new OperateResult(info,ResultCode.ok,rtn);
    }

    public static OperateResult error(String info){
        return new OperateResult(info,ResultCode.error,null);
    }

    public static OperateResult error(String info,Object rtn) {
        return new OperateResult(info,ResultCode.error,rtn);
    }

}
