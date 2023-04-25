package com.cos.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
// 사용자가 요청 -> 응답 (Data) 역할 -> @RestController
/// 사용자 요청 -> 응답 (Html) 역할 -> @Controller

//데이터를 응답하는 컨트롤러 만들거임
@RestController
public class HttpController {
	
	private static final String TAG = "HttpController : ";

	@GetMapping("/http/lombok")
	public String lombokTest() { //빌더패턴 순서 안 지켜도 됨
		Member m = new Member.MemberBuilder().username("ssar").password("1234").email("ssar@nate.com").build();
		//Member m = new Member(1, "ssar", "1234", "email"); //모든 생성자 어노테이션이 먹힘
		System.out.println(TAG+"getter : " + m.getId());
		m.setId(5000);
		System.out.println(TAG+"setter : " + m.getId());
		return "lombok Test 완료";
	}
	
	// 인터넷 브라우저 요청은 무조건 get 요청밖에 할 수 없음
	// http://localhost:8080/http/get (select)
	@GetMapping(value = "/http/get") //id=1&username=subin&password=11&email=ssar@nate.com -> Member object에 넣어줌 스프링이
	public String getTest(@RequestParam int id, @RequestParam String username, Member m) {
		return "get 요청" + id + ", " + username + m.getPassword() + ", " + m.getEmail();
	}
	// http://localhost:8080/http/post (insert)
	@PostMapping(value = "/http/post")
	public String postTest(@RequestBody Member m) { // MessageConverter (스프링부트)
		return "post 요청" + m.getId() + m.getUsername() + m.getPassword() + m.getEmail() ;
	}
	// http://localhost:8080/http/put (update)
	@PutMapping(value = "http/put")
	public String putTest(@RequestBody Member m) {
		return "put 요청" + + m.getId() + m.getUsername() + m.getPassword() + m.getEmail();
	}
	// http://localhost:8080/http/delete (delete)
	@DeleteMapping(value = "http/delete")
	public String deleteTest() {
		return "delete 요청";
	}
	
}
