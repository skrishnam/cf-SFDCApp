package io.pivotal.sfdc.controller;



import io.pivotal.sfdc.domain.Account;
import io.pivotal.sfdc.domain.AccountList;
import io.pivotal.sfdc.domain.Contact;
import io.pivotal.sfdc.domain.Opportunity;

import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class ServiceController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);
	@Autowired
	@LoadBalanced
	private RestTemplate restTemplate;
	@Value("${sfdc.authservice.endpoint}")
    private String authserviceEP;
	@Value("${sfdc.accountservice.endpoint}")
    private String accountserviceEP;
	@Value("${sfdc.contactservice.endpoint}")
    private String contactserviceEP;
	@Value("${sfdc.opportunityservice.endpoint}")
    private String opportunityserviceEP;
	@Value("${sfdc.service.unavailable}")
	private String unavailable;

    private static String ACCESS_TOKEN = "access_token";

    private static String INSTANCE_URL = "instance_url";
	
    @Autowired
	private StringRedisTemplate redisTemplate;

    final ObjectMapper mapper = new ObjectMapper();

   @RequestMapping(value = "/oauth2", method = RequestMethod.GET)
    public @ResponseBody String getoauth2() {
    	return restTemplate.getForObject(authserviceEP+"/oauth2", String.class);
    }

   @HystrixCommand(fallbackMethod = "getContactsByAccountsFallback")
   @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public @ResponseBody String getContactsByAccounts() {
    	return restTemplate.getForObject(accountserviceEP+"/accounts", String.class);
    }

   public @ResponseBody String getContactsByAccountsFallback() {
		List<Account> result = null;
		String jsonDataStr = null;
		try {
			result = ((AccountList)retrieve("/accounts", AccountList.class)).getAccounts();
			StringWriter jsonData = new StringWriter();
			mapper.writeValue(jsonData, result);
			jsonDataStr = jsonData.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
   	return jsonDataStr;
   }

   @HystrixCommand(fallbackMethod = "getOpportunitesByAccountsFallback")
   @RequestMapping(value = "/opp_by_accts", method = RequestMethod.GET)
    public @ResponseBody String getOpportunitiesByAccounts() {
    	return restTemplate.getForObject(accountserviceEP+"/opp_by_accts", String.class);
    }

   public @ResponseBody String getOpportunitesByAccountsFallback() {
		List<Account> result = null;
		String jsonDataStr = null;
		try {
			result = ((AccountList)retrieve("/opp_by_accts", AccountList.class)).getAccounts();
			StringWriter jsonData = new StringWriter();
			mapper.writeValue(jsonData, result);
			jsonDataStr = jsonData.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
   	return jsonDataStr;
   }

   @RequestMapping(value = "/account/{id}", method={RequestMethod.POST, RequestMethod.PUT})
	public @ResponseBody String cuAccount(@PathVariable("id") final String accountId, @RequestBody final Account account, UriComponentsBuilder builder) {
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
		String method = request.getMethod().toLowerCase();
		String result = unavailable;
		switch (method) {
		case "put":
			restTemplate.put(accountserviceEP+"/account/"+accountId, account);
			try {
				mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				StringWriter jsonData = new StringWriter();
				mapper.writeValue(jsonData, account);
				result = jsonData.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			result = restTemplate.postForObject(accountserviceEP+"/account/"+accountId, account, String.class);
			break;
		}
		return result;
	}
	
	@RequestMapping(value = "/account/{id}", method={RequestMethod.GET, RequestMethod.DELETE})
	public @ResponseBody String rdAccount(@PathVariable("id") final String accountId, UriComponentsBuilder builder) {
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
		String method = request.getMethod().toLowerCase();
		String result = unavailable;
		switch (method) {
		case "delete":
			restTemplate.delete(accountserviceEP+"/account/"+accountId);
			try {
				Account account = new Account();
				account.setId(accountId);
				mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				StringWriter jsonData = new StringWriter();
				mapper.writeValue(jsonData, account);
				result = jsonData.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			result = restTemplate.getForObject(accountserviceEP+"/account/"+accountId, String.class);
			break;
		}
		return result;
	}
	
	@RequestMapping(value = "/contact/{id}", method={RequestMethod.POST, RequestMethod.PUT})
	public @ResponseBody String cuContact(@PathVariable("id") final String contactId, @RequestBody final Contact contact, UriComponentsBuilder builder) {
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
		String method = request.getMethod().toLowerCase();
		String result = unavailable;
		switch (method) {
		case "put":
			restTemplate.put(contactserviceEP+"/contact/"+contactId, contact);
			try {
				mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				StringWriter jsonData = new StringWriter();
				mapper.writeValue(jsonData, contact);
				result = jsonData.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			result = restTemplate.postForObject(contactserviceEP+"/contact/"+contactId, contact, String.class);
			break;
		}
		return result;
	}
	
	@RequestMapping(value = "/contact/{id}", method={RequestMethod.GET, RequestMethod.DELETE})
	public @ResponseBody String rdContact(@PathVariable("id") final String contactId, UriComponentsBuilder builder) {
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
		String method = request.getMethod().toLowerCase();
		String result = unavailable;
		switch (method) {
		case "delete":
			restTemplate.delete(contactserviceEP+"/contact/"+contactId);
			try {
				Contact contact = new Contact();
				contact.setId(contactId);
				mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				StringWriter jsonData = new StringWriter();
				mapper.writeValue(jsonData, contact);
				result = jsonData.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			result = restTemplate.getForObject(contactserviceEP+"/contact/"+contactId, String.class);
			break;
		}
		return result;
	}
	
	@RequestMapping(value = "/opportunity/{id}", method={RequestMethod.POST, RequestMethod.PUT})
	public @ResponseBody String cuOpportunity(@PathVariable("id") final String opportunityId, @RequestBody final Opportunity opportunity, UriComponentsBuilder builder) {
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
		String method = request.getMethod().toLowerCase();
		String result = unavailable;
		switch (method) {
		case "put":
			restTemplate.put(opportunityserviceEP+"/opportunity/"+opportunityId, opportunity);
			try {
				mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				StringWriter jsonData = new StringWriter();
				mapper.writeValue(jsonData, opportunity);
				result = jsonData.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			result = restTemplate.postForObject(opportunityserviceEP+"/opportunity/"+opportunityId, opportunity, String.class);
			break;
		}
		return result;
	}

	@RequestMapping(value = "/opportunity/{id}", method={RequestMethod.GET, RequestMethod.DELETE})
	public @ResponseBody String rdOpportunity(@PathVariable("id") final String opportunityId, UriComponentsBuilder builder) {
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
		String method = request.getMethod().toLowerCase();
		String result = unavailable;
		switch (method) {
		case "delete":
			restTemplate.delete(opportunityserviceEP+"/opportunity/"+opportunityId);
			try {
				Opportunity opportunity = new Opportunity();
				opportunity.setId(opportunityId);
				mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				StringWriter jsonData = new StringWriter();
				mapper.writeValue(jsonData, opportunity);
				result = jsonData.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			result = restTemplate.getForObject(opportunityserviceEP+"/opportunity/"+opportunityId, String.class);
			break;
		}
		return result;
	}

	private Object retrieve(String key, Class classType) throws Exception {
		logger.debug("key: "+key);
		String jsonDataStr = this.redisTemplate.opsForValue().get(key);
        logger.debug("value: "+jsonDataStr);
		Object obj = mapper.readValue(jsonDataStr, classType);
		
		return obj;
	}
}
