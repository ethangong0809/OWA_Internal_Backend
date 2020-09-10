package gov.virginia.dmas.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gov.virginia.dmas.serializer.DateTimeStampDeserializer;
import gov.virginia.dmas.serializer.DateTimeStampSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@JsonIgnoreProperties(ignoreUnknown=true)
public class RequestorResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String ticketID;
	
	private Long formID;
	
	private String vaResident;
	
	private String firstName;

	private String lastName;
	
	private String phone;
	
	private String email;
	
	private String organizationName;
	
	private String requestorType;
	
	private String address1;
	
	private String address2;
	
	private String city;
	
	private String state;
	
	private String zipcode;

	private String fax;
	
	private String status;
	
	private ReasonCodeResponseDto reason;
	
	private MediaResponseDto media;
	
	private ElectedOfficialResponseDto electedOfficial;
	
	private GeneralResponseDto general;
	
	private ReportProblemResponseDto reportProblem;
	
	private List<CommentResponseDto> comments;
	
	private Timestamp createdTime;
	
	private String updatedBy;
	
	private Timestamp updatedTime;
	
	@JsonSerialize(using = DateTimeStampSerializer.class)
	public Timestamp getCreatedTime() {
		return createdTime;
	}

	@JsonDeserialize(using = DateTimeStampDeserializer.class)
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	
	@JsonSerialize(using = DateTimeStampSerializer.class)
	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	@JsonDeserialize(using = DateTimeStampDeserializer.class)
	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}
}
