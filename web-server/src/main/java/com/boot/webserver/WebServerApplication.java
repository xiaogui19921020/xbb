package com.boot.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
public class WebServerApplication {

    public static void main(String[] args) {
        //System.setProperty("server.port", "7002");
        //System.setProperty("spring.cloud.nacos.discovery.metadata.version", "V1");
        SpringApplication.run(WebServerApplication.class, args);
    }

}
