package org.beatific.microservice.composite.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@RestController
@Slf4j
public class CompositeService {
	
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
	
	@HystrixCommand(fallbackMethod = "defaultProductAndPoint")
	@RequestMapping("/get")
	 public ProductAndPoint getProductAndPoint() {

		String prodUrl = getServiceUrl("product-service").toString();
		prodUrl = prodUrl + "/get";
        log.info("GetProduct from URL: {}", prodUrl);

		Product product = restTemplate.getForObject(prodUrl, Product.class);
		log.info("str : {}" , product);
		
		String pointUrl = getServiceUrl("point-service").toString();
		pointUrl = pointUrl + "/get";
        log.info("GetPoint from URL: {}", pointUrl);
        
        Point point = restTemplate.getForObject(pointUrl, Point.class);
        
        ProductAndPoint pap = new ProductAndPoint(product.getId(), product.getName(), product.getPrice(), point.getId(), point.getName(), point.getPoint());

        return pap;
    }
	
	public ProductAndPoint defaultProductAndPoint() {
		return new ProductAndPoint(2L, "product", 100L, 3L, "kks", 100L);
	}
}
