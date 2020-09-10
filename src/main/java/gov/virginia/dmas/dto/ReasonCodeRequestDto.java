package gov.virginia.dmas.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
public class ReasonCodeRequestDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message="Please provide a reason code.")
	private Long reasonCode;
	
	@NotEmpty(message="Please provide the status type.")
	private String statusType;
	
	@NotEmpty(message="Please provide a reason description.")
	private String description;
	
	@NotNull(message="Please provide a formID.")
	private Long formID;
	
	private String createdBy;
	
	private String updatedBy;
}
