package blackflame.com.zymepro.ui.analytic.chart;


import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class DateAxisFormatter implements IAxisValueFormatter {
	

	private BarLineChartBase<?> chart;
	String[] data;
	
	public DateAxisFormatter(BarLineChartBase<?> chart,String[] data) {
		this.chart = chart;
		this.data=data;
		
	}
	
	@Override
	public String getFormattedValue(float value, AxisBase axis) {
		
		int days = (int) value;
		
		
		String appendix = "th";
		
		int dayOfMonth=0;
		
		try {
		
		dayOfMonth = Integer.valueOf(data[days]);
		}catch (Exception ex){
			//Log.e(TAG, "getFormattedValue:exception "+ex );
		}
		
		
		switch (dayOfMonth) {
			case 1:
				appendix = "st";
				break;
			case 2:
				appendix = "nd";
				break;
			case 3:
				appendix = "rd";
				break;
			case 21:
				appendix = "st";
				break;
			case 22:
				appendix = "nd";
				break;
			case 23:
				appendix = "rd";
				break;
			case 31:
				appendix = "st";
				break;
			
			
		}
		return data[days]+appendix;
		
	}

	
}
