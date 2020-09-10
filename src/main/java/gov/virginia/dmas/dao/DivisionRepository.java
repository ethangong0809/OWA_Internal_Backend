package gov.virginia.dmas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gov.virginia.dmas.entity.DivisionEntity;

public interface DivisionRepository extends JpaRepository<DivisionEntity, Long>{

	@Query("SELECT d.division from DivisionEntity d")
	List<String> getDivisions();

	List<DivisionEntity> findAllByActiveOrderByIdAsc(boolean active);

}
