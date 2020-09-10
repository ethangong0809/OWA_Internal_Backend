package gov.virginia.dmas.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
public class DocumentRequestDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message="Please provide the requestorID.")
	private Long requestorID;
	
	@NotEmpty(message="Please provide the ticketID.")
	private String ticketID;

	@Valid
	@NotNull
	private List<DocumentDto> documents;
	
	@NotEmpty(message="CreatedBy is required.")
	private String createdBy;
	
	private String updatedBy;
}
