package io.pivotal.data.eventservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by cq on 17/9/15.
 */
@Component
public class EventsService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String totalEvents;
    private String missedEventsinLast10secs;
    private String correctEventsinLast10secs;
    private String processingTimeforLast10secs;
    private final String key = "processingInfo_LATEST_DATA";

    private void getData() {
        System.out.println("getData() called");
        long size = redisTemplate.opsForList().size(key);
        if (size > 0) {
            System.out.println("Size of list == "+size);
            String processingInfo = redisTemplate.opsForList().index(key, size-1);
            if (processingInfo != null) {
                StringTokenizer stk = new StringTokenizer(processingInfo, "|");
                if (stk.hasMoreTokens() && stk != null) {
                    totalEvents = stk.nextToken();
                    correctEventsinLast10secs = stk.nextToken();
                    missedEventsinLast10secs = stk.nextToken();
                    processingTimeforLast10secs = stk.nextToken();
                    System.out.println("totalEvents ="+totalEvents);
                }
            }
        }

        // keep only 4 elements in the key
        if (size > 4) {
            while (size != 4) {
            redisTemplate.opsForList().leftPop(key);
            size = size - 1;
            }
        }
    }

    public long totalOfEvents(){
        System.out.println("totalOfEvents called:");
        if (totalEvents == null) 
            getData();

        if (totalEvents == null)
            return 0;
        else {
            long totalEvts = Long.valueOf(totalEvents);
            // let it recompute if another call is made to totalOfEvents
            totalEvents = null;
            return totalEvts;
        }
    }

    public long totalMissedEvents(){
        if (missedEventsinLast10secs == null) 
            getData();

        if (missedEventsinLast10secs == null)
            return 0;
        else {
            long missedEvents = Long.valueOf(missedEventsinLast10secs);
            // let it recompute if another call is made to totalMissedEvents
            missedEventsinLast10secs = null;
            return missedEvents;
        }
    }

    public long totalCorrectEvents(){
        if (correctEventsinLast10secs == null)
            getData();

        if(correctEventsinLast10secs == null)
            return 0;
        else {
            long correctEvents = Long.valueOf(correctEventsinLast10secs);
            // let it recompute if another call is made to totalCorrectEvents
            correctEventsinLast10secs = null;
            return correctEvents;
        }
    }
    
    /**
     * Gets the latest window processing time
     * @return
     */
    public int latestWPT(){
        if (processingTimeforLast10secs == null)
            getData();

        if (processingTimeforLast10secs == null)
            return 0;
        else {
            int wpt = Integer.valueOf(processingTimeforLast10secs);
            // let it recompute if another call is made to latestWPT
            processingTimeforLast10secs = null;
            return wpt;
        }
    }

    public String test() {
        redisTemplate.opsForValue().set("LATEST_VALUE", String.valueOf(System.currentTimeMillis()));
        return redisTemplate.opsForValue().get("LATEST_VALUE");
    }
}
