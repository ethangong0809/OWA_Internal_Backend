package gov.virginia.dmas.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gov.virginia.dmas.serializer.DateDeserializer;
import gov.virginia.dmas.serializer.DateSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReportProblemResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String category;
	
	private String categoryOther;
	
	private String relationship;
	
	private String relationshipOther;
	
	private String memFirstname;
	
	private String memLastname;
	
	private String memPhone;
	
	private String memEmail;
	
	private Date memDOB;

	private String memMedID;
	
	private String memSSN;

	private String memAddr1;
	
	private String memAddr2;
	
	private String memCity;
	
	private String memState;
	
	private String memZipcode;
	
	private String description;

	@JsonSerialize(using = DateSerializer.class)
	public Date getMemDOB() {
		return memDOB;
	}
	
	@JsonDeserialize(using = DateDeserializer.class)
	public void setMemDOB(Date memDOB) {
		this.memDOB = memDOB;
	}
}
