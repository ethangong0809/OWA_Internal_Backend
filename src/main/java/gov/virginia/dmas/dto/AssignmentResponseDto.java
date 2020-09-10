package gov.virginia.dmas.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Setter;
import lombok.ToString;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
public class AssignmentResponseDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long requestorID;
	
	private List<AssignmentDto> assignments;
}
