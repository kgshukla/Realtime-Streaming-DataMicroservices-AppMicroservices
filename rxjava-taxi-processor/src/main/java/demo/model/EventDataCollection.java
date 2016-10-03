package demo.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EventDataCollection implements Comparable<EventDataCollection> {
	
	String startendid;
	List<EventData> eventdatalist;

	public EventDataCollection (String startendid) {
		this.startendid = startendid;
		eventdatalist = new ArrayList<EventData>();
	}
	
	public void addEventData(EventData eventdata) {
		this.eventdatalist.add(eventdata);
	}
	
	public List<EventData> getEventDataList() {
		return eventdatalist;
	}
	
	public String getStartendid() {
		return startendid;
	}

	@Override
	public int compareTo(EventDataCollection eventDataCollection2) {
		return Comparators.DEFAULT.compare(this.eventdatalist, eventDataCollection2.getEventDataList());
	}
	
	public static class Comparators {
		public static Comparator<List<EventData>> DEFAULT = new Comparator<List<EventData>>() {    
			@Override
            public int compare(List<EventData> o1, List<EventData> o2) {
                if (o1.size() > o2.size())
                	return 1;
                else if (o1.size() < o2.size())
                	return -1;
                else
                    return 0;
            }
        };
	}
}

