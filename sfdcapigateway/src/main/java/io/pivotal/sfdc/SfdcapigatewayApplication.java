package io.pivotal.sfdc;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@Controller
@EnableZuulProxy
public class SfdcapigatewayApplication {

    public static void main(String[] args) {
    	new SpringApplicationBuilder(SfdcapigatewayApplication.class).web(true).run(args);
    }
}
