package gov.virginia.dmas.entity;

import java.io.Serializable;

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
@Table(name="GENERAL_REQUESTS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GeneralRequestEntity extends AbstractTimestamp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="foreigngen")
	@GenericGenerator(strategy="foreign", name="foreigngen", parameters=@Parameter(name="property", value="requestorEntity"))
	@Column(name="REQUESTOR_ID", nullable=false)
	private Long id;
	
	@Column(name="REQUEST_TYPE", nullable=false)
	private String requestType;
	
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
