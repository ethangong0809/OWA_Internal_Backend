package gov.virginia.dmas.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="REASON_CODES")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReasonCodeEntity extends AbstractTimestamp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="reasoncodes_seq")
	@SequenceGenerator(name="reasoncodes_seq",sequenceName="reasoncodes_sequence", allocationSize=1)
	@Column(name="REASON_ID")
	private Long id;
	
	@Column(name="REASON_CODE", nullable=false)
	private Long reasonCode;
	
	@Column(name="STATUS_TYPE", nullable=false)
	private String statusType;
	
	@Column(name="DESCRIPTION", nullable=false)
	private String description;
	
	@ManyToOne
	@JoinColumn(name="FORM_ID", nullable=false)
	FormNameEntity formNameEntity;
	
	@OneToMany(mappedBy="reason", cascade=CascadeType.ALL)
	private List<RequestorInternalEntity> requestors;
	
	@OneToMany(mappedBy="reasonCodeEntity", cascade=CascadeType.ALL)
	private List<CommentInternalEntity> comments;
	
	@Column(name="CREATED_BY", nullable=false)
	private String createdBy;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;
}
