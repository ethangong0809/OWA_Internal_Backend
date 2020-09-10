package gov.virginia.dmas.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import gov.virginia.dmas.config.PropertiesConfig;
import gov.virginia.dmas.dao.AssignmentRepository;
import gov.virginia.dmas.dao.DivisionRepository;
import gov.virginia.dmas.dao.DocumentInternalRepository;
import gov.virginia.dmas.dao.DocumentRepository;
import gov.virginia.dmas.dao.ElectedOfficialRequestInternalRepository;
import gov.virginia.dmas.dao.ElectedOfficialRequestRepository;
import gov.virginia.dmas.dao.FormNameRepository;
import gov.virginia.dmas.dao.GeneralRequestInternalRepository;
import gov.virginia.dmas.dao.GeneralRequestRepository;
import gov.virginia.dmas.dao.MediaRequestInternalRepository;
import gov.virginia.dmas.dao.MediaRequestRepository;
import gov.virginia.dmas.dao.MessageTemplateRepository;
import gov.virginia.dmas.dao.NotificationRepository;
import gov.virginia.dmas.dao.ReasonCodeRepository;
import gov.virginia.dmas.dao.ReportProblemInternalRepository;
import gov.virginia.dmas.dao.ReportProblemRepository;
import gov.virginia.dmas.dao.RequestorInternalRepository;
import gov.virginia.dmas.dao.RequestorRepository;
import gov.virginia.dmas.dao.UserRepository;
import gov.virginia.dmas.dto.AdminRequestsCountDto;
import gov.virginia.dmas.dto.AssignmentDto;
import gov.virginia.dmas.dto.AssignmentRequestDto;
import gov.virginia.dmas.dto.AssignmentRequestsCountDto;
import gov.virginia.dmas.dto.AssignmentResponseDto;
import gov.virginia.dmas.dto.ChangeFormDto;
import gov.virginia.dmas.dto.CommentResponseDto;
import gov.virginia.dmas.dto.DivisionDto;
import gov.virginia.dmas.dto.DocumentDto;
import gov.virginia.dmas.dto.DocumentRequestDto;
import gov.virginia.dmas.dto.DocumentResponseDto;
import gov.virginia.dmas.dto.DocumentsDto;
import gov.virginia.dmas.dto.ElectedOfficialResponseDto;
import gov.virginia.dmas.dto.GeneralResponseDto;
import gov.virginia.dmas.dto.MediaResponseDto;
import gov.virginia.dmas.dto.NotificationDto;
import gov.virginia.dmas.dto.ReasonCodeRequestDto;
import gov.virginia.dmas.dto.ReasonCodeResponseDto;
import gov.virginia.dmas.dto.ReasonCodesDto;
import gov.virginia.dmas.dto.ReportProblemResponseDto;
import gov.virginia.dmas.dto.RequestResponseDto;
import gov.virginia.dmas.dto.RequestorDto;
import gov.virginia.dmas.dto.RequestorResponseDto;
import gov.virginia.dmas.dto.RequestsCountDto;
import gov.virginia.dmas.dto.TableResponseCountDto;
import gov.virginia.dmas.dto.TableResponseDto;
import gov.virginia.dmas.entity.AssignmentEntity;
import gov.virginia.dmas.entity.CommentInternalEntity;
import gov.virginia.dmas.entity.DivisionEntity;
import gov.virginia.dmas.entity.DocumentEntity;
import gov.virginia.dmas.entity.DocumentInternalEntity;
import gov.virginia.dmas.entity.ElectedOfficialRequestEntity;
import gov.virginia.dmas.entity.ElectedOfficialRequestInternalEntity;
import gov.virginia.dmas.entity.FormNameEntity;
import gov.virginia.dmas.entity.GeneralRequestEntity;
import gov.virginia.dmas.entity.GeneralRequestInternalEntity;
import gov.virginia.dmas.entity.MediaRequestEntity;
import gov.virginia.dmas.entity.MediaRequestInternalEntity;
import gov.virginia.dmas.entity.MessageTemplateEntity;
import gov.virginia.dmas.entity.NotificationEntity;
import gov.virginia.dmas.entity.ReasonCodeEntity;
import gov.virginia.dmas.entity.ReportProblemEntity;
import gov.virginia.dmas.entity.ReportProblemInternalEntity;
import gov.virginia.dmas.entity.RequestorEntity;
import gov.virginia.dmas.entity.RequestorInternalEntity;
import gov.virginia.dmas.exception.MailNotSentException;
import gov.virginia.dmas.exception.ResourceNotFoundException;
import gov.virginia.dmas.exception.StorageException;
import gov.virginia.dmas.exception.StorageFileNotFoundException;
import gov.virginia.dmas.iam.dto.UserDTO;
import gov.virginia.dmas.ldap.dto.EmployeeDTO;
import gov.virginia.dmas.ldap.services.EmployeeService;

@Service
public class RequestServiceImpl implements RequestService {

	final static Logger logger = LogManager.getLogger(RequestServiceImpl.class);

	@Autowired
	RequestorRepository requestorRepository;

	@Autowired
	FormNameRepository formNameRepository;

	@Autowired
	GeneralRequestRepository generalRequestRepository;

	@Autowired
	MediaRequestRepository mediaRequestRepository;

	@Autowired
	ElectedOfficialRequestRepository electedOfficialRequestRepository;

	@Autowired
	ReportProblemRepository reportProblemRepository;

	@Autowired
	ReasonCodeRepository reasonCodeRepository;

	@Autowired
	RequestorInternalRepository requestorInternalRepository;

	@Autowired
	GeneralRequestInternalRepository generalRequestInternalRepository;

	@Autowired
	MediaRequestInternalRepository mediaRequestInternalRepository;

	@Autowired
	ElectedOfficialRequestInternalRepository electedOfficialRequestInternalRepository;

	@Autowired
	ReportProblemInternalRepository reportProblemInternalRepository;

	@Autowired
	DocumentInternalRepository documentInternalRepository;

	@Autowired
	DocumentRepository documentRepository;
	
	@Autowired
	AssignmentRepository assignmentRepository;

	@Autowired
	MailService mailService;
	
	@Autowired
	MessageTemplateRepository messageTemplateRepository;
	
	@Autowired
	NotificationRepository notificationRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	DivisionRepository divisionRepository;
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	@Autowired
	EmployeeService employeeService;
	
	private RestTemplate restTemplate = null;
	
	@Transactional
	public AssignmentRequestsCountDto getAssignmentsRequestsCount(String email) {
		
		logger.info("Inside getAssignmentsRequestsCount service.");
		
		AssignmentRequestsCountDto requestsCount = new AssignmentRequestsCountDto();
		
		RequestsCountDto procurementRequestsCount = new RequestsCountDto();
		RequestsCountDto cccplusRequestsCount = new RequestsCountDto();
		RequestsCountDto med4RequestsCount = new RequestsCountDto();
		RequestsCountDto generalRequestsCount = new RequestsCountDto();
		RequestsCountDto mediaRequestsCount = new RequestsCountDto();
		RequestsCountDto electedOfficialRequestsCount = new RequestsCountDto();
		RequestsCountDto reportProblemRequestsCount = new RequestsCountDto();
		
		int pendingProcurementCount = 0;
		int pendingCccplusCount = 0;
		int pendingMed4Count = 0;
		int pendingGeneralCount = 0;
		int pendingMediaCount = 0;
		int pendingElectedOfficialCount = 0;
		int pendingReportProblemCount = 0;
		
		int closedProcurementCount = 0;
		int closedCccplusCount = 0;
		int closedMed4Count = 0;
		int closedGeneralCount = 0;
		int closedMediaCount = 0;
		int closedElectedOfficialCount = 0;
		int closedReportProblemCount = 0;
		
//		Getting the pending requests for that particular person.
		List<AssignmentEntity> pendingAssignmentEntities = assignmentRepository.findByEmailAndStatus(email, "Open");
		
		for(AssignmentEntity assignmentEntity : pendingAssignmentEntities) {
			if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID()==1) {
				pendingProcurementCount++;
			}
			else if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID()==2) {
				pendingCccplusCount++;
			}
			else if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID()==3) {
				pendingMed4Count++;
			}
			else if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID()==4) {
				pendingGeneralCount++;
			}
			else if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID()==5) {
				pendingMediaCount++;
			}
			else if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID()==6) {
				pendingElectedOfficialCount++;
			}
			else if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID()==7) {
				pendingReportProblemCount++;
			}
		}
		procurementRequestsCount.setPendingRequests(pendingProcurementCount);
		cccplusRequestsCount.setPendingRequests(pendingCccplusCount);
		med4RequestsCount.setPendingRequests(pendingMed4Count);
		generalRequestsCount.setPendingRequests(pendingGeneralCount);
		mediaRequestsCount.setPendingRequests(pendingMediaCount);
		electedOfficialRequestsCount.setPendingRequests(pendingElectedOfficialCount);
		reportProblemRequestsCount.setPendingRequests(pendingReportProblemCount);
		
//		Getting the closed requests for that particular person.
		List<AssignmentEntity> closedAssignmentEntities = assignmentRepository.findByEmailAndStatus(email, "Closed");
		
		for(AssignmentEntity assignmentEntity : closedAssignmentEntities) {
			if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID()==1) {
				closedProcurementCount++;
			}
			else if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID()==2) {
				closedCccplusCount++;
			}
			else if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID()==3) {
				closedMed4Count++;
			}
			else if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID()==4) {
				closedGeneralCount++;
			}
			else if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID()==5) {
				closedMediaCount++;
			}
			else if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID()==6) {
				closedElectedOfficialCount++;
			}
			else if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID()==7) {
				closedReportProblemCount++;
			}
		}
		procurementRequestsCount.setClosedRequests(closedProcurementCount);
		cccplusRequestsCount.setClosedRequests(closedCccplusCount);
		med4RequestsCount.setClosedRequests(closedMed4Count);
		generalRequestsCount.setClosedRequests(closedGeneralCount);
		mediaRequestsCount.setClosedRequests(closedMediaCount);
		electedOfficialRequestsCount.setClosedRequests(closedElectedOfficialCount);
		reportProblemRequestsCount.setClosedRequests(closedReportProblemCount);
		
		requestsCount.setProcurementRequests(procurementRequestsCount);
		requestsCount.setCccplusRequests(cccplusRequestsCount);
		requestsCount.setMed4Requests(med4RequestsCount);
		requestsCount.setGeneralRequests(generalRequestsCount);
		requestsCount.setMediaRequests(mediaRequestsCount);
		requestsCount.setElectedOfficialRequests(electedOfficialRequestsCount);
		requestsCount.setReportProblemRequests(reportProblemRequestsCount);
		
		return requestsCount;
	}
	
	@Transactional
	public List<TableResponseDto> getAssignmentsRequests(String email, Long formID) {
		
		logger.info("Inside getAssignmentsRequests service.");
		
		List<AssignmentEntity> assignmentEntities = assignmentRepository.findByEmailAndActive(email, true);
		
		List<TableResponseDto> assignmentRequests = new ArrayList<TableResponseDto>();
		
		for(AssignmentEntity assignmentEntity : assignmentEntities) {
			if(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID().equals(formID)) {
				if (formID == 6) {
					TableResponseDto pendingRequestorDto = new TableResponseDto();
					BeanUtils.copyProperties(assignmentEntity.getRequestorInternalEntity(), pendingRequestorDto);
					pendingRequestorDto.setMemberFirstName(
							assignmentEntity.getRequestorInternalEntity().getElectedOfficialInternalEntity().getConstFirstname());
					pendingRequestorDto.setMemberLastName(
							assignmentEntity.getRequestorInternalEntity().getElectedOfficialInternalEntity().getConstLastname());
					pendingRequestorDto
							.setMedicaidID(assignmentEntity.getRequestorInternalEntity().getElectedOfficialInternalEntity().getConstMedID());
					pendingRequestorDto.setSSN(assignmentEntity.getRequestorInternalEntity().getElectedOfficialInternalEntity().getConstSSN());
					pendingRequestorDto.setFormID(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID());
					assignmentRequests.add(pendingRequestorDto);
				} else if (formID == 7) {
					TableResponseDto pendingRequestorDto = new TableResponseDto();
					BeanUtils.copyProperties(assignmentEntity.getRequestorInternalEntity(), pendingRequestorDto);
					pendingRequestorDto
							.setMemberFirstName(assignmentEntity.getRequestorInternalEntity().getReportProblemInternalEntity().getMemFirstname());
					pendingRequestorDto
							.setMemberLastName(assignmentEntity.getRequestorInternalEntity().getReportProblemInternalEntity().getMemLastname());
					pendingRequestorDto
							.setMedicaidID(assignmentEntity.getRequestorInternalEntity().getReportProblemInternalEntity().getMemMedID());
					pendingRequestorDto.setSSN(assignmentEntity.getRequestorInternalEntity().getReportProblemInternalEntity().getMemSSN());
					pendingRequestorDto.setFormID(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID());
					assignmentRequests.add(pendingRequestorDto);
				} else {
					TableResponseDto pendingRequestorDto = new TableResponseDto();
					BeanUtils.copyProperties(assignmentEntity.getRequestorInternalEntity(), pendingRequestorDto);
					pendingRequestorDto.setFormID(assignmentEntity.getRequestorInternalEntity().getFormName().getFormID());
					assignmentRequests.add(pendingRequestorDto);
				}
			}
		}
		return assignmentRequests;
	}

	@Transactional
	public AssignmentResponseDto setAssignments(AssignmentDto request) throws ResourceNotFoundException, MailNotSentException {
		
		logger.info("Inside setAssignments service.");
		
		RequestorInternalEntity requestorInternalEntity = requestorInternalRepository.findById(request.getRequestorId()).orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));
		
		List<AssignmentEntity> assignmentEntities = new ArrayList<AssignmentEntity>();
		List<AssignmentDto> savedAssignments = new ArrayList<AssignmentDto>();
		
