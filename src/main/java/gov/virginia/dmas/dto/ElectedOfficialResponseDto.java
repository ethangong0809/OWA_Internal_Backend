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
public class ElectedOfficialResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String officialFirstname;
	
	private String officialLastname;
	
	private String staffFirstname;
	
	private String staffLastname;

	private String constituent;
	
	private String constFirstname;
	
	private String constLastname;
	
	private String constPhone;
	
	private String constEmail;
	
	private String constituentMM;

	private Date constDOB;
	
	private String constMedID;
	
	private String constSSN;
	
	private String constAddr1;
	
	private String constAddr2;
	
	private String constCity;
	
	private String constState;
	
	private String constZipcode;
	
	private String description;
	
	@JsonSerialize(using = DateSerializer.class)
	public Date getConstDOB() {
		return constDOB;
	}
	
	@JsonDeserialize(using = DateDeserializer.class)
	public void setConstDOB(Date constDOB) {
		this.constDOB = constDOB;
	}
}
