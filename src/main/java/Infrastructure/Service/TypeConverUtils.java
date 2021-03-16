package Infrastructure.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
}
