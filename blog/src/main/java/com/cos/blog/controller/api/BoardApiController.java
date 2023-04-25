package com.cos.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.BoardService;
import com.cos.blog.service.UserService;

import dto.ReplySaveRequestDto;
import dto.ResponseDto;

@RestController
public class BoardApiController {

	@Autowired
	private BoardService boardService;
	
	//board는 title과 content만 가지고 있음 -> User 오브젝트 넘김
	@PostMapping("/api/board")
	public  ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal) {
		boardService.글쓰기(board, principal.getUser()); //글을 쓴 사람을 알아야함
		// 정상적이라면 응답 해줌 
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
		//응답이 오면 Board.js에서 / 페이지로 이동

	}

	@DeleteMapping("/api/board/{id}")
	public ResponseDto<Integer> deleteById(@PathVariable int id, @AuthenticationPrincipal PrincipalDetail principal) {
		boardService.글삭제하기(id, principal);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
		// OK -> 200 , 1 리턴 시 데이터 정상
	}
	
	@PutMapping("/api/board/{id}")
	public ResponseDto<Integer> update(@PathVariable int id, @RequestBody Board board) {
		// 바디 데이터는 insert하거나 update 할 것이고
		// 주소 (id)는 db의 where로 사용할 것
		boardService.글수정하기(id, board);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PostMapping("/api/board/{boardId}/reply")
	public ResponseDto<Integer> replySave(@RequestBody ReplySaveRequestDto replySaveRequestDto) {
		//1 번 방식 사용
		// @PathVariable int boardId, @RequestBody Reply reply, @AuthenticationPrincipal PrincipalDetail principal
		// boardService.댓글쓰기(principal.getUser(), boardId, reply);

		//데이터를 받을 때 컨트롤러에서 Dto를 만들어서 받는것이 좋음 (한번에)
		// 2번째 방식 
		boardService.댓글쓰기(replySaveRequestDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/board/{boardId}/reply/{replyId}")
	public ResponseDto<Integer> replyDelete(@PathVariable int replyId) {
		boardService.댓글삭제(replyId);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	
	
}
