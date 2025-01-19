package com.acon.weatherapp.model;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class User {
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]{5,20}$", message = "아이디는 5~20자의 영문, 숫자로 구성되어야 합니다.")
    private String userId;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,20}$",
            message = "8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요." )
    private String password;
    @NotBlank(message = "비밀번호 확인을 입력해주세요")
    private String confirmPassword;
    @Email(message = "유효한 이메일 주소를 입력해주세요.") // 이메일 형식 검사 어노테이션 , 엄격하지 않음  , @ 앞 도메인 확인 , @ 확인 , @ 뒤 도메인 확인
    @Nullable // 이메일은 선택적 빈값도 허용 // 값이 있다면 유효성 검사 진행
    private String email;
    @NotBlank(message = "이름을 입력하세요")
    private String name;
    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", message = "생년월일은 8자리 숫자 형식으로 입력해야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String birth;

    @NotBlank(message = "성별을 입력하세요")
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