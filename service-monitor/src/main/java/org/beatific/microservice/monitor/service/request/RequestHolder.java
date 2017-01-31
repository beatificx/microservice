package org.beatific.microservice.monitor.service.request;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class RequestHolder {
	
	int count = 0;
	int success = 0;
	int fail = 0;
	
	public synchronized void hold() {
		count++;
	}
	
	public synchronized void success() {
		success++;
	}
	
	public synchronized void fail() {
		fail++;
	}
	
	public synchronized List<Integer> takeCount() {
		int takeCount = count;
		int takeSuccess = success;
		int takeFail = fail;
		count  = 0;
		success = 0;
		fail = 0;
		List<Integer> take = Arrays.asList(new Integer[]{takeCount, takeSuccess, takeFail});
		
		return take;
	}

}
