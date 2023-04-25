package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Reply { // 답변 테이블 

	@Id // PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) // DB의 넘버링 사용
	private int id; // 시퀀스 , auto_increment
	
	@Column(nullable = false, length = 200)
	private String content;
	
	//어느 게시글에 있는 답글인지 연관관계가 필요함 
	//하나의 게시글에는 여러개의 답글이 달릴 수 있음 
	@ManyToOne // 여러개의 답변은(Reply)는 하나의(One) 게시글에 달릴 수 있음
	@JoinColumn(name="boardId")
	private Board board;
	
	// 한 명의 유저는 여러개의 답글을 달 수 있음
	//여러개의 답변을 한 명의 유저가 쓸 수 있음
	@ManyToOne //Many -> Reply, One -> User를 의미하는 듯함
	@JoinColumn(name="userId") //누가 작성했는지
	private User user; //User 테이블과 자동 매치후 컬럼을 찾아감
	
	@CreationTimestamp // insert or update시 시간이 자동으로 입력됨
	private Timestamp createDate;

	@Override
	public String toString() {
		return "Reply [id=" + id + ", content=" + content + ", board=" + board + ", user=" + user + ", createDate="
				+ createDate + "]";
	}
	
}
