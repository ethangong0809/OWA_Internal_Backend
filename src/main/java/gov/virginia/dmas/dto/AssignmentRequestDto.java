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
public class AssignmentRequestDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message="Please provide the ID of the request.")
	private Long requestorID;

	@Valid
	@NotNull
	private List<AssignmentDto> assignments;
	
	
	@Valid
	private CommentRequestDto comment;	
	
	
	private String createdBy;
	
	private String updatedBy;
}
