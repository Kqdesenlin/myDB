package Infrastructure.Service;

import Constant.TableConstant;
import Infrastructure.TableInfo.TableInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeConverUtils {

    //根据list对应map中k的顺序，顺序输出value
    public static List<String> mapToListByListOrder(Map<String,String> map,List<String> list){
        List<String> rtn = new ArrayList<>();
        for(String s : list){
            String val = map.get(s);
            rtn.add(val);
        }
        return rtn;
    }

    //字符串大写转换
    public static String stringUpperConvert(String s){
        return s.toUpperCase();
    }

    /**
     *     插入的entity是 具体的值 : 对应的列
     *     需要返回是 具体的值 ： 对应列的类型
     */
    public static Map<String, String> valAndNameConvertToValAndType(Map<String,String> insertItems,String tableName){
        for(Map.Entry<String,String> entry : insertItems.entrySet()){
            String RowName = entry.getValue();
            TableInfo tableInfo = TableConstant.getTableByName(tableName);
            Map<String,String> tableRules = tableInfo.getRules();
//            for (Map.Entry<String,String> ruleEntry : tableRules.entrySet()){
//
//            }
//            String ruleName =
        }
        Map<String,String> tableRules = TableConstant.getTableByName(tableName).getRules();
        List<Map.Entry<String,String>> entryList = new ArrayList<>(insertItems.entrySet());
        entryList.forEach(entry -> {
            String ruleType = tableRules.get(entry.getValue());
            entry.setValue(ruleType);
        });

        return entryList.stream().collect(Collectors.toMap
                (Map.Entry<String,String>::getKey,Map.Entry<String,String>::getValue));
    }
}
