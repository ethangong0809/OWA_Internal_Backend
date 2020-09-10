package gov.virginia.dmas.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gov.virginia.dmas.serializer.DateDeserializer;
import gov.virginia.dmas.serializer.DateSerializer;
import gov.virginia.dmas.validation.ExtendedEmailValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
public class AssignmentDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private Long assignmentId;
	
	@NotEmpty
	private Long requestorId;

	@NotEmpty(message="Please provide the division of the newly assigned person.")
	private String division;
	
	@NotEmpty(message="Please provide the name of the newly assigned person.")
	private String username;
	
	@NotEmpty(message="Please provide the status.")
	private String status;
	
	@NotEmpty(message="Please provide the comments")
	private String assigncomment;
	
	@NotEmpty(message="Please enter a email address.")
	@ExtendedEmailValidator
	private String email;
	
	private String createdBy;
	
	private String updatedBy;
	
	private boolean active;
	
	@FutureOrPresent
	private Date startDate;
	
	@FutureOrPresent
	private Date expectedEndDate;
	
	
	@JsonSerialize(using = DateSerializer.class)
	public Date getStartDate() {
		return startDate;
	}
	
	@JsonDeserialize(using = DateDeserializer.class)
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonSerialize(using = DateSerializer.class)
	public Date getExpectedEndDate() {
		return expectedEndDate;
	}
	
	@JsonDeserialize(using = DateDeserializer.class)
	public void setExpectedEndDate(Date expectedEndDate) {
		this.expectedEndDate = expectedEndDate;
	}
	
	
	
}
