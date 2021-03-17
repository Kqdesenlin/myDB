package Infrastructure.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
public class SelectResult{

    public String info;
    public ResultCode code;
    public List<String> rules;
    public List<List<String>> items;
    public boolean ifContainPK = false;

    public SelectResult setRules(List<String> list){
        this.rules = list;
        return this;
    }

    public List<String> getRules(){
        return this.rules;
    }

    public SelectResult setItems(List<List<String>> list){
        this.items = list;
        return this;
    }

    public SelectResult containPK(){
        this.ifContainPK = true;
        return this;
    }

    public SelectResult notContainPK(){
        this.ifContainPK = false;
        return this;
    }

    public List<List<String>> getItems(){
        return this.items;
    }

    public SelectResult(String info, ResultCode code) {
        this.info = info;
        this.code = code;
    }

    public static SelectResult error(String info){
        return new SelectResult(info,ResultCode.error);
    }

    public static SelectResult ok(String info){
        return new SelectResult(info,ResultCode.ok);
    }

}
