package gov.virginia.dmas.ldap.repository;


import java.util.List;

import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import gov.virginia.dmas.ldap.dto.EmployeeDTO;

@Repository
public interface EmployeeLDAPRepository extends LdapRepository<EmployeeDTO>{
	
	List<EmployeeDTO> findByUsernameContaining(String username);
	List<EmployeeDTO> findByUsername(String username);
	/*
	 * List<Employee> findBySurName(String surName);
	 * 
	 * List<Employee> findByGivenNameAndSurName(String givenName, String surName);
	 */
	List<EmployeeDTO> findAllByUsername();
 }
