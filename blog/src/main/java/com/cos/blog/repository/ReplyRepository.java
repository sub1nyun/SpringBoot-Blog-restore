package com.cos.blog.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cos.blog.model.Reply;

import dto.ReplySaveRequestDto;

@Repository //생략해도 Bean 등록은 됨
public interface ReplyRepository extends JpaRepository<Reply, Integer>{
	
	//인터페이스는 public 없어도 생성 가능
	
	
	@Modifying // int만 리턴 할 수 있음
	// 어떤 쿼리가 작동해야 하는지 알려줘야함
	// 해당 쿼리 사용시 영속화 해줄 필요가 없어짐
	@Query(value="INSERT INTO reply(userId, boardId, content, createDate) VALUES(?1, ?2, ?3, now())", nativeQuery = true)
	// DTO와 순서를 맞춰서 쿼리 작성
	int nativeSave(int userId, int boardId, String content);
	// 업데이트된 행의 갯수를 리턴해줌 
	// -1 리턴은 오류
	
	

}
