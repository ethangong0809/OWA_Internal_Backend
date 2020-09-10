package gov.virginia.dmas.exception;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class StorageException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LogManager.getLogger(StorageException.class);

	public StorageException() {
		super();
	}
	
	public StorageException(String message) {
		super(message);
		logger.error(message);
		
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
		logger.error(message,cause);
	}

}
