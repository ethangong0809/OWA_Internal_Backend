package gov.virginia.dmas.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="DIVISIONS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DivisionEntity extends AbstractTimestamp implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="divisions_seq")
	@SequenceGenerator(name="divisions_seq",sequenceName="divisions_sequence", allocationSize=1)
	@Column(name="DIVISION_ID")
	private Long id;
	
	@Column(name="DIVISION_NAME", nullable=false)
	private String division;
	
	@Column(name="ACTIVE")
	private boolean active;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;
}