//		Setting the assignments to the request
		AssignmentEntity assignmentEntity = new AssignmentEntity();
		BeanUtils.copyProperties(request, assignmentEntity);
		assignmentEntity.setActive(true);
		assignmentEntity.setRequestorInternalEntity(requestorInternalEntity);
		//			assignmentEntity.setCreatedBy(request.getCreatedBy());

		if(requestorInternalEntity.getAssignmentEntities().size()!=0) {
			requestorInternalEntity.getAssignmentEntities().add(assignmentEntity);
		}
		else {
			assignmentEntities.add(assignmentEntity);
			requestorInternalEntity.setAssignmentEntities(assignmentEntities);
		}

		requestorInternalEntity.setUpdatedBy(request.getCreatedBy());
		requestorInternalEntity.setUpdatedTime(new Timestamp(new Date().getTime()));
		List<AssignmentEntity> savedAssignmentEntities = assignmentRepository.saveAll(requestorInternalEntity.getAssignmentEntities());

		AssignmentResponseDto response = new AssignmentResponseDto();
		
		for(AssignmentEntity savedAssignmentEntity: savedAssignmentEntities) {
			AssignmentDto savedAssignment = new AssignmentDto();
			BeanUtils.copyProperties(savedAssignmentEntity, savedAssignment);
			savedAssignments.add(savedAssignment);
		}
		
		response.setRequestorID(request.getRequestorId());
		response.setAssignments(savedAssignments);
		
		
		
//		Sending email notification
		MessageTemplateEntity messageTemplateEntity = messageTemplateRepository.findByStatus("Assigned");
		String subject = messageTemplateEntity.getSubject();
		String messageTemplate = messageTemplateEntity.getMessageTemplate();
		boolean assigneeMailSent = mailService.sendEmailNotification(request.getEmail(), subject, messageTemplate, requestorInternalEntity.getTicketID());
		if(!assigneeMailSent) {
			throw new MailNotSentException("Problem in sending Email to the assignee.");
		}
		return response;
	}
	
	@Transactional
	public RequestorResponseDto changeForm(ChangeFormDto request) throws ResourceNotFoundException, MailNotSentException{
		
		logger.info("Inside changeForm service");
		
		RequestorResponseDto response = new RequestorResponseDto();
		RequestorInternalEntity requestorInternalEntity = requestorInternalRepository.findById(request.getRequestorID()).orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));
		Optional<FormNameEntity> formNameEntityOptional = formNameRepository.findById(request.getFormID());
		if(formNameEntityOptional.get() != null)
			requestorInternalEntity.setFormName(formNameEntityOptional.get());
		requestorInternalEntity.getFormName().setFormID(request.getFormID());
		if(request.getFormID()==1) {
			requestorInternalEntity.getGeneralRequestInternalEntity().setRequestType("Procurement");
		}
		else if(request.getFormID()==2) {
			requestorInternalEntity.getGeneralRequestInternalEntity().setRequestType("CCCPlus");
		}
		else if(request.getFormID()==3) {
			requestorInternalEntity.getGeneralRequestInternalEntity().setRequestType("Medallion4");
		}
		else {
			requestorInternalEntity.getGeneralRequestInternalEntity().setRequestType("General");
		}
		
//		Sending email notification
//		MessageTemplateEntity messageTemplateEntity = messageTemplateRepository.findByFormNameEntityAndStatus(formNameEntityOptional.get(),"Moved");
//		String subject = messageTemplateEntity.getSubject();
//		String messageTemplate = messageTemplateEntity.getMessageTemplate();
//		String[] recepients = getRecepients(formNameEntityOptional.get());
//		
//		boolean groupMailSent = mailService.sendGroupEmailNotification(recepients, subject, messageTemplate, requestorInternalEntity.getTicketID());
//		if(!groupMailSent) {
//			throw new MailNotSentException("Problem in sending Email to the group.");
//		}
		
		return response;
	}
	
	@Transactional
	public AssignmentResponseDto getAssignments(Long requestorID) throws ResourceNotFoundException {
		
		logger.info("Inside getAssignments service.");
		
		AssignmentResponseDto response = new AssignmentResponseDto();
		List<AssignmentDto> assignments = new ArrayList<AssignmentDto>();
		
		RequestorInternalEntity requestorInternalEntity = requestorInternalRepository.findById(requestorID).orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));
		
		List<AssignmentEntity> assignmentEntities = assignmentRepository.findByRequestorInternalEntityAndActive(requestorInternalEntity,true);
		assignmentEntities.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));
		for(AssignmentEntity AssignmentEntity: assignmentEntities) {
			AssignmentDto assignment = new AssignmentDto();
			BeanUtils.copyProperties(AssignmentEntity, assignment);
			assignment.setAssignmentId(AssignmentEntity.getId());
			assignment.setRequestorId(AssignmentEntity.getRequestorInternalEntity().getId());
			assignments.add(assignment);
		}
		
		response.setRequestorID(requestorInternalEntity.getId());
		response.setAssignments(assignments);
		return response;
	}
	
	@Transactional
	public AssignmentResponseDto getAssignment(Long requestorID, String email) throws ResourceNotFoundException {
		
		logger.info("Inside getAssignment service");
		
		AssignmentResponseDto response = new AssignmentResponseDto();
		List<AssignmentDto> assignments = new ArrayList<AssignmentDto>();
		
		RequestorInternalEntity requestorInternalEntity = requestorInternalRepository.findById(requestorID).orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));
		
		List<AssignmentEntity> assignmentEntities = assignmentRepository.findByRequestorInternalEntityAndEmailAndActive(requestorInternalEntity, email, true);
		
		for(AssignmentEntity AssignmentEntity: assignmentEntities) {
			AssignmentDto assignment = new AssignmentDto();
			BeanUtils.copyProperties(AssignmentEntity, assignment);
			assignment.setAssignmentId(AssignmentEntity.getId());
			assignment.setRequestorId(AssignmentEntity.getRequestorInternalEntity().getId());
			assignments.add(assignment);
		}
		
		response.setRequestorID(requestorInternalEntity.getId());
		response.setAssignments(assignments);
		return response;
	}
	
