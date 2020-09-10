package gov.virginia.dmas.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.virginia.dmas.entity.MediaRequestEntity;

public interface MediaRequestRepository extends JpaRepository<MediaRequestEntity, Long>{

}
