package com.green.web.ssh;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Component
@ConfigurationProperties("spring.ssh.tunnel")
@Profile("ssh") //이 클래스는 ssh 프로파일에서만 활성화 됩니다.
public class SSHTunnellingProperties {
	
	private String sshHost;//ec2주소
	private int sshPort;
	private String username;
	private String privateKey;
	//private int localPort; //random으로 적용해서 제외시킴
	 
	private String rdsHost;
	private int rdsPort;

}