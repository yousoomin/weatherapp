package com.acon.weatherapp.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetPasswordEmail(String email, String token) throws MessagingException {
        String resetLink = "http://localhost:8080/api/password/reset/" + token;
        // MimeMessage 객체 생성
        MimeMessage message = mailSender.createMimeMessage();

        // MimeMessageHelper 사용하여 메일 설정
        MimeMessageHelper helper = new MimeMessageHelper(message, true);  // true는 HTML 메일을 지원

        // 받는 사람, 제목, 내용 설정
        helper.setTo(email);
        helper.setSubject("비밀번호 재설정 요청");

        // HTML 내용 설정 (링크 포함)
        String content = "<p>다음 링크를 클릭하여 비밀번호를 재설정하세요:</p>" +
                "<a href=\"" + resetLink + "\">비밀번호 재설정</a>";
        helper.setText(content, true);  // true로 설정하면 HTML 내용으로 처리됨

        // 메일 보내기
        mailSender.send(message);
    }
}
