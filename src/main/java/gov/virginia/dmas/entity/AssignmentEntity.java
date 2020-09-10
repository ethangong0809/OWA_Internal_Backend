package gov.virginia.dmas.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="ASSIGNMENTS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AssignmentEntity extends AbstractTimestamp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="assignments_seq")
	@SequenceGenerator(name="assignments_seq",sequenceName="assignments_sequence", allocationSize=1)
	@Column(name="ASSIGNMENT_ID")
	private Long id;
	
	@Column(name="DIVISION", nullable=false)
	private String division;
	
	@Column(name="USERNAME", nullable=false)
	private String username;
	
	@Column(name="EMAIL", nullable=false)
	private String email;
	
	@Column(name="ACTIVE", nullable=false)
	private boolean active;
	
	@Column(name="START_DATE")
	private Date startDate;
	
	@Column(name="END_DATE")
	private Date expectedEndDate;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="CREATED_BY", nullable=false)
	private String createdBy;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;
	
	@Lob
	@Column(name="COMMENTS")
	private String assigncomment;
	
	@ManyToOne
	@JoinColumn(name="REQUESTOR_ID", nullable=false)
	private RequestorInternalEntity requestorInternalEntity;
}
