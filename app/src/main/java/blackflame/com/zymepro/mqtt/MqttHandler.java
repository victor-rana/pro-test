package blackflame.com.zymepro.mqtt;

import static blackflame.com.zymepro.Prosingleton.TAG;
import android.util.Log;
import blackflame.com.zymepro.Prosingleton;
import blackflame.com.zymepro.common.Constants;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.ui.home.MqttDataListener;
import blackflame.com.zymepro.util.LogUtils;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.jwt.KeyGenerator;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class MqttHandler {
  static MqttAndroidClient mqttAndroidClient;
  static boolean isSubscribed = false, isSubsCribedCall = false;
 static String subscriptionTopic;
static MqttDataListener listener;
  public static void registerMqtt(final String imei,MqttDataListener dataListener){
    listener=dataListener;
    if (NetworkUtils.isConnected()) {
      subscriptionTopic = CommonPreference.getInstance().getSubscriptionTopic();
      LogUtils.error("MqttHandler","Topic "+subscriptionTopic);
      String email = CommonPreference.getInstance().getEmail();

      String id = email+String.valueOf(System.currentTimeMillis() + Math.random());
        mqttAndroidClient = new MqttAndroidClient(Prosingleton.getAppContext(), Constants.MQTT_URL_TEST, id);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
          @Override
          public void connectComplete(boolean reconnect, String serverURI) {

            if(reconnect){

              try{
                if(!mqttAndroidClient.isConnected()) {
                  mqttAndroidClient.connect();
                  Log.e(TAG, "connectComplete: "+"Automatically Reconnected to Broker!" );
                }else{
                  subscribeToTopic(subscriptionTopic,imei);
                }

              }catch (MqttException ex){
                //  Log.e(TAG, "connectComplete: "+ex.getMessage() );
              }


            } else {
              // Log.e(TAG, "connectComplete: "+"Connected To Broker for the first time!" );
            }

          }


          @Override
          public void connectionLost(Throwable cause) {

          }

          @Override
          public void messageArrived(String topic, MqttMessage message) {
            //addToHistory("Incoming message: " + new String(message.getPayload()));
            //  System.out.println("first" + message.getPayload());


          }

          @Override
          public void deliveryComplete(IMqttDeliveryToken token) {

          }
        });
        try {
          MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
          mqttConnectOptions.setAutomaticReconnect(true);
          mqttConnectOptions.setCleanSession(false);
          mqttConnectOptions.setKeepAliveInterval(5);
          mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
          mqttConnectOptions.setConnectionTimeout(60);
          mqttConnectOptions.setUserName(KeyGenerator.getKey(CommonPreference.getInstance().getClientToken()));
          mqttConnectOptions.setPassword(CommonPreference.getInstance().getToken().toCharArray());

          //addToHistory("Connecting to " + serverUri);
          mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
              DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
              disconnectedBufferOptions.setBufferEnabled(true);
              disconnectedBufferOptions.setBufferSize(100);
              disconnectedBufferOptions.setPersistBuffer(false);
              disconnectedBufferOptions.setDeleteOldestMessages(false);
              mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
              System.out.println("success");
              Log.e(TAG, "onSuccess: "+"Success" );

              if (!isSubscribed && !isSubsCribedCall) {
                isSubsCribedCall = true;
                subscribeToTopic(subscriptionTopic,imei);
              }


            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
              // addToHistory("Failed to connect to: " + serverUri);

              System.out.println("Failed to connnect to");
              // Toast.makeText(getActivity(), "Failed to connect to", Toast.LENGTH_SHORT).show();
            }
          });


        } catch (MqttException ex) {
          ex.printStackTrace();
        }catch (Exception ex){
          Log.e(TAG, "registerMqtt: "+ex.getCause() );
        }
        }
    }

  public static void subscribeToTopic(String topictoSubscribe,String imei) {
    Log.e(TAG, "subscribeToTopic: "+topictoSubscribe+" Imei "+imei );
    try {
      mqttAndroidClient.subscribe(topictoSubscribe + "/" + imei, 0, null, new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
          isSubscribed = true;
          Log.e(TAG, "onSuccess:is Subscribed "+asyncActionToken.isComplete());



        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
          // Toast.makeText(getActivity(), "Failed to subscribe", Toast.LENGTH_SHORT).show();
          //System.out.println("Failed to subscribe");
          //addToHistory("Failed to subscribe");
          isSubscribed = false;
          isSubsCribedCall = false;
          // Log.e(TAG, "onSuccess:is Subscribed Failed"+exception.getMessage() );
        }
      });
      mqttAndroidClient.subscribe(topictoSubscribe + "/" + imei, 0, new IMqttMessageListener() {
        @Override
        public void messageArrived(String topic, MqttMessage message) {
         // mCounter_publish_data+=1;
         // Log.e(TAG, "messageArrived: "+ new String(message.getPayload()) );
          try{
            JSONObject data= new JSONObject(new String(message.getPayload()));
            listener.setData(data,topic);
          }catch (Exception ex){
            //Log.e(TAG, "messageArrived: "+ex.getCause() );
          }
        }

      });

    } catch (MqttException ex) {
      System.err.println("Exception while subscribing");
      ex.printStackTrace();
    }
  }

  }

