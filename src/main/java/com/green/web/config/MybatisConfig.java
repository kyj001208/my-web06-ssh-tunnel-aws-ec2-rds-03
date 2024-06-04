package com.green.web.config;

import java.util.Random;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

@Slf4j //Simple Logging Facade for Java : 이클래스에서 log를 사용할께요
@Configuration
//@RequiredArgsConstructor// final 필드를 매개변수로 반영된 생성자
@Profile("ec2")  //이 클래스는 prod 프로파일에서만 활성화 됩니다.
//@MapperScan("com.green.web.domain.mapper") //이것을 사용하지 않을 경우 각각 인터페이스에 @Mapper
public class MybatisConfig {
	
	
	private final ApplicationContext application;//생성자 DI
	
	public MybatisConfig(ApplicationContext application) {
		this.application=application;
		log.info("ec2>>>: This is activated in the ec2 profile");
		
	}
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	HikariConfig hikariConfig() {
		System.out.println(">>> : ec2");
		return new HikariConfig();
	}
	
	@Bean
	DataSource dataSource() throws JSchException {
		
		HikariConfig hikariConfig=hikariConfig();
		
		return new HikariDataSource(hikariConfig);
	}
	//Resource... resource=null; //매개변수에서만 허용하는 문법
	@Bean
	SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		
		
		
		SqlSessionFactoryBean factoryBean=new SqlSessionFactoryBean();
		//1.datasource
		factoryBean.setDataSource(dataSource);
		// log.info(dataSource);
		
		//2.Configuration
		factoryBean.setConfiguration(mybatisConfiguration());
		//3.mapper.xml-location patton
		String locationPattern="classpath*:sqlmap/**/*-mapper.xml";
		Resource[] resource=application.getResources(locationPattern); // ... 대신 여러개 집합인 배열로..
		factoryBean.setMapperLocations(resource);
		
		return factoryBean.getObject();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "mybatis.configuration")
	org.apache.ibatis.session.Configuration mybatisConfiguration() {
		return new org.apache.ibatis.session.Configuration();
	}

	@Bean
	SqlSessionTemplate sqlSessionTemplate(DataSource dataSource) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory(dataSource));
	}

}