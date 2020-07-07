package blackflame.com.zymepro.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.analytics.FirebaseAnalytics;

import blackflame.com.zymepro.db.CommonPreference;

public class Analytics {

    public static void index(Context context, String activityName){

        try {

            FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(context);
            analytics.setCurrentScreen((Activity) context, activityName, null /* class override */);
            analytics.setUserId(CommonPreference.getInstance().getEmail());

        }catch (Exception ex){
            Log.e("Index screen", "index: "+ex.getCause() );
        }

    }
}
