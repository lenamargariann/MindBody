package com.example.mindbodysecuritymodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MindBodyGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindBodyGatewayApplication.class, args);
    }

}
