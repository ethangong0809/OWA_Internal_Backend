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
@Table(name="ELECTED_OFFICIALS_OFC_INTERNAL")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ElectedOfficialRequestInternalEntity extends AbstractTimestamp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="foreigngen")
	@GenericGenerator(strategy="foreign", name="foreigngen", parameters=@Parameter(name="property", value="requestorEntity"))
	@Column(name="REQUESTOR_ID", nullable=false)
	private Long id;
	
	@Column(name="OFFICIAL_FIRSTNAME", nullable=false)
	private String officialFirstname;
	
	@Column(name="OFFICIAL_LASTNAME", nullable=false)
	private String officialLastname;

	@Column(name="CONSTITUENT")
	private String constituent;
	
	@Column(name="CONSTITUENT_FIRSTNAME")
	private String constFirstname;
	
	@Column(name="CONSTITUENT_LASTNAME")
	private String constLastname;
	
	@Column(name="CONSTITUENT_PHONE")
	private String constPhone;
	
	@Column(name="CONSTITUENT_EMAIL")
	private String constEmail;
	
	@Column(name="CONSTITUENT_DOB")
	private Date constDOB;
	
	@Column(name="CONSTITUENT_MED_ID")
	private String constMedID;
	
	@Column(name="CONSTITUENT_SSN")
	private String constSSN;

	@Lob
	@Column(name="DESCRIPTION", nullable=false)
	private String description;
	
	@Column(name="CREATED_BY", nullable=false)
	private String createdBy;

	@Column(name="UPDATED_BY")
	private String updatedBy;
	
	@OneToOne
	@PrimaryKeyJoinColumn
	private RequestorInternalEntity requestorInternalEntity;
}
