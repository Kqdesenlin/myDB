package Infrastructure.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypeConverUtils {
    public static List<String> mapToListByListOrder(Map<String,String> map,List<String> list){
        List<String> rtn = new ArrayList<>();
        for(String s : list){
            String val = map.get(s);
            rtn.add(val);
        }
        return rtn;
    }
}
