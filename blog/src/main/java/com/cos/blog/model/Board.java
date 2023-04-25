package com.cos.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity // ORM 명시 어노테이션이 tb 명에 가장 가까운것이 좋음
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
	private int id;
	 
	@Column(nullable = false, length = 100)
	private String title;

	@Lob // 대용량 데이터 
	private String content; // 내용이 아주 길수있음 -> 섬머노트 라이브러리 사용할 예정 <html>태그가 섞여서 디자인 됨
	
	// @ColumnDefault("0") //int 형이기때문에 ''처리 안 함 -> 직접 넣는 것으로 변경
	private int count; // 조회수
	
	// 앞에 오는 것이 Class 명 To 뒤에 오는것이 필드명인듯함
	@ManyToOne(fetch = FetchType.EAGER) // 기본 전략 -> 보드테이블 셀럭트 시 유저 정보는 가져오겠다 (한건이기 때문에)
	// Many = Board, User = One -> 한명의 유저는 여러개의 게시글을 쓸 수 있다는 의미
	@JoinColumn(name="userId") //DB만들어질때는 이 이름을 쓸것임
	private User user; // 작성자 -> DB는 오브젝트를 저장할 수 없음 -> FK, 자바는 오브젝트를 저장할 수 있음
	
	//LAZY -> 건수를 모르니 필요하면 가져오고 필요치 않으면 들고오지 않겠다. (명시 하지 않아도 됨)
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) //mappedBy -> 연관 관계의 주인이 아니다 -> FK 아니다
	//UI 상 댓글을 무조건 가져와야 하기때문에 EAGER 전략을 취함 (본래 LAZY)
	//값을 가져올때 join 을 위해서 필요한 컬럼임
	// 하나의 게시글은 (One - Board) 여러개의 답변을 갖을 수 있음 -> Many - Reply
	@JsonIgnoreProperties({"board"}) // Reply에서 다시 Board를 호출 할 때 무시
	@OrderBy("id desc")
	private List<Reply> replys; // 댓글은 몇 개일지 모름 -> List로 선언
	// FK는 아니나(DB에는 없음) Board를 select 할 때 EAGER 전략이기에 가져옴
	
	// 게시글에 댓글이 연관이 되어있음 --> 게시글을 삭제할때 댓글들을 어떻게 할것인지 정의가 없음
	// cascade (전파속성) -> 게시글을 삭제할때 어떤 옵션을 줄지 지정해야함
	//cascade = CascadeType.REMOVE
	
	@CreationTimestamp // 데이터 변환시 자동으로 값이 들어감
	private Timestamp createDate; //sql꺼
}
