package io.pivotal.sfdc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.ComponentScan;

/**
 * The Spring configuration and entry point for
 * the application.
 * 
 * @author Jignesh Sheth
 *
 */

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableTurbine
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }
}
