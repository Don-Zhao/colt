package com.colt.app.activemq;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Component
public class Consumer {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private SpringTemplateEngine springTemplateEngine;
	
	private static String template_key = "template";
	
	@JmsListener(destination="mail/test")
	public void receiveQueue(Map<String, Object> params) {
		String template = params.get(template_key).toString();
		String mailText = this.render(template, params);
		System.out.println("text = " + mailText);
		
		MimeMessage message = null;
		
		try {
			message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setSubject("验证码邮件");
			helper.setFrom("zhaojiahong@zhaojiahong.com");
			helper.setTo(params.get("username").toString());
			helper.setText(mailText, true);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public String render(String template, Map<String, Object> params) {
		Context context = new Context(LocaleContextHolder.getLocale());
		context.setVariables(params);
		return springTemplateEngine.process(template, context);
	}
}
