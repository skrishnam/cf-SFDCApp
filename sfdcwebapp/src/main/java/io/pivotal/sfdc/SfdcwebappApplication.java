package io.pivotal.sfdc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
@EnableEurekaClient
public class SfdcwebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(SfdcwebappApplication.class, args);
    }
}
