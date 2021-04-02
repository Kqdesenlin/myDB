package validator;

/**
 * @author: zhangQY
 * @date: 2021/4/2
 * @description:
 */
public class SqlValidator extends BaseStringValidator<Sql> {

    @Override
    public void initialize(Sql sql) {
        this.ignoreEmptyValue = sql.ignoreEmptyValue();
    }

    @Override
    public boolean isValid(ValidateService service,String str) {
        return service.isSql(str);
    }
}
