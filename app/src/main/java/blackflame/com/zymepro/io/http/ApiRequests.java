package blackflame.com.zymepro.io.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import blackflame.com.zymepro.BuildConfig;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.common.Constants;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.util.LogUtils;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
public class ApiRequests {
    private static ApiRequests apiRequests = null;
    private final VolleyErrorListener error;
    public RequestQueue mRequestQueue;
    private HashMap<String, String> mParams;
    private Constants.RequestParam requestParam;

    public static ApiRequests getInstance() {
        return new ApiRequests();
    }

    public ApiRequests() {
        if (mParams == null) {
            mParams = new HashMap<String, String>();
        } else {
            mParams.clear();
        }
        mRequestQueue = RequestManager.getnstance(GlobalReferences.getInstance().baseActivity);
        error = new VolleyErrorListener();
    }

  public void uploadImei(Context context, AppRequest appRequest, JSONObject jsonObject) {
    if (context != null) {

      requestParam = RequestParam.LINK_DEVICE;
      String url = RequestParam.LINK_DEVICE.getBaseComleteUrl();
      HttpRequestsJson requests = new HttpRequestsJson(Request.Method.POST, url, jsonObject, error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

    public void sign_up(Context context, AppRequest appRequest, JSONObject jsonObject) {
        if (context != null) {
            requestParam = Constants.RequestParam.SIGN_UP;
            String url = Constants.RequestParam.SIGN_UP.getBaseComleteUrl();
            HttpRequestsJson requests = new HttpRequestsJson(Request.Method.POST, url, jsonObject, error, appRequest, mParams);
            error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(requestParam.getRequestTag());
            }
            requests.setTag(requestParam.getRequestTag());
            mRequestQueue.add(requests);
            appRequest.onRequestStarted(requests, requestParam);
        }
    }

  public void register_car(Context context, AppRequest appRequest, JSONObject jsonObject) {
    if (context != null) {

      requestParam = RequestParam.REGISTER_CAR;
      String url = RequestParam.REGISTER_CAR.getBaseComleteUrl();
      HttpRequestsJson requests = new HttpRequestsJson(Request.Method.POST, url, jsonObject, error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }


  public void updateCar(Context context, AppRequest appRequest, JSONObject jsonObject) {
    if (context != null) {

      requestParam = RequestParam.UPDATE_CAR;
      String url = RequestParam.UPDATE_CAR.getBaseComleteUrl();
      HttpRequestsJson requests = new HttpRequestsJson(Method.PUT, url, jsonObject, error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }
  public void updateUser(Context context, AppRequest appRequest, JSONObject jsonObject) {
    if (context != null) {

      requestParam = RequestParam.UPDATE_PROFILE;
      String url = RequestParam.UPDATE_PROFILE.getBaseComleteUrl();
      HttpRequestsJson requests = new HttpRequestsJson(Method.POST, url, jsonObject, error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }
  public void updatePassword(Context context, AppRequest appRequest, JSONObject jsonObject) {
    if (context != null) {

      requestParam = RequestParam.UPDATE_PASSWORD;
      String url = RequestParam.UPDATE_PASSWORD.getBaseComleteUrl();
      HttpRequestsJson requests = new HttpRequestsJson(Method.POST, url, jsonObject, error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void uploadSetting(Context context, AppRequest appRequest, JSONObject jsonObject) {
    if (context != null) {

      requestParam = RequestParam.UPLOAD_SETTING;
      String url = RequestParam.UPLOAD_SETTING.getBaseComleteUrl();
      HttpRequestsJson requests = new HttpRequestsJson(Method.POST, url, jsonObject, error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }



  public void get_setting(Context context, AppRequest appRequest,String carId) {
    if (context != null) {
      requestParam = RequestParam.GET_SETTING;
      String url = RequestParam.GET_SETTING.getBaseComleteUrl();
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+carId, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void get_trip(Context context, AppRequest appRequest,String carId,String time) {
    if (context != null) {
      requestParam = RequestParam.LOAD_TRIP;
      String url = RequestParam.LOAD_TRIP.getBaseComleteUrl();
      LogUtils.error("History",url+carId+"/"+time);
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+carId+"/"+time, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void get_alert_list(Context context, AppRequest appRequest,String carId,String time) {
    if (context != null) {
      requestParam = RequestParam.GET_ALERT_LIST;
      String url = RequestParam.GET_ALERT_LIST.getBaseComleteUrl();
      LogUtils.error("History",url+carId+"/"+time);
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+carId+"/"+time, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void get_old_error(Context context, AppRequest appRequest,String imei) {
    if (context != null) {
      requestParam = RequestParam.GET_PREVIOUS_ERROR;
      String url = RequestParam.GET_PREVIOUS_ERROR.getBaseComleteUrl();
      LogUtils.error("History",url+imei);
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+imei, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }
  public void get_current_error(Context context, AppRequest appRequest,String imei) {
    if (context != null) {
      requestParam = RequestParam.GET_CURRENT_ERROR;
      String url = RequestParam.GET_CURRENT_ERROR.getBaseComleteUrl();
      LogUtils.error("History",url+imei);
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+imei, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }


  public void get_nearby_data(Context context, AppRequest appRequest,String type,double latitude,double longitude) {
    if (context != null) {
      requestParam = RequestParam.GET_NEAR_BY;
      String tag="near_by";
      String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=5000&type="+type+"&key="+BuildConfig.PLACES_KEY;
      HttpRequests requests = new HttpRequests(Request.Method.GET, url, tag,error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(tag);
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void get_fuel_price(Context context, AppRequest appRequest,String type) {
    if (context != null) {
      requestParam = RequestParam.GET_FUEL_PRICE;
      String tag=requestParam.getRequestTag();
      String url=RequestParam.GET_FUEL_PRICE.getBaseComleteUrl();
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+type, tag,error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(tag);
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void load_analytics(Context context, AppRequest appRequest,String start_date,String end_date,String carId) {
    if (context != null) {
      requestParam = RequestParam.LOAD_ANALYTICS;
      String tag=requestParam.getRequestTag();
      String url=RequestParam.LOAD_ANALYTICS.getBaseComleteUrl();
      LogUtils.error("Analytic",url+carId+"/"+start_date+"/"+end_date);
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+carId+"/"+start_date+"/"+end_date, tag,error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(tag);
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }


  public void load_geotag(Context context, AppRequest appRequest) {
    if (context != null) {
      requestParam = RequestParam.LOAD_GEOTAG;
      String tag=requestParam.getRequestTag();
      String url=RequestParam.LOAD_GEOTAG.getBaseComleteUrl();

      HttpRequests requests = new HttpRequests(Request.Method.GET, url, tag,error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(tag);
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }
  public void delete_geotag(Context context, AppRequest appRequest,String id) {
    if (context != null) {
      requestParam = RequestParam.DELETE_GEOTAG;
      String tag=requestParam.getRequestTag();
      String url=RequestParam.DELETE_GEOTAG.getBaseComleteUrl();

      HttpRequests requests = new HttpRequests(Method.DELETE, url+id, tag,error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(tag);
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void load_message(Context context, AppRequest appRequest) {
    if (context != null) {
      requestParam = RequestParam.LOAD_MESSAGE_FROM_TEAM;
      String tag=requestParam.getRequestTag();
      String url=RequestParam.LOAD_MESSAGE_FROM_TEAM.getBaseComleteUrl();

      HttpRequests requests = new HttpRequests(Request.Method.GET, url, tag,error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(tag);
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void load_refer(Context context, AppRequest appRequest) {
    if (context != null) {
      requestParam = RequestParam.LOAD_REFER_MESSAGE;
      String tag=requestParam.getRequestTag();
      String url=RequestParam.LOAD_REFER_MESSAGE.getBaseComleteUrl();

      HttpRequests requests = new HttpRequests(Request.Method.GET, url, tag,error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(tag);
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void get_trip_details(Context context, AppRequest appRequest,String carId,String time,int trip_id) {
    if (context != null) {
      requestParam = RequestParam.GET_TRIP_DETAILS;
      String url = RequestParam.GET_TRIP_DETAILS.getBaseComleteUrl();
      LogUtils.error("History",url+carId+"/"+time);
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+"detail/"+carId+"/"+time+"/"+trip_id, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void download_trip(Context context, AppRequest appRequest,String type,String startdate,String endDate) {
    if (context != null) {
      String url =null;
      if (type.equals("pdf")){
        requestParam = RequestParam.GET_PDF_URL;
        url=RequestParam.GET_PDF_URL.getBaseComleteUrl()+endDate+"/"+startdate;
      }else if(type.equals("csv")){
        requestParam = RequestParam.GET_CSV_URL;
        url=RequestParam.GET_CSV_URL.getBaseComleteUrl()+endDate+"/"+startdate;
      }
      HttpRequests requests = new HttpRequests(Request.Method.GET, url, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }
    public void login(Context context, AppRequest appRequest, JSONObject jsonObject) {
        if (context != null) {

            requestParam = Constants.RequestParam.SIGN_IN;
            String url = Constants.RequestParam.SIGN_IN.getBaseComleteUrl();
            String requestTag = "sign_up";
            HttpRequestsJson requests = new HttpRequestsJson(Request.Method.POST, url, jsonObject, error, appRequest, mParams);
            error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(requestParam.getRequestTag());
            }
            requests.setTag(requestParam.getRequestTag());
            mRequestQueue.add(requests);
            appRequest.onRequestStarted(requests, requestParam);
        }
    }


  public void get_profile(Context context, AppRequest appRequest) {
    if (context != null) {
      requestParam = RequestParam.LOAD_PROFILE;
      String url = RequestParam.LOAD_PROFILE.getBaseComleteUrl();
      HttpRequests requests = new HttpRequests(Request.Method.GET, url, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

    public void get_status(Context context, AppRequest appRequest) {
        if (context != null) {
            requestParam = RequestParam.STATUS;
            String url = RequestParam.STATUS.getBaseComleteUrl();
            HttpRequests requests = new HttpRequests(Request.Method.GET, url, requestParam.getRequestTag(), error, appRequest, mParams);
            error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(requestParam.getRequestTag());
            }
            requests.setTag(requestParam.getRequestTag());
            mRequestQueue.add(requests);
            appRequest.onRequestStarted(requests, requestParam);
        }
    }


  public void get_live_trip_path(Context context, AppRequest appRequest,String carId) {
    if (context != null) {
      requestParam = RequestParam.GET_LIVE_TRIP_PATH;
      String url = RequestParam.GET_LIVE_TRIP_PATH.getBaseComleteUrl()+"/"+carId;

      Log.d("Live url",url);
      HttpRequests requests = new HttpRequests(Request.Method.GET, url, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }



  public void getBrands(Context context, AppRequest appRequest) {
    if (context != null) {
      requestParam = RequestParam.LOAD_BRANDS;
      String url = RequestParam.LOAD_BRANDS.getBaseComleteUrl();
      HttpRequests requests = new HttpRequests(Request.Method.GET, url, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }
  public void getModels(Context context, AppRequest appRequest,String model) {
    if (context != null) {
      requestParam = RequestParam.LOAD_MODELS;
      String url = RequestParam.LOAD_MODELS.getBaseComleteUrl();
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+model, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }



  public void get_last_connection(Context context, AppRequest appRequest,String imei) {
    if (context != null) {
      requestParam = RequestParam.STATUS;
      String url = RequestParam.LAST_CONNECTION.getBaseComleteUrl();
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+imei, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void get_gps_status(Context context, AppRequest appRequest,String carId) {
    if (context != null) {
      requestParam = RequestParam.STATUS;
      String url = RequestParam.GPS_STATUS.getBaseComleteUrl();
      LogUtils.error("gps_status",url);
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+carId, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void get_past_notification(Context context, AppRequest appRequest,int page) {
    if (context != null) {
      requestParam = RequestParam.LOAD_PAST_NOTIFICATION;
      String url = RequestParam.LOAD_PAST_NOTIFICATION.getBaseComleteUrl();
      LogUtils.error("past_notification",url);
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+page, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void get_location(Context context, AppRequest appRequest,String carId) {
    if (context != null) {
      requestParam = RequestParam.PITSTOP;
      String url = RequestParam.PITSTOP.getBaseComleteUrl();
      LogUtils.error("gps_status",url);
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+carId, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }



  public void save_geotag(Context context, AppRequest appRequest, JSONObject jsonObject) {
    if (context != null) {

      requestParam = RequestParam.SAVE_GEO_TAG;
      String url = RequestParam.SAVE_GEO_TAG.getBaseComleteUrl();
      HttpRequestsJson requests = new HttpRequestsJson(Method.POST, url, jsonObject, error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }


  public void save_geofence(Context context, AppRequest appRequest, JSONObject jsonObject) {
    if (context != null) {

      requestParam = RequestParam.SAVE_GEOFENCE;
      String url = RequestParam.SAVE_GEOFENCE.getBaseComleteUrl();
      HttpRequestsJson requests = new HttpRequestsJson(Method.POST, url, jsonObject, error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void get_geofence(Context context, AppRequest appRequest,String carId) {
    if (context != null) {
      requestParam = RequestParam.GET_GEO_FENCE;
      String url = RequestParam.GET_GEO_FENCE.getBaseComleteUrl();
      LogUtils.error("gps_status",url+carId);
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+carId, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }


  public void get_replay(Context context, AppRequest appRequest,String carId,String date,String tripId) {
    if (context != null) {
      requestParam = RequestParam.GET_TRIP_DETAILS;
      String url = RequestParam.GET_TRIP_DETAILS.getBaseComleteUrl();
      LogUtils.error("gps_status",url+carId);
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+"timeddetail"+"/"+carId+"/"+date+"/"+tripId, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }


  public void save_payment_status(Context context, AppRequest appRequest, JSONObject jsonObject) {
    if (context != null) {

      requestParam = RequestParam.UPDATE_PAYMENT;
      String url = RequestParam.UPDATE_PAYMENT.getBaseComleteUrl();
      HttpRequestsJson requests = new HttpRequestsJson(Method.POST, url, jsonObject, error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }
  public void save_payment_failed(Context context, AppRequest appRequest, JSONObject jsonObject) {
    if (context != null) {

      requestParam = RequestParam.UPDATE_FAILED;
      String url = RequestParam.UPDATE_FAILED.getBaseComleteUrl();
      HttpRequestsJson requests = new HttpRequestsJson(Method.POST, url, jsonObject, error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void update_mobile_setting(Context context, AppRequest appRequest, JSONObject jsonObject) {
    if (context != null) {

      requestParam = RequestParam.UPDATE_MOBILE_SETTING;
      String url = RequestParam.UPDATE_MOBILE_SETTING.getBaseComleteUrl();
      HttpRequestsJson requests = new HttpRequestsJson(Method.POST, url, jsonObject, error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void get_address(Context context, AppRequest appRequest,double lat,double lon) {
        if (context != null) {
            requestParam = RequestParam.STATUS;
            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "&sensor=true+&key=AIzaSyApjqH7n5NtCjitmRG7ZiFgAsnPICWlrOM";;
            HttpRequests requests = new HttpRequests(Request.Method.GET, url, "address", error, appRequest, mParams);
            error.setRequestLister(appRequest, requests, "address");
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(requestParam.getRequestTag());
            }
            requests.setTag("address");
            mRequestQueue.add(requests);
            appRequest.onRequestStarted(requests, requestParam);
        }
    }


  public void get_existing_url(Context context, AppRequest appRequest,String carId) {
    if (context != null) {
      requestParam = RequestParam.GET_EXISTING_URL;
      String url = RequestParam.GET_EXISTING_URL.getBaseComleteUrl();
      LogUtils.error("gps_status",url+carId);
      HttpRequests requests = new HttpRequests(Request.Method.GET, url+carId, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void deactivate_url(Context context, AppRequest appRequest,String carId) {
    if (context != null) {
      requestParam = RequestParam.DEACTIVATE_URL;
      String url = RequestParam.DEACTIVATE_URL.getBaseComleteUrl();
      LogUtils.error("gps_status",url);
      HttpRequests requests = new HttpRequests(Method.DELETE, url+carId, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }

  public void generate_url(Context context, AppRequest appRequest,String carId,String seconds) {
    if (context != null) {
      requestParam = RequestParam.GENERATE_URL;
      String url = RequestParam.GENERATE_URL.getBaseComleteUrl();
      LogUtils.error("gps_status",url);
      HttpRequests requests = new HttpRequests(Method.POST, url+carId+"/"+seconds, requestParam.getRequestTag(), error, appRequest, mParams);
      error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
      if (mRequestQueue != null) {
        mRequestQueue.cancelAll(requestParam.getRequestTag());
      }
      requests.setTag(requestParam.getRequestTag());
      mRequestQueue.add(requests);
      appRequest.onRequestStarted(requests, requestParam);
    }
  }


    public void logout(Context context, AppRequest appRequest, JSONObject jsonObject) {
        if (context != null) {

            requestParam = RequestParam.SIGN_OUT;
            String url = RequestParam.SIGN_OUT.getBaseComleteUrl();
            HttpRequestsJson requests = new HttpRequestsJson(Request.Method.POST, url, jsonObject, error, appRequest, mParams);
            error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(requestParam.getRequestTag());
            }
            requests.setTag(requestParam.getRequestTag());
            mRequestQueue.add(requests);
            appRequest.onRequestStarted(requests, requestParam);
        }
    }

    /**
     * Will be reponsible for listening errors
     * <p/>
     * *
     */



    class VolleyErrorListener implements Response.ErrorListener {
        private AppRequest listener;
        private BaseTask<?> task;
        private BaseTaskJson<JSONObject> task1;
        private String api_name;

        void setRequestLister(AppRequest listener, BaseTask<?> task, String api_name) {
            this.listener = listener;
            this.task = task;
            this.api_name = api_name;
        }
        void setRequestLister(AppRequest listener, BaseTaskJson<JSONObject> task, String api_name) {
            this.listener = listener;
            this.task1 = task;
            this.api_name = api_name;
        }
        @Override
        public void onErrorResponse(VolleyError error) {

            String json = null;


            NetworkResponse response = error.networkResponse;
            if (error.getMessage()!= null) {
                json = new String(error.getMessage());
              //  json = trimMessage(json, "message");
                //   if (json != null)
                //Utility.displayToastMessage(response.statusCode, json + " on " + api_name == null ? "" : api_name);

            } else {
                // Utility.displayMessage(500, "Unexpected server error on " + api_name == null ? "" : api_name);
            }
            if (listener != null) {
                if (task != null) {
                    try {

                      
                      if(response != null && response.data != null) {
                        switch (response.statusCode) {
                          case 401:
                            // Log.e("Error", "onErrorResponse: "+new String(response.data));
                            try {
                              JSONObject obj = new JSONObject(new String(response.data));
                              String msg = obj.getString("msg");

                          


                            } catch (JSONException e) {
                              e.printStackTrace();
                            }

                            break;


                        }
                      }



                        task.setError(error);
                      Log.d("Error log =>",json);
                        task.setJsonResponse(new JSONObject(json));
                        listener.onRequestFailed(task, requestParam);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else if (task1 != null) {
                    try {
                        try {


                          Log.d("Error log =>",json);

                            task1.setError(error);
                            task1.setJsonResponse(new JSONObject(json));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        listener.onRequestFailed(task1, requestParam);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

        }
    }
}