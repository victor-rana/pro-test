package blackflame.com.zymepro.ui.livetrip.presenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import blackflame.com.zymepro.ui.home.singlecar.CarcountModel;
import blackflame.com.zymepro.ui.livetrip.LiveTripActivity;
import blackflame.com.zymepro.ui.livetrip.LiveTripInteractor;

public class LiveTripPresenter {

View view;
LiveTripInteractor liveTripInteractor;
    public LiveTripPresenter(View view, LiveTripInteractor interactor){
        this.view=view;
        this.liveTripInteractor=interactor;

    }




    public void parseTripData(JSONObject object) throws JSONException {
        JSONArray trip_path=object.getJSONObject("msg").getJSONArray("trip_path");
        view.setPolyLines(trip_path);
    }


    public interface View{

        void registerMqtt(String imei);
        void setPolyLines(JSONArray array);
    }


}
