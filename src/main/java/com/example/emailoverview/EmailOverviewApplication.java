package com.example.emailoverview;

import com.example.emailoverview.config.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.mail.MessagingException;

@SpringBootApplication
public class EmailOverviewApplication implements CommandLineRunner {

	@Autowired
	public ApplicationContext applicationContext;

	public static void main(String[] args) {
				SpringApplication.run(EmailOverviewApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Preparing to send mail");
		EmailSender config = applicationContext.getBean(EmailSender.class);
//		config.sendEmail();
//		config.sendMailWithAttachment();
//		config.sendMailWithTemplate();
//		config.sendMailWithLocalInlineImg();
	}

}
