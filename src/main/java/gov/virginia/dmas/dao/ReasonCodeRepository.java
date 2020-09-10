package gov.virginia.dmas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.virginia.dmas.entity.FormNameEntity;
import gov.virginia.dmas.entity.ReasonCodeEntity;

public interface ReasonCodeRepository extends JpaRepository<ReasonCodeEntity, Long>{

	public List<ReasonCodeEntity> findByFormNameEntity(FormNameEntity formNameEntity);
	
	public List<ReasonCodeEntity> findByFormNameEntityAndStatusType(FormNameEntity formNameEntity, String status);
	
	public List<ReasonCodeEntity> findByFormNameEntityAndStatusTypeOrderByReasonCodeAsc(FormNameEntity formNameEntity, String status);
}
