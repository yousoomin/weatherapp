package com.acon.weatherapp.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import com.acon.weatherapp.model.User;
import com.acon.weatherapp.service.UserService;

import java.util.*;

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

	@PostMapping("/validateUserId")
	@ResponseBody
	public ResponseEntity<Map<String,String>> validateUserId(@RequestBody Map<String,String> req){
		String userId = req.get("userId");
		Map<String, String> response = new HashMap<>();
		User user = userService.getUser(userId);
		if(user != null){
			response.put("status" , "unavailable");
		}else {
			response.put("status" , "available");
		}
		return ResponseEntity.ok(response);
	}

	// 회원가입 처리
	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<?> userRegister(@RequestBody @Valid User user, Errors errors) {
		System.out.println(user.getUserId());
		if (errors.hasErrors()) {
			// 에러 필드와 메시지를 함께 전송
			return ResponseEntity.badRequest().body(errors.getFieldErrors());
		}
		try{
			userService.registerUser(user);
			return ResponseEntity.ok("success");
		}catch (IllegalArgumentException | NoSuchElementException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
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

	// 프로필 수정 폼 보여주기
	@GetMapping("/update")
	public String userUpdateForm(HttpSession session, Model model) {
		String userId = (String) session.getAttribute("userId");

		 if (userId == null) {
		        return "redirect:/user/login";
		    }
		    try {
		        User user = userService.getUser(userId) ;
		        model.addAttribute("user", user);
		        return "user/update";
		    }
		    catch (NoSuchElementException e) {
		        return "redirect:/error";
		    }
	}

	// 프로필 수정 처리
	@PostMapping("/update")
	public String userUpdate(@ModelAttribute User userDto, HttpSession session, Model model) {
		String userId = (String) session.getAttribute("userId");

	    if (userId == null) {
	        throw new IllegalStateException("로그인 상태가 아닙니다.");
	    }
	    try {
	        userService.updateUser(userId, userDto);

	        model.addAttribute("message", "프로필이 성공적으로 업데이트되었습니다.");
	        return "redirect:/user/profile";
	    }
	    // 사용자 정보 없음
	    catch (NoSuchElementException e) {
	        model.addAttribute("error", e.getMessage());
	        return "redirect:/user/update";
	    }
	    // 잘못된 입력 데이터
	    catch (IllegalArgumentException e) {
	        model.addAttribute("error", e.getMessage());
	        return "redirect:/user/update";
	    }
	}

	// 회원 탈퇴
	@GetMapping("/delete")
	public String userDeleteForm(HttpSession session, Model model) {
		String userId = (String) session.getAttribute("userId");

		if(userId == null) {
			return "redirect:/user/login";
		}

		User user = userService.getUser(userId);
		model.addAttribute("user", user);
		return"user/delete";
	}

	// 회원 탈퇴 처리
	@PostMapping("/delete")
	public String userDelete(@RequestParam String password, Model model, HttpSession session) {
		String userId = (String) session.getAttribute("userId");

		if(userId == null) {
			return "redirect:/user/login";
		}
		try {
			userService.deleteUser(password, session);

			//세션 무효화
			session.invalidate();

			model.addAttribute("message", "회원 탈퇴가 완료되었습니다.");
			return "redirect:/user/login";
		}
		catch(IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
			return "user/profile";
		}
	}
	
	//주소검색 팝업연결
	@GetMapping("/addrFind")
	public String addrFind() {
		return "user/address";
	}


}

