package Infrastructure.Entity;

import lombok.Data;

import java.util.Map;

@Data
public class CreateEntity {
    String tableName;
    /**
     * rule需要遵循 自定义的值在前,约束条件在后
     */
    Map<String,String> rules;
}
