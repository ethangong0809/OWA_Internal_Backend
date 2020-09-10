package gov.virginia.dmas.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
public class GeneralRequestDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message="Please tell us about the request type.")
	private String requestType;
	
	@NotEmpty(message="Please enter a description of your request.")
	private String description;
	
	@NotEmpty(message="UpdatedBy field is required.")
	private String updatedBy;

}
