package blackflame.com.zymepro.ui.analytic.chart;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.text.DecimalFormat;


public class LineValueFromatter implements IValueFormatter {
	
	private DecimalFormat mFormat;
	
	public LineValueFromatter() {
		mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
	}
	
	@Override
	public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
		// write your logic here
		
		//Log.e(TAG, "getFormattedValue:LineChart "+value );
		return value+"";
		//return mFormat.format(value) + ""; // e.g. append a dollar-sign
	}
}