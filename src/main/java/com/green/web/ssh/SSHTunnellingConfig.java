package com.green.web.ssh;

import java.util.Random;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
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
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

@Slf4j //Simple Logging Facade for Java : 이클래스에서 log를 사용할께요
@Configuration
//@RequiredArgsConstructor// final 필드를 매개변수로 반영된 생성자
@Profile("ssh")  //이 클래스는 ssh 프로파일에서만 활성화 됩니다.
public class SSHTunnellingConfig {
	
	//Spring Boot에서 ApplicationContext 객체는 스프링 애플리케이션의 중앙 인터페이스로, 
	//애플리케이션의 모든 구성 요소(빈)를 관리하고 애플리케이션의 설정 정보를 제공하는 컨테이너
	//스코프는 기본적으로 **싱글톤(Singleton)**입니다.애플리케이션이 시작될 때 하나의 ApplicationContext 객체가 생성되고, 
	//애플리케이션이 종료될 때까지 해당 객체가 유지된다는 의미
	private final ApplicationContext application;//생성자 DI
	//스프링 애플리케이션을 시작할 때 ApplicationContext는 설정 파일을 읽고, 
	//빈을 생성 및 초기화하며, 애플리케이션의 실행을 준비합니다. 
	//ApplicationContext는 여러 종류가 있으며, 그 중 대표적인 것은 
	//AnnotationConfigApplicationContext, 
	//XmlWebApplicationContext 등이 있습니다. 
	//스프링 부트에서는 주로 AnnotationConfigApplicationContext를 사용하여 애플리케이션을 설정합니다.
	
	public SSHTunnellingConfig(ApplicationContext application) {
		this.application = application;
		System.out.println(">>> : ssh SSHTunnellingConfig");
	}
	
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	HikariConfig hikariConfig() {
		return new HikariConfig();
	}
	@Bean
	DataSource dataSource(SSHTunnellingProperties sshTunnellingProperties) throws JSchException {
		JSch jsch=new JSch();
		jsch.addIdentity(sshTunnellingProperties.getPrivateKey());
		
		Session sshsession=jsch.getSession(
				sshTunnellingProperties.getUsername(),
				sshTunnellingProperties.getSshHost(),
				sshTunnellingProperties.getSshPort());
		
		sshsession.setConfig("StrictHostKeyChecking", "no");
		//서버 측에 등록된 HOst가 아니어도 엄격하게 확인하지 않도록 한것
		sshsession.connect();
		log.info("ssh-tunnel(EC2) 접속완료! ");
		
		//정보수정
				
		Random ran=new Random();
		int lport=ran.nextInt(100)+3301;
		System.out.println("lport:"+lport);
		int localPort=sshsession.setPortForwardingL(
				lport, 
				sshTunnellingProperties.getRdsHost(), 
				sshTunnellingProperties.getRdsPort());
		
		HikariConfig hikariConfig=hikariConfig();
		hikariConfig.setJdbcUrl(hikariConfig.getJdbcUrl().replace("[LOCAL_PORT]", String.valueOf(localPort)));
		
		//hikariConfig.setMaximumPoolSize(10);
		//hikariConfig.setMaxLifetime(150000);
		// mariadb 파라미터- wait_timeout=180초; 3분
		// MaxLifetime - wait_timeout 타입보다 약간작게 2분 30초 
		
		System.out.println("MaximumPoolSize:"+hikariConfig.getMaximumPoolSize());
		System.out.println("MaxLifetime:"+hikariConfig.getMaxLifetime());
		
		return new HikariDataSource(hikariConfig);
	}
	//Resource... resource=null; //매개변수에서만 허용하는 문법
	@Bean
	SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		
		
		
		SqlSessionFactoryBean factoryBean=new SqlSessionFactoryBean();
		//1.datasource
		factoryBean.setDataSource(dataSource);
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