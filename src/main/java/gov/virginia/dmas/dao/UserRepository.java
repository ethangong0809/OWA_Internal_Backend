package gov.virginia.dmas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gov.virginia.dmas.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

	@Query("SELECT u.email from UserEntity u")
	List<String> getAdminEmails();
}
