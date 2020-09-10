package gov.virginia.dmas.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
public class ReasonCodesDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ReasonCodeResponseDto> open;
	
	private List<ReasonCodeResponseDto> closed;
}
