package gov.virginia.dmas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.virginia.dmas.entity.DocumentEntity;
import gov.virginia.dmas.entity.RequestorEntity;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long>{

	List<DocumentEntity> findByRequestorEntityAndActive(RequestorEntity requestorEntity, boolean active);
}
