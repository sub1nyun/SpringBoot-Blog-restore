package com.cos.blog.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import dto.ResponseDto;

//어디에서든 익셉션 발생시 오기 위해서
@ControllerAdvice 
@RestController
public class GlobalExceptionHandler {

	/*
	 * //익셉션 처리시 실행시킬 것
	 * 
	 * @ExceptionHandler(value = IllegalArgumentException.class) //해당 익셉션 발생시 e 에
	 * 전달해줌 public String handleArgumentException(IllegalArgumentException e) {
	 * return "<h1>" + e.getMessage() + "</h1>";
	 */
	
	/*	//익셉션 처리시 실행시킬 것
		@ExceptionHandler(value = Exception.class) //해당 익셉션 발생시 e 에 전달해줌
		public String handleArgumentException(Exception e) {
			return "<h1>" + e.getMessage() + "</h1>";	*/
	
		@ExceptionHandler(value = Exception.class)
		public ResponseDto<String> handleArgumentException(Exception e) {
		return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()); // 500
	} // INTERNAL_SERVER_ERROR --> 500 에러
}
