package org.beatific.microservice.composite.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@RestController
@Slf4j
public class CompositeService {
	
	@Autowired
	CompositeIntegrationService integrationService;
	
//    private RestOperations restTemplate = new RestTemplate();
//	
//	@Autowired
//    private LoadBalancerClient loadBalancer;
//	
//	private URI getServiceUrl(String serviceId) {
//		return getServiceUrl(serviceId, null);
//	}
//	
//	private URI getServiceUrl(String serviceId, String fallbackUri) {
//        URI uri = null;
//        try {
//            ServiceInstance instance = loadBalancer.choose(serviceId);
//
//            if (instance == null) {
//                throw new RuntimeException("Can't find a service with serviceId = " + serviceId);
//            }
//
//            uri = instance.getUri();
//            log.debug("Resolved serviceId '{}' to URL '{}'.", serviceId, uri);
//
//        } catch (RuntimeException e) {
//            if (fallbackUri == null) {
//                throw e;
//
//            } else {
//                uri = URI.create(fallbackUri);
//            }
//        }
//
//        return uri;
//    }
	
	@RequestMapping("/")
	 public ProductAndPoint getProductAndPoint(HttpServletRequest request) {

		Point point = integrationService.getPoint(request);
		Product product = integrationService.getProduct(request);
		
        ProductAndPoint pap = new ProductAndPoint(product.getId(), product.getName(), product.getPrice(), point.getId(), point.getName(), point.getPoint());

        return pap;
    }
	
}
