package com.green.web.service.impl;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.green.web.domain.dto.PostDTO;
import com.green.web.domain.mapper.PostMapper;
import com.green.web.service.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor //final 생성자초기화
public class PostServiceProcess implements PostService {

	//dao
	//아키텍쳐 패턴: 컨트롤러 -> 서비스 -> Persistence layer(Data Access=DAO) : SqlSessionFactory	
	//Mybatis에서는 SqlSessionTempate를 테이블별로 접근 가능한 @Mapper 제공
	//풀 생성, 컨피그 제공
	
	private final PostMapper mapper; // xml을 통해 자바 코드마냥 작성 후 mybatis에서 자동으로 진행
	
	@Override //: 오버라이드를 한것을 알려주는 메서드
	public void findAllProcess(Model model) {

		List<PostDTO> result =mapper.findAll();
		model.addAttribute("list", result);
		
	}

	@Override
	public void saveProcess(PostDTO dto) {
		
		//update,delete,insert문은 기본적으로 실행 후 변경된 행수를 리턴해준다.
		int result=mapper.save(dto);
		log.debug(result+"개의 포스트 저장완료");
		
	}	
	
}