//	@Transactional
//	public AssignmentResponseDto deleteAssignment(AssignmentRequestDto assignmentDetails) throws ResourceNotFoundException {
//		
//		logger.info("Inside deleteAssignment service.");
//		
//		AssignmentEntity assignmentEntity = new AssignmentEntity();
//
//		assignmentEntity = assignmentRepository.findById(assignmentDetails.getId()).orElseThrow(() -> new ResourceNotFoundException("Assignment Not Found."));
//		assignmentEntity.setUpdatedBy(assignmentDetails.getUpdatedBy());
//		
////		assignmentEntity.setActive(false);
//		
//		assignmentEntity.getRequestorInternalEntity().setUpdatedBy(assignmentDetails.getUpdatedBy());
//		assignmentEntity.getRequestorInternalEntity().setUpdatedTime(new Timestamp(new Date().getTime()));
//		
//		assignmentEntity = assignmentRepository.saveAndFlush(assignmentEntity);
//		
//		AssignmentResponseDto response = this.getAssignments(assignmentEntity.getRequestorInternalEntity().getId());
//
//		
//		AssignmentDto assingmentDTO = null;
//		List<AssignmentDto> assignmentDTOs = new ArrayList<AssignmentDto>();
//		
//		RequestorInternalEntity requestorInternalEntity = new RequestorIn
//		List<AssignmentEntity> assignmentEntities = assignmentRepository.findByRequestorInternalEntity();
//		if(assignmentEntities.size() > 0) {
//			for(AssignmentEntity assignment : assignmentEntities) {
//				assingmentDTO = new AssignmentDto();
//				BeanUtils.copyProperties(assignment, assingmentDTO);
//				assignmentDTOs.add(assingmentDTO);
//			}
//		}
//		
//		
//		response.setAssignments(assignmentDTOs);
//		return response;
//	}
	
	@Transactional
	public RequestsCountDto getAllRequestsCount() throws ResourceNotFoundException {

		logger.info("Inside getAllRequestsCount service.");

		int pendingRequests = 0;
		int closedRequests = 0;
		RequestsCountDto count = new RequestsCountDto();

		pendingRequests = requestorInternalRepository.findByStatus("Open").size();
		closedRequests = requestorInternalRepository.findByStatus("Closed").size();

		count.setPendingRequests(pendingRequests);
		count.setClosedRequests(closedRequests);
		return count;
	}

	@Transactional
	public AssignmentResponseDto updateAssignment(AssignmentRequestDto assignmentDetails) throws ResourceNotFoundException{
		
		logger.info("Inside updateAssignment service.");
		//Setting the assignment 
		AssignmentEntity assignmentEntity = new AssignmentEntity();
		//get original Entity
		assignmentEntity =assignmentRepository.findById(assignmentDetails.getId()).orElseThrow(() -> new ResourceNotFoundException("Assignment Not Found"));
		AssignmentResponseDto reponse = new AssignmentResponseDto();
		assignmentEntity.setUpdatedBy(assignmentDetails.getUpdatedBy());
		assignmentEntity.getRequestorInternalEntity().setUpdatedBy(assignmentDetails.getUpdatedBy());
		assignmentEntity.getRequestorInternalEntity().setUpdatedTime(new Timestamp(new Date().getTime()));
		assignmentEntity=assignmentRepository.saveAndFlush(assignmentEntity);
		
		AssignmentResponseDto response = this.getAssignments(assignmentEntity.getRequestorInternalEntity().getId());
		response.setAssignments(assignmentDetails.getAssignments());
		return reponse;
	}
		
	@Transactional
	public RequestsCountDto getRequestsCount(Long formID) throws ResourceNotFoundException {

		logger.info("Inside getRequestsCount service.");

		int pendingRequests = 0;
		int closedRequests = 0;
		RequestsCountDto count = new RequestsCountDto();

		FormNameEntity formNameEntity = formNameRepository.findById(formID)
				.orElseThrow(() -> new ResourceNotFoundException("Form not found."));
		pendingRequests = requestorInternalRepository.findByFormNameAndStatus(formNameEntity, "Open").size();
		closedRequests = requestorInternalRepository.findByFormNameAndStatus(formNameEntity, "Closed").size();

		count.setPendingRequests(pendingRequests);
		count.setClosedRequests(closedRequests);
		return count;
	}
	
	@Transactional
	public AdminRequestsCountDto getAdminRequestsCount() throws ResourceNotFoundException {
		
		logger.info("Inside getAdminRequestsCount service.");
		
		AdminRequestsCountDto requestsCount = new AdminRequestsCountDto();
		
		RequestsCountDto procurementRequestsCount = new RequestsCountDto();
		RequestsCountDto cccplusRequestsCount = new RequestsCountDto();
		RequestsCountDto med4RequestsCount = new RequestsCountDto();
		RequestsCountDto generalRequestsCount = new RequestsCountDto();
		RequestsCountDto mediaRequestsCount = new RequestsCountDto();
		RequestsCountDto electedOfficialRequestsCount = new RequestsCountDto();
		RequestsCountDto reportProblemRequestsCount = new RequestsCountDto();
		
		List<RequestorInternalEntity> pendingRequests = new ArrayList<RequestorInternalEntity>();
		List<RequestorInternalEntity> closedRequests = new ArrayList<RequestorInternalEntity>();
		
		int pendingProcurementCount = 0;
		int pendingCccplusCount = 0;
		int pendingMed4Count = 0;
		int pendingGeneralCount = 0;
		int pendingMediaCount = 0;
		int pendingElectedOfficialCount = 0;
		int pendingReportProblemCount = 0;
		
		int closedProcurementCount = 0;
		int closedCccplusCount = 0;
		int closedMed4Count = 0;
		int closedGeneralCount = 0;
		int closedMediaCount = 0;
		int closedElectedOfficialCount = 0;
		int closedReportProblemCount = 0;
		
//		Getting all pending requests count
		pendingRequests = requestorInternalRepository.findByStatus("Open");
		
		for(RequestorInternalEntity requestorInternalEntity : pendingRequests) {
			if(requestorInternalEntity.getFormName().getFormID()==1) {
				pendingProcurementCount++;
			}
			else if(requestorInternalEntity.getFormName().getFormID()==2) {
				pendingCccplusCount++;
			}
			else if(requestorInternalEntity.getFormName().getFormID()==3) {
				pendingMed4Count++;
			}
			else if(requestorInternalEntity.getFormName().getFormID()==4) {
				pendingGeneralCount++;
			}
			else if(requestorInternalEntity.getFormName().getFormID()==5) {
				pendingMediaCount++;
			}
			else if(requestorInternalEntity.getFormName().getFormID()==6) {
				pendingElectedOfficialCount++;
			}
			else if(requestorInternalEntity.getFormName().getFormID()==7) {
				pendingReportProblemCount++;
			}
		}
		procurementRequestsCount.setPendingRequests(pendingProcurementCount);
		cccplusRequestsCount.setPendingRequests(pendingCccplusCount);
		med4RequestsCount.setPendingRequests(pendingMed4Count);
		generalRequestsCount.setPendingRequests(pendingGeneralCount);
		mediaRequestsCount.setPendingRequests(pendingMediaCount);
		electedOfficialRequestsCount.setPendingRequests(pendingElectedOfficialCount);
		reportProblemRequestsCount.setPendingRequests(pendingReportProblemCount);
		
//		Getting all closed requests count
		closedRequests = requestorInternalRepository.findByStatus("Closed");
		
		for(RequestorInternalEntity requestorInternalEntity : closedRequests) {
			if(requestorInternalEntity.getFormName().getFormID()==1) {
				closedProcurementCount++;
			}
			else if(requestorInternalEntity.getFormName().getFormID()==2) {
				closedCccplusCount++;
			}
			else if(requestorInternalEntity.getFormName().getFormID()==3) {
				closedMed4Count++;
			}
			else if(requestorInternalEntity.getFormName().getFormID()==4) {
				closedGeneralCount++;
			}
			else if(requestorInternalEntity.getFormName().getFormID()==5) {
				closedMediaCount++;
			}
			else if(requestorInternalEntity.getFormName().getFormID()==6) {
				closedElectedOfficialCount++;
			}
			else if(requestorInternalEntity.getFormName().getFormID()==7) {
				closedReportProblemCount++;
			}
		}
		procurementRequestsCount.setClosedRequests(closedProcurementCount);
		cccplusRequestsCount.setClosedRequests(closedCccplusCount);
		med4RequestsCount.setClosedRequests(closedMed4Count);
		generalRequestsCount.setClosedRequests(closedGeneralCount);
		mediaRequestsCount.setClosedRequests(closedMediaCount);
		electedOfficialRequestsCount.setClosedRequests(closedElectedOfficialCount);
		reportProblemRequestsCount.setClosedRequests(closedReportProblemCount);
				
		requestsCount.setProcurementRequests(procurementRequestsCount);
		requestsCount.setCccplusRequests(cccplusRequestsCount);
		requestsCount.setMed4Requests(med4RequestsCount);
		requestsCount.setGeneralRequests(generalRequestsCount);
		requestsCount.setMediaRequests(mediaRequestsCount);
		requestsCount.setElectedOfficialRequests(electedOfficialRequestsCount);
		requestsCount.setReportProblemRequests(reportProblemRequestsCount);
		
		return requestsCount;
	}
	
	@Transactional
	public List<TableResponseDto> getPendingRequestsTable(Long formID) throws ResourceNotFoundException {

		logger.info("Inside getPendingRequestsTable service.");

		List<RequestorInternalEntity> pendingRequestorEntities = new ArrayList<RequestorInternalEntity>();

		List<TableResponseDto> pendingRequests = new ArrayList<TableResponseDto>();

//		Getting all pending requestor entities for the given form type
		FormNameEntity formNameEntity = formNameRepository.findById(formID)
				.orElseThrow(() -> new ResourceNotFoundException("Form not found."));
		pendingRequestorEntities = requestorInternalRepository
				.findByFormNameAndStatusOrderByCreatedTimeAsc(formNameEntity, "Open");

		if (formID == 6) {
			pendingRequestorEntities.forEach(pendingRequestorEntity -> {

				TableResponseDto pendingRequestorDto = new TableResponseDto();
				BeanUtils.copyProperties(pendingRequestorEntity, pendingRequestorDto);
				pendingRequestorDto.setMemberFirstName(
						pendingRequestorEntity.getElectedOfficialInternalEntity().getConstFirstname());
				pendingRequestorDto.setMemberLastName(
						pendingRequestorEntity.getElectedOfficialInternalEntity().getConstLastname());
				pendingRequestorDto
						.setMedicaidID(pendingRequestorEntity.getElectedOfficialInternalEntity().getConstMedID());
				pendingRequestorDto.setSSN(pendingRequestorEntity.getElectedOfficialInternalEntity().getConstSSN());
				pendingRequestorDto.setFormID(pendingRequestorEntity.getFormName().getFormID());
				pendingRequests.add(pendingRequestorDto);
			});
		} else if (formID == 7) {
			pendingRequestorEntities.forEach(pendingRequestorEntity -> {

				TableResponseDto pendingRequestorDto = new TableResponseDto();
				BeanUtils.copyProperties(pendingRequestorEntity, pendingRequestorDto);
				pendingRequestorDto
						.setMemberFirstName(pendingRequestorEntity.getReportProblemInternalEntity().getMemFirstname());
				pendingRequestorDto
						.setMemberLastName(pendingRequestorEntity.getReportProblemInternalEntity().getMemLastname());
				pendingRequestorDto
						.setMedicaidID(pendingRequestorEntity.getReportProblemInternalEntity().getMemMedID());
				pendingRequestorDto.setSSN(pendingRequestorEntity.getReportProblemInternalEntity().getMemSSN());
				pendingRequestorDto.setFormID(pendingRequestorEntity.getFormName().getFormID());
				pendingRequests.add(pendingRequestorDto);
			});
		} else {
			pendingRequestorEntities.forEach(pendingRequestorEntity -> {

				TableResponseDto pendingRequestorDto = new TableResponseDto();
				BeanUtils.copyProperties(pendingRequestorEntity, pendingRequestorDto);
				pendingRequestorDto.setFormID(pendingRequestorEntity.getFormName().getFormID());
				pendingRequests.add(pendingRequestorDto);
			});
		}
		return pendingRequests;
	}

	@Transactional
	public TableResponseCountDto getClosedRequestsTable(Long formID, int startNumber) throws ResourceNotFoundException {

		logger.info("Inside getClosedRequestsTable service.");

		List<RequestorInternalEntity> closedRequestorEntities = new ArrayList<RequestorInternalEntity>();

		TableResponseCountDto closedRequests = new TableResponseCountDto();
		List<TableResponseDto> closedRequestsDto = new ArrayList<TableResponseDto>();

		int entityCount = 0;
		int maxCount = 0;

//		Getting all closed requestor entities for the given form type
		FormNameEntity formNameEntity = formNameRepository.findById(formID)
				.orElseThrow(() -> new ResourceNotFoundException("Form not found."));
		closedRequestorEntities = requestorInternalRepository
				.findByFormNameAndStatusOrderByCreatedTimeDesc(formNameEntity, "Closed");

		entityCount = closedRequestorEntities.size();

		if (entityCount < 20) {
			maxCount = entityCount;
		} else if ((entityCount > 20) && (entityCount - startNumber < 20)) {
			maxCount = entityCount;
		} else {
			maxCount = startNumber + 20;
		}

		if (formID == 6) {
			for (int i = startNumber; i < maxCount; i++) {

				TableResponseDto closedRequestorDto = new TableResponseDto();
				BeanUtils.copyProperties(closedRequestorEntities.get(i), closedRequestorDto);
				closedRequestorDto.setMemberFirstName(
						closedRequestorEntities.get(i).getElectedOfficialInternalEntity().getConstFirstname());
				closedRequestorDto.setMemberLastName(
						closedRequestorEntities.get(i).getElectedOfficialInternalEntity().getConstLastname());
				closedRequestorDto.setMedicaidID(
						closedRequestorEntities.get(i).getElectedOfficialInternalEntity().getConstMedID());
				closedRequestorDto
						.setSSN(closedRequestorEntities.get(i).getElectedOfficialInternalEntity().getConstSSN());
				closedRequestorDto.setFormID(closedRequestorEntities.get(i).getFormName().getFormID());
				closedRequestsDto.add(closedRequestorDto);
			}
		} else if (formID == 7) {
			for (int i = startNumber; i < maxCount; i++) {

				TableResponseDto closedRequestorDto = new TableResponseDto();
				BeanUtils.copyProperties(closedRequestorEntities.get(i), closedRequestorDto);
				closedRequestorDto.setMemberFirstName(
						closedRequestorEntities.get(i).getReportProblemInternalEntity().getMemFirstname());
				closedRequestorDto.setMemberLastName(
						closedRequestorEntities.get(i).getReportProblemInternalEntity().getMemLastname());
				closedRequestorDto
						.setMedicaidID(closedRequestorEntities.get(i).getReportProblemInternalEntity().getMemMedID());
				closedRequestorDto.setSSN(closedRequestorEntities.get(i).getReportProblemInternalEntity().getMemSSN());
				closedRequestorDto.setFormID(closedRequestorEntities.get(i).getFormName().getFormID());
				closedRequestsDto.add(closedRequestorDto);
			}
		} else {
			for (int i = startNumber; i < maxCount; i++) {

				TableResponseDto closedRequestorDto = new TableResponseDto();
				BeanUtils.copyProperties(closedRequestorEntities.get(i), closedRequestorDto);
				closedRequestorDto.setFormID(closedRequestorEntities.get(i).getFormName().getFormID());
				closedRequestsDto.add(closedRequestorDto);
			}
		}
		closedRequests.setRequests(closedRequestsDto);
		closedRequests.setCount(maxCount);
		return closedRequests;
	}

	@Transactional
	public TableResponseCountDto getAllPendingRequestsTable(int startNumber) throws ResourceNotFoundException {

		logger.info("Inside getAllPendingRequestsTable service.");

		List<RequestorInternalEntity> pendingRequestorEntities = new ArrayList<RequestorInternalEntity>();

		TableResponseCountDto allPendingRequests = new TableResponseCountDto();
		List<TableResponseDto> pendingRequestsDto = new ArrayList<TableResponseDto>();

		int entityCount = 0;
		int maxCount = 0;

//		Getting all pending requestor entities
		pendingRequestorEntities = requestorInternalRepository.findByStatusOrderByCreatedTimeAsc("Open");

		entityCount = pendingRequestorEntities.size();

		if (entityCount < 20) {
			maxCount = entityCount;
		} else if ((entityCount > 20) && (entityCount - startNumber < 20)) {
			maxCount = entityCount;
		} else {
			maxCount = startNumber + 20;
		}

		for (int i = startNumber; i < maxCount; i++) {

			TableResponseDto pendingRequestorDto = new TableResponseDto();
			BeanUtils.copyProperties(pendingRequestorEntities.get(i), pendingRequestorDto);
			if (pendingRequestorEntities.get(i).getFormName().getFormID() == 6) {
				pendingRequestorDto.setMemberFirstName(
						pendingRequestorEntities.get(i).getElectedOfficialInternalEntity().getConstFirstname());
				pendingRequestorDto.setMemberLastName(
						pendingRequestorEntities.get(i).getElectedOfficialInternalEntity().getConstLastname());
				pendingRequestorDto.setMedicaidID(
						pendingRequestorEntities.get(i).getElectedOfficialInternalEntity().getConstMedID());
				pendingRequestorDto.setSSN(
						pendingRequestorEntities.get(i).getElectedOfficialInternalEntity().getConstSSN());
			} else if (pendingRequestorEntities.get(i).getFormName().getFormID() == 7) {
				pendingRequestorDto.setMemberFirstName(
						pendingRequestorEntities.get(i).getReportProblemInternalEntity().getMemFirstname());
				pendingRequestorDto.setMemberLastName(
						pendingRequestorEntities.get(i).getReportProblemInternalEntity().getMemLastname());
				pendingRequestorDto.setMedicaidID(
						pendingRequestorEntities.get(i).getReportProblemInternalEntity().getMemMedID());
				pendingRequestorDto.setSSN(
						pendingRequestorEntities.get(i).getReportProblemInternalEntity().getMemSSN());
			}
			pendingRequestorDto.setFormID(pendingRequestorEntities.get(i).getFormName().getFormID());
			pendingRequestsDto.add(pendingRequestorDto);
		}
		allPendingRequests.setRequests(pendingRequestsDto);
		allPendingRequests.setCount(maxCount);
		return allPendingRequests;
	}

	@Transactional
	public TableResponseCountDto getAllClosedRequestsTable(int startNumber) throws ResourceNotFoundException {

		logger.info("Inside getAllClosedRequestsTable service.");

		List<RequestorInternalEntity> closedRequestorEntities = new ArrayList<RequestorInternalEntity>();

		TableResponseCountDto allClosedRequests = new TableResponseCountDto();
		List<TableResponseDto> closedRequestsDto = new ArrayList<TableResponseDto>();

		int entityCount = 0;
		int maxCount = 0;

//		Getting all closed requestor entities
		closedRequestorEntities = requestorInternalRepository.findByStatusOrderByCreatedTimeDesc("Closed");

		entityCount = closedRequestorEntities.size();

		if (entityCount < 20) {
			maxCount = entityCount;
		} else if ((entityCount > 20) && (entityCount - startNumber < 20)) {
			maxCount = entityCount;
		} else {
			maxCount = startNumber + 20;
		}

		for (int i = startNumber; i < maxCount; i++) {

			TableResponseDto closedRequestorDto = new TableResponseDto();
			BeanUtils.copyProperties(closedRequestorEntities.get(i), closedRequestorDto);
			if (closedRequestorEntities.get(i).getFormName().getFormID() == 6) {
				closedRequestorDto.setMemberFirstName(
						closedRequestorEntities.get(i).getElectedOfficialInternalEntity().getConstFirstname());
				closedRequestorDto.setMemberLastName(
						closedRequestorEntities.get(i).getElectedOfficialInternalEntity().getConstLastname());
				closedRequestorDto.setMedicaidID(
						closedRequestorEntities.get(i).getElectedOfficialInternalEntity().getConstMedID());
				closedRequestorDto.setSSN(
						closedRequestorEntities.get(i).getElectedOfficialInternalEntity().getConstSSN());
			} else if (closedRequestorEntities.get(i).getFormName().getFormID() == 7) {
				closedRequestorDto.setMemberFirstName(
						closedRequestorEntities.get(i).getReportProblemInternalEntity().getMemFirstname());
				closedRequestorDto.setMemberLastName(
						closedRequestorEntities.get(i).getReportProblemInternalEntity().getMemLastname());
				closedRequestorDto.setMedicaidID(
						closedRequestorEntities.get(i).getReportProblemInternalEntity().getMemMedID());
				closedRequestorDto.setSSN(
						closedRequestorEntities.get(i).getReportProblemInternalEntity().getMemSSN());
			}
			closedRequestorDto.setFormID(closedRequestorEntities.get(i).getFormName().getFormID());
			closedRequestsDto.add(closedRequestorDto);
		}
		allClosedRequests.setRequests(closedRequestsDto);
		allClosedRequests.setCount(maxCount);
		return allClosedRequests;
	}

	@Transactional
	public RequestResponseDto getGeneralRequest(Long id, Long formID) throws ResourceNotFoundException {

		logger.info("Inside getGeneralRequest service.");

		RequestResponseDto response = new RequestResponseDto();

		RequestorResponseDto originalResponse = new RequestorResponseDto();
		GeneralResponseDto originalGeneralResponse = new GeneralResponseDto();

		RequestorResponseDto internalResponse = new RequestorResponseDto();
		GeneralResponseDto internalGeneralResponse = new GeneralResponseDto();
		List<CommentResponseDto> internalCommentResponses = new ArrayList<CommentResponseDto>();
		ReasonCodeResponseDto reason = new ReasonCodeResponseDto();

		GeneralRequestEntity generalRequestEntity = new GeneralRequestEntity();
		RequestorEntity requestorEntity = new RequestorEntity();

		GeneralRequestInternalEntity generalRequestInternalEntity = new GeneralRequestInternalEntity();
		RequestorInternalEntity requestorInternalEntity = new RequestorInternalEntity();

//		Getting both the original and internal general request entities for the given ID
		requestorEntity = requestorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		generalRequestEntity = generalRequestRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

		requestorInternalEntity = requestorInternalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		generalRequestInternalEntity = generalRequestInternalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

//		Setting both the original and internal DTOs
		BeanUtils.copyProperties(requestorEntity, originalResponse);
		BeanUtils.copyProperties(generalRequestEntity, originalGeneralResponse);
		originalResponse.setFormID(requestorEntity.getFormName().getFormID());
		originalResponse.setGeneral(originalGeneralResponse);

		BeanUtils.copyProperties(requestorInternalEntity, internalResponse);
		BeanUtils.copyProperties(generalRequestInternalEntity, internalGeneralResponse);
		if (requestorInternalEntity.getReason() != null) {
			BeanUtils.copyProperties(requestorInternalEntity.getReason(), reason);
			reason.setFormID(requestorInternalEntity.getFormName().getFormID());
			internalResponse.setReason(reason);
		}
		if (!requestorInternalEntity.getCommentInternalEntities().isEmpty()) {
			requestorInternalEntity.getCommentInternalEntities().sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
			for (CommentInternalEntity commentEntity : requestorInternalEntity.getCommentInternalEntities()) {
				CommentResponseDto commentResponse = new CommentResponseDto();
				BeanUtils.copyProperties(commentEntity, commentResponse);
				if (commentEntity.getReasonCodeEntity() != null) {
					ReasonCodeResponseDto reasonCodeResponse = new ReasonCodeResponseDto();
					BeanUtils.copyProperties(commentEntity.getReasonCodeEntity(), reasonCodeResponse);
					reasonCodeResponse.setFormID(requestorInternalEntity.getFormName().getFormID());
					commentResponse.setReason(reasonCodeResponse);
				}
				internalCommentResponses.add(commentResponse);
			}
			internalResponse.setComments(internalCommentResponses);
		}
		internalResponse.setFormID(requestorInternalEntity.getFormName().getFormID());
		internalResponse.setGeneral(internalGeneralResponse);

		response.setOriginalRequest(originalResponse);
		response.setInternalRequest(internalResponse);
		return response;
	}

	@Transactional
	public RequestResponseDto getMediaRequest(Long id, Long formID) throws ResourceNotFoundException {

		logger.info("Inside getMediaRequest service.");

		RequestResponseDto response = new RequestResponseDto();

		RequestorResponseDto originalResponse = new RequestorResponseDto();
		MediaResponseDto originalMediaResponse = new MediaResponseDto();

		RequestorResponseDto internalResponse = new RequestorResponseDto();
		MediaResponseDto internalMediaResponse = new MediaResponseDto();
		List<CommentResponseDto> internalCommentResponses = new ArrayList<CommentResponseDto>();
		ReasonCodeResponseDto reason = new ReasonCodeResponseDto();

		MediaRequestEntity mediaRequestEntity = new MediaRequestEntity();
		RequestorEntity requestorEntity = new RequestorEntity();

		MediaRequestInternalEntity mediaRequestInternalEntity = new MediaRequestInternalEntity();
		RequestorInternalEntity requestorInternalEntity = new RequestorInternalEntity();

//		Getting both the original and internal media request entities for the given ID
		requestorEntity = requestorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		mediaRequestEntity = mediaRequestRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

		requestorInternalEntity = requestorInternalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		mediaRequestInternalEntity = mediaRequestInternalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

//		Setting both the original and internal DTOs
		BeanUtils.copyProperties(requestorEntity, originalResponse);
		BeanUtils.copyProperties(mediaRequestEntity, originalMediaResponse);
		originalResponse.setFormID(requestorEntity.getFormName().getFormID());
		originalResponse.setMedia(originalMediaResponse);

		BeanUtils.copyProperties(requestorInternalEntity, internalResponse);
		BeanUtils.copyProperties(mediaRequestInternalEntity, internalMediaResponse);
		if (requestorInternalEntity.getReason() != null) {
			BeanUtils.copyProperties(requestorInternalEntity.getReason(), reason);
			reason.setFormID(requestorInternalEntity.getFormName().getFormID());
			internalResponse.setReason(reason);
		}
		if (!requestorInternalEntity.getCommentInternalEntities().isEmpty()) {
			requestorInternalEntity.getCommentInternalEntities().sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
			for (CommentInternalEntity commentEntity : requestorInternalEntity.getCommentInternalEntities()) {
				CommentResponseDto commentResponse = new CommentResponseDto();
				BeanUtils.copyProperties(commentEntity, commentResponse);
				if (commentEntity.getReasonCodeEntity() != null) {
					ReasonCodeResponseDto reasonCodeResponse = new ReasonCodeResponseDto();
					BeanUtils.copyProperties(commentEntity.getReasonCodeEntity(), reasonCodeResponse);
					reasonCodeResponse.setFormID(requestorInternalEntity.getFormName().getFormID());
					commentResponse.setReason(reasonCodeResponse);
				}
				internalCommentResponses.add(commentResponse);
			}
			internalResponse.setComments(internalCommentResponses);
		}
		internalResponse.setFormID(requestorInternalEntity.getFormName().getFormID());
		internalResponse.setMedia(internalMediaResponse);

		response.setOriginalRequest(originalResponse);
		response.setInternalRequest(internalResponse);
		return response;
	}

	@Transactional
	public RequestResponseDto getElectedOfficialRequest(Long id, Long formID) throws ResourceNotFoundException {

		logger.info("Inside getElectedOfficialRequest service.");

		RequestResponseDto response = new RequestResponseDto();

		RequestorResponseDto originalResponse = new RequestorResponseDto();
		ElectedOfficialResponseDto originalElectedOfficialResponse = new ElectedOfficialResponseDto();

		RequestorResponseDto internalResponse = new RequestorResponseDto();
		ElectedOfficialResponseDto internalElectedOfficialResponse = new ElectedOfficialResponseDto();
		List<CommentResponseDto> internalCommentResponses = new ArrayList<CommentResponseDto>();
		ReasonCodeResponseDto reason = new ReasonCodeResponseDto();

		ElectedOfficialRequestEntity electedOfficialRequestEntity = new ElectedOfficialRequestEntity();
		RequestorEntity requestorEntity = new RequestorEntity();

		ElectedOfficialRequestInternalEntity electedOfficialRequestInternalEntity = new ElectedOfficialRequestInternalEntity();
		RequestorInternalEntity requestorInternalEntity = new RequestorInternalEntity();

//		Getting both the original and internal elected official request entities for the given ID
		requestorEntity = requestorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		electedOfficialRequestEntity = electedOfficialRequestRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

		requestorInternalEntity = requestorInternalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		electedOfficialRequestInternalEntity = electedOfficialRequestInternalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

//		Setting both the original and internal DTOs
		BeanUtils.copyProperties(requestorEntity, originalResponse);
		BeanUtils.copyProperties(electedOfficialRequestEntity, originalElectedOfficialResponse);
		originalResponse.setFormID(requestorEntity.getFormName().getFormID());
		originalResponse.setElectedOfficial(originalElectedOfficialResponse);

		BeanUtils.copyProperties(requestorInternalEntity, internalResponse);
		BeanUtils.copyProperties(electedOfficialRequestInternalEntity, internalElectedOfficialResponse);
		if (requestorInternalEntity.getReason() != null) {
			BeanUtils.copyProperties(requestorInternalEntity.getReason(), reason);
			reason.setFormID(requestorInternalEntity.getFormName().getFormID());
			internalResponse.setReason(reason);
		}
		if (!requestorInternalEntity.getCommentInternalEntities().isEmpty()) {
			requestorInternalEntity.getCommentInternalEntities().sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
			for (CommentInternalEntity commentEntity : requestorInternalEntity.getCommentInternalEntities()) {
				CommentResponseDto commentResponse = new CommentResponseDto();
				BeanUtils.copyProperties(commentEntity, commentResponse);
				if (commentEntity.getReasonCodeEntity() != null) {
					ReasonCodeResponseDto reasonCodeResponse = new ReasonCodeResponseDto();
					BeanUtils.copyProperties(commentEntity.getReasonCodeEntity(), reasonCodeResponse);
					reasonCodeResponse.setFormID(requestorInternalEntity.getFormName().getFormID());
					commentResponse.setReason(reasonCodeResponse);
				}
				internalCommentResponses.add(commentResponse);
			}
			internalResponse.setComments(internalCommentResponses);
		}
		internalResponse.setFormID(requestorInternalEntity.getFormName().getFormID());
		internalResponse.setElectedOfficial(internalElectedOfficialResponse);

		response.setOriginalRequest(originalResponse);
		response.setInternalRequest(internalResponse);
		return response;
	}

	@Transactional
	public RequestResponseDto getReportProblemRequest(Long id, Long formID) throws ResourceNotFoundException {

		logger.info("Inside getReportProblemRequest service.");

		RequestResponseDto response = new RequestResponseDto();

		RequestorResponseDto originalResponse = new RequestorResponseDto();
		ReportProblemResponseDto originalReportProblemResponse = new ReportProblemResponseDto();

		RequestorResponseDto internalResponse = new RequestorResponseDto();
		ReportProblemResponseDto internalReportProblemResponse = new ReportProblemResponseDto();
		List<CommentResponseDto> internalCommentResponses = new ArrayList<CommentResponseDto>();
		ReasonCodeResponseDto reason = new ReasonCodeResponseDto();

		ReportProblemEntity reportProblemEntity = new ReportProblemEntity();
		RequestorEntity requestorEntity = new RequestorEntity();

		ReportProblemInternalEntity reportProblemInternalEntity = new ReportProblemInternalEntity();
		RequestorInternalEntity requestorInternalEntity = new RequestorInternalEntity();

//		Getting both the original and internal report problem entities for the given ID
		requestorEntity = requestorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		reportProblemEntity = reportProblemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

		requestorInternalEntity = requestorInternalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		reportProblemInternalEntity = reportProblemInternalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

//		Setting both the original and internal DTOs
		BeanUtils.copyProperties(requestorEntity, originalResponse);
		BeanUtils.copyProperties(reportProblemEntity, originalReportProblemResponse);
		originalResponse.setFormID(requestorEntity.getFormName().getFormID());
		originalResponse.setReportProblem(originalReportProblemResponse);

		BeanUtils.copyProperties(requestorInternalEntity, internalResponse);
		BeanUtils.copyProperties(reportProblemInternalEntity, internalReportProblemResponse);
		if (requestorInternalEntity.getReason() != null) {
			BeanUtils.copyProperties(requestorInternalEntity.getReason(), reason);
			reason.setFormID(requestorInternalEntity.getFormName().getFormID());
			internalResponse.setReason(reason);
		}
		if (!requestorInternalEntity.getCommentInternalEntities().isEmpty()) {
			requestorInternalEntity.getCommentInternalEntities().sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
			for (CommentInternalEntity commentEntity : requestorInternalEntity.getCommentInternalEntities()) {
				CommentResponseDto commentResponse = new CommentResponseDto();
				BeanUtils.copyProperties(commentEntity, commentResponse);
				if (commentEntity.getReasonCodeEntity() != null) {
					ReasonCodeResponseDto reasonCodeResponse = new ReasonCodeResponseDto();
					BeanUtils.copyProperties(commentEntity.getReasonCodeEntity(), reasonCodeResponse);
					reasonCodeResponse.setFormID(requestorInternalEntity.getFormName().getFormID());
					commentResponse.setReason(reasonCodeResponse);
				}
				internalCommentResponses.add(commentResponse);
			}
			internalResponse.setComments(internalCommentResponses);
		}
		internalResponse.setFormID(requestorInternalEntity.getFormName().getFormID());
		internalResponse.setReportProblem(internalReportProblemResponse);

		response.setOriginalRequest(originalResponse);
		response.setInternalRequest(internalResponse);
		return response;
	}

	@Transactional
	public RequestResponseDto updateGeneralRequest(RequestorDto request) throws ResourceNotFoundException, MailNotSentException {

		logger.info("Inside updateGeneraRequest service.");

		RequestResponseDto response = new RequestResponseDto();

		RequestorResponseDto originalResponse = new RequestorResponseDto();
		GeneralResponseDto originalGeneralResponse = new GeneralResponseDto();

		RequestorResponseDto internalResponse = new RequestorResponseDto();
		GeneralResponseDto internalGeneralResponse = new GeneralResponseDto();
		List<CommentResponseDto> commentResponses = new ArrayList<CommentResponseDto>();
		ReasonCodeResponseDto reasonCodeResponse = new ReasonCodeResponseDto();

		GeneralRequestEntity generalRequestEntity = new GeneralRequestEntity();
		RequestorEntity requestorEntity = new RequestorEntity();

		GeneralRequestInternalEntity generalRequestInternalEntity = new GeneralRequestInternalEntity();
		RequestorInternalEntity requestorInternalEntity = new RequestorInternalEntity();
		CommentInternalEntity commentInternalEntity = new CommentInternalEntity();
		ReasonCodeEntity reasonCodeEntity = new ReasonCodeEntity();

		List<CommentInternalEntity> comments = new ArrayList<CommentInternalEntity>();
		List<CommentInternalEntity> commentsRC = new ArrayList<CommentInternalEntity>();
		List<RequestorInternalEntity> requestors = new ArrayList<RequestorInternalEntity>();

//		Getting the original request and setting it to DTO
		requestorEntity = requestorRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		generalRequestEntity = generalRequestRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

		BeanUtils.copyProperties(requestorEntity, originalResponse);
		BeanUtils.copyProperties(generalRequestEntity, originalGeneralResponse);
		originalResponse.setFormID(requestorEntity.getFormName().getFormID());
		originalResponse.setGeneral(originalGeneralResponse);

//		Setting the internal requestor and request
		requestorInternalEntity = requestorInternalRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		generalRequestInternalEntity = generalRequestInternalRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

		BeanUtils.copyProperties(request.getGeneral(), generalRequestInternalEntity);
		BeanUtils.copyProperties(request, requestorInternalEntity);
		BeanUtils.copyProperties(request.getComment(), commentInternalEntity);

//		Setting the comments to the requestor
		commentInternalEntity.setRequestorInternalEntity(requestorInternalEntity);

		if (requestorInternalEntity.getCommentInternalEntities().size() != 0) {
			requestorInternalEntity.getCommentInternalEntities().add(commentInternalEntity);
		} else {
			comments.add(commentInternalEntity);
			requestorInternalEntity.setCommentInternalEntities(comments);
		}

//		Setting the reason code(if any) for the comment
		if (request.getComment().getReasonID() != null) {
			reasonCodeEntity = reasonCodeRepository.findById(request.getComment().getReasonID())
					.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));

			commentInternalEntity.setReasonCodeEntity(reasonCodeEntity);

			if (reasonCodeEntity.getComments().size() != 0) {
				reasonCodeEntity.getComments().add(commentInternalEntity);
			} else {
				commentsRC.add(commentInternalEntity);
				reasonCodeEntity.setComments(commentsRC);
			}
		} else {
			if (request.getFormID() == 1) {
				reasonCodeEntity = reasonCodeRepository.findById((long) 8)
						.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));
			} else if (request.getFormID() == 2) {
				reasonCodeEntity = reasonCodeRepository.findById((long) 9)
						.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));
			} else if (request.getFormID() == 3) {
				reasonCodeEntity = reasonCodeRepository.findById((long) 10)
						.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));
			} else if (request.getFormID() == 4) {
				reasonCodeEntity = reasonCodeRepository.findById((long) 11)
						.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));
			}
			commentInternalEntity.setReasonCodeEntity(reasonCodeEntity);

			if (reasonCodeEntity.getComments().size() != 0) {
				reasonCodeEntity.getComments().add(commentInternalEntity);
			} else {
				commentsRC.add(commentInternalEntity);
				reasonCodeEntity.setComments(commentsRC);
			}
		}

