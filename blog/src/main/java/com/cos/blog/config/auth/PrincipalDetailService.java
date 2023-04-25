package com.cos.blog.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

@Service //Bean 등록
public class PrincipalDetailService implements  UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	// 스프링이 로그인 요청을 가로챌 때, username, password 변수 2개를 가로채는데
	// password 부분 처리는 알아서 처리함
	// username이 DB에 있는지만 확인해주면 처리가 가능 -> loadUserByUsername

	
	// 1. 로그인 진행시 해당 메서드가 자동으로 실행 됨
	// 2. findByUsername으로 DB에서 username을 찾고
	// 3. 찾을 수 없다면 익셉션  (패스워드가 틀리다면 알아서 처리)
	// 4. 찾으면 User 오브젝트 리턴 후 디테일에 담아주고 리턴해줌
	@Override // 오버라이드로 구현하지 않으면 DB의 데이터를 담아 줄 수 없음
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User principal = userRepository.findByUsername(username)
				.orElseThrow(()->{
					return new UsernameNotFoundException("해당 사용자를 찾을 수 없음 " + username);
				});
		return new PrincipalDetail(principal); // 시큐리티의 세션에 유저 정보가 저장이 됨 -> 타입이 UserDetails 여야함
	}

}
