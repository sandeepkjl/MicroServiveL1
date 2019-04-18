package com.wipro.RegisterServiceDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@EnableHystrixDashboard
public class RegisterServiceDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegisterServiceDemoApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }

}
