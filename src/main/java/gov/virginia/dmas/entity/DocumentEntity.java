package gov.virginia.dmas.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="DOCUMENTS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DocumentEntity extends AbstractTimestamp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="documents_seq")
	@SequenceGenerator(name="documents_seq",sequenceName="documents_sequence", allocationSize=1)
	@Column(name="DOCUMENT_ID")
	private Long id;

	@Column(name="DOCUMENT_NAME", nullable=false)
	private String documentName;
	
	@Column(name="FILENAME", nullable=false)
	private String fileName;
	
	@Column(name="FILE_LOCATION", nullable=false)
	private String fileLocation;
	
	@Column(name="ACTIVE", nullable=false)
	private boolean active;
	
	@ManyToOne
	@JoinColumn(name="REQUESTOR_ID", nullable=false)
	private RequestorEntity requestorEntity;
	
	@Column(name="CREATED_BY", nullable=false)
	private String createdBy;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;
}
