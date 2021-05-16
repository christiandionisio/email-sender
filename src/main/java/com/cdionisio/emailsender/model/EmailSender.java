package com.cdionisio.emailsender.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

public class EmailSender {
	
	@NotEmpty
	private String asunto;
	
	private String remitente;
	
	@NotEmpty
	private List<String> destinatario;
	
	@NotEmpty
	private String mensaje;
	
	private List<String> cc;
	
	private MultipartFile[] attachments;

	public MultipartFile[] getAttachments() {
		return attachments;
	}

	public void setAttachments(MultipartFile[] attachments) {
		this.attachments = attachments;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getRemitente() {
		return remitente;
	}

	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}

	public List<String> getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(List<String> destinatario) {
		this.destinatario = destinatario;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public List<String> getCc() {
		return cc;
	}

	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	@Override
	public String toString() {
		return "EmailSender [asunto=" + asunto + ", remitente=" + remitente + ", destinatario=" + destinatario
				+ ", mensaje=" + mensaje + ", cc=" + cc + "]";
	}

	
	
	

}
