package gov.virginia.dmas.exception;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class StorageFileNotFoundException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LogManager.getLogger(StorageFileNotFoundException.class);

	public StorageFileNotFoundException() {
		super();
	}
	
	public StorageFileNotFoundException(String message) {
		super(message);
		logger.error(message);
		
	}

	public StorageFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
		logger.error(message,cause);
	}

}
