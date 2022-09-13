package com.boot.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class WebServerApplicationV2 {

    public static void main(String[] args) {
        System.setProperty("server.port", "7003");
        System.setProperty("spring.cloud.nacos.discovery.metadata.version", "V2");
        SpringApplication.run(WebServerApplicationV2.class, args);
    }

}
