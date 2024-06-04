package com.green.web.domain.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import com.green.web.domain.dto.PostDTO;


//@Mapper: Mybatis 프록시 생성: 동적 프록시 객체로 생성: Mybatis  프록시 매커니즘을 통해 내부적으로 생성-스피링과 통합해서 빈으로 등록한다
@Mapper //:@MapperScan("")사용하지 않을 경우에는 Mybatis 매퍼인터페이스를 스프링빈으로 등록하기 위해 명시적으로 설정 
public interface PostMapper {

	
	List<PostDTO> findAll();

	
	//returnType int : DML실행에 적용된 행수를 리턴
	int save(PostDTO dto);
	
	

}

//구현체는 직접만들지 않지만 mapper xml 파일(또는 @어노테이션 적용)은 생성해야한다.
//그래서 mapper xml을 구현클래스처럼 생각합시다.
