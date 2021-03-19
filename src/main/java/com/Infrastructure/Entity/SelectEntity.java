package Infrastructure.Entity;

import lombok.Data;

import java.util.List;

@Data
public class SelectEntity {
    String tableName;
    List<String> selectItems;

}
