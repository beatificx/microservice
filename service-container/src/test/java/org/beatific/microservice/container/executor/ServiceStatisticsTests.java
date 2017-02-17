package org.beatific.microservice.container.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.beatific.microservice.container.instance.Instance;
import org.beatific.microservice.container.monitor.InstanceStatus;
import org.junit.Test;

public class ServiceStatisticsTests {

	Random random = new Random();
	private final static Integer mmx = 1000000; 
	
	@Test
	public void test2() {
		
	}
	
	@Test
	public void test() {
		ServiceStatistics stats = new ServiceStatistics();
		
		List<InstanceStatus> status = new ArrayList<>();
		
		for(int i = 0; i < 100; i++, status.clear()) {
			
			int total = next(mmx);
			int success = success(total);
			int fail = total - success;
			
			Instance instance = new Instance();
			InstanceStatus ins = new InstanceStatus(instance);
			int max = next();
			int used = next(max);
			int free = max - used;
			ins.setMaxMemory(max);
			ins.setUsedMemory(used);
			ins.setFreeMemory(free);
			ins.setRequest(total);
			ins.setSuccess(success);
			ins.setFail(fail);
			
			status.add(ins);
			
			System.out.println("total [" + total + "], success [" + success + "], fail [" + fail + "], status [" + status + "]");
			stats.collect(total, success, fail, status);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private int success(int total) {
		System.out.println((double)total/(double)mmx);
		return (int)(total * total / mmx * 0.1);
	}
	
	private int next() {
		int response = -1;
		while(response <= 0) {
			response = random.nextInt();
		}
		return response;
	}
	
	private int next(int bound) {
		int response = -1;
		while(response <= 0) {
			response = random.nextInt(bound);
		}
		return response;
	}
	
}
