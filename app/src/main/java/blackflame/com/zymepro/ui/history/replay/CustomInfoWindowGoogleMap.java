package blackflame.com.zymepro.ui.history.replay;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.ui.history.replay.model.ReplayData;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    String speed,coolant,rpm,voltage;




    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.layout_custom_info_window, null);

        TextView tvSpeed = view.findViewById(R.id.tvSpeed);
        TextView tvRpm = view.findViewById(R.id.tvRpm);
        TextView tvVoltage = view.findViewById(R.id.tvVoltage);

        TextView tvCoolant = view.findViewById(R.id.tvCoolant);


        ReplayData infoWindowData = (ReplayData) marker.getTag();


//

        if (infoWindowData != null) {
            tvSpeed.setText(infoWindowData.getSpeed());
            tvRpm.setText(infoWindowData.getRpm());
            tvVoltage.setText(infoWindowData.getVoltage());
            tvCoolant.setText(infoWindowData.getCoolant());
        }

        return view;
    }

    public void updateData(ReplayData data){

        speed=data.getSpeed();
        rpm=data.getRpm();
        voltage=data.getVoltage();
        coolant=data.getCoolant();

    }
}