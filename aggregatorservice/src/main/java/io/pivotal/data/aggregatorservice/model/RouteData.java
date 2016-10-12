/**
 * 
 */
package io.pivotal.data.aggregatorservice.model;

/**
 * @author shuklk2
 *
 */
public class RouteData {

	private String route;
	private String numtrips;
	private String starttime;
	private String endtime;
	
	public RouteData() {}
	
	public RouteData (String route, String numtrips, String starttime, String endtime) {
		this.route = route;
		this.numtrips = numtrips;
		this.starttime = starttime;
		this.endtime = endtime;
	}
	
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getNumtrips() {
		return numtrips;
	}
	public void setNumtrips(String numtrips) {
		this.numtrips = numtrips;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}	
}
