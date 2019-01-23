package blackflame.com.zymepro.ui.analytic.listener;

import com.android.volley.VolleyError;

public interface AnalyticResponseListener {
	void onDateData(String[] data);
	void onTimeData(int[] data);
	void onDistanceData(float[] data);
	void onIdleTimeData(int[] data);
	void onMaxSpeedData(int[] data);
	void onMaxRpmData(int[] data);
	void onAlertData(int[] count);
	void onActiveTime(float[] active, float[] idle);
	void onAlertCount(float[] data);

	void onPieData(float[] time, float[] distance);
	void onTripCount(float[] trip);
	void onSummaryData(int totalTrip, String totalDistance, int totalAlerts, int averageSpeed,
      String driveTime, String idleTime);
	void onError(VolleyError error);
	
	
	
}