//		Setting the reason code (if any)
		if (request.getReasonID() != null) {
			reasonCodeEntity = reasonCodeRepository.findById(request.getReasonID())
					.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));

			requestorInternalEntity.setReason(reasonCodeEntity);

			if (reasonCodeEntity.getRequestors().size() != 0) {
				reasonCodeEntity.getRequestors().add(requestorInternalEntity);
			} else {
				requestors.add(requestorInternalEntity);
				reasonCodeEntity.setRequestors(requestors);
			}
		} else {
			if (request.getFormID() == 1) {
				reasonCodeEntity = reasonCodeRepository.findById((long) 8)
						.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));
			} else if (request.getFormID() == 2) {
				reasonCodeEntity = reasonCodeRepository.findById((long) 9)
						.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));
			} else if (request.getFormID() == 3) {
				reasonCodeEntity = reasonCodeRepository.findById((long) 10)
						.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));
			} else if (request.getFormID() == 4) {
				reasonCodeEntity = reasonCodeRepository.findById((long) 11)
						.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));
			}
			requestorInternalEntity.setReason(reasonCodeEntity);

			if (reasonCodeEntity.getRequestors().size() != 0) {
				reasonCodeEntity.getRequestors().add(requestorInternalEntity);
			} else {
				requestors.add(requestorInternalEntity);
				reasonCodeEntity.setRequestors(requestors);
			}
		}

		requestorInternalEntity.setUpdatedTime(new Timestamp(new Date().getTime()));

		requestorInternalEntity.setGeneralRequestInternalEntity(generalRequestInternalEntity);
		generalRequestInternalEntity.setRequestorInternalEntity(requestorInternalEntity);

