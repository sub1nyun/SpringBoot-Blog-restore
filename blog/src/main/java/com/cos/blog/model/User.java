package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data //get & setter
@NoArgsConstructor // 빈생성자
@AllArgsConstructor // 전체 생성자
@Builder // 빌더 패턴b
//ORM -> JAVA , 다른언어 포함 Object -> 테이블로 매핑해주는 기술
@Entity // Table화 시키기 위한 어노테이션 
//user 클래스가 프로젝트 시작시 자동으로 필드를 읽어서 MySQL에 테이블이 생성이 됨
// @DynamicInsert // insert 쿼리 작동시 null 인 필드를 제외 해줌 (JPA에서)

public class User {	
	@Id //PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라감 -> auto_increment
	//IDENTITY -> 기본 키 생성을 DB에 위임하는 전략 주로 MySQL에서 사용 -> AUTO_INCREMENT
	private int id; // 오라클 시퀀스 개념 -> auto_increment
	
	@Column(nullable = false, length = 30, unique = true) //null 불가 30글자 이상 불가
	private String username; // 계정 아이디
	
	@Column(nullable = false, length = 100) // 들어오는 비밀번호를 해쉬화 => 암호화 할 예정(넉넉히 잡아둠)
	private String password;
	
	@Column(nullable = false, length = 50)
	private String email;
	
	
	//@ColumnDefault("'user'") // ' '->안에 써줘야함 띄어쓰기 x
	// DB는 RoleType이라는 형태가 없음
	@Enumerated(EnumType.STRING) //해당 ENUM이 String인것을 알려줌
	private RoleType role; // Enum을 쓰는 게 좋음 -> 데이터의 도메인(어떤 범위)을 만들어 줄 수 있음 -> 권한
	//RoleType -> 선언해놓은 타입을 강제함
	
	@CreationTimestamp //시간을 자동 입력
	private Timestamp createDate; //Timestamp 타입
}
