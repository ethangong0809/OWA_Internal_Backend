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
@Conditional(selected = "constituent", values = {"Yes"}, required = {"constFirstname", "constLastname", "constPhone", "constEmail"})
@Conditional(selected = "constituentMM", values = {"Yes"}, required = {"constDOB", "constAddr1", "constCity", "constState", "constZipcode"})
public class ElectedOfficialRequestDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message = "Please enter a Elected Official's First Name.")
	private String officialFirstname;
	
	@NotEmpty(message = "Please enter a Elected Official's Last Name.")
	private String officialLastname;
	
	private String staffFirstname;
	
	private String staffLastname;

	private String constituent;
	
	private String constFirstname;
	
	private String constLastname;
	
	@Pattern(regexp="[\\d]{10}", message="Phone number must be 10 digits.")
	private String constPhone;
	
	@ExtendedEmailValidator
	private String constEmail;
	
	private String constituentMM;
	
	@Past
	private Date constDOB;
	
	@Size(min=12, max=12)
	private String constMedID;
	
	@Pattern(regexp="[\\d]{9}", message="SSN must be 9 digits.")
	private String constSSN;
	
	private String constAddr1;
	
	private String constAddr2;
	
	private String constCity;
	
	private String constState;
	
	private String constZipcode;
	
	@NotEmpty(message = "Please enter a description of your request.")
	private String description;
	
	@NotEmpty(message="UpdatedBy field is required.")
	private String updatedBy;

	@JsonSerialize(using = DateSerializer.class)
	public Date getConstDOB() {
		return constDOB;
	}
	
	@JsonDeserialize(using = DateDeserializer.class)
	public void setConstDOB(Date constDOB) {
		this.constDOB = constDOB;
	}
}
