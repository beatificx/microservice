package org.beatific.microservice.container.discovery;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DiscoveryExecutor<T> {

	final Class<T> typeParameterClass;
	
	public DiscoveryExecutor(Class<T> typeParameterClass) {
		this.typeParameterClass = typeParameterClass;
	}

	private LoadBalancerClient loadBalancer;
	private DiscoveryClient discoveryClient;

	private RestOperations restTemplate = new RestTemplate();
	private AsyncRestOperations aRestTemplate = new AsyncRestTemplate();

	private URI getServiceUrl(String serviceName) {
		URI uri = null;
		ServiceInstance instance = loadBalancer.choose(serviceName);

		if (instance == null) {
			log.debug("Can't find a service with serviceName = {}", serviceName);
			throw new RuntimeException("Can't find a service with serviceName = " + serviceName);
		}

		uri = instance.getUri();
		return uri;
	}

	public List<ServiceInstance> getServices(String serviceName) {
		return discoveryClient.getInstances(serviceName);
	}
	
	public List<String> getServiceUrls(String serviceName) {
		List<String> urls = new ArrayList<String>();
		
		discoveryClient.getInstances(serviceName).forEach((ServiceInstance instance) -> {
			urls.add(instance.getUri().toString());
		});
		
		return urls;
	}

	public T execute(String serviceName) {
		URI url = getServiceUrl(serviceName);
		return restTemplate.getForObject(url, typeParameterClass);
	}

	public T execute(String serviceName, Object[] paramArrayOfObject) {
		URI url = getServiceUrl(serviceName);
		return restTemplate.getForObject(url.toString(), typeParameterClass, paramArrayOfObject);
	}

	public T execute(String serviceName, Map<String, ?> paramMap) {
		URI url = getServiceUrl(serviceName);
		return restTemplate.getForObject(url.toString(), typeParameterClass, paramMap);
	}

	public List<T> multiExecute(String serviceName) {

		List<T> resultList = new ArrayList<T>();

		for (ServiceInstance instance : getServices(serviceName)) {
			ListenableFuture<ResponseEntity<T>> future = aRestTemplate.getForEntity(instance.getUri().toString(),
					typeParameterClass);
			future.addCallback(result -> {
				resultList.add(result.getBody());
			}, ex -> {
			});
		}

		return resultList;
	}

	public List<T> multiExecute(String serviceName, Object[] paramArrayOfObject) {

		List<T> resultList = new ArrayList<T>();

		for (ServiceInstance instance : getServices(serviceName)) {
			ListenableFuture<ResponseEntity<T>> future = aRestTemplate.getForEntity(instance.getUri().toString(),
					typeParameterClass, paramArrayOfObject);
			future.addCallback(result -> {
				resultList.add(result.getBody());
			}, ex -> {
			});
		}

		return resultList;
	}

	public List<T> multiExecute(String serviceName, Map<String, ?> paramMap) {

		List<T> resultList = new ArrayList<T>();

		for (ServiceInstance instance : getServices(serviceName)) {
			ListenableFuture<ResponseEntity<T>> future = aRestTemplate.getForEntity(instance.getUri().toString(),
					typeParameterClass, paramMap);
			future.addCallback(result -> {
				resultList.add(result.getBody());
			}, ex -> {
			});
		}

		return resultList;
	}

	public List<T> multiExecute(String serviceName, String path) {

		List<T> resultList = new ArrayList<T>();

		for (ServiceInstance instance : getServices(serviceName)) {
			ListenableFuture<ResponseEntity<T>> future = aRestTemplate.getForEntity(instance.getUri().toString() + path,
					typeParameterClass);
			future.addCallback(result -> {
				resultList.add(result.getBody());
			}, ex -> {
				System.out.println();
			});
		}

		return resultList;
	}

	public List<T> multiExecute(String serviceName, String path, Object[] paramArrayOfObject) {

		List<T> resultList = new ArrayList<T>();

		for (ServiceInstance instance : getServices(serviceName)) {
			ListenableFuture<ResponseEntity<T>> future = aRestTemplate.getForEntity(instance.getUri().toString() + path,
					typeParameterClass, paramArrayOfObject);
			future.addCallback(result -> {
				resultList.add(result.getBody());
			}, ex -> {
			});
		}

		return resultList;
	}

	public List<T> multiExecute(String serviceName, String path, Map<String, ?> paramMap) {

		List<T> resultList = new ArrayList<T>();

		for (ServiceInstance instance : getServices(serviceName)) {
			ListenableFuture<ResponseEntity<T>> future = aRestTemplate.getForEntity(instance.getUri().toString() + path,
					typeParameterClass, paramMap);
			future.addCallback(result -> {
				resultList.add(result.getBody());
			}, ex -> {
			});
		}

		return resultList;
	}
	
	public List<T> multiExecute(List<String> urls, String path) {

		List<T> resultList = new ArrayList<T>();

		for (String url : urls) {
			ListenableFuture<ResponseEntity<T>> future = aRestTemplate.getForEntity(url + path,
					typeParameterClass);
			future.addCallback(result -> {
				resultList.add(result.getBody());
			}, ex -> {
			});
		}

		return resultList;
	}

	public List<T> multiExecute(List<String> urls, String path, Object[] paramArrayOfObject) {

		List<T> resultList = new ArrayList<T>();

		for (String url : urls) {
			ListenableFuture<ResponseEntity<T>> future = aRestTemplate.getForEntity(url + path,
					typeParameterClass, paramArrayOfObject);
			future.addCallback(result -> {
				resultList.add(result.getBody());
			}, ex -> {
			});
		}

		return resultList;
	}

	public List<T> multiExecute(List<String> urls, String path, Map<String, ?> paramMap) {

		List<T> resultList = new ArrayList<T>();

		for (String url : urls) {
			ListenableFuture<ResponseEntity<T>> future = aRestTemplate.getForEntity(url + path,
					typeParameterClass, paramMap);
			future.addCallback(result -> {
				resultList.add(result.getBody());
			}, ex -> {
			});
		}

		return resultList;
	}
}
