package gov.virginia.dmas.exception;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MailNotSentException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LogManager.getLogger(MailNotSentException.class);
	
	public MailNotSentException() {
		super();
	}
	
	public MailNotSentException(String message, Throwable cause) {
		super(message, cause);
		logger.error(message,cause);
	}

	public MailNotSentException(String message) {
		super(message);
		logger.error(message);
	}

}
