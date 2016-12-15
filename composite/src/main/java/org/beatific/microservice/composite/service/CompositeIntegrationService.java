package org.beatific.microservice.composite.service;

import java.net.URI;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CompositeIntegrationService {

    private RestOperations restTemplate = new RestTemplate();
	
	@Autowired
    private LoadBalancerClient loadBalancer;
	
	private URI getServiceUrl(String serviceId) {
		return getServiceUrl(serviceId, null);
	}
	
	private URI getServiceUrl(String serviceId, String fallbackUri) {
        URI uri = null;
        try {
            ServiceInstance instance = loadBalancer.choose(serviceId);

            if (instance == null) {
                throw new RuntimeException("Can't find a service with serviceId = " + serviceId);
            }

            uri = instance.getUri();
            log.debug("Resolved serviceId '{}' to URL '{}'.", serviceId, uri);

        } catch (RuntimeException e) {
            if (fallbackUri == null) {
                throw e;

            } else {
                uri = URI.create(fallbackUri);
            }
        }

        return uri;
    }
	
	private HttpEntity<String> getEntity(HttpServletRequest request) {
		Enumeration<String> headerNames =  request.getHeaderNames();
        HttpHeaders headers = new HttpHeaders();
        while(headerNames.hasMoreElements()) {
        	String headerName = headerNames.nextElement();
        	String headerValue = request.getHeader(headerName);
        	headers.set(headerName, headerValue);
        	log.error("headerName : {}, headerValue : {}", headerName, headerValue);
        }
        return new HttpEntity<String>("parameters", headers);
	}
	
	@HystrixCommand(fallbackMethod = "defaultProduct")
	public Product getProduct(HttpServletRequest request) {
		String prodUrl = getServiceUrl("product-service").toString();
        log.error("GetProduct from URL: {}", prodUrl);

        HttpEntity<String> entity = getEntity(request);
        
        ResponseEntity<Product> productEntity = restTemplate.exchange(prodUrl, HttpMethod.POST, entity, Product.class);
//		Product product = restTemplate.getForObject(prodUrl, Product.class);
        return productEntity.getBody();
	}
	
	@HystrixCommand(fallbackMethod = "defaultPoint")
	public Point getPoint(HttpServletRequest request) {
		String pointUrl = getServiceUrl("point-service").toString();
		log.error("GetPoint from URL: {}", pointUrl);
        
		HttpEntity<String> entity = getEntity(request);
        ResponseEntity<Point> pointEntity = restTemplate.exchange(pointUrl, HttpMethod.POST, entity, Point.class);
        return pointEntity.getBody();
	}
	
	public Product defaultProduct(HttpServletRequest request) {
		return new Product(2L, "product", 100L);
	}
	
	public Point defaultPoint(HttpServletRequest request) {
		return new Point(3L, "user", 100L);
	}
}
