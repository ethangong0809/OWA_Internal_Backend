package gov.virginia.dmas.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.virginia.dmas.entity.ElectedOfficialRequestEntity;

public interface ElectedOfficialRequestRepository extends JpaRepository<ElectedOfficialRequestEntity, Long>{

}
