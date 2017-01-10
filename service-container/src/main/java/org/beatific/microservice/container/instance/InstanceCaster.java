package org.beatific.microservice.container.instance;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import lombok.Setter;

public class InstanceCaster<T> {

    private final Class<T> typeParameterClass;
    
    @Setter
    private InstanceFinder finder;
	
	public InstanceCaster(Class<T> typeParameterClass) {
		this.typeParameterClass = typeParameterClass;
	}
	
	private RestOperations restTemplate = new RestTemplate();
	private AsyncRestOperations aRestTemplate = new AsyncRestTemplate();
	
	public T cast(String serviceName) {
		URI url = finder.getServiceUrl(serviceName);
		return restTemplate.getForObject(url, typeParameterClass);
	}

	public T cast(String serviceName, Object... paramArrayOfObject) {
		URI url = finder.getServiceUrl(serviceName);
		return restTemplate.getForObject(url.toString(), typeParameterClass, paramArrayOfObject);
	}

	public T cast(String serviceName, Map<String, ?> paramMap) {
		URI url = finder.getServiceUrl(serviceName);
		return restTemplate.getForObject(url.toString(), typeParameterClass, paramMap);
	}

	public List<T> multiCast(String serviceName) {

		List<T> resultList = new ArrayList<T>();

		for (ServiceInstance instance : finder.getAllInstaces(serviceName)) {
			ListenableFuture<ResponseEntity<T>> future = aRestTemplate.getForEntity(instance.getUri().toString(),
					typeParameterClass);
			future.addCallback(result -> {
				resultList.add(result.getBody());
			}, ex -> {
			});
		}

		return resultList;
	}

	public List<T> multiCast(String serviceName, Object... paramArrayOfObject) {

		List<T> resultList = new ArrayList<T>();

		for (ServiceInstance instance : finder.getAllInstaces(serviceName)) {
			ListenableFuture<ResponseEntity<T>> future = aRestTemplate.getForEntity(instance.getUri().toString(),
					typeParameterClass, paramArrayOfObject);
			future.addCallback(result -> {
				resultList.add(result.getBody());
			}, ex -> {
			});
		}

		return resultList;
	}

	public List<T> multiCast(String serviceName, Map<String, ?> paramMap) {

		List<T> resultList = new ArrayList<T>();

		for (ServiceInstance instance : finder.getAllInstaces(serviceName)) {
			ListenableFuture<ResponseEntity<T>> future = aRestTemplate.getForEntity(instance.getUri().toString(),
					typeParameterClass, paramMap);
			future.addCallback(result -> {
				resultList.add(result.getBody());
			}, ex -> {
			});
		}

		return resultList;
	}

	public List<T> multiCast(String serviceName, String path) {

		List<T> resultList = new ArrayList<T>();

		for (ServiceInstance instance : finder.getAllInstaces(serviceName)) {
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

	public List<T> multiCast(String serviceName, String path, Object... paramArrayOfObject) {

		List<T> resultList = new ArrayList<T>();

		for (ServiceInstance instance : finder.getAllInstaces(serviceName)) {
			ListenableFuture<ResponseEntity<T>> future = aRestTemplate.getForEntity(instance.getUri().toString() + path,
					typeParameterClass, paramArrayOfObject);
			future.addCallback(result -> {
				resultList.add(result.getBody());
			}, ex -> {
			});
		}

		return resultList;
	}

	public List<T> multiCast(String serviceName, String path, Map<String, ?> paramMap) {

		List<T> resultList = new ArrayList<T>();

		for (ServiceInstance instance : finder.getAllInstaces(serviceName)) {
			ListenableFuture<ResponseEntity<T>> future = aRestTemplate.getForEntity(instance.getUri().toString() + path,
					typeParameterClass, paramMap);
			future.addCallback(result -> {
				resultList.add(result.getBody());
			}, ex -> {
			});
		}

		return resultList;
	}
	
	public List<T> multiCast(List<String> urls, String path) {

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

	public List<T> multiCast(List<String> urls, String path, Object... paramArrayOfObject) {

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

	public List<T> multiCast(List<String> urls, String path, Map<String, ?> paramMap) {

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
