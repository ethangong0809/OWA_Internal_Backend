package gov.virginia.dmas.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gov.virginia.dmas.serializer.DateTimeStampDeserializer;
import gov.virginia.dmas.serializer.DateTimeStampSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
public class DocumentResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String documentName;
	
	private String fileName;
	
	private String fileLocation;
	
	private String createdBy;
	
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
