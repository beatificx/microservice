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
		URI uri = finder.getServiceUrl(serviceName);
		return cast(uri);
	}

	public T cast(String serviceName, Object... paramArrayOfObject) {
		URI uri = finder.getServiceUrl(serviceName);
		return cast(uri, paramArrayOfObject);
	}

	public T cast(String serviceName, Map<String, ?> paramMap) {
		URI uri = finder.getServiceUrl(serviceName);
		return cast(uri, paramMap);
	}
	
	public T cast(URI uri) {
		return restTemplate.getForObject(uri, typeParameterClass);
	}
	
	public T cast(URI uri, Object... paramArrayOfObject) {
		return restTemplate.getForObject(uri.toString(), typeParameterClass, paramArrayOfObject);
	}
	
	public T cast(URI uri, Map<String, ?> paramMap) {
		return restTemplate.getForObject(uri.toString(), typeParameterClass, paramMap);
	}
	
	public T cast(String url, String path) {
		return restTemplate.getForObject(url + path, typeParameterClass);
	}
	
	public T cast(String url, String path, Object... paramArrayOfObject) {
		return restTemplate.getForObject(url + path, typeParameterClass, paramArrayOfObject);
	}
	
	public T cast(String url, String path, Map<String, ?> paramMap) {
		return restTemplate.getForObject(url + path, typeParameterClass, paramMap);
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
