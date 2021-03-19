package Infrastructure.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 操作返回结果
 */
@AllArgsConstructor
public class OperateResult {
    public String info;
    public ResultCode code;

    public static OperateResult error(String info){
        return new OperateResult(info,ResultCode.error);
    }
    public static OperateResult ok(String info){
        return new OperateResult(info,ResultCode.ok);
    }
}
