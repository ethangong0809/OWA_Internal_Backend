package gov.virginia.dmas.ldap.services;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

import gov.virginia.dmas.ldap.dto.EmployeeDTO;

public class EmployeeAttributesMapper implements AttributesMapper<EmployeeDTO>{

	@Override 	
	public EmployeeDTO mapFromAttributes(Attributes attributes) throws NamingException {
		// TODO Auto-generated method stub
		EmployeeDTO employeeDTO = new EmployeeDTO();
		if(attributes.get("mail") != null && attributes.get("cn") != null)
		{
			employeeDTO.setUsername((String)attributes.get("cn").get());
		
			employeeDTO.setEmailId((String)attributes.get("mail").get());
			employeeDTO.setFirstName((String)attributes.get("givenName").get());
			employeeDTO.setLastName((String)attributes.get("sn").get());
			return employeeDTO;
		}
		return null;
	}

}
