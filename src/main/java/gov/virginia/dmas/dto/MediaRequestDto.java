package gov.virginia.dmas.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gov.virginia.dmas.serializer.DeadlineDeserializer;
import gov.virginia.dmas.serializer.DeadlineSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
public class MediaRequestDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Timestamp deadline;
	
	private boolean lessThan24;
	
	@NotEmpty(message="Please enter a description of your request.")
	private String description;

	@NotEmpty(message="UpdatedBy field is required.")
	private String updatedBy;
	
	@JsonSerialize(using = DeadlineSerializer.class)
	public Timestamp getDeadline() {
		return deadline;
	}
	
	@JsonDeserialize(using = DeadlineDeserializer.class)
	public void setDeadline(Timestamp deadline) {
		this.deadline = deadline;
	}
}
