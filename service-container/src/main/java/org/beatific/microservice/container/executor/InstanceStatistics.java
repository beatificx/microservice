package org.beatific.microservice.container.executor;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.beatific.microservice.container.utils.CalendarUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InstanceStatistics {

	private Integer totalRequests = 0;

//	private Integer averageRequestPerSeconds;

	private Integer transactionPerSeconds = 0;

	private Map<Integer, Integer> requestPerHours = new HashMap<>();

	private Integer totalDays = 0;
	private Map<Integer, Integer> todayRequestPerHours = new HashMap<>();

	private Map<Integer, Integer> failRateCollectCounts = new HashMap<>();
	private Map<Integer, Integer> requestPerFailRate = new HashMap<>();

	private Map<Integer, Integer> memoryCollectCounts = new HashMap<>();
	private Map<Integer, Integer> requestPerMemory = new HashMap<>();

	private Map<Integer, Integer> failRatePerMemory = new HashMap<>();
	private Map<Integer, Integer> memoryPerFailRate = new HashMap<>();
	
	public void collect(Integer total, Integer tps, Integer fail, Integer memory) {
		Integer previousTotal = totalRequests;
		totalRequests = total + totalRequests;
		transactionPerSeconds = totalRequests == 0 ? 0 : (transactionPerSeconds * previousTotal + tps * total) / totalRequests;
//		averageRequestPerSeconds = (previousTotal + total) / (previousTotal / averageRequestPerSeconds + 1);

		Calendar today = Calendar.getInstance();
		Integer hour = today.get(Calendar.HOUR_OF_DAY);
//		if (todayRequestPerHours == null)
//			todayRequestPerHours = new HashMap<>();
		Integer request = todayRequestPerHours.get(hour);
		if (request == null) {
			Integer beforeHour = CalendarUtils.addHours(today, -1).get(Calendar.HOUR_OF_DAY);

			collectStatistics(requestPerHours, beforeHour, () -> 0,
					previous -> totalDays == 0 ? previous + nvl(todayRequestPerHours.get(beforeHour), 0) : (previous + nvl(todayRequestPerHours.get(beforeHour), 0)) / totalDays);
			if (beforeHour > hour)
				totalDays++;
			request = 0;
		}
		
		todayRequestPerHours.put(hour, request + total);

		System.out.println("hour [" + hour +"] request [" + request + "] total [" + total + "] fail [" + fail + "]");
		int failRate = total == 0 ? 0 : fail / total * 1000000;
		
		System.out.println("failRate [" + failRate + "]");

		int failCount = collectStatistics(failRateCollectCounts, failRate, () -> 0, previous -> previous + 1);
		collectStatistics(requestPerFailRate, failRate, () -> 0, previous -> (previous + total) / failCount);
		collectStatistics(memoryPerFailRate, failRate, () -> 0, previous -> (previous * (failCount -1) + memory) / failCount);

		int memoryCount = collectStatistics(memoryCollectCounts, memory, () -> 0, previous -> previous + 1);
		collectStatistics(requestPerMemory, memory, () -> 0, previous -> (previous + total) / memoryCount);
		
		collectStatistics(failRatePerMemory, memory, () -> 0, previous -> (previous * (memoryCount - 1) + failRate) / memoryCount);
		
		analyze();
		
	}
	
	private Integer nvl(Integer integer, Integer defaultValue) {
		if(integer == null)return defaultValue;
		else return integer;
	}

	private <T, R> R collectStatistics(Map<T, R> map, T key, Supplier<R> previousInitial, Function<R, R> updateValue) {

//		if (map == null) map = new HashMap<>();
		
		R previous = map.get(key);
		if (previous == null) previous = previousInitial.get();

		R recent = updateValue.apply(previous);
		
		map.put(key, recent);
		
		return recent;
	}
	
	public Integer countByFailRate(Integer failRate) {
        return failRateCollectCounts == null || !failRateCollectCounts.containsKey(failRate) ? 0 : failRateCollectCounts.get(failRate);
	}
	
    public Integer memoryByFailRate(Integer failRate) {
    	return memoryPerFailRate == null || !memoryPerFailRate.containsKey(failRate) ? 0 : memoryPerFailRate.get(failRate);
	}
    
    public Integer requestByMemory(Integer memory) {
    	return requestPerMemory == null || !requestPerMemory.containsKey(memory) ? 0 : requestPerMemory.get(memory);
	}
	
	public void analyze() {
		log.debug("InstanceStatistics [totalRequests=" + totalRequests + ", transactionPerSeconds=" + transactionPerSeconds + ", requestPerHours="
				+ requestPerHours + ", totalDays=" + totalDays + ", todayRequestPerHours=" + todayRequestPerHours
				+ ", failRateCollectCounts=" + failRateCollectCounts + ", requestPerFailRate=" + requestPerFailRate
				+ ", memoryCollectCounts=" + memoryCollectCounts + ", requestPerMemory=" + requestPerMemory
				+ ", failRatePerMemory=" + failRatePerMemory + ", memoryPerFailRate=" + memoryPerFailRate + "]");
	}

}
