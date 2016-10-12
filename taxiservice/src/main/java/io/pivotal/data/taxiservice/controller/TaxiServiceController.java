/**
 * 
 */
package io.pivotal.data.taxiservice.controller;

import java.util.List;

import io.pivotal.data.taxiservice.models.RouteData;
import io.pivotal.data.taxiservice.services.TaxiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuklk2
 *
 */

@RestController
public class TaxiServiceController {

	@Autowired
	private TaxiService service;
	
	@RequestMapping(value = "/routes/top10routes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<List<String>> getTop10Routes() {
		return service.getTop10Routes();
	}
	
	@RequestMapping(value = "/freetaxies/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<List<String>> getFreeTaxiesList() {
		return service.getFreeTaxiesList();
	}
	
	@RequestMapping(value = "/kill", method = RequestMethod.GET)
	public String kill() {
		System.exit(1);
		return "";
	}
}
