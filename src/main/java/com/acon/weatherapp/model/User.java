package com.acon.weatherapp.model;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class User {
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]{5,20}$", message = "아이디 : 영문, 숫자로 5~20자여야 합니다.")
    private String userId;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,20}$",
            message = "비밀번호 : 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요." )
    private String password;
	@NotBlank(message = "비밀번호 : 비밀번호가 일치하지 않습니다.")
    private String confirmPassword;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$" , message = "이메일 : 이메일 형식을 올바르게 입력해주세요")
    @NotBlank(message = "이메일 : 이메일은 필수 값입니다.")
    private String email;
    @NotBlank(message = "이름 : 이름은 한글 2~5자여야 합니다.")
    private String name;
    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", message = "생년월일 : 생년월일을 올바르게 입력해주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String birth;
    @NotBlank(message = "성별 : 성별을 선택해주세요")
    private String gender;
    @Pattern(regexp = "^[가-힣0-9\\s\\-]+$" , message = "주소 : 주소를 검색해주세요")
    private String address;
	@NotBlank(message = "전화번호 : 전화번호는 필수 입력 항목입니다. ")
    private String phone;
    private String role;
    private String resetPasswordToken; // 비밀번호 재설정 토큰
    private Date resetPasswordExpires;
}

/*
CREATE TABLE users(
userid varchar(50) unique not null,
name varchar(20) not null,
birth DATE not null ,
password varchar(20) not null ,
gender varchar(20) not null ,
address varchar(100) ,
phone varchar(20),
role varchar(10) not null
 );
 
 
*/