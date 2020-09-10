package gov.virginia.dmas.services;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import gov.virginia.dmas.dto.AdminRequestsCountDto;
import gov.virginia.dmas.dto.AssignmentDto;
import gov.virginia.dmas.dto.AssignmentRequestDto;
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

public interface RequestService {
	
	public RequestsCountDto getAllRequestsCount() throws ResourceNotFoundException;
	
	public RequestsCountDto getRequestsCount(Long formID) throws ResourceNotFoundException;
	
	public AdminRequestsCountDto getAdminRequestsCount() throws ResourceNotFoundException;
	
	public TableResponseCountDto getAllPendingRequestsTable(int startNumber) throws ResourceNotFoundException;
	
	public TableResponseCountDto getAllClosedRequestsTable(int startNumber) throws ResourceNotFoundException;
	
	public List<TableResponseDto> getPendingRequestsTable(Long formID) throws ResourceNotFoundException;
	
	public TableResponseCountDto getClosedRequestsTable(Long formID, int startNumber) throws ResourceNotFoundException;
	
	public RequestResponseDto getGeneralRequest(Long id, Long formID) throws ResourceNotFoundException;
	
	public RequestResponseDto getMediaRequest(Long id, Long formID) throws ResourceNotFoundException;
	
	public RequestResponseDto getElectedOfficialRequest(Long id, Long formID) throws ResourceNotFoundException;
	
	public RequestResponseDto getReportProblemRequest(Long id, Long formID) throws ResourceNotFoundException;
	
	public RequestResponseDto updateGeneralRequest(RequestorDto request) throws ResourceNotFoundException, MailNotSentException;
	
	public RequestResponseDto updateMediaRequest(RequestorDto request) throws ResourceNotFoundException, MailNotSentException;
	
	public RequestResponseDto updateElectedOfficialRequest(RequestorDto request) throws ResourceNotFoundException, MailNotSentException;
	
	public RequestResponseDto updateReportProblemRequest(RequestorDto request) throws ResourceNotFoundException, MailNotSentException;
	
	public ReasonCodeResponseDto saveReasonCode(ReasonCodeRequestDto reasonCode) throws ResourceNotFoundException;
	
	public ReasonCodesDto getReasonCodes(Long formID) throws ResourceNotFoundException;
		
	public DocumentsDto saveDocuments(DocumentRequestDto documentDetails, MultipartFile[] files) throws StorageException, IOException, ResourceNotFoundException;
	
	public DocumentsDto getDocuments(Long requestorID) throws ResourceNotFoundException;
	
	public DocumentsDto deleteDocument(DocumentRequestDto documentDetails) throws ResourceNotFoundException;
	
	public Resource loadAsResource(Long id) throws StorageFileNotFoundException, ResourceNotFoundException;
	
	public Resource loadAsRequestorResource(Long id) throws ResourceNotFoundException, StorageFileNotFoundException;
	
	public DocumentsDto getRequestorDocuments(Long requestorID) throws ResourceNotFoundException;
	
	public AssignmentResponseDto setAssignments(AssignmentDto request) throws ResourceNotFoundException, MailNotSentException;
	
	public AssignmentResponseDto getAssignments(Long requestorID) throws ResourceNotFoundException;
	
//	public AssignmentResponseDto deleteAssignment(AssignmentRequestDto assignmentDetails) throws ResourceNotFoundException;
	
	public AssignmentRequestsCountDto getAssignmentsRequestsCount(String email);
	
	public AssignmentResponseDto updateAssignment(AssignmentRequestDto assignmentDetails) throws ResourceNotFoundException;
	
	public List<TableResponseDto> getAssignmentsRequests(String email, Long formID);
	
	public List<AssignmentDto> updateAssignmentDTO(AssignmentDto assignmentDto) throws MailNotSentException;

	public RequestorResponseDto changeForm(ChangeFormDto request) throws ResourceNotFoundException, MailNotSentException;

	public ResponseEntity<String> sendCreationEmails(NotificationDto notification) throws ResourceNotFoundException, MailNotSentException;
	
	public AssignmentResponseDto getAssignment(Long requestorID, String email) throws ResourceNotFoundException;
	
	public List<DivisionDto> getDivisions();
	
	public List<DivisionDto> saveDivisions(List<DivisionDto> divisions);
	
	public List<DivisionDto> deleteDivision(DivisionDto division) throws ResourceNotFoundException;

	public AssignmentResponseDto setAssignments(@Valid AssignmentDto request, String accessToken) throws ResourceNotFoundException, MailNotSentException;
}
