package gov.virginia.dmas.responseWrappers;

import java.util.ArrayList;
import java.util.List;

public class BaseResponseWrapper {
	
	private List<BaseError> errors;	
	
	public BaseResponseWrapper() {
		
	}

	public void addError(BaseError baseErr) {
		if(this.errors == null) {
			this.errors = new ArrayList<BaseError>();
		}
		
		this.errors.add(baseErr);
	}

	public BaseResponseWrapper(List<BaseError> errors) {
		super();
		this.errors = errors;
	}

	public List<BaseError> getErrors() {
		return errors;
	}

	public void setErrors(List<BaseError> errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "BaseResponseWrapper [errors=" + errors + "]";
	}
}
