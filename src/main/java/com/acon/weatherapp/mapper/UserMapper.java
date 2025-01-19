package com.acon.weatherapp.mapper;

import org.apache.ibatis.annotations.*;

import com.acon.weatherapp.model.User;

import java.util.List;


@Mapper
public interface UserMapper {


//회원가입
  @Insert("INSERT INTO users (userid, name, birth, password, gender, address, phone, role, email) " +
           "VALUES (#{userId}, #{name}, #{birth}, #{password}, #{gender}, #{address}, #{phone}, #{role}, #{email})")
   void save(User user);
   
   //id로 해당 유저 있는지 찾기
   @Select("SELECT * FROM users WHERE userid = #{userId}")
   User findByUserId(String userId);
  
   // users인 사람 전체 확인
   @Select("SELECT * FROM users")
   List<User> findAll();
   
   // 수정
   @Update("UPDATE users SET name= #{name} , phone = #{phone}, address= #{address} WHERE userid = #{userId}")
   void update(User user);
   
   // 삭제
   @Delete("DELETE FROM users WHERE userid = #{userId}")
   void delete(String userId);

}