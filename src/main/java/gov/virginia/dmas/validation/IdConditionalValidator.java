package gov.virginia.dmas.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;

public class IdConditionalValidator implements ConstraintValidator<IdConditional, Object>{

	private String selected;
	private String verifyCreatedBy;
	private String verifyUpdatedBy;

	@Override
	public void initialize(IdConditional constraintAnnotation) {
	    selected = constraintAnnotation.selected();
	    verifyCreatedBy = constraintAnnotation.verifyCreatedBy();
	    verifyUpdatedBy = constraintAnnotation.verifyUpdatedBy();
	}
	
	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		
		try {
			Object checkedValue = BeanUtils.getProperty(object, selected);		
			Object verifyCreated = BeanUtils.getProperty(object, verifyCreatedBy);
			Object verifyUpdated = BeanUtils.getProperty(object, verifyUpdatedBy);
			
			if(checkedValue == null && verifyCreated == null) {
				context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addPropertyNode(verifyCreatedBy).addConstraintViolation();
                return false;
            }
			if(checkedValue!=null && verifyUpdated == null) {
				context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addPropertyNode(verifyUpdatedBy).addConstraintViolation();
                return false;
			}
		}
		catch (Exception e) {
            return false;
		}
		return true;
	}
	

}
