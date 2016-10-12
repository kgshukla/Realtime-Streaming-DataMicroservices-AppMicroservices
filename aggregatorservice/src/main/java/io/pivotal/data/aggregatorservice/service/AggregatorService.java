/**
 * 
 */
package io.pivotal.data.aggregatorservice.service;

import io.pivotal.data.aggregatorservice.model.DriverIncorrectData;
import io.pivotal.data.aggregatorservice.model.DriverRevenueData;
import io.pivotal.data.aggregatorservice.model.RouteData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * @author shuklk2
 *
 */
@Service
public class AggregatorService {

	/*
	@Primary
	@Bean
	RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	*/
	
	@Autowired
	RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "fallbackEventCalls")
	public long getTotalEvents() {
		return restTemplate.getForObject("https://EVENTSERVICE-IOT-V1/events/total", Long.class);
	}
	
	@HystrixCommand(fallbackMethod = "fallbackEventCalls")
	public long getMissedEvents() {
		return restTemplate.getForObject("https://EVENTSERVICE-IOT-V1/events/missed", Long.class);
	}
	
	@HystrixCommand(fallbackMethod = "fallbackEventCalls")
	public long getCorrectEvents() {
		return restTemplate.getForObject("https://EVENTSERVICE-IOT-V1/events/correct", Long.class);
	}
	
	@HystrixCommand(fallbackMethod = "fallbackEventCalls")
	public long processTime() {
		return restTemplate.getForObject("https://EVENTSERVICE-IOT-V1/events/proctime", Long.class);
	}
	
	@HystrixCommand(fallbackMethod = "fallbackfreeTaxiCall")
	public List<List<String>> getFreeTaxiesList() {
		return restTemplate.getForObject("https://TAXISERVICE-IOT-V1/freetaxies/list", List.class);
	}
	
	@HystrixCommand(fallbackMethod = "fallbackRoutesCall")
	public List<RouteData> getTop10Routes() {
		return restTemplate.getForObject("https://TAXISERVICE-IOT-V1/routes/top10routes", List.class);
	}
	
	public List<DriverRevenueData> getTop10EarningDrivers() {
		return restTemplate.getForObject("https://ANALYTICSERVICE-IOT-V1/analytics/top10earningdrivers", List.class);
	}
	
	public List<DriverIncorrectData> getTop10ErringDrivers() {
		return restTemplate.getForObject("https://ANALYTICSERVICE-IOT-V1/analytics/top10erringdrivers", List.class);
	}
	
	private List<RouteData> fallbackRoutesCall() {
		return new ArrayList<RouteData>();
	}
	
	private List<List<String>> fallbackfreeTaxiCall() {
		return new ArrayList<List<String>>();
	}
	
	private long fallbackEventCalls() {
		return 0L;
	}
	
	private List<DriverRevenueData> fallbackAnalyticsEarningDriverCall() {
		return new ArrayList<DriverRevenueData>();
	}
	
	private List<DriverIncorrectData> fallbackAnalyticsErringDriverCall() {
		return new ArrayList<DriverIncorrectData>();
	}
}
