package com.cos.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import com.cos.blog.repository.UserRepository;

import dto.ReplySaveRequestDto;

//스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌 -> IOC를 해준다(메모리에 대신)
@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	@Autowired 
	private ReplyRepository replyRepository;
	
	// 누가 작성했는지 user 정보가 필요
	
	@Transactional //회원가입 전체가 하나의 트랜잭션으로 묶임
	public void 글쓰기(Board board, User user) { // title, content 
			board.setCount(0); // 조회수 강제로 넣음
			board.setUser(user);
			boardRepository.save(board); //저장 후 -> BoardApiController
	}

	@Transactional(readOnly = true) //select만
	public Page<Board> 글목록(Pageable pageable) {
		return boardRepository.findAll(pageable); // 페이지 정보 리턴
		//전체 글목록 리턴
		/*
		Page<Board> testBoardList = boardRepository.findAll(pageable);
		List<Board> tt = testBoardList.getContent(); // 현재 페이지에서 조회한 데이터 넣기
		return tt; // 페이지 정보 리턴
		*/
	}

	@Transactional(readOnly = true) //select만
	public Board 글상세보기(int id) {
		return boardRepository.findById(id).orElseThrow(()-> {
			//findById를 통해서 Board를 받아오면 Reply가 있음
			return new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
		});
	}

	@Transactional
	public void 삭제(int id) {
		boardRepository.deleteById(id);
	}
	
	// 권한 
	@Transactional
    public void 글삭제하기(int id, PrincipalDetail principal) {
        Board board = boardRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("글 찾기 실패 : 해당 글이 존재하지 않습니다.");
        });

        if (board.getUser().getId() != principal.getUser().getId()) {
            throw new IllegalStateException("글 삭제 실패 : 해당 글을 삭제할 권한이 없습니다.");
        }
        boardRepository.delete(board);
}

	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		Board board = boardRepository.findById(id) 
				.orElseThrow(() -> {
					return new IllegalArgumentException("글 찾기 실패 : 해당 글이 존재하지 않습니다.");
				}); //영속화 완료
		// 1. 영속성 컨텍스트에 board가 들어옴 -> DB의 board 테이블데이터와 동일하게 동기화 되어 있음
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		// 해당 함수 종료시에 -> Service가 종료될 때 트랜잭션이 종료됨
		// 이때 더티체킹이 일어남 -> 자동 업데이트 = > Flush (Commit)
	}
	// findAll() -> 옵셔널이 아닌 컬렉션을 new 해서 가져오기에 사이즈가 0 이지 null X
	// findById() -> 오브젝트 리턴 -> 못 찾을시 null을 리턴하기에 옵셔널로 null 처리ㅣ
	
/*
	@Transactional
	public void 댓글쓰기(User user, int boardId, Reply requestReply) {
		// save할 것인데 댓글(reply)를 완성해서 저장할 것
		requestReply.setUser(user);
		
		// 영속화 과정
		Board board =  boardRepository.findById(boardId).orElseThrow(()-> {
			return new IllegalArgumentException("댓글 작성 실패");
		});
		requestReply.setBoard(board);
		
		// 빌더 타입 사용해서 넣기

		Reply reply = Reply.builder()
				.user(user)
				.board(board)
				.content(requestReply.getContent())
				.build();
	
		
		replyRepository.save(requestReply);	
	}
	*/
	
	@Transactional
	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
		int result = replyRepository.nativeSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());
		//System.out.println(reply); // 오브젝트를 출력하게되면 자동으로 toString()이 호출됨
	}
	
	@Transactional
	public void 댓글삭제(int replyId) {
		replyRepository.deleteById(replyId);
	}
	
	
	
	
	// 무한 참조 예시
	//@RestController 
	@GetMapping("/test/board/{id}") 
	public Board getBoard(@PathVariable int id) {
		return boardRepository.findById(id).get();
		//jackson 라이브러리 (오브젝트를 json으로 리턴) => 모델을 getter를 호출
		//Board의 필드를 get() 하여 모두 가지고오는데
		//Reply에서 Board를 get() -> 또 다시 Board에서 Reply를 get()이 반복 됨
	}
	
	// Reply를 다이렉트로 요청하면 -> Board와  User를 가지고옴
	// 그 안의 Board의 Reply 에서는 무시됨
	@GetMapping("test/reply")
	public List<Reply> getReply() {
		return replyRepository.findAll();
	}




	
}
