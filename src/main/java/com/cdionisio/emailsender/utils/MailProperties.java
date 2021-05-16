package com.cdionisio.emailsender.utils;

public enum MailProperties {
	
	MAIL_SMTP_AUTH("mail.smtp.auth"),
	MAIL_SMTP_STARTTLS_ENABLE("mail.smtp.starttls.enable"),
	MAIL_SMTP_HOST("mail.smtp.host"),
	MAIL_SMTP_PORT("mail.smtp.port"),
	MAIL_SMTP_USERNAME("mail.smtp.username"),
	MAIL_SMTP_PASSWORD("mail.smtp.password");
	
	private String value;

	private MailProperties(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}	

}
