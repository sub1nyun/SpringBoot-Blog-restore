package com.cos.blog.service;

import javax.persistence.Persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

//스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌 -> IOC를 해준다(메모리에 대신)
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	

	
	/*
	@Transactional //회원가입 전체가 하나의 트랜잭션으로 묶임
	public int 회원가입(User user) {
		try {
			//정상 가입
			String rawPassword = user.getPassword(); //원문
			String encPassword = encoder.encode(rawPassword); //해쉬
			user.setPassword(encPassword);
			userRepository.save(user);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("UserService : 회원가입() "+e.getMessage());
		}
		return -1;
	}
	*/
	@Transactional 
		public void 회원가입(User user) {
		String rawPassword = user.getPassword(); // 1234 원문
		String encPassword = encoder.encode(rawPassword); // 해쉬 변경
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}

	@Transactional
	public void 회원수정(User user) {
		// 수정시에는 영속성 컨텍스트 User 오브젝트를 영속화를 시키고, 
		// 영속화된 User 오브젝트를 수정
		// select를 해서 User 오브젝트를 DB로부터 가져오는 이유는 영속화를 하기 위해서
		// 영속화된 오브젝트를 변경하면 자동으로 DB에 update문을 날려주기때문
		User persistance = userRepository.findById(user.getId()).orElseThrow(()->{
			return new IllegalArgumentException("회원 찾기 실패");
		});
		//정상적으로 user를 찾았다면
		// 가장먼저 패스워드를 받음 -> 암호화 해서 넣음
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		persistance.setPassword(encPassword);
		persistance.setEmail(user.getEmail());
		// 회원수정 함수 종료 시 = 서비스가 종료 -> 트랜잭션이 종료 -> Commit 이 자동
		// 영속화된 persistance 객체가 변화가 감지되면(더티체킹) 변화가 된 것을 update문을 날려줌

	}
	

	/* 시큐리티 이후로 사용 안 함
	 * @Transactional(readOnly = true) //Select 할 때 트랜잭션이 시작 됨 -> 서비스 종료시에 트랜잭션 종료
	 * (정합성 유지) //Select 를 여러번 하더라도 같은 데이터가 조회
	 *  public User 로그인(User user) { 
	 *  return userRepository.findByUsernameAndPassword(user.getUsername(),
	 * user.getPassword()); }
	 */
	
	// 01-01 스프링 작동 원리 이론 복습
	// 1. 톰켓이 시작되면 필터와 디스패처가 메모리에 뜨게 됨
	// 2. 세션, DB 등 한 번만 메모리에 띄우면 되는 것을 먼저 
	// 3. 사용자가 요청 -> 컨트롤러, 서비스 JPA, 영속성 컨텍스트 메모리에 뜸
	
	

}
