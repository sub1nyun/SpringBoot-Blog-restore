package com.cos.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cos.blog.config.auth.PrincipalDetailService;

//설정 파일
// 빈 등록 : 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
@Configuration 
// 해당 시큐리티가 모든 요청을 가로챔 
@EnableWebSecurity // 시큐리티 필터가 등록이 된다. -> 해당 설정을  SecurityConfig 진행
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 의미
// 위 세가지 어노테이션은 주로 세트로 사용함
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalDetailService principalDetailService;
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	

	//시큐리티가 가지고 있는 메소드 
	@Bean // IOC 가능 -> return 값을 스프링이 관리
	public BCryptPasswordEncoder encoderPWD() {
		//해당 비밀번호를 암호화하여 돌려줌 
		//String encPassword =  new BCryptPasswordEncoder().encode("1234");
		return new BCryptPasswordEncoder();
	}
	
	// 시큐리티가 대신 로그인을 해주는데 password를 가로채기
	// 해당 password가 어떤 값으로 해쉬화 되어 회원가입이 되었는지 알아야
	// 같은 해쉬로 암호화하여 DB의 해쉬와 비교를 할 수 있음
	// @Override -> 버전 지원 안 해서 지움
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encoderPWD());
		// 해당 서비스를 통해서 로그인을 할때 패스워드를 인코딩하여 알아서 비교 후 처리해줌
	}
	
	
	//기존
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() // csrf 토큰 비활성화 (테스트시 걸어두는 것이 좋음)
			.authorizeHttpRequests() //요청이 들어오면
				.antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**", "/dummy/**") // /auth/~ 로 들어오는 건 누구나 접속 가능
				.permitAll()
				.anyRequest() //다른 모든 요청은
				.authenticated() //인증이 되어야 한다 -> 여기까지 하면 서버 요청시 /auth/~ 제외 모두 튕겨냄
			.and() // 로그인 화면 만들기
				.formLogin()
				.loginPage("/auth/loginForm") // 인증이 필요한 곳으로 요청이 오면 보낼 곳(인증이 되지 않은 요청은 로그인 폼으로)
				.loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인을 해준다.
				.defaultSuccessUrl("/"); // 로그인이 정상으로 요청이 되면 이동하는 경로
				// .failureUrl("/fali"); 로그인 실패시 경로 
		
		// 사용자가 입력한 username과 password를 가로채서 로그인을 하기 위해서
		// 만들어야할 Class가 존재함 -> UserDetails 타입의 User 오브젝트가 필요함
		// 로그인 요청후 세션에 등록을 해줄때 User 오브젝트를 등록이 불가능함 -> 타입이 맞지 않음 -> UserDetails 타입이 필요함
	}
	
	
	/*
	// 업데이트 방식 
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.formLogin().disable()
				.httpBasic().disable()
				.apply(new MyCustomDsl()) // 커스텀 필터 등록
				.and()
				.authorizeRequests(authroize -> authroize.antMatchers("/api/v1/user/**")
						.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
						.antMatchers("/api/v1/manager/**")
						.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
						.antMatchers("/api/v1/admin/**")
						.access("hasRole('ROLE_ADMIN')")
						.anyRequest().permitAll())
				.build();
	}
	*/
	
	// 로그인 진행 순서 정리
	// 로그인 요청 ->1. loginProcessingUrl("/auth/loginProc") 주소 요청을 가로챔
	// 2. 가로챈 username과 password 정보를 -> loadUserByUsername 던짐(username)
	// 3. username을 통해 DB조회 후 리턴할때 ->configure
	// 4. userDetailsService(principalDetailService) -> 로그인 요청
	// 5. passwordEncoder(encoderPWD()); 사용자가 적은 패스워드를 암호화 후 DB와 비교
	// 6. 데이터가 모두 정상이라면 스프링시큐리티 세션에 PrincipalDetail로 감싸서 저장됨
	
	// 01-03 이론 복습 시작
	
	
	
}
