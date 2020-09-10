package gov.virginia.dmas.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import gov.virginia.dmas.exception.EmptyFieldException;
import gov.virginia.dmas.exception.ResourceNotFoundException;
import gov.virginia.dmas.exception.StorageException;
import gov.virginia.dmas.exception.StorageFileNotFoundException;
import gov.virginia.dmas.responseWrappers.BaseError;
import gov.virginia.dmas.responseWrappers.BaseResponseWrapper;

@ControllerAdvice
public class ErrorController {

	final static Logger logger = LogManager.getLogger(ErrorController.class);
	
//	private MessageSource messageSource;
//	
//	@Autowired
//	public ErrorController(MessageSource messageSource) {
//		this.messageSource = messageSource;
//	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public BaseResponseWrapper processException(HttpServletResponse httpRes, Exception exception) {
		BaseResponseWrapper response = new BaseResponseWrapper();
		if(exception instanceof CannotCreateTransactionException || exception instanceof SQLException || exception instanceof TransactionSystemException) {
			logger.info("Creating response for DBException");
			response.addError(new BaseError("DB ISSUE", "Under maintenance. Will be up shortly"));
			httpRes.setStatus(500);
			return response;
		}
		logger.info("Creating response for GlobalException");
		response.addError(new BaseError("UNEXPECTED RESULT", exception.getMessage()));
		exception.printStackTrace();
		httpRes.setStatus(400);
		return response;
	}
	
	@ExceptionHandler(EmptyFieldException.class)
	@ResponseStatus(code=HttpStatus.BAD_REQUEST)
	@ResponseBody
	public BaseResponseWrapper processEmptyFieldException(EmptyFieldException emptyFieldException) {
		
		logger.info("Creating response for EmptyFieldException");
		BaseResponseWrapper response = new BaseResponseWrapper();
		response.addError(new BaseError("FIELD IS EMPTY", emptyFieldException.getMessage()));
		return response;
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(code=HttpStatus.BAD_REQUEST)
	@ResponseBody
	public BaseResponseWrapper processResourceNotFoundException(ResourceNotFoundException resourceNotFoundException) {
		logger.info("Creating response for ResourceNotFoundException");
		BaseResponseWrapper response = new BaseResponseWrapper();
		response.addError(new BaseError("ID NOT FOUND", resourceNotFoundException.getMessage()));
		return response;
	}
	
	@ExceptionHandler({StorageException.class})
	@ResponseStatus(code=HttpStatus.BAD_REQUEST)
	@ResponseBody
	public BaseResponseWrapper processStorageException(HttpServletResponse httpRes, StorageException storageException) {
		logger.info("Creating response for FileStorageException");
		BaseResponseWrapper response = new BaseResponseWrapper();
		response.addError(new BaseError("FILE STORAGE ERROR", storageException.getMessage()));
		return response;
	}
	
	@ExceptionHandler({StorageFileNotFoundException.class})
	@ResponseStatus(code=HttpStatus.BAD_REQUEST)
	@ResponseBody
	public BaseResponseWrapper processStorageFileNotFoundException(HttpServletResponse httpRes, StorageFileNotFoundException fileNt) {
		logger.info("Creating response for StorageFileNotFoundException");
		BaseResponseWrapper response = new BaseResponseWrapper();
		response.addError(new BaseError("ID NOT FOUND", fileNt.getMessage()));
		return response;
	}
	
	@ExceptionHandler({MaxUploadSizeExceededException.class})
	@ResponseStatus(code=HttpStatus.BAD_REQUEST)
	@ResponseBody
	public BaseResponseWrapper processMaxUploadSizeExceededException(HttpServletResponse httpRes, MaxUploadSizeExceededException maxUploadSize) {
		logger.info("Creating response for MaxUploadSizeExceededException");
		BaseResponseWrapper response = new BaseResponseWrapper();
		response.addError(new BaseError("FILE SIZE EXCEEDED", "File size cannot be more than "+maxUploadSize.getMaxUploadSize()+" bytes"));
		return response;
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code=HttpStatus.BAD_REQUEST)
    @ResponseBody
	public BaseResponseWrapper processValidationError(MethodArgumentNotValidException ex) {
		
		logger.info("Creating response for Validation Errors");
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		
		return processFieldErrors(fieldErrors);
	}
	
	public BaseResponseWrapper processFieldErrors(List<FieldError> fieldErrors) {
		BaseResponseWrapper wrapper = new BaseResponseWrapper();
		
		for(FieldError fieldError: fieldErrors) {
			BaseError error = new BaseError();
			String localizedErrorMessage = fieldError.getDefaultMessage();
//			String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
			error.setFieldId(fieldError.getField());
			error.setDetail(localizedErrorMessage);
			wrapper.addError(error);
		}
		
		return wrapper;
	}

//	private String resolveLocalizedErrorMessage(FieldError fieldError) {
//        Locale currentLocale =  LocaleContextHolder.getLocale();
//        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);
// 
//        return localizedErrorMessage;
//    }
}
