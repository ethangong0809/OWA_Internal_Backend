package gov.virginia.dmas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.virginia.dmas.entity.AssignmentEntity;
import gov.virginia.dmas.entity.RequestorInternalEntity;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Long>{
	
	public List<AssignmentEntity> findByEmailAndActive(String email, boolean active);
	
	public List<AssignmentEntity> findByEmailAndStatus(String email, String status);

	public List<AssignmentEntity> findByRequestorInternalEntityAndActive(RequestorInternalEntity requestorInternalEntity, boolean active);
	
	List<AssignmentEntity> findByRequestorInternalEntity(RequestorInternalEntity requestorInternalEntity);

	public List<AssignmentEntity> findByRequestorInternalEntityAndEmailAndActive(
			RequestorInternalEntity requestorInternalEntity, String email, boolean b);
	
}
