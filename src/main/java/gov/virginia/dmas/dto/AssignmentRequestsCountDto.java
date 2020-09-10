package gov.virginia.dmas.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
public class AssignmentRequestsCountDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RequestsCountDto procurementRequests;
	
	private RequestsCountDto cccplusRequests;
	
	private RequestsCountDto med4Requests;
	
	private RequestsCountDto generalRequests;
	
	private RequestsCountDto mediaRequests;
	
	private RequestsCountDto electedOfficialRequests;
	
	private RequestsCountDto reportProblemRequests;

}
