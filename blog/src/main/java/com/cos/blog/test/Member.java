package com.cos.blog.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
@Data // get&setter 동시에
//@AllArgsConstructor //전체 생성자
//@RequiredArgsConstructor //final이 붙은 것에대한 생성자를 만들어줌 
@NoArgsConstructor //비어있는 생성자
public class Member {
	private int id;
	private String username;
	private String password;
	private String email;
	
	@Builder
	public Member(int id, String username, String password, String email) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}	
	
	
}