//		Saving the request
		generalRequestInternalEntity = generalRequestInternalRepository.saveAndFlush(generalRequestInternalEntity);

//		Setting the DTOs for the response
		BeanUtils.copyProperties(generalRequestInternalEntity, internalGeneralResponse);
		BeanUtils.copyProperties(generalRequestInternalEntity.getRequestorInternalEntity(), internalResponse);
		comments = generalRequestInternalEntity.getRequestorInternalEntity().getCommentInternalEntities();
		comments.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
		for (CommentInternalEntity comment : comments) {
			CommentResponseDto commentResponse = new CommentResponseDto();
			BeanUtils.copyProperties(comment, commentResponse);
			if (comment.getReasonCodeEntity() != null) {
				ReasonCodeResponseDto reasonCode = new ReasonCodeResponseDto();
				BeanUtils.copyProperties(comment.getReasonCodeEntity(), reasonCode);
				reasonCode.setFormID(comment.getReasonCodeEntity().getFormNameEntity().getFormID());
				commentResponse.setReason(reasonCode);
			}
			commentResponses.add(commentResponse);
		}
		if (generalRequestInternalEntity.getRequestorInternalEntity().getReason() != null) {
			BeanUtils.copyProperties(generalRequestInternalEntity.getRequestorInternalEntity().getReason(),
					reasonCodeResponse);
			reasonCodeResponse.setFormID(generalRequestInternalEntity.getRequestorInternalEntity().getReason()
					.getFormNameEntity().getFormID());
			internalResponse.setReason(reasonCodeResponse);
		}

		internalResponse.setFormID(generalRequestInternalEntity.getRequestorInternalEntity().getFormName().getFormID());
		internalResponse.setGeneral(internalGeneralResponse);
		internalResponse.setComments(commentResponses);

		response.setOriginalRequest(originalResponse);
		response.setInternalRequest(internalResponse);
		
		if((generalRequestInternalEntity.getRequestorInternalEntity().getStatus()).equalsIgnoreCase("Closed")) {
//			Sending email notification
			MessageTemplateEntity messageTemplateEntity = messageTemplateRepository.findByStatus("UserClosed");
			String subject = messageTemplateEntity.getSubject();
			String messageTemplate = messageTemplateEntity.getMessageTemplate();

			boolean endUserMailSent = mailService.sendEmailNotification(internalResponse.getEmail(), subject, messageTemplate, internalResponse.getTicketID());
			if(!endUserMailSent) {
				throw new MailNotSentException("Problem in sending Email to User.");
			}
		}
		
		if(((request.getPreviousStatus()).equalsIgnoreCase("Closed")) && ((request.getStatus()).equalsIgnoreCase("Open"))) {
//			Sending email notification
			MessageTemplateEntity messageTemplateEntity = messageTemplateRepository.findByStatus("UserReopen");
			String subject = messageTemplateEntity.getSubject();
			String messageTemplate = messageTemplateEntity.getMessageTemplate();

			boolean endUserMailSent = mailService.sendEmailNotification(internalResponse.getEmail(), subject, messageTemplate, internalResponse.getTicketID());
			if(!endUserMailSent) {
				throw new MailNotSentException("Problem in sending Email to User.");
			}
		}
		return response;
	}

	@Transactional
	public RequestResponseDto updateMediaRequest(RequestorDto request) throws ResourceNotFoundException, MailNotSentException {

		logger.info("Inside updateMediaRequest service.");

		RequestResponseDto response = new RequestResponseDto();

		RequestorResponseDto originalResponse = new RequestorResponseDto();
		MediaResponseDto originalMediaResponse = new MediaResponseDto();

		RequestorResponseDto internalResponse = new RequestorResponseDto();
		MediaResponseDto internalMediaResponse = new MediaResponseDto();
		List<CommentResponseDto> commentResponses = new ArrayList<CommentResponseDto>();
		ReasonCodeResponseDto reasonCodeResponse = new ReasonCodeResponseDto();

		MediaRequestEntity mediaRequestEntity = new MediaRequestEntity();
		RequestorEntity requestorEntity = new RequestorEntity();

		MediaRequestInternalEntity mediaRequestInternalEntity = new MediaRequestInternalEntity();
		RequestorInternalEntity requestorInternalEntity = new RequestorInternalEntity();
		CommentInternalEntity commentInternalEntity = new CommentInternalEntity();
		ReasonCodeEntity reasonCodeEntity = new ReasonCodeEntity();

		List<CommentInternalEntity> comments = new ArrayList<CommentInternalEntity>();
		List<CommentInternalEntity> commentsRC = new ArrayList<CommentInternalEntity>();
		List<RequestorInternalEntity> requestors = new ArrayList<RequestorInternalEntity>();

//		Getting the original request and setting it to DTO
		requestorEntity = requestorRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		mediaRequestEntity = mediaRequestRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

		BeanUtils.copyProperties(requestorEntity, originalResponse);
		BeanUtils.copyProperties(mediaRequestEntity, originalMediaResponse);
		originalResponse.setFormID(requestorEntity.getFormName().getFormID());
		originalResponse.setMedia(originalMediaResponse);

//		Setting the internal requestor and request
		requestorInternalEntity = requestorInternalRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		mediaRequestInternalEntity = mediaRequestInternalRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

		BeanUtils.copyProperties(request.getMedia(), mediaRequestInternalEntity);
		BeanUtils.copyProperties(request, requestorInternalEntity);
		BeanUtils.copyProperties(request.getComment(), commentInternalEntity);

//		Setting the comments to the requestor
		commentInternalEntity.setRequestorInternalEntity(requestorInternalEntity);

		if (requestorInternalEntity.getCommentInternalEntities().size() != 0) {
			requestorInternalEntity.getCommentInternalEntities().add(commentInternalEntity);
		} else {
			comments.add(commentInternalEntity);
			requestorInternalEntity.setCommentInternalEntities(comments);
		}

//		Setting the reason code(if any) for the comment
		if (request.getComment().getReasonID() != null) {
			reasonCodeEntity = reasonCodeRepository.findById(request.getComment().getReasonID())
					.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));

			commentInternalEntity.setReasonCodeEntity(reasonCodeEntity);

			if (reasonCodeEntity.getComments().size() != 0) {
				reasonCodeEntity.getComments().add(commentInternalEntity);
			} else {
				commentsRC.add(commentInternalEntity);
				reasonCodeEntity.setComments(commentsRC);
			}
		} else {
			reasonCodeEntity = reasonCodeRepository.findById((long) 12)
					.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));

			commentInternalEntity.setReasonCodeEntity(reasonCodeEntity);

			if (reasonCodeEntity.getComments().size() != 0) {
				reasonCodeEntity.getComments().add(commentInternalEntity);
			} else {
				commentsRC.add(commentInternalEntity);
				reasonCodeEntity.setComments(commentsRC);
			}
		}

//		Setting the reason code (if any)
		if (request.getReasonID() != null) {
			reasonCodeEntity = reasonCodeRepository.findById(request.getReasonID())
					.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));

			requestorInternalEntity.setReason(reasonCodeEntity);

			if (reasonCodeEntity.getRequestors().size() != 0) {
				reasonCodeEntity.getRequestors().add(requestorInternalEntity);
			} else {
				requestors.add(requestorInternalEntity);
				reasonCodeEntity.setRequestors(requestors);
			}
		} else {
			reasonCodeEntity = reasonCodeRepository.findById((long) 12)
					.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));

			requestorInternalEntity.setReason(reasonCodeEntity);

			if (reasonCodeEntity.getRequestors().size() != 0) {
				reasonCodeEntity.getRequestors().add(requestorInternalEntity);
			} else {
				requestors.add(requestorInternalEntity);
				reasonCodeEntity.setRequestors(requestors);
			}
		}

		requestorInternalEntity.setUpdatedTime(new Timestamp(new Date().getTime()));

		requestorInternalEntity.setMediaRequestInternalEntity(mediaRequestInternalEntity);
		mediaRequestInternalEntity.setRequestorInternalEntity(requestorInternalEntity);

//		Saving the request
		mediaRequestInternalEntity = mediaRequestInternalRepository.saveAndFlush(mediaRequestInternalEntity);

