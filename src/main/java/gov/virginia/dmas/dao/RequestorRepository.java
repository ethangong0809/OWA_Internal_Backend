package gov.virginia.dmas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gov.virginia.dmas.entity.FormNameEntity;
import gov.virginia.dmas.entity.RequestorEntity;

public interface RequestorRepository extends JpaRepository<RequestorEntity, Long>{

	@Query("select ticketID from RequestorEntity")
	public List<String> getTicketID();
	
	public RequestorEntity findByTicketID(String ticketID);

	public List<RequestorEntity> findByFormName(FormNameEntity formNameEntity);

	public List<RequestorEntity> findByFormNameOrderByIdDesc(FormNameEntity formNameEntity);
	
	public List<RequestorEntity> findByFormNameAndStatus(FormNameEntity formNameEntity, String status);
	
	public List<RequestorEntity> findByFormNameAndStatusOrderByCreatedTimeAsc(FormNameEntity formNameEntity, String status);
	
	public List<RequestorEntity> findByFormNameAndStatusOrderByCreatedTimeDesc(FormNameEntity formNameEntity, String status);
	
	public List<RequestorEntity> findByStatus(String status);
}
