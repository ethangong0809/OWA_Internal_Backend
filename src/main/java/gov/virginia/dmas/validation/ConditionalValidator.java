package gov.virginia.dmas.validation;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.isEmpty;

public class ConditionalValidator implements ConstraintValidator<Conditional, Object> {
	
	private String selected;
    private String[] required;
    private String message;
    private String[] values;
    
	@Override
    public void initialize(Conditional requiredIfChecked) {
        selected = requiredIfChecked.selected();
        required = requiredIfChecked.required();
        message = requiredIfChecked.message();
        values = requiredIfChecked.values();
    }
	
	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {

		Boolean valid = true;
        try {
        	int count = 0;
            Object checkedValue = BeanUtils.getProperty(object, selected);
            if (Arrays.asList(values).contains(checkedValue)) {
                for (String propName : required) {
                    Object requiredValue = BeanUtils.getProperty(object, propName);
                    valid = requiredValue != null && !isEmpty(requiredValue);
//                    System.out.println("value: " + "" + requiredValue);
                    if (!valid) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(message).addPropertyNode(propName).addConstraintViolation();
                        count = count + 1;
                    }
                }
                if(count > 0) {
                	return false;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
        return valid;
	}
	

}
