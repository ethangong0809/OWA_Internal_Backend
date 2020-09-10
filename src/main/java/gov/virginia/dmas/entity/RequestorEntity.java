package gov.virginia.dmas.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="REQUESTORS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RequestorEntity extends AbstractTimestamp implements Serializable{

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
	
	@OneToOne(mappedBy="requestorEntity", cascade=CascadeType.ALL)
	private ReportProblemEntity reportProblemEntity;
	
	@OneToOne(mappedBy="requestorEntity", cascade=CascadeType.ALL)
	private GeneralRequestEntity generalRequestEntity;
	
	@OneToOne(mappedBy="requestorEntity", cascade=CascadeType.ALL)
	private MediaRequestEntity mediaRequestEntity;
	
	@OneToOne(mappedBy="requestorEntity", cascade=CascadeType.ALL)
	private ElectedOfficialRequestEntity electedOfficialEntity;
	
	@ManyToOne
	@JoinColumn(name="FORM_ID")
	private FormNameEntity formName;
}
