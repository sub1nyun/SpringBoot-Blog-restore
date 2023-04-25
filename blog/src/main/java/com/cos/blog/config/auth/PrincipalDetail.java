package com.cos.blog.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.blog.model.User;

import lombok.Getter;

@Getter
public class PrincipalDetail implements UserDetails{
	
	//user가 널이기때문에 생성자 만듬
	public PrincipalDetail(User user) {
		this.user = user;
	}

	private User user; // (상속이 아닌)객체를 품고 있음 -> 콤포지션

	// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면
	// UserDetails 타입의 오브젝트를 시큐리티의 고유한 세션저장소에 저장해줌
	// UserDetails 타입의 PrincipalDetail Class가 저장됨
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}
	
	
	

	// 계정이 만료되지 않았는지를 리턴함 (ture: 만료 안 됨) 
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정이 잠겨져 있는지를 리턴 true : 잠기지 않음
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호가 만료되지 않았는지를 리턴함 (true : 만료 안 ㄷ됨)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정 활성화 여부 리턴
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	//가장 밑으로 내렸음
	// 계정의 권한을 리턴함 -> 계정이 갖고있는 권한이 무엇인지
	// 계정의 권한이 여러개 있을 수 있어서 루프를 돌아야 하기때문에 컬렉션
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//interface List<E> extends Collection<E> -> ArrayList 컬렉션 타입임
		Collection<GrantedAuthority> collectors = new ArrayList<>();
//		collectors.add(new GrantedAuthority() {
			
//			@Override
//			public String getAuthority() { //추상메서드
//				return "ROLE_"+user.getRole(); // 스프링에서 ROLE을 받을때의 규칙 -> ROLE_USER
//			}               //▲ 꼭 명시
//		}); //익명 클래스 생성
		
		//람다식 표현 현재 add 안에 들어올 메서드가 하나 뿐임
		collectors.add(()->{return "ROLE_"+user.getRole();});
		
		return collectors;
	}


}
