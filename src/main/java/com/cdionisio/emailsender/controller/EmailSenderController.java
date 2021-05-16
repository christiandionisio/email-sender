package com.cdionisio.emailsender.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cdionisio.emailsender.model.EmailSender;
import com.cdionisio.emailsender.service.IEmailSenderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/mail-sender")
public class EmailSenderController {
	
	@Autowired
	private IEmailSenderService service;
	
	@PostMapping("/send-with-attachments")
	public ResponseEntity<?> sendEmailWithAttachments(@RequestParam("data") String emailSender, 
			@RequestParam("attachment") MultipartFile[] attachments) 
					throws AddressException, MessagingException, IOException {
		
		EmailSender emailSenderJson = new ObjectMapper().readValue(emailSender, EmailSender.class);
		emailSenderJson.setAttachments(attachments);

		if (service.sendEmail(emailSenderJson)) {
			Map<String, Object> response = new HashMap<>();
			response.put("ok", true);
			response.put("message", "Correo enviado");
			return ResponseEntity.ok(response);
		} else {
			Map<String, Object> response = new HashMap<>();
			response.put("ok", false);
			response.put("message", "Ocurrio un error, revisar los logs");
			
			return ResponseEntity.ok(response);
		}
	}
	
	@PostMapping("/send-mail")
	public ResponseEntity<?> sendEmail(@RequestBody @Valid EmailSender emailSender, BindingResult result) 
					throws AddressException, MessagingException, IOException {
		
		if (result.hasErrors()) {
			Map<String, Object> response = new HashMap<>();
			response.put("ok", false);
			
			Map<String, Object> errors = new HashMap<>();
			
			for(FieldError error: result.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}
			
			response.put("errors", errors);
			
			return ResponseEntity.badRequest().body(response);
		}

		if (service.sendEmail(emailSender)) {
			Map<String, Object> response = new HashMap<>();
			response.put("ok", true);
			response.put("message", "Correo enviado");
			return ResponseEntity.ok(response);
		} else {
			Map<String, Object> response = new HashMap<>();
			response.put("ok", false);
			response.put("message", "Ocurrio un error, revisar los logs");
			
			return ResponseEntity.badRequest().body(response);
		}
	}
	
}
