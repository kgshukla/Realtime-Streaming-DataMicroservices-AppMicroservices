/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.rxjava.EnableRxJavaProcessor;
import org.springframework.cloud.stream.annotation.rxjava.RxJavaProcessor;
import org.springframework.context.annotation.Bean;

import demo.model.EventData;
import demo.model.FreeTaxiData;
import demo.model.EventDataCollection;

import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableRxJavaProcessor
public class RxJavaTransformer {

	private static Logger logger = LoggerFactory.getLogger(RxJavaTransformer.class);

    // this variable captures how many events have been processed thus far
    // this information should not be stored here and rather be stored outside in a cache 
    // so that when we scale this app, all instances could get the data. 
    private static int totalEventsProcessed = 0;

	@Bean
	public RxJavaProcessor<String,String> processor() {
		return inputStream -> inputStream.map(data -> {
			return data;
		}).buffer(10, 10, TimeUnit.SECONDS).map(data -> processData(data));
		
	}

	private static String processData(List<String> data) {
	
        if (data == null || data.size() == 0)
            return "";


		//start time to find out much time it takes to process data
		Long processingstarttime = System.currentTimeMillis();
		
		// filter the stream based on number of parameters. It should be 17
		List<String> filtereddata = filterData(data);
		
		// get the cell start and end information for each event and store it as EventData
		List<EventData> transformedData = transformData(filtereddata);
		
		// get list of all free taxies
		List<FreeTaxiData> freeTaxiesList = get50FreeTaxies(transformedData);
		
		// reduced transformed data based on "startendid" key which denotes the starting square and ending square
		List<EventDataCollection> eventsDataCollectionList = reducebyCount(transformedData);
		
		// Find top 10 areas where taxies are traveling the most
		List<RouteData> top10areas = getTop10Areas(eventsDataCollectionList);
		
		// Find data related to processing time
		List<String> processingInfoList = getProcessingInfo (processingstarttime, eventsDataCollectionList, data);
		
        totalEventsProcessed = totalEventsProcessed + data.size();
	   
        // returned all required data as json
        ReturnedData obj = new ReturnedData(processingInfoList, freeTaxiesList, top10areas, totalEventsProcessed);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try { 
            jsonInString = mapper.writeValueAsString(obj);
        } catch (Exception e) { e.printStackTrace(); }
        return jsonInString;
    }
	

    private static class ReturnedData {
        String processingInfoString = "";
        String freetaxiesListString = "";
        String top10routesString = "";

        ReturnedData(List<String> processingInfo, List<FreeTaxiData> freetaxiesList, List<RouteData> top10areas, int totalEventsProcessed) {
            processingInfoString = String.valueOf(totalEventsProcessed).concat("|");
            for (String e : processingInfo) {
                processingInfoString = processingInfoString.concat(e).concat("|");
            }

            for (FreeTaxiData e : freetaxiesList) {
                freetaxiesListString = freetaxiesListString.concat(e.toString()).concat("|");
            }

            for (RouteData e : top10areas) {
                top10routesString = top10routesString.concat(e.toString()).concat("|");
            }
        }

        public String getProcessingInfoString() {
            return processingInfoString;
        }

        public String getFreetaxiesListString() {
            return freetaxiesListString;
        }

        public String getTop10routesString() {
            return top10routesString;
        }
    }


	private static List<String> getProcessingInfo(Long processingstarttime,
			List<EventDataCollection> eventsDataCollectionList, List<String> rawData) {
		List<String> processingInfoList = new ArrayList<String>();
		Long currentTime = System.currentTimeMillis();
		Long delayinms = currentTime - Long.valueOf(processingstarttime).longValue();
		
		int totalProcessedEvents = 0;
		for (EventDataCollection edc : eventsDataCollectionList) {
			totalProcessedEvents = totalProcessedEvents + edc.getEventDataList().size();
		}
		
		processingInfoList.add(String.valueOf(totalProcessedEvents));
		processingInfoList.add(String.valueOf(rawData.size()-totalProcessedEvents));
		processingInfoList.add(String.valueOf(delayinms));
		return processingInfoList;
	}

	private static List<RouteData> getTop10Areas(
			List<EventDataCollection> eventsDataCollectionList) {
		List<RouteData> top10areas = new ArrayList<RouteData>();
	
        // minimum is 10 or eventsDataCollectionList size
        int j = (eventsDataCollectionList.size() < 10) ? eventsDataCollectionList.size() : 10;

        int collsize = eventsDataCollectionList.size();

		for (int i = 1; i <= j; i++) {
			// since the list is ordered in ascending, we need to get last one first
            EventDataCollection edc = eventsDataCollectionList.get(collsize-i);
			RouteData rd = new RouteData(edc.getStartendid(), String.valueOf(edc.getEventDataList().size()));
            top10areas.add(rd);
		}
		return top10areas;
	}

    private static class RouteData {
        private String route;
        private String numTrips;

