package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

@RestController //페이지 이동이 아닌 데이터 리턴 응답용
public class DummyControllerTest {
	
	@Autowired //DummyControllerTest가 메모리에 뜰때 같이 메모리에 띄워줌
	// 스프링이 관리하고 있는 객체가 있다면 할당 해주라는 의미 -> 의존성 주입(DI)
	private UserRepository userepository; 
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		//받아온 id가 없다면 void라 후처리가 위험함
		try {
			userepository.deleteById(id); //return이 없음
		} catch (EmptyResultDataAccessException e) { //노데이터 익셉션
			//최고 부모인 Exception을 사용해도 됨
			return "해당 id가 없습니다. 삭제 실패";
		}
		return "삭제 완료";
	}
	
	
	//email, password
	//매핑이 같아도 get, put을 알아서 구분함
	//JSON data 받기 위해 Body태그 추가
	@Transactional // 함수 종료시에 자동 commit이 됨 -> 변경감지 -> DB 수정 쿼리 전달 -> 더티체킹
	//더티체킹 -> 상태 변경 검사로
	//JPA에서는 트랜잭션이 끝나는 시점에 변화가 있는 모든 엔티티 객체를 DB에 반여ㅕㅇ
	//그렇기 때문에 값 변경 후 save()를 하지 않더라도 DB에 반영이 됨
	
	//영속성 컨텍스트란 서버와 DB 사이에 존재함
	//JPA는 엔티티를 영속성 컨텍스트에 보관할 때, 최초 상태를 복사해서 저장해둠
	//트랜잭션이 끝나고 flush할 때 스냅샷과 현재 엔티티를 비교하여 변경된 엔티티를 찾아냄
	//JPA는 변경된 엔티티를 DB에 반영하여 한번에 쿼리문을 전송해줌 
	@PutMapping("/dummy/user/{id}")
	public User upadateUser(@PathVariable int id, @RequestBody User requestUser) {
		//JSON 데이터 요청 -> JAVA Object로 변환하여 받아줌
		//MessageConverter의 Jackson 라이브러리가 변환해서 받아줌
		//이때 필요한 어노테이션이 RequestBody 
		System.out.println("id :" + id);
		System.out.println("password :" + requestUser.getPassword());
		System.out.println("email : " + requestUser.getEmail());
		
		//save는 insert시 사용하는 메서드
//		requestUser.setId(id); -> 넘어온 데이터가 없어서 null임
	//	requestUser.setUsername("ssar");

		//save를 통해서 update를 해줄때는
		//id를 통해서 해당 user를 찾음 (findByid)
		//못 찾을 수 있으니 orElseThrow
		//자바는 함수를 파싱할 수 없어서 파라미터에 메소드를 넣는게 안 됨 
		//람다식의 등장으로 함수를 바로 넣을 수 있음
		User user  = userepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였수다");
		}); 
		//실제 db에서 받은 데이터 -> 조회해서 왔기때문에 null이 없음
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		//save시 만약 해당 id가 db에 있다면, update를 해주는데 없는 데이터는 null로 변함
		//1. 번 방법 : 
		//userepository.save(user);
		//save 함수는 id를 전달하지 않으면 insert를 해주고
		// save 함수를 id를 전달하면 해당 id에 대한 데이터가 있다면 update
		// save 함수를 id를 전달하면 해당 id에 대한 데이터가 없다면 insert
		
		//@Transactional 을  통한 '더티 체킹'
		//db에서 셀렉를 통해 받아온 객체에 값을 변경하고
		//트랙젝션 어노테이션 사용하면 업데이트가 된다
		//save를 사용하지 않더라도 update - > 더티 체킹
		return user;
	}
	
	//여러건 받기 
	//http://localhost:8000/blog/dummy/user
	@GetMapping("/dummy/users")
	public List<User> list() {
		return userepository.findAll(); //전체 리턴 
	}
	
	// 한 페이지당 2건의 데이터를 리턴받아 보기 -> 페이징
	// 스프링부트의 경우 페이징 편함
	@GetMapping("/dummy/user")
	//페이징 기본 전략
	// 2건을 가져와서 sort= id로 id를 최신순으로 정렬
	public List<User> pageList(@PageableDefault(size=2, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
		//Page<User> users =  userepository.findAll(pageable);
		//List<User> users =  userepository.findAll(pageable).getContent();
		//페이지 정보 없이 컨텐츠만 가져올 것이라 변경함
		//가장 좋은 방법?
		//페이지 정보를 모두 받아와서
		Page<User> pagingUser =  userepository.findAll(pageable);
		//컨텐츠 정보만 따로 넘김
		
//		if(pagingUser.isLast()) {
//			//분기점 가능
//		}
		
		List<User> users = pagingUser.getContent();
		return users;
	}
	
	
	//{id} 주소로 파라메터를 전달 받을 수 있음
	// http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		// user/4 를 찾으면 -> DB에서 못 찾을 시 user가 null이 됨
		// 해당 return이 null 이 되기 때문에 -> 문제 발생
		// Optional로 User 객체를 감싸서 가져오니 null 인지 아닌지 판단해서 return
		
		/*
		// 비선호 방법 
		//.get() -> null이 들어올 일이 없다 
		//.orElseGet() -> 데이터가 없으면 객체를 하나 만들어서 넣어줌
		User user =  userepository.findById(id).orElseGet(new Supplier<User>() {
			//new Supplier 인터페이스가 들고 있는 메소드 오버라이드
			// 인터페이스 -> new 불가함 -> new 하려면 익명클래스
			@Override
			public User get() {
				//비어 있는 객체를 user에 넣어줌 => null은 아님
				return new User();
			}
		});
		*/
		
		/*
		// 람다식 
		//타입 신경 안 쓰고 익명으로 처리가 가능함
		User user2 = userepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("이거는 람다식으로");
		}); //익명클래스는 뒤에 ; 해줘야함 
		*/
		
		//추천 방법
		// Supplier -> 인터페이스에 T(Type) get() -> 추상메서드 들어있음
		//추상메서드 -> 매개변수 x, 단순히 무엇인가 반환 함
		User user = userepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 유저 없음 id : " + id);
			}
		});
		//AOP 개념 -> 스프링에서 Exception이 발생하면 가로채서 에러 페이지로 이동
		// 요청은 -> 웹브라우저 -> RestController기 때문에 html이 아님-> data 리턴하는 컨트롤러
		// user 객체는 = 자바 오브젝트 -> 웹 브라우저는 이해 할 수 없음(html)
		// user 객체를 변환해줘야함 -> 웹 브라우저가 이해할 수 있는 데이터 -> json  ( 이전의 경우 -> Gson 라이브러리)
		// 스프링부트 = MessageConverter라는 애가 응답시에 자동 작동함
		// 만약 자바 오브젝트를 리턴 시 -> MessageConverter가 Jackson 라이브러리 호출
		// user 오브젝트를 json으로 변환하여 브라우저에게 전달함
		return user;
	}

	@PostMapping("/dummy/join") //insert 할것이니 Post
	public String join(User user) { // key = value (약속된 규칙)
		System.out.println("username : " + user.getUsername());
		System.out.println("password : " + user.getPassword());
		System.out.println("email : " + user.getEmail());
		
		// user.setRole("user"); -> 실수 할 수 있음
		user.setRole(RoleType.USER); //RoleType 중 고를 수 있도록 유도
		
		//id의 경우 auto_increment가 적용됨 
		//createDate -> 현재 시간을 넣어서 데이터베이스에 넣어줌 --> 자바에서 만듬
		
		userepository.save(user);
		return "회원가입 완료";
	}
}
