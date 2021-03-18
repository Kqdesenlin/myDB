package Infrastructure.Entity;

import lombok.Data;

import java.util.Map;

@Data
public class DeleteEntity {
    String tableName;
    Map<String,String> deleteRules;
    boolean ifContainPK = false;
}
