package gov.virginia.dmas.dto;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import gov.virginia.dmas.validation.Conditional;
import gov.virginia.dmas.validation.ExtendedEmailValidator;
import gov.virginia.dmas.validation.ValidateRequest;
import gov.virginia.dmas.validation.ValidateStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
@Conditional(selected = "formID", values = {"1", "2", "3", "4", "7"}, required = {"vaResident"}, message = "'Resident of Virginia?' information is required.")
@Conditional(selected = "status", values = {"Closed"}, required = {"reasonID"}, message = "ReasonID is required.")
@Conditional(selected = "requestorType", values = {"Organization"}, required = {"organizationName"}, message = "Organization Name is required.")
@ValidateRequest
@ValidateStatus
public class RequestorDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="Please provide the Id.")
	private Long id;
	
	@NotEmpty(message="Please provide the ticketID.")
	private String ticketID;

	@NotNull(message="Please provide a FormID.")
	private Long formID;
	
	private String vaResident;
	
	@NotEmpty(message="Please enter a FirstName.")
	private String firstName;
	
	@NotEmpty(message="Please enter a LastName.")
	private String lastName;

	@NotNull(message="Please enter a phone number.")
	@Pattern(regexp="[\\d]{10}", message="Phone number must be 10 digits.")
	private String phone;
	
	@NotEmpty(message="Please enter a email address.")
	@ExtendedEmailValidator
	private String email;
	
	private String organizationName;
	
	private String requestorType;
	
	private String address1;
	
	private String address2;
	
	private String city;
	
	private String state;
	
	@Pattern(regexp="[\\d]{5,9}", message="Please enter a valid zipcode.")
	private String zipcode;
	
	@Pattern(regexp="[\\d]{10}", message="Fax number must be 10 digits.")
	private String fax;

	@NotEmpty(message="Please tell us about the previous status of the request.")
	private String previousStatus;
	
	@NotEmpty(message="Please tell us about the status of the request.")
	private String status;
	
	private Long reasonID;	
	
	private GeneralRequestDto general;
	
	private MediaRequestDto media;
	
	private ElectedOfficialRequestDto electedOfficial;
	
	private ReportProblemRequestDto reportProblem;
	
	@Valid
	@NotNull(message="Please provide the comments.")
	private CommentRequestDto comment;
		
	@NotEmpty(message="UpdatedBy field is required.")
	private String updatedBy;

}