        public RouteData(String route, String numTrips) {
            this.route = route;
            this.numTrips = numTrips;
        }

        public String getRoute() {
            return this.route;
        }

        public String getNumTrips() {
            return this.numTrips;
        }

        public String toString() {
            ObjectMapper mapper = new ObjectMapper();
            String returnedString = "";
            try {
                returnedString = mapper.writeValueAsString(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedString;
        }
    }

	private static List<EventDataCollection> reducebyCount(
			List<EventData> transformedData) {
		List<EventDataCollection> eventDataCollection = new ArrayList<EventDataCollection> ();
		Map<String, EventDataCollection> mapData = new HashMap<String, EventDataCollection>();
		
		for (EventData event : transformedData) {
			String startEndId = event.getStartendId();
			if (mapData.containsKey(startEndId)) {
				EventDataCollection edc = mapData.get(startEndId);
				edc.addEventData(event);
			} else {
				EventDataCollection edc = new EventDataCollection(startEndId);
				edc.addEventData(event);
				mapData.put(startEndId, edc);
			}
		}
		List<EventDataCollection> reducedEventDataCollection = new ArrayList<EventDataCollection> (mapData.values());
		Collections.sort(reducedEventDataCollection);
		return reducedEventDataCollection;
		
	}

	private static List<FreeTaxiData> get50FreeTaxies(List<EventData> transformedData) {
		List<FreeTaxiData> freetaxiesList = new ArrayList<FreeTaxiData>();
		int i = 0;
		for(EventData entry : transformedData) {
			if (i<50) {
				freetaxiesList.add(new FreeTaxiData(entry));
				i = i+1;
			}
		}
		
		return freetaxiesList;
	}

	private static List<EventData> transformData(List<String> filtereddata) {
		
		List<EventData> transformedData = new ArrayList<EventData>();
		
		for (String d : filtereddata) {
			List<String> entryList = Arrays.asList(d.split(","));
			String medallion = entryList.get(0);
			String pickupTime = entryList.get(2);
			String dropoffTime = entryList.get(3);
			String tripDistance = entryList.get(5);
			String pickuplong = entryList.get(6);
			String pickuplat = entryList.get(7);
			String dropofflong = entryList.get(8);
			String dropofflat = entryList.get(9);
			String startCellid = getCellId(pickuplong, pickuplat);
			String destCellid = getCellId(dropofflong, dropofflat);
			//String cellId = "wrong";
			if ((null != startCellid) && (null != destCellid)) {
				String cellId = startCellid.concat("_to_").concat(destCellid);
				EventData eventData = new EventData(medallion, pickupTime, dropoffTime, tripDistance, 
						                            pickuplong, pickuplat, dropofflong, dropofflat, startCellid, destCellid); 
				eventData.setProcessingTimeStart(String.valueOf(System.currentTimeMillis()));
				transformedData.add(eventData);
			}
		}
		return transformedData;
	}
	
	private static List<String> filterData(List<String> data) {
		List<String> filteredList = new ArrayList<String>();
		List<String> wrongDataList = new ArrayList<String>();
		
		for (String d : data) {
			List<String> entryList = Arrays.asList(d.split(","));
			if(entryList.size() == 17) {
				// check for amount integrity - TODO 
				filteredList.add(d);
			}
			else {
                logger.info("Wrong event : " + d);
				wrongDataList.add(d);
				//accuCount.add(1);
				//totalStreamCount.add(1);
			}
		}
		return filteredList;
	}
	
	private static String getCellId(String pickuplong, String pickuplat) {
		double initialLong = Double.valueOf("-74.913585");
		double initialLat = Double.valueOf("41.474937");
		
		int horizontalDist = distance(initialLat, initialLong, initialLat, Double.valueOf(pickuplong));
		if(horizontalDist < 0 || horizontalDist > 300) {
			// cannot be negative
			return null;
		}
		
		int verticalDist = distance(initialLat, initialLong, Double.valueOf(pickuplat), initialLong);
		
		if(verticalDist < 0 || verticalDist > 300) {
			return null;
		}
		return "c:".concat(String.valueOf(horizontalDist)).concat(".").concat(String.valueOf(verticalDist));
	}
	
	private static int distance(double lat1, double lon1, double lat2, double lon2) {
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1515;
		  // distance in meters
		  dist = dist * 1.609344 * 1000;
		  return (Double.valueOf(dist/1000).intValue()+1);
		}
	
	private static double deg2rad(double deg) {
		  return (deg * Math.PI / 180.0);
	}
	
	private static double rad2deg(double rad) {
		  return (rad * 180 / Math.PI);
	}
	
	
    private static String combine(List<String> data) {
        String result = "";
        for (String d : data) {
            result = result.concat(":").concat(d);
        }
        return result;
    }

	private static Double avg(List<String> data) {
		double sum = 0;
		double count = 0;
		for(String d : data) {
			count++;
			sum += Double.valueOf(d);
		}
		return sum/count;
	}

}
