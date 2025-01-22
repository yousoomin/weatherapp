package com.acon.weatherapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.acon.weatherapp.model.User;
import com.acon.weatherapp.service.UserService;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/user")
public class LoginController {

	private final UserService userService;

	public LoginController(UserService userService) {
		this.userService = userService;
	}
	
	// 로그인 페이지 보여주기
	@GetMapping("/login")
	public String userLogin() {
		return "user/login";
	}
	
	// 로그인 처리
	@PostMapping("/login")
	public String login(@RequestParam String userId, @RequestParam String password, Model model, HttpSession session) {
		// 로그인 성공 시 홈으로 리다이렉트
		try {
			User user = userService.login(userId, password);
			session.setAttribute("userId", user.getUserId());
			return "redirect:/";
		}
		catch(IllegalArgumentException | NoSuchElementException e){
			model.addAttribute("error", e.getMessage());
			return "user/login";
		}
		catch(Exception e) {
			model.addAttribute("error",e.getMessage());
			return "user/login";
		}
	}
	
	// 로그아웃 처리
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "redirect:/user/login";
	}
	
	// 아이디 찾기 페이지 보여주기
	@GetMapping("/find-id")
	public String userFindIdForm() {
		return "user/find-id";
	}
	
	// 아이디 찾기
	
	// 비밀번호 찾기 페이지 보여주기
	@GetMapping("/find-password")
	public String userFindPasswordForm() {
		return "/user/find-password";
	}
	
	// 비밀번호 찾기 처리
	@PostMapping("/find-password")
	public String userFindPassword(@RequestParam String userId, Model model) {
		// 비밀번호 재설정 로직 실행
		try {
			userService.findPasswordUser(userId);
			model.addAttribute("message", "비밀번호 재설정 SMS가 발송되었습니다.");
		}
		catch(IllegalArgumentException | NoSuchElementException e) {
			model.addAttribute("error", "입력하신 아이디를 찾을 수 없습니다.");
		}
		catch(Exception e) {
			model.addAttribute("error","다시 시도해주세요.");
		}
		return "/user/find-password";
	}
}

