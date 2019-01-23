package blackflame.com.zymepro.notification;

import android.content.Intent;
import blackflame.com.zymepro.db.CommonPreference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class ProInstanceIdListenerService extends FirebaseInstanceIdService {
  String token_send;
  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    CommonPreference.initializeInstance(getApplicationContext());
    CommonPreference.getInstance().setFcmToken(refreshedToken);

    // Saving reg id to shared preferences
    storeRegIdInPref(refreshedToken);

    // sending reg id to your server
    sendRegistrationToServer(refreshedToken);
    //  Log.e("ProfirebaseMessaginf", "FCM Registration Token: " + refreshedToken);

  }

  private void sendRegistrationToServer(final String token) {
    // sending gcm token to server
//        Log.e(TAG, "sendRegistrationToServer: " + token);
    Intent intent = new Intent(this, ProFirebaseMessagingService.class);
    startService(intent);
  }

  private void storeRegIdInPref(String token) {

    CommonPreference.getInstance().setFcmToken(token);
    // editor.apply();
  }
}
