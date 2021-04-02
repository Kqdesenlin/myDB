package provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author: zhangQY
 * @date: 2021/4/2
 * @description:
 */
@Slf4j
@Component
public class ServiceProvider {

    private HashMap<Class,Object> serviceMap = new HashMap<>();

    public void addService(Object obj) {
        addService(obj.getClass(),obj);
    }

    public void addService(Class type,Object obj) {
        if (null != type && type.equals(Object.class)) {
            if (this.serviceMap.containsKey(type)) {
                log.warn("add service already exist with type: " + type);
            } else {
                serviceMap.put(type,obj);
            }
        }
    }

    public Object getService(Class type) {
        return serviceMap.get(type);
    }
}
