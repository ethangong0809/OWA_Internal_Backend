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
@Table(name="NOTIFICATIONS_USERS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NotificationEntity extends AbstractTimestamp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private UserEntity userEntity;
	
	@ManyToOne
	@JoinColumn(name="FORM_ID")
	private FormNameEntity formNameEntity;
	
	@Column(name="STATUS")
	private boolean status;
}
