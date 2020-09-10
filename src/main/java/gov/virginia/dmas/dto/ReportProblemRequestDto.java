package gov.virginia.dmas.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gov.virginia.dmas.serializer.DateDeserializer;
import gov.virginia.dmas.serializer.DateSerializer;
import gov.virginia.dmas.validation.Conditional;
import gov.virginia.dmas.validation.ExtendedEmailValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
@Conditional(selected = "category", values = {"Medicaid Member"}, required = {"memFirstname", "memLastname", "memPhone", "memEmail", "memDOB"})
@Conditional(selected = "category", values = {"Assisting a Medicaid Member"}, required = {"relationship", "memFirstname", "memLastname", "memDOB", "memAddr1", "memCity", "memState", "memZipcode"})
@Conditional(selected = "category", values = {"Other"}, required = {"categoryOther"})
@Conditional(selected = "relationship", values = {"Other"}, required = {"relationshipOther"})
public class ReportProblemRequestDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private String category;
	
	private String categoryOther;
	
	private String relationship;
	
	private String relationshipOther;
	
	private String memFirstname;
	
	private String memLastname;
	
	@Pattern(regexp="[\\d]{10}", message="Phone number must be 10 digits.")
	private String memPhone;
	
	@ExtendedEmailValidator
	private String memEmail;
	
	@Past
	private Date memDOB;

	@Size(min=12, max=12)	
	private String memMedID;
	
	@Pattern(regexp="[\\d]{9}", message="SSN must be 9 digits.")
	private String memSSN;
	
	private String memAddr1;
	
	private String memAddr2;
	
	private String memCity;
	
	private String memState;
	
	private String memZipcode;

	@NotEmpty(message="Please enter a description of your request.")
	private String description;

	@NotEmpty(message="UpdatedBy field is required.")
	private String updatedBy;
	
	@JsonSerialize(using = DateSerializer.class)
	public Date getMemDOB() {
		return memDOB;
	}
	
	@JsonDeserialize(using = DateDeserializer.class)
	public void setMemDOB(Date memDOB) {
		this.memDOB = memDOB;
	}
}
