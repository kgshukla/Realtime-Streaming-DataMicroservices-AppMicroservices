/**
 * 
 */
package io.pivotal.data.taxiservice.models;

/**
 * @author shuklk2
 *
 */
public class RouteData {

	private String route;
	private String numTrips;

    public RouteData() {}

    public RouteData (String route, String numTrips) {
        this.route = route;
        this.numTrips = numTrips;
    }

    public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getNumTrips() {
		return numTrips;
	}

	public void setNumTrips(String numTrips) {
		this.numTrips = numTrips;
	}
}
