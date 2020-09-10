package gov.virginia.dmas.ldap.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/*@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"gov.verginia.vec.*"})
@EnableLdapRepositories(basePackages = "gov.verginia.vec.**")*/
//@ConfigurationProperties(prefix="ldapconfigprops")

@Configuration
public class LDAPConfig {
	 @Autowired
	    private Environment env;
	 
		public String LDAP_URL;
		public String DOMAIN;
		public String LDAP_DN_ROOT;
		public String USER_NAME;
		public String PASSWORD;
		
	 
	 @Bean
	    public LdapContextSource contextSource() {
		    System.out.println("env.getRequiredProperty(\"ldapconfigprops.DOMAIN\") "+env.getRequiredProperty("ldapconfigprops.DOMAIN"));
	        LdapContextSource contextSource = new LdapContextSource();
	        contextSource.setUrl(env.getRequiredProperty("ldapconfigprops.ldapBaseURL"));
	        contextSource.setBase(env.getRequiredProperty("ldapconfigprops.DOMAIN"));
	        contextSource.setUserDn(env.getRequiredProperty("ldapconfigprops.USER_NAME"));
	        contextSource.setPassword(env.getRequiredProperty("ldapconfigprops.PASSWORD"));
	        return contextSource;
	    }

	    @Bean
	    public LdapTemplate ldapTemplate() {
	        return new LdapTemplate(contextSource());
	    }

	   /* @Bean
	    public LdapClient ldapClient() {
	        return new LdapClient();
	    }*/

	    
}
