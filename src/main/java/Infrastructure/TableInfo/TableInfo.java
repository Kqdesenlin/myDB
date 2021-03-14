package Infrastructure.TableInfo;

import BTree.BTree;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * 具体表信息
 */
@AllArgsConstructor
public class TableInfo {
    BTree bTree;
    Map<String,String> rules;

}
