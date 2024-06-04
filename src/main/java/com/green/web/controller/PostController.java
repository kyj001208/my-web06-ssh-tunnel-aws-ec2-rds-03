package com.green.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.green.web.domain.dto.PostDTO;
import com.green.web.service.PostService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class PostController {
	
	//생성자 DI : 서비스 객체 = private final PostService service=new PostService()
	private final PostService service; //인터페이스로 만들고 클래스와 상속관계가 좋음
	
	//생성자인덱션
	
	@GetMapping("/posts")
	public String list(Model model) {//requestScope-HttpServletRequest
		
		service.findAllProcess(model);
		return "views/post/list";
	}
	
	
	//글쓰기 기능 처리(포스트내용 모두 읽어오기-db 조회)
	@PostMapping("/posts") //데이터를 저장하기 위해서 요청하는경우 PostMapping으로 사용
	public String save(PostDTO dto) { //데이터 바인딩을 위해 setter 필요 , 파라미터 이름과 필드 이름과 동일해야지만 적용(Sette 메서드를 통해서)
		
		service.saveProcess(dto);
		
		return "redirect:/posts";
		//HttpServletResponse respose
	}
	
	@GetMapping("/posts/new")
	public String write() {//requestScope-HttpServletRequest
		return "views/post/write";
	}
	



	

	
}
