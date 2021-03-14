package Infrastructure.Entity;

import lombok.Data;

import java.util.Map;

@Data
public class InsertEntity {
    String tableName;
    Map<String,String> items;
}
