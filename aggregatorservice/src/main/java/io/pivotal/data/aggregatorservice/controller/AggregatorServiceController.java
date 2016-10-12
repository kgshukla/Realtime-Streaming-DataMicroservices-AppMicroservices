/**
 * 
 */
package io.pivotal.data.aggregatorservice.controller;

import io.pivotal.data.aggregatorservice.model.DriverIncorrectData;
import io.pivotal.data.aggregatorservice.model.DriverRevenueData;
import io.pivotal.data.aggregatorservice.model.RouteData;
import io.pivotal.data.aggregatorservice.service.AggregatorService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuklk2
 *
 */
@RestController
public class AggregatorServiceController {

	@Autowired
	private AggregatorService service;
	
	@RequestMapping(value = "/totalevents", method = RequestMethod.GET)
	public long getTotalEvents() {
		return service.getTotalEvents();
	}
	
	@RequestMapping(value = "/missedevents", method = RequestMethod.GET)
	public long getMissedEvents() {
		return service.getMissedEvents();
	}
	
	@RequestMapping(value = "/correctevents", method = RequestMethod.GET)
	public long getCorrectEvents() {
		return service.getCorrectEvents();
	}
	
	@RequestMapping(value = "/processtime", method = RequestMethod.GET)
	public long processTime() {
		return service.processTime();
	}
	
	@RequestMapping(value = "/freetaxies", method = RequestMethod.GET)
	public List<List<String>> getFreeTaxiesList() {
		return service.getFreeTaxiesList();
	}
	
	@RequestMapping(value = "/top10routes", method = RequestMethod.GET)
	public List<RouteData> getTop10Routes() {
		return service.getTop10Routes();
	}
}
