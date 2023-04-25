package com.cos.blog.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;

import com.cos.blog.model.User;

// DAO
// Bean 등록 -> 스프링 IOC에서 객체를 가지고 있는지
// 자동으로 Bean이 등록이 된다
// @Repository -> 생략이 가능함 
public interface UserRepository extends JpaRepository<User, Integer>{
	
	//디테일서비스에서 사용할 메서드 만들기
	// SELECT * FROM user WHERE username = ?
	Optional<User> findByUsername(String username); //By뒤 WHERE 오기때문에 대문자로 명시
	
	
// JpaRepository -> 해당 User 테이블을 관리함 pk-> 인티저 타입
	//findAll() 이라는 함수를 들고 있음 -> User 테이블이 갖고 있는 모든 행을 리턴
	// 기본 CRUD를 만들 필요가 없음 -> 모두 갖고 있음 (함수를)
	
	//로그인을 위한 메소드
	//JPA Naming 쿼리 전략
	//실제로는 존재하지 않는 함수이지만 -> 내가 만든 것(이름)
	// select * from user where (findby 뒤에 있는- 대문자) username = ? and pasword = ?;
	//User findByUsernameAndPassword(String username, String password);
	// ▲ 시큐리티 이후로 사용 안 함
	//매개변수가 각 ?에 할당 됨
	
	//다른 방법 - 네이티브쿼리 -> JPA는 SQL을 직접 사용할 수 있는 기능을 지원 
//	@Query(value = "SELECT * FROM user WHERE username = ? AND password = ?", nativeQuery = true)
//	User login(String username, String password);
	
	//QueryDSL -> JPA 표준은 아니며 오픈소스로 현재 스프링 데이터 프로젝트가 지원할 저어도로 기대되는 중
	//다양한 이유르 JPQL을 사용할 수 없을 때 JPA는 SQL을 직접 사용할 수 있는 기능을 제공하는데 이것을 네이티브 SQL이라 한다. 
	//JPQL을 사용하면 JPA가 SQL을 생성하지만, 네이티브 SQL은 개발자가 직접 이 SQL을 정의하는 것이다
	
}
