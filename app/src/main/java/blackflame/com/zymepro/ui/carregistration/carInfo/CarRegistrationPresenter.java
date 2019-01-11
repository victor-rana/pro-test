package blackflame.com.zymepro.ui.carregistration.carInfo;
import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CarRegistrationPresenter {
  View view;
  public CarRegistrationPresenter(View v){
    this.view=v;
  }
  public  void validateData(String nickname,String registration,String brand,String model,String CC,String fuelType,String state){
    String fuel=null;

    if (TextUtils.isEmpty(nickname)){
      view.nameError();
    }else if (TextUtils.isEmpty(registration)){
      view.registrationError();
    }else  if (brand.equals("Brand")||brand.equals("Choose One")){
      view.brandError();
    }else if (model.equals("Model")||model.equals("Choose One")){
      view.modelError();
    }else if (CC.equals("CC")){
      view.ccError();
    }else if (fuelType ==null ){
      view.fuelError();
    }else if (state.equals("Select state")){
      view.setStateError();
    }else{
      if(fuelType.equals("Diesel")) {
        fuel="DIESEL";
      }else if(fuelType.equals("Petrol")){
        fuel="PETROL";
      }

      view.uploadRegistration(nickname,registration,brand,model,fuel,state,CC);
    }
  }


  public void parseBrand(JSONObject brand){
    String [] carBrand;
    try{
      JSONArray object=brand.getJSONArray("msg");
      int length=object.length();
      carBrand=new String[length];
      for(int i=0;i<length;i++){
        carBrand[i]=object.getString(i);
      }


      view.setBrand(carBrand);
    }catch (JSONException ex){

    }


  }
  public void parseModel(JSONObject model){
String [] carModel;
    try {

      JSONArray object=model.getJSONArray("msg");
      int length=object.length();
      carModel=new String[length];

      for(int i=0;i<length;i++){
        carModel[i]=object.getString(i);
      }


      view.setModel(carModel);


    }catch (JSONException ex){

    }
  }


  interface View{
    void nameError();
    void registrationError();
    void brandError();
    void modelError();
    void ccError();
    void fuelError();
    void setBrand(String[] data);
    void setModel(String[] model);
    void setStateError();
    void uploadRegistration(String name,String reg,String brand,String model,String fuel,String state,String cc);
  }

}
