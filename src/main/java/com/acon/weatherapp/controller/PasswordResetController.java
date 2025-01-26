package com.acon.weatherapp.controller;

import com.acon.weatherapp.mapper.UserMapper;
import com.acon.weatherapp.model.User;
import com.acon.weatherapp.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordResetController {
    private UserMapper userMapper;
    private EmailService emailService;

    public PasswordResetController(UserMapper userMapper, EmailService emailService) {
        this.userMapper = userMapper;
        this.emailService = emailService;

    }

    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<User> user = userMapper.findByEmail(email);

        if (user.isPresent()) {
            String token = UUID.randomUUID().toString(); // 랜덤 토큰 생성
            User existingUser = user.get();
            existingUser.setResetPasswordToken(token);
            existingUser.setResetPasswordExpires(new Date(System.currentTimeMillis() + 3600000)); // 1시간 유효
            userMapper.save(existingUser);
            emailService.sendResetPasswordEmail(email, token); // 이메일 전송
            return ResponseEntity.ok("비밀번호 재설정 링크가 이메일로 전송되었습니다.");
        } else {
            return ResponseEntity.status(404).body("이메일이 존재하지 않습니다.");
        }
    }

    @GetMapping("/reset")
    public String showResetPage(@RequestParam("token") String token, Model model) {
        // 토큰 검증 로직
        Optional<User> user = userMapper.findByResetPasswordToken(token);

        if (user.isPresent() && user.get().getResetPasswordExpires().after(new Date())) {
            // 토큰이 유효하면 재설정 페이지를 반환
            model.addAttribute("token", token); // 토큰을 폼에 전달
            return "reset-password"; // reset-password.html을 반환
        } else {
            // 토큰이 유효하지 않으면 에러 페이지 반환
            return "error"; // error.html을 반환
        }
    }
}
