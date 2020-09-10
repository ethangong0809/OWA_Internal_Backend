package gov.virginia.dmas.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import gov.virginia.dmas.dto.RequestorDto;

public class StatusValidator implements ConstraintValidator<ValidateStatus, RequestorDto>{

	@Override
	public boolean isValid(RequestorDto requestor, ConstraintValidatorContext context) {
		
		if(requestor.getPreviousStatus()=="Closed" && requestor.getStatus()=="Open" && requestor.getReasonID()==null) {
			context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addPropertyNode("reasonID").addConstraintViolation();
            return false;
		}
		return true;
	}

}
