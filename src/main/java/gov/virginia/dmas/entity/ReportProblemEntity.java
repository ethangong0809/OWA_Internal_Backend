package gov.virginia.dmas.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="REPORT_PROBLEMS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReportProblemEntity extends AbstractTimestamp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="foreigngen")
	@GenericGenerator(strategy="foreign", name="foreigngen", parameters=@Parameter(name="property", value="requestorEntity"))
	@Column(name="REQUESTOR_ID", nullable=false)
	private Long id;
	
	@Column(name="CATEGORY", nullable=false)
	private String category;
	
	@Column(name="CATEGORY_OTHER")
	private String categoryOther;
	
	@Column(name="RELATIONSHIP")
	private String relationship;
	
	@Column(name="RELATIONSHIP_OTHER")
	private String relationshipOther;
	
	@Column(name="MEM_FIRSTNAME")
	private String memFirstname;
	
	@Column(name="MEM_LASTNAME")
	private String memLastname;
	
	@Column(name="MEM_PHONE")
	private String memPhone;
	
	@Column(name="MEM_EMAIL")
	private String memEmail;
	
	@Column(name="MEM_DOB")
	private Date memDOB;

	@Column(name="MEM_MED_ID")
	private String memMedID;
	
	@Column(name="MEM_SSN")
	private String memSSN;

	@Lob
	@Column(name="DESCRIPTION", nullable=false)
	private String description;
	
	@Column(name="CREATED_BY", nullable=false)
	private String createdBy;

	@Column(name="UPDATED_BY")
	private String updatedBy;
	
	@OneToOne
	@PrimaryKeyJoinColumn
	private RequestorEntity requestorEntity;
}
