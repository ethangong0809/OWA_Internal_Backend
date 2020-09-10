package gov.virginia.dmas.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.virginia.dmas.entity.GeneralRequestInternalEntity;

public interface GeneralRequestInternalRepository extends JpaRepository<GeneralRequestInternalEntity, Long>{

}
