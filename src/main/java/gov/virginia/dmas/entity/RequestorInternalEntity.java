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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="REQUESTORS_INTERNAL")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RequestorInternalEntity extends AbstractTimestamp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="requestors_seq")
	@SequenceGenerator(name="requestors_seq",sequenceName="requestors_sequence", allocationSize=1)
	@Column(name="REQUESTOR_ID", nullable=false)
	private Long id;
	
	@Column(name="TICKET_ID", nullable=false)
	private String ticketID;
	
	@Column(name="VA_RESIDENT")
	private String vaResident;
	
	@Column(name="FIRSTNAME", nullable=false)
	private String firstName;
	
	@Column(name="LASTNAME", nullable=false)
	private String lastName;
	
	@Column(name="PHONE", nullable=false)
	private String phone;
	
	@Column(name="EMAIL", nullable=false)
	private String email;
	
	@Column(name="ORGANIZATION_NAME")
	private String organizationName;
	
	@Column(name="REQUESTOR_TYPE")
	private String requestorType;
	
	@Column(name="ADDRESS1")
	private String address1;
	
	@Column(name="ADDRESS2")
	private String address2;
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="STATE")
	private String state;
	
	@Column(name="ZIPCODE")
	private String zipcode;
	
	@Column(name="FAX")
	private String fax;
	
	@Column(name="STATUS", nullable=false)
	private String status;
	
	@Column(name="CREATED_BY", nullable=false)
	private String createdBy;

	@Column(name="UPDATED_BY")
	private String updatedBy;
	
	@OneToOne(mappedBy="requestorInternalEntity", cascade=CascadeType.ALL)
	private ReportProblemInternalEntity reportProblemInternalEntity;
	
	@OneToOne(mappedBy="requestorInternalEntity", cascade=CascadeType.ALL)
	private GeneralRequestInternalEntity generalRequestInternalEntity;
	
	@OneToOne(mappedBy="requestorInternalEntity", cascade=CascadeType.ALL)
	private MediaRequestInternalEntity mediaRequestInternalEntity;
	
	@OneToOne(mappedBy="requestorInternalEntity", cascade=CascadeType.ALL)
	private ElectedOfficialRequestInternalEntity electedOfficialInternalEntity;
	
	@ManyToOne
	@JoinColumn(name="FORM_ID")
	private FormNameEntity formName;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="requestorInternalEntity")
	private List<CommentInternalEntity> commentInternalEntities;
	
	@ManyToOne
	@JoinColumn(name="REASON_ID")
	private ReasonCodeEntity reason;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="requestorInternalEntity")
	private List<DocumentInternalEntity> documentInternalEntities;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="requestorInternalEntity")
	private List<AssignmentEntity> assignmentEntities;

	public RequestorInternalEntity(Long id) {
		super();
		this.id = id;
	}
	
	
	
}
