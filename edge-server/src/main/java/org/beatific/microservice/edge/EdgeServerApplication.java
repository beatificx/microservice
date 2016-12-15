package org.beatific.microservice.edge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableDiscoveryClient
@SpringBootApplication
@EnableZuulProxy
//@EnableOAuth2Sso
//public class EdgeServerApplication extends WebSecurityConfigurerAdapter {
//	public static void main(String[] args) {
//		SpringApplication.run(EdgeServerApplication.class, args);
//	}
//	
//	@Override
//	public void configure(HttpSecurity http) throws Exception {
//		http
//			.authorizeRequests()
//				.antMatchers("/index.html", "/home.html", "/", "/login").permitAll()
//				.anyRequest().authenticated()
//				.and()
//			.csrf()
//				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//			.and()
//			.logout().deleteCookies("XSRF-TOKEN").deleteCookies("JSESSIONID").invalidateHttpSession(true);
//	}
//}

public class EdgeServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EdgeServerApplication.class, args);
	}
}