//		Setting the DTOs for the response
		BeanUtils.copyProperties(mediaRequestInternalEntity, internalMediaResponse);
		BeanUtils.copyProperties(mediaRequestInternalEntity.getRequestorInternalEntity(), internalResponse);
		comments = mediaRequestInternalEntity.getRequestorInternalEntity().getCommentInternalEntities();
		comments.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
		for (CommentInternalEntity comment : comments) {
			CommentResponseDto commentResponse = new CommentResponseDto();
			BeanUtils.copyProperties(comment, commentResponse);
			if (comment.getReasonCodeEntity() != null) {
				ReasonCodeResponseDto reasonCode = new ReasonCodeResponseDto();
				BeanUtils.copyProperties(comment.getReasonCodeEntity(), reasonCode);
				reasonCode.setFormID(comment.getReasonCodeEntity().getFormNameEntity().getFormID());
				commentResponse.setReason(reasonCode);
			}
			commentResponses.add(commentResponse);
		}
		if (mediaRequestInternalEntity.getRequestorInternalEntity().getReason() != null) {
			BeanUtils.copyProperties(mediaRequestInternalEntity.getRequestorInternalEntity().getReason(),
					reasonCodeResponse);
			reasonCodeResponse.setFormID(mediaRequestInternalEntity.getRequestorInternalEntity().getReason()
					.getFormNameEntity().getFormID());
			internalResponse.setReason(reasonCodeResponse);
		}

		internalResponse.setFormID(mediaRequestInternalEntity.getRequestorInternalEntity().getFormName().getFormID());
		internalResponse.setMedia(internalMediaResponse);
		internalResponse.setComments(commentResponses);

		response.setOriginalRequest(originalResponse);
		response.setInternalRequest(internalResponse);
		
		if((mediaRequestInternalEntity.getRequestorInternalEntity().getStatus()).equalsIgnoreCase("Closed")) {
//			Sending email notification
			MessageTemplateEntity messageTemplateEntity = messageTemplateRepository.findByStatus("UserClosed");
			String subject = messageTemplateEntity.getSubject();
			String messageTemplate = messageTemplateEntity.getMessageTemplate();

			boolean endUserMailSent = mailService.sendEmailNotification(internalResponse.getEmail(), subject, messageTemplate, internalResponse.getTicketID());
			if(!endUserMailSent) {
				throw new MailNotSentException("Problem in sending Email to User.");
			}
		}
		if(((request.getPreviousStatus()).equalsIgnoreCase("Closed")) && ((request.getStatus()).equalsIgnoreCase("Open"))) {
//			Sending email notification
			MessageTemplateEntity messageTemplateEntity = messageTemplateRepository.findByStatus("UserReopen");
			String subject = messageTemplateEntity.getSubject();
			String messageTemplate = messageTemplateEntity.getMessageTemplate();

			boolean endUserMailSent = mailService.sendEmailNotification(internalResponse.getEmail(), subject, messageTemplate, internalResponse.getTicketID());
			if(!endUserMailSent) {
				throw new MailNotSentException("Problem in sending Email to User.");
			}
		}
		return response;
	}

	@Transactional
	public RequestResponseDto updateElectedOfficialRequest(RequestorDto request) throws ResourceNotFoundException, MailNotSentException {

		logger.info("Inside updateElectedOfficialRequest service.");

		RequestResponseDto response = new RequestResponseDto();

		RequestorResponseDto originalResponse = new RequestorResponseDto();
		ElectedOfficialResponseDto originalElectedOfficialResponse = new ElectedOfficialResponseDto();

		RequestorResponseDto internalResponse = new RequestorResponseDto();
		ElectedOfficialResponseDto internalElectedOfficialResponse = new ElectedOfficialResponseDto();
		List<CommentResponseDto> commentResponses = new ArrayList<CommentResponseDto>();
		ReasonCodeResponseDto reasonCodeResponse = new ReasonCodeResponseDto();

		ElectedOfficialRequestEntity electedOfficialRequestEntity = new ElectedOfficialRequestEntity();
		RequestorEntity requestorEntity = new RequestorEntity();

		ElectedOfficialRequestInternalEntity electedOfficialRequestInternalEntity = new ElectedOfficialRequestInternalEntity();
		RequestorInternalEntity requestorInternalEntity = new RequestorInternalEntity();
		CommentInternalEntity commentInternalEntity = new CommentInternalEntity();
		ReasonCodeEntity reasonCodeEntity = new ReasonCodeEntity();

		List<CommentInternalEntity> comments = new ArrayList<CommentInternalEntity>();
		List<CommentInternalEntity> commentsRC = new ArrayList<CommentInternalEntity>();
		List<RequestorInternalEntity> requestors = new ArrayList<RequestorInternalEntity>();

//		Getting the original request and setting it to DTO
		requestorEntity = requestorRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		electedOfficialRequestEntity = electedOfficialRequestRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

		BeanUtils.copyProperties(requestorEntity, originalResponse);
		BeanUtils.copyProperties(electedOfficialRequestEntity, originalElectedOfficialResponse);
		originalResponse.setFormID(requestorEntity.getFormName().getFormID());
		originalResponse.setElectedOfficial(originalElectedOfficialResponse);

//		Setting the internal requestor and request
		requestorInternalEntity = requestorInternalRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		electedOfficialRequestInternalEntity = electedOfficialRequestInternalRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

		BeanUtils.copyProperties(request.getElectedOfficial(), electedOfficialRequestInternalEntity);
		BeanUtils.copyProperties(request, requestorInternalEntity);
		BeanUtils.copyProperties(request.getComment(), commentInternalEntity);

//		Setting the comments to the requestor
		commentInternalEntity.setRequestorInternalEntity(requestorInternalEntity);

		if (requestorInternalEntity.getCommentInternalEntities().size() != 0) {
			requestorInternalEntity.getCommentInternalEntities().add(commentInternalEntity);
		} else {
			comments.add(commentInternalEntity);
			requestorInternalEntity.setCommentInternalEntities(comments);
		}

//		Setting the reason code(if any) for the comment
		if (request.getComment().getReasonID() != null) {
			reasonCodeEntity = reasonCodeRepository.findById(request.getComment().getReasonID())
					.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));

			commentInternalEntity.setReasonCodeEntity(reasonCodeEntity);

			if (reasonCodeEntity.getComments().size() != 0) {
				reasonCodeEntity.getComments().add(commentInternalEntity);
			} else {
				commentsRC.add(commentInternalEntity);
				reasonCodeEntity.setComments(commentsRC);
			}
		} else {
			reasonCodeEntity = reasonCodeRepository.findById((long) 13)
					.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));

			commentInternalEntity.setReasonCodeEntity(reasonCodeEntity);

			if (reasonCodeEntity.getComments().size() != 0) {
				reasonCodeEntity.getComments().add(commentInternalEntity);
			} else {
				commentsRC.add(commentInternalEntity);
				reasonCodeEntity.setComments(commentsRC);
			}
		}

//		Setting the reason code (if any)
		if (request.getReasonID() != null) {
			reasonCodeEntity = reasonCodeRepository.findById(request.getReasonID())
					.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));

			requestorInternalEntity.setReason(reasonCodeEntity);

			if (reasonCodeEntity.getRequestors().size() != 0) {
				reasonCodeEntity.getRequestors().add(requestorInternalEntity);
			} else {
				requestors.add(requestorInternalEntity);
				reasonCodeEntity.setRequestors(requestors);
			}
		} else {
			reasonCodeEntity = reasonCodeRepository.findById((long) 13)
					.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));

			requestorInternalEntity.setReason(reasonCodeEntity);

			if (reasonCodeEntity.getRequestors().size() != 0) {
				reasonCodeEntity.getRequestors().add(requestorInternalEntity);
			} else {
				requestors.add(requestorInternalEntity);
				reasonCodeEntity.setRequestors(requestors);
			}
		}

		requestorInternalEntity.setUpdatedTime(new Timestamp(new Date().getTime()));

		requestorInternalEntity.setElectedOfficialInternalEntity(electedOfficialRequestInternalEntity);
		electedOfficialRequestInternalEntity.setRequestorInternalEntity(requestorInternalEntity);

//		Saving the request
		electedOfficialRequestInternalEntity = electedOfficialRequestInternalRepository
				.saveAndFlush(electedOfficialRequestInternalEntity);

//		Setting the DTOs for the response
		BeanUtils.copyProperties(electedOfficialRequestInternalEntity, internalElectedOfficialResponse);
		BeanUtils.copyProperties(electedOfficialRequestInternalEntity.getRequestorInternalEntity(), internalResponse);
		comments = electedOfficialRequestInternalEntity.getRequestorInternalEntity().getCommentInternalEntities();
		comments.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
		for (CommentInternalEntity comment : comments) {
			CommentResponseDto commentResponse = new CommentResponseDto();
			BeanUtils.copyProperties(comment, commentResponse);
			if (comment.getReasonCodeEntity() != null) {
				ReasonCodeResponseDto reasonCode = new ReasonCodeResponseDto();
				BeanUtils.copyProperties(comment.getReasonCodeEntity(), reasonCode);
				reasonCode.setFormID(comment.getReasonCodeEntity().getFormNameEntity().getFormID());
				commentResponse.setReason(reasonCode);
			}
			commentResponses.add(commentResponse);
		}
		if (electedOfficialRequestInternalEntity.getRequestorInternalEntity().getReason() != null) {
			BeanUtils.copyProperties(electedOfficialRequestInternalEntity.getRequestorInternalEntity().getReason(),
					reasonCodeResponse);
			reasonCodeResponse.setFormID(electedOfficialRequestInternalEntity.getRequestorInternalEntity().getReason()
					.getFormNameEntity().getFormID());
			internalResponse.setReason(reasonCodeResponse);
		}

		internalResponse
				.setFormID(electedOfficialRequestInternalEntity.getRequestorInternalEntity().getFormName().getFormID());
		internalResponse.setElectedOfficial(internalElectedOfficialResponse);
		internalResponse.setComments(commentResponses);

		response.setOriginalRequest(originalResponse);
		response.setInternalRequest(internalResponse);
		
		if((electedOfficialRequestInternalEntity.getRequestorInternalEntity().getStatus()).equalsIgnoreCase("Closed")) {
//			Sending email notification
			MessageTemplateEntity messageTemplateEntity = messageTemplateRepository.findByStatus("UserClosed");
			String subject = messageTemplateEntity.getSubject();
			String messageTemplate = messageTemplateEntity.getMessageTemplate();
			
			boolean endUserMailSent = mailService.sendEmailNotification(internalResponse.getEmail(), subject, messageTemplate, internalResponse.getTicketID());
			if(!endUserMailSent) {
				throw new MailNotSentException("Problem in sending Email to User.");
			}
		}
		if(((request.getPreviousStatus()).equalsIgnoreCase("Closed")) && ((request.getStatus()).equalsIgnoreCase("Open"))) {
//			Sending email notification
			MessageTemplateEntity messageTemplateEntity = messageTemplateRepository.findByStatus("UserReopen");
			String subject = messageTemplateEntity.getSubject();
			String messageTemplate = messageTemplateEntity.getMessageTemplate();

			boolean endUserMailSent = mailService.sendEmailNotification(internalResponse.getEmail(), subject, messageTemplate, internalResponse.getTicketID());
			if(!endUserMailSent) {
				throw new MailNotSentException("Problem in sending Email to User.");
			}
		}
		return response;
	}

	@Transactional
	public RequestResponseDto updateReportProblemRequest(RequestorDto request) throws ResourceNotFoundException, MailNotSentException {

		logger.info("Inside updateReportProblemRequest service.");

		RequestResponseDto response = new RequestResponseDto();

		RequestorResponseDto originalResponse = new RequestorResponseDto();
		ReportProblemResponseDto originalReportProblemResponse = new ReportProblemResponseDto();

		RequestorResponseDto internalResponse = new RequestorResponseDto();
		ReportProblemResponseDto internalReportProblemResponse = new ReportProblemResponseDto();
		List<CommentResponseDto> commentResponses = new ArrayList<CommentResponseDto>();
		ReasonCodeResponseDto reasonCodeResponse = new ReasonCodeResponseDto();

		ReportProblemEntity reportProblemEntity = new ReportProblemEntity();
		RequestorEntity requestorEntity = new RequestorEntity();

		ReportProblemInternalEntity reportProblemInternalEntity = new ReportProblemInternalEntity();
		RequestorInternalEntity requestorInternalEntity = new RequestorInternalEntity();
		CommentInternalEntity commentInternalEntity = new CommentInternalEntity();
		ReasonCodeEntity reasonCodeEntity = new ReasonCodeEntity();

		List<CommentInternalEntity> comments = new ArrayList<CommentInternalEntity>();
		List<CommentInternalEntity> commentsRC = new ArrayList<CommentInternalEntity>();
		List<RequestorInternalEntity> requestors = new ArrayList<RequestorInternalEntity>();

//		Getting the original request and setting it to DTO
		requestorEntity = requestorRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		reportProblemEntity = reportProblemRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

		BeanUtils.copyProperties(requestorEntity, originalResponse);
		BeanUtils.copyProperties(reportProblemEntity, originalReportProblemResponse);
		originalResponse.setFormID(requestorEntity.getFormName().getFormID());
		originalResponse.setReportProblem(originalReportProblemResponse);

//		Setting the internal requestor and request
		requestorInternalEntity = requestorInternalRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		reportProblemInternalEntity = reportProblemInternalRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));

		BeanUtils.copyProperties(request.getReportProblem(), reportProblemInternalEntity);
		BeanUtils.copyProperties(request, requestorInternalEntity);
		BeanUtils.copyProperties(request.getComment(), commentInternalEntity);

//		Setting the comments to the requestor
		commentInternalEntity.setRequestorInternalEntity(requestorInternalEntity);

		if (requestorInternalEntity.getCommentInternalEntities().size() != 0) {
			requestorInternalEntity.getCommentInternalEntities().add(commentInternalEntity);
		} else {
			comments.add(commentInternalEntity);
			requestorInternalEntity.setCommentInternalEntities(comments);
		}

//		Setting the reason code(if any) for the comment
		if (request.getComment().getReasonID() != null) {
			reasonCodeEntity = reasonCodeRepository.findById(request.getComment().getReasonID())
					.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));

			commentInternalEntity.setReasonCodeEntity(reasonCodeEntity);

			if (reasonCodeEntity.getComments().size() != 0) {
				reasonCodeEntity.getComments().add(commentInternalEntity);
			} else {
				commentsRC.add(commentInternalEntity);
				reasonCodeEntity.setComments(commentsRC);
			}
		} else {
			reasonCodeEntity = reasonCodeRepository.findById((long) 14)
					.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));

			commentInternalEntity.setReasonCodeEntity(reasonCodeEntity);

			if (reasonCodeEntity.getComments().size() != 0) {
				reasonCodeEntity.getComments().add(commentInternalEntity);
			} else {
				commentsRC.add(commentInternalEntity);
				reasonCodeEntity.setComments(commentsRC);
			}
		}

