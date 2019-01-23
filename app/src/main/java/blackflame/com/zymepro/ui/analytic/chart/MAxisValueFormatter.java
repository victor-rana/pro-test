package blackflame.com.zymepro.ui.analytic.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import java.text.DecimalFormat;

public class MAxisValueFormatter implements IAxisValueFormatter {
		
		private DecimalFormat mFormat;
String[] data;
    public MAxisValueFormatter() {
    
		mFormat = new DecimalFormat("###,###,###,##0.0");
	}
		
		@Override
		public String getFormattedValue(float value, AxisBase axis) {
		return (int)value+" %";
	}

}

