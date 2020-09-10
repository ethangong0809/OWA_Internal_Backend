package gov.virginia.dmas.validation;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import gov.virginia.dmas.dto.ElectedOfficialRequestDto;
import gov.virginia.dmas.dto.GeneralRequestDto;
import gov.virginia.dmas.dto.MediaRequestDto;
import gov.virginia.dmas.dto.ReportProblemRequestDto;
import gov.virginia.dmas.dto.RequestorDto;

public class RequestValidator implements ConstraintValidator<ValidateRequest, RequestorDto> {

	private Validator validator;
	
	public RequestValidator(Validator validator) {
		this.validator = validator;
	}
	
	@Override
	public boolean isValid(RequestorDto requestor, ConstraintValidatorContext context) {
		
		if(requestor.getFormID()==1 || requestor.getFormID()==2 || requestor.getFormID()==3 || requestor.getFormID()==4) {
			Set<ConstraintViolation<GeneralRequestDto>> violations = validator.validate(requestor.getGeneral());
			if(violations.isEmpty()) {
				return true;
			}
			violations.forEach(violation -> context
			    .buildConstraintViolationWithTemplate(violation.getMessage()).addPropertyNode("general").addPropertyNode(violation.getPropertyPath().toString())
			    .addConstraintViolation());
			return false;
		}
		else if(requestor.getFormID()==5) {
			Set<ConstraintViolation<MediaRequestDto>> violations = validator.validate(requestor.getMedia());
			if(violations.isEmpty()) {
				return true;
			}
			violations.forEach(violation -> context
			    .buildConstraintViolationWithTemplate(violation.getMessage()).addPropertyNode("media").addPropertyNode(violation.getPropertyPath().toString())
			    .addConstraintViolation());
			return false;
		}
		else if(requestor.getFormID()==6) {
			Set<ConstraintViolation<ElectedOfficialRequestDto>> violations = validator.validate(requestor.getElectedOfficial());
			if(violations.isEmpty()) {
				return true;
			}
			violations.forEach(violation -> context
			    .buildConstraintViolationWithTemplate(violation.getMessage()).addPropertyNode("electedOfficial").addPropertyNode(violation.getPropertyPath().toString())
			    .addConstraintViolation());
			return false;
		}
		else if(requestor.getFormID()==7) {
			Set<ConstraintViolation<ReportProblemRequestDto>> violations = validator.validate(requestor.getReportProblem());
			if(violations.isEmpty()) {
				return true;
			}
			violations.forEach(violation -> context
			    .buildConstraintViolationWithTemplate(violation.getMessage()).addPropertyNode("reportProblem").addPropertyNode(violation.getPropertyPath().toString())
			    .addConstraintViolation());
			return false;
		}
		return true;
	}

}
