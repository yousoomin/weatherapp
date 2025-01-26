package com.acon.weatherapp.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetPasswordEmail(String email, String token) {
        String resetLink = "http://localhost:8080/reset/" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("비밀번호 재설정 요청");
        message.setText("다음 링크를 클릭하여 비밀번호를 재설정하세요: " + resetLink);
        mailSender.send(message);
    }
}
