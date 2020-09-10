package gov.virginia.dmas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.virginia.dmas.entity.GeneralRequestEntity;

public interface GeneralRequestRepository extends JpaRepository<GeneralRequestEntity, Long>{

	public List<GeneralRequestEntity> findByRequestType(String requestType);
}
