package com.acon.weatherapp.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {

	private Long id;
	private String userId;
	private String name;
	private LocalDate birth;
	private String password;
	private String confirmPassword;
	private String gender;
	private String address;
	private String phone;
	private String role;

}

/*
CREATE TABLE users(
id int auto_increment primary key,
userid varchar(50) unique not null,
name varchar(20) not null,
birth DATE not null ,
password varchar(20) not null ,
gender varchar(20) not null ,
address varchar(100) ,
phone varchar(20),
create_at timestamp default current_timestamp,
role varchar(10) not null
 );
 
 
*/