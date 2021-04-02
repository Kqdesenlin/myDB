package validator;

import org.springframework.stereotype.Component;
import provider.ServiceProvider;

/**
 * @author: zhangQY
 * @date: 2021/4/2
 * @description:
 */
@Component
public class ValidateServiceImpl implements ValidateService{

    public ValidateServiceImpl(ServiceProvider serviceProvider) {
        serviceProvider.addService(ValidateService.class);
    }
    @Override
    public boolean isSql(String sql) {
        return true;
    }
}
