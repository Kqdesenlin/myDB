package validator;

import org.springframework.beans.factory.annotation.Autowired;
import provider.ServiceProvider;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * @author: zhangQY
 * @date: 2021/4/2
 * @description:
 */
public abstract class BaseStringValidator<A extends Annotation> implements ConstraintValidator<A,String> {

    protected boolean ignoreEmptyValue;

    @Autowired
    private ServiceProvider serviceProvider;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if ((null == s)||(s.isEmpty())&&ignoreEmptyValue) {
            return true;
        } else {
            ValidateService service = (ValidateService) serviceProvider.getService(ValidateService.class);
            return this.isValid(service,s);
        }
    }

    public abstract boolean isValid(ValidateService validateService, String str);
}
