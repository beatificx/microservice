package org.beatific.microservice.container.executor;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.beatific.microservice.container.monitor.InstanceStatus;
import org.beatific.microservice.container.utils.CalendarUtils;
import org.mortbay.log.Log;

public class ServiceStatistics {

	private Integer totalRequests = 0;

	private Integer averageRequestPerSeconds = 0;

	private Integer transactionPerSeconds = 0;

	private Map<Integer, Integer> requestPerHours;

	private Integer totalDays = 0;
	private Map<Integer, Integer> todayRequestPerHours;

	private Map<Integer, Integer> failRateCollectCounts;
	private Map<Integer, Integer> requestPerFailRate;
	
	private InstanceStatistics istatistics = new InstanceStatistics();

	private static int STANDARD_FAIL_RATE = 10; // 0.1%를 의미함
	private static int AVERAGE_MEMORY_USE_RATE = 70;

	public int collect(Integer total, Integer success, Integer fail, List<InstanceStatus> status) {
		Integer previousTotal = totalRequests;
		totalRequests = total + totalRequests;
		transactionPerSeconds = totalRequests == 0 ? 0 : (transactionPerSeconds * previousTotal + success * total) / totalRequests;
//		averageRequestPerSeconds = (previousTotal + total) / (previousTotal / averageRequestPerSeconds + 1);

		Calendar today = Calendar.getInstance();
		Integer hour = today.get(Calendar.HOUR_OF_DAY);
		if (todayRequestPerHours == null)
			todayRequestPerHours = new HashMap<>();
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

		int failRate = total == 0 ? 0 : fail / total * 10000;

		int failCount = collectStatistics(failRateCollectCounts, failRate, () -> 0, previous -> previous + 1);
		collectStatistics(requestPerFailRate, failRate, () -> 0, previous -> (previous + total) / failCount);
		
		status.forEach(istatus -> istatistics.collect(istatus.getRequest(), istatus.getSuccess(), istatus.getFail(), (int)(istatus.getUsedMemory() / istatus.getMaxMemory())));
		
		return analyze(total, success, fail, status);

	}
	
	private Integer nvl(Integer integer, Integer defaultValue) {
		if(integer == null)return defaultValue;
		else return integer;
	}

	private <T, R> R collectStatistics(Map<T, R> map, T key, Supplier<R> previousInitial, Function<R, R> updateValue) {

		if (map == null) map = new HashMap<>();
		
		R previous = map.get(key);
		if (previous == null) previous = previousInitial.get();

		R recent = updateValue.apply(previous);
		
		map.put(key, recent);
		
		return recent;
	}
	
	private int analyze(Integer total, Integer success, Integer fail, List<InstanceStatus> status) {
		Integer count = istatistics.countByFailRate(STANDARD_FAIL_RATE);
		Integer cNeeds = count == 0 ? 0 : (int)Math.ceil(total / count);
		
		Integer memory = istatistics.memoryByFailRate(STANDARD_FAIL_RATE);
		Integer memoryPerOne = (memory > AVERAGE_MEMORY_USE_RATE) ? AVERAGE_MEMORY_USE_RATE : memory;
		Integer mNeeds = memoryPerOne == 0 ? 0 : (int)status.stream().mapToDouble(s -> s.getUsedMemory() / s.getMaxMemory()).average().orElse(0) * status.size() / memoryPerOne;
		 
		Integer requestPerOne = istatistics.requestByMemory(memoryPerOne);
		Integer rNeeds = requestPerOne == 0 ? 0 : (int)Math.ceil(total / requestPerOne);

		Integer needs = Math.max(Math.max(cNeeds, mNeeds), rNeeds);
		
		Log.debug("count[{}], cNeeds[{}]", count, cNeeds);
		Log.debug("memoryPerOne[{}], mNeeds[{}]", memoryPerOne, mNeeds);
		Log.debug("requestPerOne[{}], rNeeds[{}]", requestPerOne, rNeeds);
		Log.debug("needs[{}]", needs);
		return needs - status.size();
	}
	
}
