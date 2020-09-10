package gov.virginia.dmas.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Log4jConfig {

	@Autowired
	PropertiesConfig propertiesConfig;
	
	@PostConstruct
	public void configureProperties() {
		
		Properties properties = new Properties();
		FileInputStream file = null;
		try {
			String value = propertiesConfig.getProperties().getProperty("log4jFile");
			file = new FileInputStream(value);
			properties.load(file);
			PropertyConfigurator.configure(properties);
		} catch (IOException e) {
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
	}

}
