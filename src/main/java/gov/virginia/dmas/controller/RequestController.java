package gov.virginia.dmas.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import gov.virginia.dmas.dto.AdminRequestsCountDto;
import gov.virginia.dmas.dto.AssignmentDto;
import gov.virginia.dmas.dto.AssignmentRequestsCountDto;
import gov.virginia.dmas.dto.AssignmentResponseDto;
import gov.virginia.dmas.dto.ChangeFormDto;
import gov.virginia.dmas.dto.DivisionDto;
import gov.virginia.dmas.dto.DocumentRequestDto;
import gov.virginia.dmas.dto.DocumentsDto;
import gov.virginia.dmas.dto.NotificationDto;
import gov.virginia.dmas.dto.ReasonCodeRequestDto;
import gov.virginia.dmas.dto.ReasonCodeResponseDto;
import gov.virginia.dmas.dto.ReasonCodesDto;
import gov.virginia.dmas.dto.RequestResponseDto;
import gov.virginia.dmas.dto.RequestorDto;
import gov.virginia.dmas.dto.RequestorResponseDto;
import gov.virginia.dmas.dto.RequestsCountDto;
import gov.virginia.dmas.dto.TableResponseCountDto;
import gov.virginia.dmas.dto.TableResponseDto;
import gov.virginia.dmas.exception.MailNotSentException;
import gov.virginia.dmas.exception.ResourceNotFoundException;
import gov.virginia.dmas.exception.StorageException;
import gov.virginia.dmas.exception.StorageFileNotFoundException;
import gov.virginia.dmas.ldap.dto.EmployeeDTO;
import gov.virginia.dmas.ldap.services.EmployeeService;
import gov.virginia.dmas.services.RequestService;

@RestController
@CrossOrigin(origins = "*")
public class RequestController {

	@Autowired
	RequestService requestService;
	@Autowired
	EmployeeService employeeService;

