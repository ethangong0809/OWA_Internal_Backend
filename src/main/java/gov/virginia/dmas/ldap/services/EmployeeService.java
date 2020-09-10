package gov.virginia.dmas.ldap.services;

import java.util.List;

import gov.virginia.dmas.ldap.dto.EmployeeDTO;


public interface EmployeeService {
	
	public List<EmployeeDTO> searchEmployees(String name);

	public List<EmployeeDTO> getAllFromLDAP();

	public EmployeeDTO findByMail(String email);

}
