package gov.virginia.dmas.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="FORM_NAMES")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FormNameEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="FORM_ID", nullable=false)
	private Long formID;
	
	@Column(name="FORM_NAME", nullable=false)
	private String formName;

	@OneToMany(mappedBy="formName")
	private List<RequestorEntity> requestors;
	
	@OneToMany(mappedBy="formName", cascade=CascadeType.ALL)
	private List<RequestorInternalEntity> internalRequestors;

	@OneToMany(mappedBy="formNameEntity")
	private List<ReasonCodeEntity> reasonCodes;	
	
	@OneToMany(mappedBy="formNameEntity")
	private List<MessageTemplateEntity> messageTemplates;
	
	@OneToMany(mappedBy="formNameEntity")
	private List<NotificationEntity> notifications;
}
