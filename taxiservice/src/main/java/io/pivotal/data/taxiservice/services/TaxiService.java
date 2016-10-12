/**
 * 
 */
package io.pivotal.data.taxiservice.services;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import io.pivotal.data.taxiservice.models.RouteData;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shuklk2
 *
 */
@Component
public class TaxiService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private final String top10RoutesKey = "top10Routes_LATEST_DATA"; 
    private final String freeTaxiesKey = "freeTaxies_LATEST_DATA";

	public List<List<String>> getTop10Routes() {
        List<List<String>> rdlist = new ArrayList<List<String>>();

        long size = redisTemplate.opsForList().size(top10RoutesKey);
        if (size > 0) {
            String top10routes = redisTemplate.opsForList().index(top10RoutesKey, size-1);
            if (top10routes != null) {
                StringTokenizer stk = new StringTokenizer(top10routes, "|");
                ObjectMapper mapper = new ObjectMapper();
                if (stk != null) {
                    while (stk.hasMoreTokens()) {
                        String e = stk.nextToken();
                        System.out.println("data == "+e);
                        RouteData rd = new RouteData();
                        try {
                            rd = mapper.readValue(e, RouteData.class);
                            System.out.println("Route: "+rd.getRoute());
                            System.out.println("NumTrips: "+rd.getNumTrips());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        
                        //String route = list.get(0);
                        //String numtrips = list.get(1);
                        //StringTokenizer stk2 = new StringTokenizer(e, ",");
                        //String route = stk2.nextToken();
                        //String numtrips = stk2.nextToken();
                        //List <String> rd = new ArrayList<String>();
                        //rd.add(route);
                        //rd.add(numtrips);
                        //RouteData rd = new RouteData(route, numtrips);
                        List<String> data = new ArrayList<String>();
                        data.add(rd.getRoute());
                        data.add(rd.getNumTrips());
                        rdlist.add(data);
                    }
                }
            }
        }

        // keep only 4 elements in the key
        if (size > 4) {
            while (size != 4) {
                redisTemplate.opsForList().leftPop(top10RoutesKey);
                size = size -1;
            }
        }
        return rdlist;
	}
	
    public List<List<String>> getFreeTaxiesList() {
        List<List<String>> freetaxiesList = new ArrayList<List<String>>();
		
        long size = redisTemplate.opsForList().size(freeTaxiesKey);
        if (size > 0) {
            String freetaxies = redisTemplate.opsForList().index(freeTaxiesKey, size -1);
            if (freetaxies != null) {
                StringTokenizer stk = new StringTokenizer(freetaxies, "|");
                while (stk.hasMoreTokens()) {
                    String e = stk.nextToken();
                    StringTokenizer stk2 = new StringTokenizer(e, ",");
                    String medallion = stk2.nextToken();
                    String dropofflat = stk2.nextToken();
                    String dropofflong = stk2.nextToken();
                    List<String> freetaxi = new ArrayList<String>();
                    freetaxi.add(medallion);
                    freetaxi.add(dropofflat);
                    freetaxi.add(dropofflong);
                    freetaxiesList.add(freetaxi);
                }
            }
        }

        // keep only 4 elements in the key
        if (size > 4) {
            while (size != 4) {
                redisTemplate.opsForList().leftPop(freeTaxiesKey);
                size = size -1;
            }
        }
        return freetaxiesList;
    }
}
