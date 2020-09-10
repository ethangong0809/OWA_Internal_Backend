package gov.virginia.dmas.entity;

import java.io.Serializable;

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
@Table(name="COMMENTS_INTERNAL")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CommentInternalEntity extends AbstractTimestamp implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="comments_seq")
	@SequenceGenerator(name="comments_seq",sequenceName="comments_sequence", allocationSize=1)
	@Column(name="COMMENT_ID")
	private Long id;
	
	@Lob
	@Column(name="NOTES")
	private String notes;
	
	@ManyToOne
	@JoinColumn(name="REQUESTOR_ID", nullable=false)
	private RequestorInternalEntity requestorInternalEntity;
	
	@ManyToOne
	@JoinColumn(name="REASON_ID")
	private ReasonCodeEntity reasonCodeEntity;
	
	@Column(name="CREATED_BY", nullable=false)
	private String createdBy;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;

}
