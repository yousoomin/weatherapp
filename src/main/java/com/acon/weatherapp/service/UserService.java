package com.acon.weatherapp.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import com.acon.weatherapp.mapper.UserMapper;
import com.acon.weatherapp.model.User;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // 필드 에러 메소드
    public void filedErrorsHandler(Errors errors , Model model){
        for(FieldError fieldError :  errors.getFieldErrors()) {
            model.addAttribute(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }

    // 회원가입
    public void registerUser(User userDto) {
    	// 비밀번호 검사
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        }

        if (userMapper.findByUserId(userDto.getUserId()) != null) {
            throw new NoSuchElementException("중복된 회원입니다.");
        }
        userDto.setRole("ROLE_USER");
        //사용자 저장
        userMapper.save(userDto);
    }

    // 로그인
    public User login(String userId, String password){
        User user = userMapper.findByUserId(userId);
        if(user == null) {
            throw new NoSuchElementException("존재하지 않는 사용자입니다.");
        }
        if(!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    // 각 회원 조회
    public User getUser(String userId) {
    	return userMapper.findByUserId(userId);
    }

    // 회원 정보 수정
    // userId는 세션에서 받아와서 넘겨주는 걸로 || 아니면 view에서 hidden으로 같이 전송
    public void updateUser(String userId, User userDto) {
        User user = userMapper.findByUserId(userId);
        if(user == null) {
              throw  new NoSuchElementException("사용자를 찾을 수 없습니다.");
        }
        // 업데이트 시에 비밀번호 요구
        if(!user.getPassword().equals(userDto.getPassword())) {
            throw  new IllegalArgumentException("기존 비밀번호가 일치 하지 않습니다.");
        }
            user.setName(userDto.getName());
            user.setAddress(userDto.getAddress());
            user.setPhone(userDto.getPhone());
            userMapper.update(user);
    }
    // 회원삭제
    public void deleteUser(String password , HttpSession session) {
        User user = userMapper.findByUserId(session.getAttribute("userId").toString());
        if(user.getPassword().equals(password)) {
            userMapper.delete(user.getUserId());
        }
    }

    // 모든 회원 조회
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    
}
