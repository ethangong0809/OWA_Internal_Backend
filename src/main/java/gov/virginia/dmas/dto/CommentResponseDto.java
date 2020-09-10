package gov.virginia.dmas.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gov.virginia.dmas.serializer.DateTimeStampDeserializer;
import gov.virginia.dmas.serializer.DateTimeStampSerializer;
import gov.virginia.dmas.validation.IdConditional;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
@IdConditional(selected="id", verifyCreatedBy="createdBy", verifyUpdatedBy="updatedBy")
public class CommentResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String notes;
	
	private ReasonCodeResponseDto reason;
	
	private String createdBy;
	
	private Timestamp createdTime;
	
	@JsonSerialize(using = DateTimeStampSerializer.class)
	public Timestamp getCreatedTime() {
		return createdTime;
	}

	@JsonDeserialize(using = DateTimeStampDeserializer.class)
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
}
