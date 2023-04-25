package com.cos.blog.test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

 // 스프링이 com.cos.blog 패키지 이하를 스캔해서 모든 파일을 메모리에 new 하는 것은 아님
 // 특정 어노테이션이 붙어있는 클래스 파일들을 new해서(IOC) 스프링 컨테이너에 관리해줌
@RestController
public class BlogControllerTest {

	//http://localhost:8080/test/hello
	@GetMapping("/test/hello")
	public String hello() {
		return "<h1>hello Spring boot</h1>";
	}
	
	
	//@Autowired 는 자료형을 기본으로 IOC 컨테이너에 있는것을 인젝션 해줌
	//식별이 모호할 경우 -> 변수명을 사용하나
	//bean의 id로 변수명을 결정하는 것은 문제가 있음 -> Qualifier()를 사용함
	
	//private 필드에 어떻게 바인딩이 되는가 -> 기본 생성자를 호출하면서 인젝션
	//오버로드 생성자의 경우 메게변수가 여러개가 들어올 수 있어서 Qualifier가 오류
	//-> 변수 앞에 직접 입력이 가능하게 설정되어있음 
	//@Autowired(required = false) -> bean이 없어도 작동 됨 -> 기본생성자
	//Component -> mvc를 구성하고 있는 코드들의 구성
	//Controller 컴포넌트 Service '' , Repository ~~ -> 역할을 부여하는 것과 같음
	//이름이 특화 되었음 --> 객체화 하고자 하는 클래스의 역할을 명시 -> 받아드리기 좋음

	//설정을 위한 자바 파일임을 명시 -> 	@Configuration
	//@ComponentScan("패키지명") 2가지 라면 -> {"명", "명"} 으로 가능
	//@Bean -> public ~~~ ~~~() return new ~~~~(); -> 객체를 직접 만드는 것
	//Ioc -> 프로그램에서 공유할 수 있는 객체를 모아두는 것 @Bean -> 만든 객체를 컨테이너에 담아줌
	//@Bean 어노테이션을 달면 함수명이 아닌 bean의 id로 생각을 해야함 -> 생성하는 객체의 이름
	//@Bean -> 스프링이 호출함
	
}
