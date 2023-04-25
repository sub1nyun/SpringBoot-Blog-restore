package com.cos.blog.controller;

import java.lang.ProcessBuilder.Redirect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.service.BoardService;

@Controller
public class BoardController {
	
//	@Autowired
//	private PrincipalDetail principal;
	
	@Autowired
	private BoardService boardService;

	@GetMapping({"", "/"}) //▼ 세션 찾기
							//	@AuthenticationPrincipal PrincipalDetail principal
	public String index(Model model, @PageableDefault(size=3, sort="id", direction = Sort.Direction.DESC) Pageable pageable ) { // 인증이 된 상태 -> 세션을 어떻게 찾는가?
		// /WEB-INF/views/index.jsp
		// Controller기 때문에 리턴시 -> viewResolver가 작동함 -> 모델의 정보를 들고 index로 이동
		// viewResolver는 return 값 index의 앞뒤로 prefix sup~ 붙혀줌
		model.addAttribute("boards", boardService.글목록(pageable));
		return "index";
	}
	
	@GetMapping("/board/{id}")
	public String findById(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.글상세보기(id));
		//넘어온 Board는 Reply를 들고있음 -> 뷰에서 뿌려주면 됨
		return "board/detail";
		
	}
	
	@GetMapping("/board/{id}/updateForm")
	public String updateForm(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/updateForm";
	}
	
	
	//정리 
	//1. 로그인 후 -> saveForm() 호출 -> board/saveForm
	//2. saveForm 페이지에서 title, content 정보를 가지고 -> board.js -> ajax
	//3. title과 content 값을 가지고 -> BoardApiController 이동
	
	// USER 권한이 필요함
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}

	
	
}
