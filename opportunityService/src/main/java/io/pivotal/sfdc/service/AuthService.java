package io.pivotal.sfdc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.force.api.ApiSession;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class AuthService {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthService.class);
	
    @Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	@LoadBalanced
	private RestTemplate restTemplate;

    private static String ACCESS_TOKEN = "access_token";

    private static String INSTANCE_URL = "instance_url";

	@HystrixCommand(fallbackMethod = "getApiSessionFallback")
	public ApiSession getApiSession() {
		logger.debug("Fetching ApiSession with key: " + ACCESS_TOKEN);
        ApiSession apiSession = null;
    	ValueOperations<String, String> ops = this.redisTemplate.opsForValue();
		if (this.redisTemplate.hasKey(ACCESS_TOKEN)) {
			apiSession = new ApiSession(ops.get(ACCESS_TOKEN),ops.get(INSTANCE_URL));
		} else
			throw new NullPointerException("ApiSession not found!");
    	
        return apiSession;
	}
	
	private ApiSession getApiSessionFallback() {
		logger.debug("Fetching fallback ApiSession from authservice");
		ApiSession apiSession = restTemplate.getForObject("http://authservice/oauth2", ApiSession.class);
		return apiSession;
	}
}
