package br.com.imageliteapi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class EmailSenderService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    private MimeMessage prepareMimeMessage(String template, String subject, String email) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
        mmh.setTo(email);
        mmh.setFrom(from);
        mmh.setSubject(subject);
        mmh.setSentDate(new Date(System.currentTimeMillis()));
        mmh.setText(template, true);
        return mimeMessage;
    }

    public void enviarEmail(String template, String subject, String email)   {
        MimeMessage mm;
        try {
            mm = prepareMimeMessage(template, subject, email);
        } catch (MessagingException e) {
            throw new MailSendException("Erro durante envio do email.");
        }
        mailSender.send(mm);
    }

}
