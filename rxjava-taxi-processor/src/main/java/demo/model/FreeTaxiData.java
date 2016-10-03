package demo.model;

public class FreeTaxiData {
    String medallion;
    String dropofflong;
    String dropofflat;

    public FreeTaxiData (EventData eventdata) {
        this.medallion = eventdata.getMedallion();
        this.dropofflong = eventdata.getDropofflong();
        this.dropofflat = eventdata.getDropofflat();
    }

    public String toString() {
        return this.medallion+","+this.dropofflat+","+this.dropofflong;
    }

    public String getMedallion() {
        return medallion;
    }

    public String getDropofflong() {
        return dropofflong;
    }

    public String getDropofflat() {
        return dropofflat;
    }
}
