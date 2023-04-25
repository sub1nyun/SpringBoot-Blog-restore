package com.cos.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;

import dto.ResponseDto;

@RestController
public class UserApiController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	// AuthenticationManger로 만들면 UserDetailService가 
	// username으로 db에 질의해서 있는지 확인후 있으면 그걸 토대로 비밀번호 암호화 후 
	//	그 암호화된 비밀번호까지 db 질의를 통해 확인 한 뒤 시큐리티 컨텍스트에 넣는거

	@Autowired
	private UserService userService;
	//@Autowired
	//private HttpSession session; //Session 객체는 스프링이 Bean으로 등록해서 가지고 있음
	
	@PostMapping("/auth/joinProc")
	public  ResponseDto<Integer> save(@RequestBody User user) { // username, password, email
		//username password email만 존재함 -> 나머지는 알아서 들어감
		System.out.println("save 호출 확인");
		userService.회원가입(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
		//실제로 DB에 insert를 하고 아래에서 return이  되면 됨
		// 자바 오브젝트를 JSON으로 변환해서 리턴 -> Jackson 라이브러리가 처리함
		//1  자리에는 나중에 DB에서 리턴 된 결과 넣을 것
	}
	
	// 로그인 요청은 시큐리티가 가로채게 만들 것이라 메소드 만들지 않음
	
	/*
	// 스프링 시큐리티를 이용해서 로그인 할 예정
	 //전통적인 로그인 방식
	@PostMapping("/api/user/login")
	public ResponseDto<Integer> login(@RequestBody User user, HttpSession session){
		System.out.println("loing 호출 확인");
		User principal =  userService.로그인(user); //(principal (접근주체)
		
		if(principal != null) {
			session.setAttribute("principal", principal); //세선 완성
		}
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
		//정상작동
		
	}
	pom.xml에서 시큐리티 시작 전까지 사용한 로그인 방식
	*/
	//시큐리티 라이브러리가 설치가 되면 홈페이지 어느 곳에 접근하더라도 
	//시큐리티가 가로채고 login 페이지로 이동시킴
	//ID는 'user'이고 password는 콘솔창에 출력되는 password를 사용하여 로그인
	//로그인 한다면 Session이 생성이 됨(자동)
	// 
	
	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user) { // @RequestBody가 없다면 key=value 타입만 
		userService.회원수정(user);
		// 여기서는 트랜잭션이 종료되기 때문에 DB의 값은 변경이 됐음
		// 하지만 세션의 값이 변경되지 않았기에 반영이 되지 않음 -> 직접 세션값 변경하기
		
		//세션 등록
		//Authentication 객체를 만들면서 세션에 등록
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		

		return new ResponseDto<Integer> (HttpStatus.OK.value(), 1);
	}
	
	
	
}
