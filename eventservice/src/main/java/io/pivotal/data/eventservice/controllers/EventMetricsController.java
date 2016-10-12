package io.pivotal.data.eventservice.controllers;

import io.pivotal.data.eventservice.services.EventsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by cq on 17/9/15.
 */
@RestController
public class EventMetricsController {

    @Autowired
    private EventsService eventsService;

    @RequestMapping(value = "/events/test", method = RequestMethod.GET)
    public String test(){
        return eventsService.test();
    }
    
    @RequestMapping(value = "/events/total", method = RequestMethod.GET)
    public long getTotalEvents(){
        return eventsService.totalOfEvents();
    }

    @RequestMapping(value = "/events/missed", method = RequestMethod.GET)
    public long getMissedEvents(){
        return eventsService.totalMissedEvents();
    }

    @RequestMapping(value = "/events/correct", method = RequestMethod.GET)
    public long getCorrectEvents(){
        return eventsService.totalCorrectEvents();
    }
    
    @RequestMapping(value = "/events/proctime", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public int processTime(){
        return eventsService.latestWPT();
    }
    
    @RequestMapping(value = "/events/kill", method = RequestMethod.GET)
	public String kill() {
		System.exit(1);
		return "";
	}
    
    @RequestMapping(value = "/events/load", method = RequestMethod.GET)
	public String load() {
		System.exit(1);
		return "";
	}
 
    @RequestMapping(value = "/events/instance", method = RequestMethod.GET)
	public String instance() {
		
		return "";
	}
    
}