//		Setting the reason code (if any)
		if (request.getReasonID() != null) {
			reasonCodeEntity = reasonCodeRepository.findById(request.getReasonID())
					.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));

			requestorInternalEntity.setReason(reasonCodeEntity);

			if (reasonCodeEntity.getRequestors().size() != 0) {
				reasonCodeEntity.getRequestors().add(requestorInternalEntity);
			} else {
				requestors.add(requestorInternalEntity);
				reasonCodeEntity.setRequestors(requestors);
			}
		} else {
			reasonCodeEntity = reasonCodeRepository.findById((long) 14)
					.orElseThrow(() -> new ResourceNotFoundException("Reason Code Not Found."));

			requestorInternalEntity.setReason(reasonCodeEntity);

			if (reasonCodeEntity.getRequestors().size() != 0) {
				reasonCodeEntity.getRequestors().add(requestorInternalEntity);
			} else {
				requestors.add(requestorInternalEntity);
				reasonCodeEntity.setRequestors(requestors);
			}
		}

		requestorInternalEntity.setUpdatedTime(new Timestamp(new Date().getTime()));

		requestorInternalEntity.setReportProblemInternalEntity(reportProblemInternalEntity);
		reportProblemInternalEntity.setRequestorInternalEntity(requestorInternalEntity);

//		Saving the request
		reportProblemInternalEntity = reportProblemInternalRepository.saveAndFlush(reportProblemInternalEntity);

//		Setting the DTOs for the response
		BeanUtils.copyProperties(reportProblemInternalEntity, internalReportProblemResponse);
		BeanUtils.copyProperties(reportProblemInternalEntity.getRequestorInternalEntity(), internalResponse);
		comments = reportProblemInternalEntity.getRequestorInternalEntity().getCommentInternalEntities();
		comments.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
		for (CommentInternalEntity comment : comments) {
			CommentResponseDto commentResponse = new CommentResponseDto();
			BeanUtils.copyProperties(comment, commentResponse);
			if (comment.getReasonCodeEntity() != null) {
				ReasonCodeResponseDto reasonCode = new ReasonCodeResponseDto();
				BeanUtils.copyProperties(comment.getReasonCodeEntity(), reasonCode);
				reasonCode.setFormID(comment.getReasonCodeEntity().getFormNameEntity().getFormID());
				commentResponse.setReason(reasonCode);
			}
			commentResponses.add(commentResponse);
		}
		if (reportProblemInternalEntity.getRequestorInternalEntity().getReason() != null) {
			BeanUtils.copyProperties(reportProblemInternalEntity.getRequestorInternalEntity().getReason(),
					reasonCodeResponse);
			reasonCodeResponse.setFormID(reportProblemInternalEntity.getRequestorInternalEntity().getReason()
					.getFormNameEntity().getFormID());
			internalResponse.setReason(reasonCodeResponse);
		}

		internalResponse.setFormID(reportProblemInternalEntity.getRequestorInternalEntity().getFormName().getFormID());
		internalResponse.setReportProblem(internalReportProblemResponse);
		internalResponse.setComments(commentResponses);

		response.setOriginalRequest(originalResponse);
		response.setInternalRequest(internalResponse);
		
		if((reportProblemInternalEntity.getRequestorInternalEntity().getStatus()).equalsIgnoreCase("Closed")) {
//			Sending email notification
			MessageTemplateEntity messageTemplateEntity = messageTemplateRepository.findByStatus("UserClosed");
			String subject = messageTemplateEntity.getSubject();
			String messageTemplate = messageTemplateEntity.getMessageTemplate();

			boolean endUserMailSent = mailService.sendEmailNotification(internalResponse.getEmail(), subject, messageTemplate, internalResponse.getTicketID());
			if(!endUserMailSent) {
				throw new MailNotSentException("Problem in sending Email to User.");
			}
		}
		if(((request.getPreviousStatus()).equalsIgnoreCase("Closed")) && ((request.getStatus()).equalsIgnoreCase("Open"))) {
//			Sending email notification
			MessageTemplateEntity messageTemplateEntity = messageTemplateRepository.findByStatus("UserReopen");
			String subject = messageTemplateEntity.getSubject();
			String messageTemplate = messageTemplateEntity.getMessageTemplate();

			boolean endUserMailSent = mailService.sendEmailNotification(internalResponse.getEmail(), subject, messageTemplate, internalResponse.getTicketID());
			if(!endUserMailSent) {
				throw new MailNotSentException("Problem in sending Email to User.");
			}
		}
		return response;
	}

	@Transactional
	public ReasonCodeResponseDto saveReasonCode(ReasonCodeRequestDto reasonCode) throws ResourceNotFoundException {

		logger.info("Inside addReasonCode service");

		ReasonCodeResponseDto response = new ReasonCodeResponseDto();

		ReasonCodeEntity reasonCodeEntity = new ReasonCodeEntity();

		List<ReasonCodeEntity> reasonCodes = new ArrayList<ReasonCodeEntity>();

//		Getting the formName and saving the reason code
		FormNameEntity formNameEntity = formNameRepository.findById(reasonCode.getFormID())
				.orElseThrow(() -> new ResourceNotFoundException("FormID not found."));

		BeanUtils.copyProperties(reasonCode, reasonCodeEntity);

		reasonCodeEntity.setFormNameEntity(formNameEntity);

		if (formNameEntity.getReasonCodes().size() != 0) {
			formNameEntity.getReasonCodes().add(reasonCodeEntity);
		} else {
			reasonCodes.add(reasonCodeEntity);
			formNameEntity.setReasonCodes(reasonCodes);
		}

		reasonCodeEntity = reasonCodeRepository.saveAndFlush(reasonCodeEntity);
		BeanUtils.copyProperties(reasonCodeEntity, response);
		response.setFormID(reasonCodeEntity.getFormNameEntity().getFormID());

		return response;
	}

	@Transactional
	public ReasonCodesDto getReasonCodes(Long formID) throws ResourceNotFoundException {

		logger.info("Inside getReasonCodes service.");

		ReasonCodesDto reasonCodes = new ReasonCodesDto();
		List<ReasonCodeResponseDto> openReasonCodes = new ArrayList<ReasonCodeResponseDto>();
		List<ReasonCodeResponseDto> closedReasonCodes = new ArrayList<ReasonCodeResponseDto>();

		FormNameEntity formNameEntity = formNameRepository.findById(formID)
				.orElseThrow(() -> new ResourceNotFoundException("Form not found."));

//		Getting both the open and closed reason codes and setting the respective DTOs
		List<ReasonCodeEntity> openReasonCodeEntities = reasonCodeRepository
				.findByFormNameEntityAndStatusTypeOrderByReasonCodeAsc(formNameEntity, "Open");
		List<ReasonCodeEntity> closedReasonCodeEntities = reasonCodeRepository
				.findByFormNameEntityAndStatusTypeOrderByReasonCodeAsc(formNameEntity, "Closed");

		openReasonCodeEntities.forEach(reasonCodeEntity -> {
			ReasonCodeResponseDto reasonCodeResponseDto = new ReasonCodeResponseDto();

			BeanUtils.copyProperties(reasonCodeEntity, reasonCodeResponseDto);

			reasonCodeResponseDto.setFormID(reasonCodeEntity.getFormNameEntity().getFormID());

			openReasonCodes.add(reasonCodeResponseDto);
		});

		closedReasonCodeEntities.forEach(reasonCodeEntity -> {
			ReasonCodeResponseDto reasonCodeResponseDto = new ReasonCodeResponseDto();

			BeanUtils.copyProperties(reasonCodeEntity, reasonCodeResponseDto);

			reasonCodeResponseDto.setFormID(reasonCodeEntity.getFormNameEntity().getFormID());

			closedReasonCodes.add(reasonCodeResponseDto);
		});

		reasonCodes.setOpen(openReasonCodes);
		reasonCodes.setClosed(closedReasonCodes);
		return reasonCodes;
	}

	@Transactional
	public DocumentsDto saveDocuments(DocumentRequestDto documentDetails, MultipartFile[] files)
			throws StorageException, IOException, ResourceNotFoundException {

		logger.info("Inside saveDocuments service.");

		List<DocumentInternalEntity> documentInternalEntities = new ArrayList<DocumentInternalEntity>();

		final Path rootLocation = Paths.get(propertiesConfig.getProperties().getProperty("filesPath"));

//		Creating data for storing the file(s)
		Path dir = rootLocation.resolve("" + documentDetails.getTicketID());

		RequestorInternalEntity requestorInternalEntity = requestorInternalRepository
				.findById(documentDetails.getRequestorID())
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));

		for (MultipartFile file : files) {

//			Testing for new link
			if (file == null) {
				throw new StorageException("File should be given for saving a new document.");
			}

//			Testing for file content
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
			}

			String newFileName = getNewFileName(file.getOriginalFilename());

			String url = dir.resolve(newFileName).toString();

			DocumentInternalEntity documentInternalEntity = new DocumentInternalEntity();

			for (DocumentDto document : documentDetails.getDocuments()) {
				if ((document.getFileName()).equals(file.getOriginalFilename())) {
					documentInternalEntity.setDocumentName(document.getDocumentName());
					break;
				}
			}
			if (documentInternalEntity.getDocumentName() == null) {
				throw new ResourceNotFoundException("Filenames not set correctly.");
			}
			documentInternalEntity.setActive(true);
			documentInternalEntity.setFileName(newFileName);
			documentInternalEntity.setFileLocation(url);
			documentInternalEntity.setCreatedBy(documentDetails.getCreatedBy());
			documentInternalEntity.setRequestorInternalEntity(requestorInternalEntity);

			if (requestorInternalEntity.getDocumentInternalEntities() == null) {
				documentInternalEntities.add(documentInternalEntity);
				requestorInternalEntity.setDocumentInternalEntities(documentInternalEntities);
			} else if (requestorInternalEntity.getDocumentInternalEntities().size() != 0) {
				requestorInternalEntity.getDocumentInternalEntities().add(documentInternalEntity);
			}
			
			requestorInternalEntity.setUpdatedBy(documentDetails.getCreatedBy());
			requestorInternalEntity.setUpdatedTime(new Timestamp(new Date().getTime()));
			
			documentInternalEntity = documentInternalRepository.saveAndFlush(documentInternalEntity);

