package blackflame.com.zymepro.ui.home.singlecar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

public class MsgReceiver {
  public static void registerReceiver(Context context) {

    /* ---- Preparing Intents To Check While Sms Sent & Delivered ---- */


    context.registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(Context mContext, Intent arg1) {

        switch (getResultCode()) {


          case Activity.RESULT_OK:
            Toast.makeText(mContext, "SOS sent", Toast.LENGTH_LONG).show();
            break;
          case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
            Toast.makeText(mContext, "Not sent: Generic failure", Toast.LENGTH_LONG).show();
            break;
          case SmsManager.RESULT_ERROR_NO_SERVICE:
            Toast.makeText(mContext, "Not sent: No service (possibly, no SIM-card)", Toast.LENGTH_LONG).show();
            break;
          case SmsManager.RESULT_ERROR_NULL_PDU:
            Toast.makeText(mContext, "Not sent: Null PDU", Toast.LENGTH_LONG).show();
            break;
          case SmsManager.RESULT_ERROR_RADIO_OFF:
            Toast.makeText(mContext, "Not sent: Radio off (possibly, Airplane mode enabled in Settings)", Toast.LENGTH_LONG).show();
            break;
        }
      }
    }, new IntentFilter("SENT"));


    context.registerReceiver(
        new BroadcastReceiver() {
          @Override
          public void onReceive(Context mContext, Intent arg1) {
            switch (getResultCode()) {
              case Activity.RESULT_OK:

                Toast.makeText(mContext, " SOS delivered", Toast.LENGTH_LONG).show();
                break;
              case Activity.RESULT_CANCELED:


                Toast.makeText(mContext, "Not delivered: Canceled", Toast.LENGTH_LONG).show();
                break;
            }
          }
        }, new IntentFilter("DELIVERED"));
  }
}
