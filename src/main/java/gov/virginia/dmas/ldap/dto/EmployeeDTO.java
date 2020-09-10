package gov.virginia.dmas.ldap.dto;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entry(objectClasses = { "person",
		"organizationalPerson", "top", "user" })
@JsonIgnoreProperties(ignoreUnknown=true)
@Getter @Setter @ToString
public final class EmployeeDTO {
	
	@Id
	@JsonIgnore
	private Name dn;
	private @Attribute(name = "cn") String username;
	private @Attribute(name = "sn") String lastName;
	private @Attribute(name = "sAMAccountName") String sAMAccountName;
	private @Attribute(name = "mail") String emailId;
	private @Attribute(name = "givenName") String firstName;

}
