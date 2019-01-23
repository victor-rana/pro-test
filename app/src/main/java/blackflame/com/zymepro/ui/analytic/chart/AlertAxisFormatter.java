package blackflame.com.zymepro.ui.analytic.chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class AlertAxisFormatter implements IAxisValueFormatter {
	String[] data;
	
	public AlertAxisFormatter(String[] data) {
	
		this.data=data;
	}
	
	@Override
	public String getFormattedValue(float value, AxisBase axis) {
		
		//Log.e("ALert Label", "getFormattedValue: "+data[(int) value % data.length] );
		return data[(int) value % data.length];
		
	}
	
	
}
