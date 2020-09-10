package gov.virginia.dmas.services;

public interface MailService {

	public boolean sendEmailNotification(String recepient, String subject, String message, String ticketID);

	public boolean sendGroupEmailNotification(String[] recepients, String subject, String message, String ticketID);
}
