package gov.virginia.dmas.responseWrappers;

public class BaseError {
	
	private String fieldId;
	
	private String detail;

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public BaseError() {
		
	}

	public BaseError(String fieldId, String detail) {
		super();
		this.fieldId = fieldId;
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "BaseError [fieldId=" + fieldId + ", detail=" + detail + "]";
	}
}
