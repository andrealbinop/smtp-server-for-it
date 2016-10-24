
package com.github.andreptb.smtpserverforit;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class ApplicationBootstrap {

	@Bean
	EmbeddedServletContainerFactory servletContainer(@Value("${server.ajp.port:8009}") int ajpPort) {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
		Connector ajpConnector = new Connector("AJP/1.3");
		ajpConnector.setPort(ajpPort);
		tomcat.addAdditionalTomcatConnectors(ajpConnector);
		return tomcat;
	}


	public static void main(String[] args) {
		SpringApplication.run(ApplicationBootstrap.class, args);
	}
}
