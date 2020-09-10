package gov.virginia.dmas.entity;

import java.io.Serializable;
import java.sql.Timestamp;

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
@Table(name="MEDIA_MEMBERS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MediaRequestEntity extends AbstractTimestamp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="foreigngen")
	@GenericGenerator(strategy="foreign", name="foreigngen", parameters=@Parameter(name="property", value="requestorEntity"))
	@Column(name="REQUESTOR_ID", nullable=false)
	private Long id;
	
	@Column(name="DEADLINE")
	private Timestamp deadline;
	
	@Column(name="LESSTHAN24")
	private boolean lessThan24;
	
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