//		Saving the file in the file system
			if (Files.exists(dir)) {
				Files.copy(file.getInputStream(), dir.resolve(newFileName));
			} else {
				Files.createDirectory(dir);
				Files.copy(file.getInputStream(), dir.resolve(newFileName));
			}
		}
		
		DocumentsDto documents = this.getDocuments(documentDetails.getRequestorID());
		return documents;
	}

	@Transactional
	public DocumentsDto getRequestorDocuments(Long requestorID) throws ResourceNotFoundException {

		logger.info("Inside getRequestorDocuments service.");

		DocumentsDto documents = new DocumentsDto();
		List<DocumentResponseDto> existingRequestorDocuments = new ArrayList<DocumentResponseDto>();

		List<DocumentEntity> existingRequestorDocumentEntities = new ArrayList<DocumentEntity>();

//		Getting the document related to the respective ticket
		RequestorEntity requestorEntity = requestorRepository.findById(requestorID)
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		existingRequestorDocumentEntities = documentRepository.findByRequestorEntityAndActive(requestorEntity, true);

//		Grouping the existing documents
		existingRequestorDocumentEntities.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
		existingRequestorDocumentEntities.forEach(documentEntity -> {
			DocumentResponseDto documentResponse = new DocumentResponseDto();
			BeanUtils.copyProperties(documentEntity, documentResponse);

//			Appending the filetype to the documentName
//			String fileName = documentInternalEntity.getFileName();
//			String extension = fileName.substring(fileName.lastIndexOf('.')+1);
//			String documentName = documentInternalEntity.getDocumentName().concat(" [" + extension + "]");
//			documentResponse.setDocumentName(documentName);

//			Setting the name without the timestamp attached
			String editName = documentEntity.getFileName();
			int lastIndex = editName.lastIndexOf("[");
			int endingIndex = editName.lastIndexOf(".");
			editName = editName.substring(0, lastIndex) + editName.substring(endingIndex);
			documentResponse.setFileName(editName);

			existingRequestorDocuments.add(documentResponse);
		});

		documents.setExistingDocuments(existingRequestorDocuments);
		return documents;
	}

	@Transactional
	public DocumentsDto getDocuments(Long requestorID) throws ResourceNotFoundException {

		logger.info("Inside getDocuments service.");

		DocumentsDto documents = new DocumentsDto();
		List<DocumentResponseDto> existingDocuments = new ArrayList<DocumentResponseDto>();
		List<DocumentResponseDto> archiveDocuments = new ArrayList<DocumentResponseDto>();

		List<DocumentInternalEntity> existingDocumentInternalEntities = new ArrayList<DocumentInternalEntity>();
		List<DocumentInternalEntity> archiveDocumentInternalEntities = new ArrayList<DocumentInternalEntity>();

//		Getting all the documents related to the respective ticket
		RequestorInternalEntity requestorInternalEntity = requestorInternalRepository.findById(requestorID)
				.orElseThrow(() -> new ResourceNotFoundException("Requestor Not Found."));
		existingDocumentInternalEntities = documentInternalRepository
				.findByRequestorInternalEntityAndActive(requestorInternalEntity, true);
		archiveDocumentInternalEntities = documentInternalRepository
				.findByRequestorInternalEntityAndActive(requestorInternalEntity, false);

//		Grouping the existing documents
		existingDocumentInternalEntities.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
		existingDocumentInternalEntities.forEach(documentInternalEntity -> {
			DocumentResponseDto documentResponse = new DocumentResponseDto();
			BeanUtils.copyProperties(documentInternalEntity, documentResponse);

//			Appending the filetype to the documentName
//			String fileName = documentInternalEntity.getFileName();
//			String extension = fileName.substring(fileName.lastIndexOf('.')+1);
//			String documentName = documentInternalEntity.getDocumentName().concat(" [" + extension + "]");
//			documentResponse.setDocumentName(documentName);

//			Setting the name without the timestamp attached
			String editName = documentInternalEntity.getFileName();
			int lastIndex = editName.lastIndexOf("[");
			int endingIndex = editName.lastIndexOf(".");
			editName = editName.substring(0, lastIndex) + editName.substring(endingIndex);
			documentResponse.setFileName(editName);

			existingDocuments.add(documentResponse);
		});

//		Grouping the archive documents
		archiveDocumentInternalEntities.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
		archiveDocumentInternalEntities.forEach(documentInternalEntity -> {
			DocumentResponseDto documentResponse = new DocumentResponseDto();
			BeanUtils.copyProperties(documentInternalEntity, documentResponse);

//			Appending the filetype to the documentName
//			String fileName = documentInternalEntity.getFileName();
//			String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
//			String documentName = documentInternalEntity.getDocumentName().concat(" [" + extension + "]");
//			documentResponse.setDocumentName(documentName);

//			Setting the name without the timestamp attached
			String editName = documentInternalEntity.getFileName();
			int lastIndex = editName.lastIndexOf("[");
			int endingIndex = editName.lastIndexOf(".");
			editName = editName.substring(0, lastIndex) + editName.substring(endingIndex);
			documentResponse.setFileName(editName);

			archiveDocuments.add(documentResponse);
		});

		documents.setExistingDocuments(existingDocuments);
		documents.setArchiveDocuments(archiveDocuments);
		return documents;
	}

	@Transactional
	public DocumentsDto deleteDocument(DocumentRequestDto documentDetails) throws ResourceNotFoundException {

		logger.info("Inside deleteDocument service.");

		DocumentInternalEntity documentInternalEntity = new DocumentInternalEntity();

//		Retrieving the document and making it archive
		documentInternalEntity = documentInternalRepository.findById(documentDetails.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Document Not Found."));
		documentInternalEntity.setUpdatedBy(documentDetails.getUpdatedBy());
		documentInternalEntity.setActive(false);
		
		documentInternalEntity.getRequestorInternalEntity().setUpdatedBy(documentDetails.getUpdatedBy());
		documentInternalEntity.getRequestorInternalEntity().setUpdatedTime(new Timestamp(new Date().getTime()));

		documentInternalEntity = documentInternalRepository.saveAndFlush(documentInternalEntity);

		DocumentsDto documents = this.getDocuments(documentInternalEntity.getRequestorInternalEntity().getId());
		return documents;
	}

	@Transactional
	public Resource loadAsRequestorResource(Long id) throws ResourceNotFoundException, StorageFileNotFoundException {
		try {
			logger.info("Inside loadAsResource service of requestor documents.");

			Path path = null;
			DocumentResponseDto documentResponse = new DocumentResponseDto();

//			Getting the path of the file from the database
			DocumentEntity documentEntity = documentRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Document Not Found."));
			if (documentEntity == null) {
				throw new StorageFileNotFoundException("No file available.");
			}
			path = Paths.get(documentEntity.getFileLocation());
			BeanUtils.copyProperties(documentEntity, documentResponse);

//	        Building the resource to serve the file
			Resource resource = new UrlResource(path.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file");
			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file", e);
		}
	}
	
	@Transactional
	public Resource loadAsResource(Long id) throws StorageFileNotFoundException, ResourceNotFoundException {
		try {
			logger.info("Inside loadAsResource service of documents.");

			Path path = null;
			DocumentResponseDto documentResponse = new DocumentResponseDto();

//			Getting the path of the file from the database
			DocumentInternalEntity documentInternalEntity = documentInternalRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Document Not Found."));
			if (documentInternalEntity == null) {
				throw new StorageFileNotFoundException("No file available.");
			}
			path = Paths.get(documentInternalEntity.getFileLocation());
			BeanUtils.copyProperties(documentInternalEntity, documentResponse);

//	        Building the resource to serve the file
			Resource resource = new UrlResource(path.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file");
			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file", e);
		}
	}
	
//	Methods to create a unique filename
	public static String getFileExtension(final String path) {
		if (path != null && path.lastIndexOf('.') != -1) {
			return path.substring(path.lastIndexOf('.'));
		}
		return null;
	}

	public static String getFileName(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf('.'));
	}

	public static String getNewFileName(String fileName) {
		String ext = getFileExtension(fileName);
		String name = getFileName(fileName);
		DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("MM-dd-yyyy HH.mm.ss");
		String timestamp = timeStampPattern.format(java.time.LocalDateTime.now());
		File newFile = new File(name + "[" + timestamp + "]" + ext);
		String newFileName = newFile.toString();
		return newFileName;
	}

	@Transactional
	public List<AssignmentDto> updateAssignmentDTO(AssignmentDto assignmentDto) throws MailNotSentException {
		
		logger.info("Inside updateAssignment service.");
		
		AssignmentDto resultDTO = null;
		AssignmentEntity assignmentEntity= null;
		
		RequestorInternalEntity requestorEntity = null;
		List<AssignmentDto> assignmentsDTOs = new ArrayList<AssignmentDto>();
		List<AssignmentEntity> assignmentEntities = null;
		try {
			assignmentEntity = assignmentRepository.findById(assignmentDto.getAssignmentId()).get();

			
			if(assignmentEntity != null && assignmentEntity.getId() > 0) {
				BeanUtils.copyProperties(assignmentDto, assignmentEntity);
				assignmentEntity.setRequestorInternalEntity(assignmentEntity.getRequestorInternalEntity());
				assignmentRepository.save(assignmentEntity);
								
				requestorEntity = new RequestorInternalEntity(assignmentDto.getRequestorId());
				assignmentEntities = assignmentRepository.findByRequestorInternalEntityAndActive(requestorEntity, true);
				assignmentEntities.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));

				for(AssignmentEntity record : assignmentEntities) {
					resultDTO = new AssignmentDto();
					BeanUtils.copyProperties(record, resultDTO);
					resultDTO.setRequestorId(record.getRequestorInternalEntity().getId());
					assignmentsDTOs.add(resultDTO);		
				}
				
			}
		}catch(NullPointerException | EntityNotFoundException e) {
			e.printStackTrace();
		}
		
//		Sending email notification
		if((assignmentDto.getStatus()).equalsIgnoreCase("Closed")) {
			
			List<String> adminEmails = userRepository.getAdminEmails();
			String[] recepients = new String[adminEmails.size()];
			adminEmails.toArray(recepients);
			
			MessageTemplateEntity messageTemplateEntity = messageTemplateRepository.findByStatus("AssignmentClosed");
			String subject = messageTemplateEntity.getSubject();
			String messageTemplate = messageTemplateEntity.getMessageTemplate();
			
			boolean groupMailSent = mailService.sendGroupEmailNotification(recepients, subject, messageTemplate, assignmentEntity.getRequestorInternalEntity().getTicketID());
			if(!groupMailSent) {
				throw new MailNotSentException("Problem in sending Email to the group.");
			}
		}		
		return assignmentsDTOs;
	}
	
	@Transactional
	public ResponseEntity<String> sendCreationEmails(NotificationDto notification) throws ResourceNotFoundException, MailNotSentException {
		
		logger.info("Inside sendCreationEmails service");
		
		List<String> adminEmails = userRepository.getAdminEmails();
		String[] recepients = new String[adminEmails.size()];
		adminEmails.toArray(recepients);
		
		MessageTemplateEntity groupMessageEntity = messageTemplateRepository.findByStatus("Open");
		MessageTemplateEntity userMessageEntity = messageTemplateRepository.findByStatus("UserOpen");
				
		boolean endUserMailSent = mailService.sendEmailNotification(notification.getEmail(), userMessageEntity.getSubject(), userMessageEntity.getMessageTemplate(), notification.getTicketID());
		if(!endUserMailSent) {
			throw new MailNotSentException("Problem in sending Email to User.");
		}
		
		boolean groupMailSent = mailService.sendGroupEmailNotification(recepients, groupMessageEntity.getSubject(), groupMessageEntity.getMessageTemplate(), notification.getTicketID());
		if(!groupMailSent) {
			throw new MailNotSentException("Problem in sending Email to the group.");
		}
		return ResponseEntity.ok().body("Mails sent.");	
	}
	
	@Transactional
	public List<DivisionDto> getDivisions() {
		
		logger.info("Inside getDivisions service");
		
		List<DivisionDto> response = new ArrayList<DivisionDto>();
		
		List<DivisionEntity> divisionEntities = divisionRepository.findAllByActiveOrderByIdAsc(true);
		for(DivisionEntity entity: divisionEntities) {
			DivisionDto division = new DivisionDto();
			BeanUtils.copyProperties(entity, division);
			response.add(division);
		}
		return response; 
	}
	
	@Transactional
	public List<DivisionDto> saveDivisions(List<DivisionDto> divisions) {
		
		logger.info("Inside saveDivisions service");
		
		List<DivisionDto> response = new ArrayList<DivisionDto>();
		List<DivisionEntity> divisionEntities = new ArrayList<DivisionEntity>();
		for(DivisionDto division: divisions) {
			DivisionEntity divisionEntity = new DivisionEntity();
			BeanUtils.copyProperties(division, divisionEntity);
			divisionEntities.add(divisionEntity);
		}
		divisionEntities = divisionRepository.saveAll(divisionEntities);
		
		response = this.getDivisions();
		return response;
	}
	
	@Transactional
	public List<DivisionDto> deleteDivision(DivisionDto division) throws ResourceNotFoundException {
		
		logger.info("Inside deleteDivision service");
		
		List<DivisionDto> response = new ArrayList<DivisionDto>();
		
		DivisionEntity divisionEntity = divisionRepository.findById(division.getId()).orElseThrow(() -> new ResourceNotFoundException("Division Not Found"));
		BeanUtils.copyProperties(division, divisionEntity, "createdBy", "createdTime");
		divisionEntity = divisionRepository.save(divisionEntity);
		
		response = this.getDivisions();
		return response;
		
	}
		
//	Method to get email addresses for notification
	public String[] getRecepients(FormNameEntity formNameEntity) {
		
		List<String> emailAddresses = new ArrayList<String>();
		
		List<NotificationEntity> notificationEntities = notificationRepository.findByFormNameEntityAndStatus(formNameEntity,true);
		
		for(int i=0; i<notificationEntities.size(); i++) {
			emailAddresses.add(notificationEntities.get(i).getUserEntity().getEmail());
		}
		String[] recepients = new String[emailAddresses.size()]; 
		emailAddresses.toArray(recepients);
		return recepients;
	}
	
	@Override
	public AssignmentResponseDto setAssignments(@Valid AssignmentDto request, String accessToken) throws ResourceNotFoundException, MailNotSentException {
		
		logger.info("Inside setAssignments service.");
		
		RequestorInternalEntity requestorInternalEntity = requestorInternalRepository.findById(request.getRequestorId()).orElseThrow(() -> new ResourceNotFoundException("Request Not Found."));
		
		List<AssignmentEntity> assignmentEntities = new ArrayList<AssignmentEntity>();
		List<AssignmentDto> savedAssignments = new ArrayList<AssignmentDto>();
		
//		Setting the assignments to the request
		AssignmentEntity assignmentEntity = new AssignmentEntity();
		BeanUtils.copyProperties(request, assignmentEntity);
		assignmentEntity.setActive(true);
		assignmentEntity.setRequestorInternalEntity(requestorInternalEntity);
		//			assignmentEntity.setCreatedBy(request.getCreatedBy());

		if(requestorInternalEntity.getAssignmentEntities().size()!=0) {
			requestorInternalEntity.getAssignmentEntities().add(assignmentEntity);
		}
		else {
			assignmentEntities.add(assignmentEntity);
			requestorInternalEntity.setAssignmentEntities(assignmentEntities);
		}

		requestorInternalEntity.setUpdatedBy(request.getCreatedBy());
		requestorInternalEntity.setUpdatedTime(new Timestamp(new Date().getTime()));
		List<AssignmentEntity> savedAssignmentEntities = assignmentRepository.saveAll(requestorInternalEntity.getAssignmentEntities());

		AssignmentResponseDto response = new AssignmentResponseDto();
		
		for(AssignmentEntity savedAssignmentEntity: savedAssignmentEntities) {
			AssignmentDto savedAssignment = new AssignmentDto();
			BeanUtils.copyProperties(savedAssignmentEntity, savedAssignment);
			savedAssignments.add(savedAssignment);
		}
		
		response.setRequestorID(request.getRequestorId());
		response.setAssignments(savedAssignments);
		
		restTemplate = new RestTemplate();
		ResponseEntity<UserDTO> responseEntity = new ResponseEntity<UserDTO>(HttpStatus.OK);
		
		restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
			
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
					throws IOException {
				request.getHeaders().set("Authorization", "Bearer "+accessToken);
				return execution.execute(request, body);
			}
		});
		InitialContext initial_ctx;
		String value = null;
		try {
			initial_ctx = new InitialContext();
			value = (String) initial_ctx.lookup("search_employee");
		}catch (NamingException e) {
			e.printStackTrace();
		}
		try {
		responseEntity = restTemplate.getForEntity(value+request.getEmail(), UserDTO.class);
		}
		catch(HttpClientErrorException e) {
			if(e.getStatusCode()== HttpStatus.NOT_FOUND) {
				UserDTO userDTO = new UserDTO();
				userDTO.setEmailId(request.getEmail());
				EmployeeDTO employeeDTO = employeeService.findByMail(request.getEmail());
				userDTO.setFirstName(employeeDTO.getFirstName());
				userDTO.setLastName(employeeDTO.getLastName());
				//userDTO.setFirstName(request.get);
				String value2 = null;
				try {
					initial_ctx = new InitialContext();
					value2 = (String) initial_ctx.lookup("onboard_ifnotfound");
				}catch (NamingException ex) {
					ex.printStackTrace();
				}
				restTemplate.postForEntity(value2,userDTO, UserDTO.class);
			}
		}
		
//		Sending email notification
		MessageTemplateEntity messageTemplateEntity = messageTemplateRepository.findByStatus("Assigned");
		String subject = messageTemplateEntity.getSubject();
		String messageTemplate = messageTemplateEntity.getMessageTemplate();
		boolean assigneeMailSent = mailService.sendEmailNotification(request.getEmail(), subject, messageTemplate, requestorInternalEntity.getTicketID());
		if(!assigneeMailSent) {
			throw new MailNotSentException("Problem in sending Email to the assignee.");
		}
		return response;
	}
}
	