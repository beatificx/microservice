package org.beatific.microservice.monitor.service.request;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

public class RequestFilter implements Filter  {

	@Autowired
	private RequestHolder holder;
	
	private final static String EXCEPTION = "monitorRequest";
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		
		if(!((HttpServletRequest)request).getRequestURL().toString().matches(EXCEPTION)) {
			holder.hold();
			StatusServletResponse statusResponse = new StatusServletResponse((HttpServletResponse)response);
			filterChain.doFilter(request, statusResponse);
			
			if(statusResponse.getStatus() >= 200 || statusResponse.getStatus() < 300) {
				holder.success();
			} else {
				holder.fail();
			}
		}
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

}

// config 파일에 아래 내용 추가
//@Bean
//public FilterRegistrationBean someFilterRegistration() {
//
//    FilterRegistrationBean registration = new FilterRegistrationBean();
//    registration.setFilter(requestFilter());
//    registration.addUrlPatterns("/*");
//    registration.setName("requestFilter");
//    registration.setOrder(1);
//    return registration;
//} 
//
//@Bean(name = "requestFilter")
//public Filter requestFilter() {
//    return new RequestFilter();
//}