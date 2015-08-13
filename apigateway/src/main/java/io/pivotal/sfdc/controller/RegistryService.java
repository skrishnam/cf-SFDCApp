package io.pivotal.sfdc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;


public class RegistryService {

    private static final Logger LOG = LoggerFactory.getLogger(RegistryService.class);

	@Autowired
	private DiscoveryClient discoveryClient;    
    
    public String getServiceUrl(String serviceId, String fallbackUri) {
        String uri = null;
        try {
        	LOG.debug("discoveryClient null?? '{}'",discoveryClient == null);
        	InstanceInfo instance = discoveryClient.getNextServerFromEureka(serviceId, false);
            uri = instance.getHomePageUrl();
            LOG.debug("Resolved serviceId '{}' to URL '{}'.", serviceId, uri);

        } catch (RuntimeException e) {
            // Eureka not available
            uri = fallbackUri;
            LOG.error("Failed to resolve serviceId '{}'. Fallback to URL '{}'.", serviceId, uri);
       }

        return uri;
    }

}