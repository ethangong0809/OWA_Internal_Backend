package gov.virginia.dmas.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="MESSAGE_TEMPLATES")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MessageTemplateEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MESSAGE_ID")
	private Long messageID;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="SUBJECT")
	private String subject;
	
	@Column(name="MESSAGE_TEMPLATE")
	private String messageTemplate;
	
	@ManyToOne
	@JoinColumn(name="FORM_ID")
	private FormNameEntity formNameEntity;
}
