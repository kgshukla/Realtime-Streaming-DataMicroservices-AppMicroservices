package demo.model;

public class EventData {

	String medallion; 
	String pickupTime;
	String dropoffTime;
	String tripDistance;
	String pickuplong;
	String pickuplat;
	String dropofflong;
	String dropofflat;
	String startCellid;
	String destCellid;
	String processingTimeStart;
	String startendId;

	public EventData(String medallion, String pickupTime, String dropoffTime,
			String tripDistance, String pickuplong, String pickuplat,
			String dropofflong, String dropofflat, String startCellid,
			String destCellid) {
		super();
		this.medallion = medallion;
		this.pickupTime = pickupTime;
		this.dropoffTime = dropoffTime;
		this.tripDistance = tripDistance;
		this.pickuplong = pickuplong;
		this.pickuplat = pickuplat;
		this.dropofflong = dropofflong;
		this.dropofflat = dropofflat;
		this.startCellid = startCellid;
		this.destCellid = destCellid;
		this.startendId = startCellid.concat("_to_").concat(destCellid);
	}

	
	
	public String getStartendId() {
		return startendId;
	}



	public String getProcessingTimeStart() {
		return processingTimeStart;
	}

	public void setProcessingTimeStart(String processingTimeStart) {
		this.processingTimeStart = processingTimeStart;
	}
	
	public String getMedallion() {
		return medallion;
	}

	public void setMedallion(String medallion) {
		this.medallion = medallion;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getDropoffTime() {
		return dropoffTime;
	}

	public void setDropoffTime(String dropoffTime) {
		this.dropoffTime = dropoffTime;
	}

	public String getTripDistance() {
		return tripDistance;
	}

	public void setTripDistance(String tripDistance) {
		this.tripDistance = tripDistance;
	}

	public String getPickuplong() {
		return pickuplong;
	}

	public void setPickuplong(String pickuplong) {
		this.pickuplong = pickuplong;
	}

	public String getPickuplat() {
		return pickuplat;
	}

	public void setPickuplat(String pickuplat) {
		this.pickuplat = pickuplat;
	}

	public String getDropofflong() {
		return dropofflong;
	}

	public void setDropofflong(String dropofflong) {
		this.dropofflong = dropofflong;
	}

	public String getDropofflat() {
		return dropofflat;
	}

	public void setDropofflat(String dropofflat) {
		this.dropofflat = dropofflat;
	}

	public String getStartCellid() {
		return startCellid;
	}

	public void setStartCellid(String startCellid) {
		this.startCellid = startCellid;
	}

	public String getDestCellid() {
		return destCellid;
	}

	public void setDestCellid(String destCellid) {
		this.destCellid = destCellid;
	}
	
}
