package com.acon.weatherapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import com.acon.weatherapp.model.User;
import com.acon.weatherapp.service.UserService;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/user")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	// 회원가입 폼 보여주기
	@GetMapping("/register")
	public String userRegisterForm() {
		return "user/register";
	}
	
	// 회원가입 처리
	@PostMapping("/register")
	public String userRegister(User user, Model model){
		try{
			userService.registerUser(user);
		}catch (IllegalArgumentException | NoSuchElementException e){
			model.addAttribute("error", e.getMessage());
			return "user/register";
		} catch (Exception e){
			model.addAttribute("error", "회원가입에 실패하였습니다.");
			return "user/register";
		}
		 // user 가입 등록 처리
		return "redirect:/user/login";
	}

	@GetMapping("/profile")
	public String userProfile(HttpSession session , Model model	) {
		if(session.getAttribute("userId") == null) {
			return "redirect:/user/login";
		}
		String userId = (String) session.getAttribute("userId");
		User user =  userService.getUser(userId);
		model.addAttribute("user", user);
		return "user/profile";
	}


	
}

