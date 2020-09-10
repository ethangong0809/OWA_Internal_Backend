package gov.virginia.dmas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.virginia.dmas.entity.DocumentInternalEntity;
import gov.virginia.dmas.entity.RequestorInternalEntity;

public interface DocumentInternalRepository extends JpaRepository<DocumentInternalEntity, Long>{

	List<DocumentInternalEntity> findByRequestorInternalEntityAndActive(RequestorInternalEntity requestorInternalEntity, boolean active);
}
