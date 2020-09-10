package gov.virginia.dmas.ldap.services;

import java.util.Comparator;
import java.util.List;

import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import gov.virginia.dmas.ldap.dto.EmployeeDTO;
import gov.virginia.dmas.ldap.repository.EmployeeLDAPRepository;

@Service
public class EmployeeServicImpl implements EmployeeService {

	@Autowired
	EmployeeLDAPRepository employeeLDAPRepository; 
	@Autowired
    private LdapTemplate ldapTemplate;

	
	@Override
	public List<EmployeeDTO> searchEmployees(String username) {
		// TODO Auto-generated method stub
		Integer THREE_SECONDS = 3000;
		SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(1000);
        sc.setReturningAttributes(new String[]{"cn","mail","givenName","sn"});

        String filter = "(&(objectclass=person)(cn=*" + username + "*))";
        List<EmployeeDTO> employeeDTOs =  ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new EmployeeAttributesMapper());
//        if(employeeDTOs != null && employeeDTOs.size() > 1)
//        employeeDTOs.sort((EmployeeDTO s1, EmployeeDTO s2)->s1.getUsername().compareTo(s2.getUsername()));
		return employeeDTOs;
	}
	@Override
	public List<EmployeeDTO> getAllFromLDAP() {
		/*
		 * // TODO Auto-generated method stub List<EmployeeDTO> employeeDTOs =
		 * employeeLDAPRepository.findAllByUsername(); employeeDTOs.sort((EmployeeDTO
		 * s1, EmployeeDTO s2)->s1.getUsername().compareTo(s2.getUsername())); return
		 * employeeDTOs;
		 */
		Integer THREE_SECONDS = 3000;
		SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(1000);
        sc.setReturningAttributes(new String[]{"cn","mail","givenName","sn"});

        String filter = "(&(objectclass=person)(cn=*))";
        List<EmployeeDTO> employeeDTOs =  ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new EmployeeAttributesMapper());
        employeeDTOs.sort((EmployeeDTO s1, EmployeeDTO s2)->s1.getUsername().compareTo(s2.getUsername()));
		return employeeDTOs;
        
	}
	
	@Override
	public EmployeeDTO findByMail(String email) {
		Integer THREE_SECONDS = 3000;
		SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(1000);
        sc.setReturningAttributes(new String[]{"cn","mail","givenName","sn"});

        String filter = "(&(objectclass=person)(mail="+email+"))";
        List<EmployeeDTO> employeeDTOs =  ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new EmployeeAttributesMapper());
        return employeeDTOs.get(0);
        
	}

}
