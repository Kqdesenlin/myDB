package Infrastructure.TableInfo;

import BTree.BTree;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 具体表信息
 */
@Data
@AllArgsConstructor
public class TableInfo {
    BTree<Integer,List<String>> bTree;
    Map<String,String> rules;
    List<String> rulesOrder;
    public AtomicInteger primaryKey = new AtomicInteger(1);

    public TableInfo(BTree<Integer, List<String>> bTree, Map<String, String> tableRules, List<String> rulesOrder) {
        this.bTree = bTree;
        this.rules = tableRules;
        this.rulesOrder = rulesOrder;
    }
}
