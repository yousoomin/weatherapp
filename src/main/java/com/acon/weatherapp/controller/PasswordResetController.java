package com.acon.weatherapp.controller;

import com.acon.weatherapp.mapper.UserMapper;
import com.acon.weatherapp.model.User;
import com.acon.weatherapp.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Controller
@RequestMapping("/api/password")
public class PasswordResetController {
    private UserMapper userMapper;
    private EmailService emailService;

    public PasswordResetController(UserMapper userMapper, EmailService emailService) {
        this.userMapper = userMapper;
        this.emailService = emailService;

    }

    @PostMapping("/reset")
    @ResponseBody
    public ResponseEntity<String> resetPasswordEmail(@RequestBody Map<String, String> request) throws MessagingException {
        String email = request.get("email");
        Optional<User> user = userMapper.findByEmail(email);
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString().substring(0,8); // 랜덤 토큰 생성
            User existingUser = user.get();
            userMapper.updatePasswordTokenAndExpired(token , new Date(System.currentTimeMillis() + 3600000)  , existingUser.getUserId());
            emailService.sendResetPasswordEmail(email, token); // 이메일 전송
            return ResponseEntity.ok("비밀번호 재설정 링크가 이메일로 전송되었습니다.");
        } else {
            return ResponseEntity.status(404).body("이메일이 존재하지 않습니다. 확인 후 다시 입력해주세요");
        }
    }

    // 비밀번호 재설정 링크를 클릭했을 때
    @GetMapping("/reset/{token}")
    public String resetPasswordPage(@PathVariable("token") String token, Model model) {
        // 유저의 이메일로 보낸 토큰이 db에 있는 토큰과 비교
        Optional<User> user = userMapper.findByResetPasswordToken(token);

        // 보낸 토큰과 같은 토큰을 가진 회원이 있고 , 토큰이 유효하다면 비밀번호 재설정 페이지 반환
        if (user.isPresent() && user.get().getResetPasswordExpires().after(new Date())) {
            model.addAttribute("token", token);
            return "user/reset-password";
        } else {
            // 토큰이 유효하지 않으면 에러 페이지 반환 (1시간을 넘겨서 재설정 링크를 클릭했다면)
            return "user/error";
        }
    }

    // 비밀번호 재설정 요청시
    @PostMapping("/reset/{token}")
    public String resetPassword(@PathVariable("token") String token ,@RequestParam String password) throws MessagingException {
        Optional<User> user = userMapper.findByResetPasswordToken(token);
        if (user.isPresent() && user.get().getResetPasswordExpires().after(new Date())) {
            userMapper.updatePassword(password, user.get().getResetPasswordToken());
            return "user/login";
        }
        else{
            return "user/error";
        }
    }

}
