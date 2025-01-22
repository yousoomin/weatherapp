package com.acon.weatherapp.service;

import org.springframework.stereotype.Service;

// 가상의 SMS API 
@Service
public class SmsService {
	public static void sendSMS(String phone, String message) {
		// 실제로는 SMS 전송을 위한 API를 호출하는 코드가 있어야 함
		System.out.println("sending SMS to" + phone + ": " + message);
	}
}
