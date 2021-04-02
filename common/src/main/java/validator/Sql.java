package validator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(
        validatedBy = {SqlValidator.class}
)
@Target({ElementType.TYPE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sql {
    String message() default "非法SQL";

    boolean ignoreEmptyValue() default true;


}
