package com.acon.weatherapp.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class User {
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]{5,20}$", message = "아이디는 5~20자의 영문, 숫자로 구성되어야 합니다.")
    private String userId;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,20}$",
            message = "비밀번호는 8자 이상, 20자 이하로 소문자, 대문자, 숫자, 특수문자가 포함되어야 합니다." )
    private String password;
    @NotBlank(message = "비밀번호 확인을 입력해주세요")
    private String confirmPassword;
    @NotBlank(message = "이름을 입력하세요")
    private String name;
    @NotNull(message = "생년월일을 입력하세요")
    private LocalDate birth;
    @NotNull(message = "성별을 입력하세요")
    private String gender;
    @NotBlank(message = "주소를 입력하세요")
    private String address;
    @NotBlank(message = "핸드폰 번호를 입력하세요")
    private String phone;
    private String role;
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