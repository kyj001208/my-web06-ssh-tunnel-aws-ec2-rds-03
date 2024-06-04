package com.green.web.domain.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;


//ORM: 관계형데이터베이스의 데이터를 JAVA의 객체로 매핑하기 위한 클래스
//테이블의 컬럼명과 필드명이 일치하도록 설계한다.
//단 map-underscore-to-camel-case=true가 적용되어 있으면 카멜로 선언 

@Getter//HTML에서 데이터를 표현하려면 필요함 
@Setter //데이터 바인딩하기 위해 -view에서 넘어올때 컨트롤러
public class PostDTO {
	
	private long no;
	private String title;
	private String content;
	private int readCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	

}
