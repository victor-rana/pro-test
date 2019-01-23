package blackflame.com.zymepro.ui.alerts.model;

import java.io.Serializable;


public class Alert implements Serializable {
   private String alertName;
    private String date;
    private String time;
    double latitude;
    double longitude;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String address;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getDate() {
        return date;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.date = registrationNumber;
    }


}
