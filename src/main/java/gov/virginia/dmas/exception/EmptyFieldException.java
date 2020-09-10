package gov.virginia.dmas.exception;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class EmptyFieldException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LogManager.getLogger(EmptyFieldException.class);

	public EmptyFieldException() {
		super();
	}
	
	public EmptyFieldException(String message) {
		super(message);
		logger.error(message);
		
	}

	public EmptyFieldException(String message, Throwable cause) {
		super(message, cause);
		logger.error(message,cause);
	}


}
