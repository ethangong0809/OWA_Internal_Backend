package gov.virginia.dmas.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.stereotype.Component;

@Component
public class PropertiesConfig {

	@PostConstruct
	public Properties getProperties() throws IOException {
		
		Properties properties = new Properties();
		FileInputStream file = null;
		InitialContext initial_ctx;
		try {
			initial_ctx = new InitialContext();
			String value = (String) initial_ctx.lookup("owa_internal_properties_file");
			file = new FileInputStream(value);
//			file = new FileInputStream("C:/Properties/OWA_Internal/config.properties");
			properties.load(file);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		catch (NamingException e) {
			e.printStackTrace();
		} 
		finally {
			if(file!=null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}
}
