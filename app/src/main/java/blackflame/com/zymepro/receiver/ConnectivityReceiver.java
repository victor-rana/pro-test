package blackflame.com.zymepro.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import blackflame.com.zymepro.Prosingleton;
import blackflame.com.zymepro.constant.ActivityConstants;
import blackflame.com.zymepro.util.NetworkUtils;

public class ConnectivityReceiver extends BroadcastReceiver {

    private static final String TAG =ConnectivityReceiver.class.getCanonicalName() ;
    public static ConnectivityReceiverListener connectivityReceiverListener;
    Context mContext;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        mContext = context;
      String statusString = NetworkUtils.getConnectivityStatusString(context);
        Log.e(TAG, "onReceive: "+statusString );



        if (statusString.equals(ActivityConstants.NOT_CONNECT)) {
            Log.e("Receiver ", "not connection");// your code when internet lost
            if (connectivityReceiverListener!=null){
                connectivityReceiverListener.onNetworkConnectionChanged(false);
            }


        } else {
            if (connectivityReceiverListener!=null) {
                connectivityReceiverListener.onNetworkConnectionChanged(true);
            }
            Log.e("Receiver ", "connected to internet");//your code when internet connection come back
        }




    }

    public static boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) Prosingleton.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }




    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