	final static Logger logger = LogManager.getLogger(RequestController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody String welcomeMessage() {
		return "Welcome to OCLA Internal Web Application";
	}

	@RequestMapping(value = "/getAllRequestsCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RequestsCountDto getAllRequestsCount() throws ResourceNotFoundException {

		logger.info("Getting all the requests count.");

		RequestsCountDto response = requestService.getAllRequestsCount();

		return response;
	}

	@RequestMapping(value = "/getRequestsCount/{formID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RequestsCountDto getRequestsCount(@PathVariable Long formID) throws ResourceNotFoundException {

		logger.info("Getting the requests count based on the formID.");

		RequestsCountDto response = requestService.getRequestsCount(formID);

		return response;
	}
	
	@RequestMapping(value = "/getAdminRequestsCount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AdminRequestsCountDto getAdminRequestsCount() throws ResourceNotFoundException {
		
		logger.info("Getting the requests count for the Admin role.");
		
		AdminRequestsCountDto response = requestService.getAdminRequestsCount();
		
		return response;
	}

	@RequestMapping(value = "/getPendingRequestsTable/{formID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<TableResponseDto> getPendingRequestsTable(@PathVariable Long formID)
			throws ResourceNotFoundException {

		logger.info("Getting the pending requests based on the Form Type.");

		List<TableResponseDto> response = new ArrayList<TableResponseDto>();

		response = requestService.getPendingRequestsTable(formID);

		return response;
	}

	@RequestMapping(value = "/getClosedRequestsTable/{formID}/{startNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody TableResponseCountDto getClosedRequestsTable(@PathVariable Long formID,
			@PathVariable int startNumber) throws ResourceNotFoundException {

		logger.info("Getting the closed requests based on the form Type");

		TableResponseCountDto response = new TableResponseCountDto();

		response = requestService.getClosedRequestsTable(formID, startNumber);

		return response;
	}

	@RequestMapping(value = "/getAllPendingRequestsTable/{startNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody TableResponseCountDto getAllPendingRequestsTable(@PathVariable int startNumber)
			throws ResourceNotFoundException {

		logger.info("Getting all the pending requests");

		TableResponseCountDto response = new TableResponseCountDto();

		response = requestService.getAllPendingRequestsTable(startNumber);

		return response;
	}

	@RequestMapping(value = "/getAllClosedRequestsTable/{startNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody TableResponseCountDto getAllClosedRequestsTable(@PathVariable int startNumber)
			throws ResourceNotFoundException {

		logger.info("Getting all the closed requests");

		TableResponseCountDto response = new TableResponseCountDto();

		response = requestService.getAllClosedRequestsTable(startNumber);

		return response;
	}

	@RequestMapping(value = "/getRequest/{formID}/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RequestResponseDto getRequest(@PathVariable Long id, @PathVariable Long formID)
			throws ResourceNotFoundException {

		logger.info("Getting the request data based on the ID and form type.");

		RequestResponseDto response = new RequestResponseDto();

		if (formID == 1 || formID == 2 || formID == 3 || formID == 4) {
			response = requestService.getGeneralRequest(id, formID);
		} else if (formID == 5) {
			response = requestService.getMediaRequest(id, formID);
		} else if (formID == 6) {
			response = requestService.getElectedOfficialRequest(id, formID);
		} else if (formID == 7) {
			response = requestService.getReportProblemRequest(id, formID);
		}
		return response;
	}

	@RequestMapping(value = "/updateRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RequestResponseDto updateRequest(@Valid @RequestBody RequestorDto request)
			throws ResourceNotFoundException, MailNotSentException {

		logger.info("Updating the request based on the form type.");

		RequestResponseDto response = new RequestResponseDto();

		if (request.getFormID() == 1 || request.getFormID() == 2 || request.getFormID() == 3
				|| request.getFormID() == 4) {
			response = requestService.updateGeneralRequest(request);
		} else if (request.getFormID() == 5) {
			response = requestService.updateMediaRequest(request);
		} else if (request.getFormID() == 6) {
			response = requestService.updateElectedOfficialRequest(request);
		} else if (request.getFormID() == 7) {
			response = requestService.updateReportProblemRequest(request);
		}
		return response;
	}

	@RequestMapping(value = "/getRequestorDocuments/{requestorID}", method = RequestMethod.GET)
	public @ResponseBody DocumentsDto getRequestorDocuments(@PathVariable Long requestorID) throws ResourceNotFoundException {
		
		logger.info("Getting the requestor documents for the respective ticket.");

		DocumentsDto response = requestService.getRequestorDocuments(requestorID);

		return response;
	}
	
	@RequestMapping(value = "/saveDocuments", method = RequestMethod.POST)
	public @ResponseBody DocumentsDto saveDocuments(
			@RequestPart(value = "data", required = true) @Valid DocumentRequestDto documents,
			@RequestPart(value = "files", required = true) @NotNull MultipartFile[] files)
			throws JsonParseException, JsonMappingException, IOException, StorageException, ResourceNotFoundException {

		logger.info("Adding the documents to the provided ticketID.");

		DocumentsDto response = requestService.saveDocuments(documents, files);

		return response;
	}

	@RequestMapping(value = "/getDocuments/{requestorID}", method = RequestMethod.GET)
	public @ResponseBody DocumentsDto getDocuments(@PathVariable Long requestorID) throws ResourceNotFoundException {

		logger.info("Getting the documents for the respective ticket.");

		DocumentsDto response = requestService.getDocuments(requestorID);

		return response;
	}

	@RequestMapping(value = "/deleteDocument", method = RequestMethod.POST)
	public @ResponseBody DocumentsDto deleteDocument(@RequestBody DocumentRequestDto documentDetails)
			throws ResourceNotFoundException {

		logger.info("Deleting the document and making it archive.");

		DocumentsDto response = requestService.deleteDocument(documentDetails);

		return response;
	}

	@RequestMapping(value = "documents/{id}/{filename}", method = RequestMethod.GET)
	public ResponseEntity<Resource> serveFile(@PathVariable long id, @PathVariable String filename)
			throws StorageFileNotFoundException, IOException, ResourceNotFoundException {
		logger.info("Serving the file on the browser");
		Resource file = requestService.loadAsResource(id);

//		Sending the name without the timestamp attached
		String fileName = file.getFilename();
		int lastIndex = fileName.lastIndexOf("%5B");
		int endingIndex = fileName.lastIndexOf(".");
		fileName = fileName.substring(0, lastIndex) + fileName.substring(endingIndex);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(file);
	}
	
	@RequestMapping(value = "requestorDocuments/{id}/{filename}", method = RequestMethod.GET)
	public ResponseEntity<Resource> serveRequestorFile(@PathVariable long id, @PathVariable String filename)
			throws StorageFileNotFoundException, IOException, ResourceNotFoundException {
		logger.info("Serving the requestor file on the browser");
		Resource file = requestService.loadAsRequestorResource(id);

//		Sending the name without the timestamp attached
		String fileName = file.getFilename();
		int lastIndex = fileName.lastIndexOf("%5B");
		int endingIndex = fileName.lastIndexOf(".");
		fileName = fileName.substring(0, lastIndex) + fileName.substring(endingIndex);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(file);
	}

	@RequestMapping(value = "/saveReasonCode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReasonCodeResponseDto saveReasonCode(@Valid @RequestBody ReasonCodeRequestDto reasonCode)
			throws ResourceNotFoundException {

		logger.info("Saving the reason code to the list of reason codes.");

		ReasonCodeResponseDto response = requestService.saveReasonCode(reasonCode);

		return response;
	}

	@RequestMapping(value = "/getReasonCodes/{formID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReasonCodesDto getReasonCodes(@PathVariable Long formID) throws ResourceNotFoundException {

		logger.info("Getting all the reason codes based on the formID.");

		ReasonCodesDto response = requestService.getReasonCodes(formID);

		return response;
	}
	
	@RequestMapping(value = "/setAssignments", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AssignmentResponseDto setAssignments(@Valid @RequestBody AssignmentDto request, HttpServletRequest req) throws ResourceNotFoundException, MailNotSentException {
		
		logger.info("Setting the assignments for a particular request.");
		String accessToken = null;
		accessToken = req.getHeader("Authorization");
//		Cookie cookies [] = req.getCookies();
//		for(int index= 0; index < cookies.length ; index++)
//		{
//			if(cookies[index].getName().equalsIgnoreCase("access_token"))
//			{
//				accessToken = cookies[index].getValue();
//				break;
//			}
//		}
		AssignmentResponseDto response = requestService.setAssignments(request,accessToken);
		
		
		
		return response;
	}
	
	@RequestMapping(value = "/getAssignments/{requestorID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AssignmentResponseDto getAssignments(@PathVariable Long requestorID) throws ResourceNotFoundException {
		
		logger.info("Getting all the assignments for a particular request.");
		
		AssignmentResponseDto response = requestService.getAssignments(requestorID);
		
		return response;
	}
	
	@RequestMapping(value = "/getAssignment/{requestorID}/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AssignmentResponseDto getAssignment(@PathVariable Long requestorID, @PathVariable String email) throws ResourceNotFoundException {
		
		logger.info("Getting the assignment for a particular request.");
		
		AssignmentResponseDto response = requestService.getAssignment(requestorID, email);
		
		return response;
	}
	
	/*
	 * @RequestMapping(value = "/deleteAssignment", method = RequestMethod.POST,
	 * consumes = MediaType.APPLICATION_JSON_VALUE) public @ResponseBody
	 * AssignmentResponseDto deleteAssignment(@RequestBody AssignmentRequestDto
	 * request) throws ResourceNotFoundException {
	 * 
	 * logger.info("Removing the assignment for a particular request.");
	 * 
	 * AssignmentResponseDto response = requestService.deleteAssignment(request);
	 * 
	 * return response; }
	 */	
	
	@RequestMapping(value= "/updateAssigment", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<AssignmentDto> updateAssignment(@RequestBody AssignmentDto assignmentDto) throws MailNotSentException{
		logger.info("Updating the assignment.");
		return requestService.updateAssignmentDTO(assignmentDto);
	}
	
	@RequestMapping(value = "/changeForm", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RequestorResponseDto  changeForm(@RequestBody ChangeFormDto request) throws ResourceNotFoundException, MailNotSentException {
		logger.info("ChangeForm for a particular request");
		return requestService.changeForm(request);
	}
	
	@RequestMapping(value = "/getAssignmentsRequestsCount/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public AssignmentRequestsCountDto getAssignmentsRequestsCount(@PathVariable String email) {
		
		logger.info("Getting the assignments for the respective person.");
		
		AssignmentRequestsCountDto response = requestService.getAssignmentsRequestsCount(email);
		
		return response;
	}
	
	@RequestMapping(value = "/getAssignmentsRequests/{email}/{formID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TableResponseDto> getAssignmentsRequests(@PathVariable String email, @PathVariable Long formID) {
		
		logger.info("Getting the assignments for the respective person.");
		
		List<TableResponseDto> response = requestService.getAssignmentsRequests(email, formID);
		
		return response;
	}
	
	@RequestMapping(value = "/getEmployeesFromLDAP/{userName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EmployeeDTO> getEmployeesFromLDAP(@PathVariable String userName) {
		logger.info("Getting the assignments for the respective person.");
		List<EmployeeDTO> employeeDTOs = employeeService.searchEmployees(userName);
		return employeeDTOs;
	}
	
	@RequestMapping(value = "/getAllFromLDAP", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EmployeeDTO> getAllFromLDAP() {
		logger.info("Getting the assignments for the respective person.");
		List<EmployeeDTO> employeeDTOs = employeeService.getAllFromLDAP();
		return employeeDTOs;
	}
	
	@RequestMapping(value = "/sendNotifications", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> sendCreationNotifications(@RequestBody NotificationDto notification) throws ResourceNotFoundException, MailNotSentException {
		
		logger.info("Sending notifications to users and internal staff.");
		return requestService.sendCreationEmails(notification);
	}
	
	@RequestMapping(value = "/getDivisions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DivisionDto> getDivisions() {
	
		logger.info("Getting all the divisions.");
		return requestService.getDivisions();
	}
	
	@RequestMapping(value = "/saveDivisions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<DivisionDto> saveDivisions(@RequestBody List<DivisionDto> divisions) {
		
		logger.info("Adding the divisions to the list of divisions");
		return requestService.saveDivisions(divisions);
	}
	
	@RequestMapping(value = "/deleteDivision", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<DivisionDto> deleteDivision(@RequestBody DivisionDto division) throws ResourceNotFoundException {
		
		logger.info("Removing the division from the list of divisions.");
		return requestService.deleteDivision(division);
	}
	
}
