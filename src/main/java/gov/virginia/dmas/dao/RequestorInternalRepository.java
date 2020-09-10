package gov.virginia.dmas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.virginia.dmas.entity.FormNameEntity;
import gov.virginia.dmas.entity.RequestorInternalEntity;

public interface RequestorInternalRepository extends JpaRepository<RequestorInternalEntity, Long>{

	public List<RequestorInternalEntity> findByStatus(String status);
	
	public List<RequestorInternalEntity> findByFormName(FormNameEntity formNameEntity);
	
	public List<RequestorInternalEntity> findByFormNameAndStatus(FormNameEntity formNameEntity, String status);
	
	public List<RequestorInternalEntity> findByFormNameAndStatusOrderByCreatedTimeAsc(FormNameEntity formNameEntity, String status);
	
	public List<RequestorInternalEntity> findByFormNameAndStatusOrderByCreatedTimeDesc(FormNameEntity formNameEntity, String status);
	
	public List<RequestorInternalEntity> findByFormNameOrderByCreatedTimeDesc(FormNameEntity formNameEntity);
	
	public List<RequestorInternalEntity> findByStatusOrderByCreatedTimeAsc(String status);
	
	public List<RequestorInternalEntity> findByStatusOrderByCreatedTimeDesc(String status);
	
	public List<RequestorInternalEntity> findAllByOrderByCreatedTimeDesc();
}
