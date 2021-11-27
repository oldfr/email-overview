package com.example.emailoverview.config;

import com.sun.mail.smtp.SMTPMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class EmailSender {

    Logger log = LoggerFactory.getLogger(EmailSender.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.toMailID}")
    String toMailID;

    @Value("${spring.mail.fromMailID}")
    String fromMailID;

    @Value("${spring.mail.password}")
    String password;

    @Value("${spring.mail.bccMailID}")
    String bccMailID;

    public void sendEmail() {
        log.info("sending mail from: {} to: {}", fromMailID, toMailID);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromMailID);
        msg.setTo(toMailID);
        msg.setSubject("Simple mail from Spring Boot Application");
        String body = "Hi Raju,\n\n" +
                "Please send the candidate details ASAP. " +
                "\n\nThanks, " +
                "\nLaxman\n";
        msg.setText(body);
        javaMailSender.send(msg);
        System.out.println("sent mail");
    }

    public void sendMailWithAttachment() throws MessagingException {
        log.info("sending mail from: {} to: {} bcc: {}", fromMailID, toMailID, bccMailID);
        ClassPathResource pic = new ClassPathResource("static/doggie.jpeg");
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(fromMailID);
            String[] toMailList = {toMailID};
            messageHelper.setTo(toMailList);
            String[] bccMailList = {bccMailID};
            messageHelper.setBcc(bccMailList);
            messageHelper.setSubject("This is a mail with attachment");

            messageHelper.addInline("cat",new ClassPathResource("static/cat.jpeg").getFile());
            messageHelper.setText("Hi Raju," +
                    "<br/> <br/> Please find the attachment for your reference. <br/><br/>" +
                    "Thanks,<br/> Laxman", true);
/*            if(StringUtils.isNotEmpty(attachmentUrl) && isHttpUrl(attachmentUrl)) {
                URL url = new URL(attachmentUrl);
                String filename = url.getFile();
                byte fileContent [] = getFileContent(url);
                messageHelper.addAttachment(filename, new ByteArrayResource(fileContent));
            }*/
            messageHelper.addAttachment("pug.jpeg",pic);
//            messageHelper.addInline("cat",new ClassPathResource("static/cat.jpeg").getFile());

        };

/*        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(msg,true);
        mimeMessageHelper.setTo("ankithahjp@gmail.com");
        mimeMessageHelper.setFrom("ankithajayaprakash@gmail.com");
        mimeMessageHelper.setSubject("This is a mail with attachment");
        mimeMessageHelper.addAttachment("doggie.jpeg",pic);*/
        javaMailSender.send(messagePreparator);
        System.out.println("sent mail with attachment");
    }

    public void sendMailWithTemplate() throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.addAttachment("template-cat.png", new ClassPathResource("static/cat.jpeg"));
        Context context = new Context();
        context.setVariable("name","Laxman");
        context.setVariable("department","Information Technology");
//        context.setVariable("catImg","/resources/static/cat.jpeg"); // can not send local images
        context.setVariable("catImg","https://cdn.pixabay.com/photo/2014/11/30/14/11/cat-551554__340.jpg");

        String html = templateEngine.process("user-data", context);
        helper.setFrom(fromMailID);
        helper.setTo(toMailID);
        helper.setText(html, true);
        helper.setSubject("User Details");
        javaMailSender.send(message);
    }

}
