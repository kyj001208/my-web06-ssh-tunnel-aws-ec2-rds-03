package com.green.web.service;

import org.springframework.ui.Model;

import com.green.web.domain.dto.PostDTO;

public interface PostService { //인터페이스 서비스클래스의 부모역할

	void findAllProcess(Model model);// 추상메서드-미완성 코드(바디가 없으므로 오버라이드 필요)

	void saveProcess(PostDTO dto);

	
	

}
