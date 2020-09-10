package gov.virginia.dmas.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
public class DocumentDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="Please provide the document name for the file.")
	private String documentName;
	
	@NotEmpty(message="Please provide the file name for the file.")
	private String fileName;

}
