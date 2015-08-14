package io.pivotal.sfdc.zuul.filters.post;

import io.pivotal.sfdc.domain.Account;
import io.pivotal.sfdc.domain.AccountList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.context.RequestContext;

public class FallBackFilter extends SendErrorFilter {

	
	private static final Logger logger = LoggerFactory.getLogger(FallBackFilter.class);

	@Autowired
	private StringRedisTemplate redisTemplate;

    final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public Object run() {
		logger.info("inside filter.run");
		
		try {
			RequestContext ctx = RequestContext.getCurrentContext();
			logger.info("CurrentContext: "+ctx.toString());
			String uri = (String) ctx.get("requestURI");
			if(uri.equalsIgnoreCase("/accounts") || uri.equalsIgnoreCase("/opp_by_accts")) {
				List<Account> result = null;
				String jsonDataStr = null;
				try {
					result = ((AccountList)retrieve(uri, AccountList.class)).getAccounts();
					StringWriter jsonData = new StringWriter();
					mapper.writeValue(jsonData, result);
					jsonDataStr = jsonData.toString();
					writeResponse(jsonDataStr);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
//			RequestDispatcher dispatcher = ctx.getRequest().getRequestDispatcher(arg0)
//			if (!ctx.getResponse().isCommitted()) {
//				dispatcher.forward(ctx.getRequest(), ctx.getResponse());
//			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
//		super.run();
		return null;
	}

	private void writeResponse(String body) throws Exception {
		RequestContext context = RequestContext.getCurrentContext();

		HttpServletResponse servletResponse = context.getResponse();
		servletResponse.setCharacterEncoding("UTF-8");
		OutputStream outStream = servletResponse.getOutputStream();
		try {
				writeResponse(new ByteArrayInputStream(body.getBytes()), outStream);
				return;
		}
		finally {
			try {
				outStream.flush();
				outStream.close();
			}
			catch (IOException ex) {
			}
		}
	}

	private void writeResponse(InputStream zin, OutputStream out) throws Exception {
		byte[] bytes = new byte[1024];
		int bytesRead = -1;
		while ((bytesRead = zin.read(bytes)) != -1) {
			try {
				out.write(bytes, 0, bytesRead);
				out.flush();
			}
			catch (IOException ex) {
				// ignore
			}
			// doubles buffer size if previous read filled it
			if (bytesRead == bytes.length) {
				bytes = new byte[bytes.length * 2];
			}
		}
	}

	private Object retrieve(String key, Class classType) throws Exception {
			logger.debug("key: "+key);
			String jsonDataStr = this.redisTemplate.opsForValue().get(key);
	        logger.debug("value: "+jsonDataStr);
			Object obj = mapper.readValue(jsonDataStr, classType);
			
			return obj;
		}
}
