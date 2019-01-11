package blackflame.com.zymepro.ui.history.model;

public class TripHistory {
  String startAddress;
  String endAddress;
  String startTime;
  String startDate;

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  String endTime;
  int tripId;
  int total_distance,total_time,max_speed,total_idle_time;
  int trip_count;

  public int getTrip_count() {
    return trip_count;
  }

  public void setTrip_count(int trip_count) {
    this.trip_count = trip_count;
  }

  public int getTotal_distance() {
    return total_distance;
  }

  public void setTotal_distance(int total_distance) {
    this.total_distance = total_distance;
  }

  public int getTotal_time() {
    return total_time;
  }

  public void setTotal_time(int total_time) {
    this.total_time = total_time;
  }

  public int getMax_speed() {
    return max_speed;
  }

  public void setMax_speed(int max_speed) {
    this.max_speed = max_speed;
  }

  public int getTotal_idle_time() {
    return total_idle_time;
  }

  public void setTotal_idle_time(int total_idle_time) {
    this.total_idle_time = total_idle_time;
  }

  public int getTime_consumed() {
    return time_consumed;
  }

  public void setTime_consumed(int time_consumed) {
    this.time_consumed = time_consumed;
  }

  int time_consumed;

  public String getStartAddress() {
    return startAddress;
  }

  public void setStartAddress(String startAddress) {
    this.startAddress = startAddress;
  }

  public String getEndAddress() {
    return endAddress;
  }

  public void setEndAddress(String endAddress) {
    this.endAddress = endAddress;
  }


  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public int getTripId() {
    return tripId;
  }

  public void setTripId(int tripId) {
    this.tripId = tripId;
  }
}
