package gov.virginia.dmas.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.virginia.dmas.entity.FormNameEntity;
import gov.virginia.dmas.entity.MessageTemplateEntity;

public interface MessageTemplateRepository extends JpaRepository<MessageTemplateEntity, Long>{

	MessageTemplateEntity findByFormNameEntityAndStatus(FormNameEntity formNameEntity, String status);

	MessageTemplateEntity findByStatus(String status);  

}
