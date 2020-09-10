package gov.virginia.dmas.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService{

	final static Logger logger = LogManager.getLogger(MailServiceImpl.class);
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String userName;

	public boolean sendGroupEmailNotification(String[] recepients, String subject, String message, String ticketID) {
		
		logger.info("Inside sendGroupEmailNotification service");
		try {
			MimeMessage mime = this.javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mime,true);
			helper.setFrom(userName);
			helper.setTo(recepients);
			helper.setSubject(subject);
			message = message.replace("ticketID", ticketID);
			helper.setText(message, true);
			this.javaMailSender.send(mime);
			return true;
		}
		catch (MailException e) {
			e.printStackTrace();
		}catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean sendEmailNotification(String recepient, String subject, String message, String ticketID) {
		
		logger.info("Inside sendEmailNotification service");
		try {
			MimeMessage mime = this.javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mime,true);
			helper.setFrom(userName);
			helper.setTo(recepient);
			helper.setSubject(subject);
			message = message.replace("ticketID", ticketID);
			helper.setText(message, true);
			this.javaMailSender.send(mime);
			return true;
		}
		catch (MailException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
}
