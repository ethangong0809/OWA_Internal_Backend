package gov.virginia.dmas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.virginia.dmas.entity.FormNameEntity;
import gov.virginia.dmas.entity.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long>{

	List<NotificationEntity> findByFormNameEntityAndStatus(FormNameEntity formNameEntity, boolean status);

}
