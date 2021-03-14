package Infrastructure.Entity;

import lombok.Data;

import java.util.Map;

@Data
public class CreateEntity {
    String tableName;
    Map<String,String> rules;
}
