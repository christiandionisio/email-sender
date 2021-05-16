package com.cdionisio.emailsender.service;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cdionisio.emailsender.model.EmailSender;
import com.cdionisio.emailsender.utils.MailProperties;

@Service
public class EmailSenderServiceImpl implements IEmailSenderService {
	
	@Autowired
	private Environment env;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderServiceImpl.class);

	@Override
	public Boolean sendEmail(EmailSender emailSender) throws AddressException, MessagingException, IOException {
		
		LOGGER.info("Inicia servicio envio mail");
		
		Boolean isSendEmailOk = false;
		
		Session session = Session.getInstance(getProperties(), new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(env.getProperty(MailProperties.MAIL_SMTP_USERNAME.getValue()), 
						env.getProperty(MailProperties.MAIL_SMTP_PASSWORD.getValue()));
			}
		});
		
		
		LOGGER.info("Configurando parametros del mail");
		
		Message msg = new MimeMessage(session);

		addReceivers(msg, emailSender);
		msg.setSubject(emailSender.getAsunto());
		msg.setSentDate(new Date());

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(emailSender.getMensaje(), "text/html");
		
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		
		if (emailSender.getAttachments() != null) {
			addAttachements(multipart, emailSender.getAttachments());			
		}
		
		msg.setContent(multipart);
		Transport.send(msg);
		
		LOGGER.info("Se envio el mail satisfactoriamente");
		isSendEmailOk = true;
		
		return isSendEmailOk;

	}
	
	private Properties getProperties() {
		
		Properties props = new Properties();
		props.put(MailProperties.MAIL_SMTP_AUTH.getValue(), env.getProperty(MailProperties.MAIL_SMTP_AUTH.getValue()));
		props.put(MailProperties.MAIL_SMTP_STARTTLS_ENABLE.getValue(), env.getProperty(MailProperties.MAIL_SMTP_STARTTLS_ENABLE.getValue()));
		props.put(MailProperties.MAIL_SMTP_HOST.getValue(), env.getProperty(MailProperties.MAIL_SMTP_HOST.getValue()));
		props.put(MailProperties.MAIL_SMTP_PORT.getValue(), env.getProperty(MailProperties.MAIL_SMTP_PORT.getValue()));
		
		return props;
	}
	
	private void addReceivers(Message msg, EmailSender emailSender) throws AddressException, MessagingException {
		
		LOGGER.info("Agregando destinatarios");
		
		for (String destinatario : emailSender.getDestinatario()) {
			msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
		}

		if (emailSender.getCc() != null) {
			LOGGER.info("Agregando copias Cc");
			for (String cc : emailSender.getCc()) {
				msg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
			}
		}
		
		LOGGER.info("Destinatarios agregados con exito");
	}
	
	private void addAttachements(Multipart multipart,  MultipartFile[] attachments) throws MessagingException, IOException {
		
		LOGGER.info("Agregando archivos adjuntos");
		
		for (MultipartFile attachment : attachments) {
			MimeBodyPart attachPart = new MimeBodyPart();
			attachPart.setContent(attachment.getBytes(), attachment.getContentType());
			attachPart.setFileName(attachment.getOriginalFilename());
			attachPart.setDisposition(Part.ATTACHMENT);
			multipart.addBodyPart(attachPart);
		}
		
		LOGGER.info("Archivos adjuntos agregado con exito");
		
	}

}
