package com.cdionisio.emailsender.service;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.cdionisio.emailsender.model.EmailSender;

public interface IEmailSenderService {
	
	public Boolean sendEmail(EmailSender emailSender) throws AddressException, MessagingException, IOException;

}
