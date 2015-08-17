package io.pivotal.sfdc;

import io.pivotal.springcloud.ssl.CloudFoundryCertificateTruster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
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
@EnableHystrixDashboard
public class HystrixDashboardApplication {

    public static void main(String[] args) {
    	CloudFoundryCertificateTruster.trustCertificates();
        SpringApplication.run(HystrixDashboardApplication.class, args);
    }
}
