package com.eg.mod.service;

import java.util.Date;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	public JavaMailSender sender;

	public void sendMail(String from, String to, String subject, String text) {

		try {
			MimeMessage message = sender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setText(text, true); // true to set to html formatted text
			helper.setSubject(subject);
			helper.setSentDate(new Date());
			sender.send(message);
			logger.info("Mail Sent Success!");		
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void sendMailWithAttachment(String from, String to, String subject, String text, String fileNameWithPath) {

		try {
			MimeMessage message = sender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);	        			
			helper.setFrom(from);
			helper.setTo(to);
			helper.setText(text, true);
			helper.setSubject(subject);
			helper.setSentDate(new Date());
			FileSystemResource file = new FileSystemResource(fileNameWithPath);
			helper.addAttachment(file.getFilename(), file);
			sender.send(message);
			logger.info("Mail Sent Success!");	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
