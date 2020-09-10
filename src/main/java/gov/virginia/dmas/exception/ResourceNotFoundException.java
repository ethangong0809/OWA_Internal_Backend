package gov.virginia.dmas.exception;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ResourceNotFoundException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LogManager.getLogger(ResourceNotFoundException.class);

	public ResourceNotFoundException() {
		super();
	}
	
	public ResourceNotFoundException(String message) {
		super(message);
		logger.error(message);
		
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
		logger.error(message,cause);
	}

}
