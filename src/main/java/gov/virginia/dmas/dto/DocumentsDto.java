package gov.virginia.dmas.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.ToString;
import lombok.Setter;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
public class DocumentsDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<DocumentResponseDto> existingDocuments;
	
	private List<DocumentResponseDto> archiveDocuments;
	
}
