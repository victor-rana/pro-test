package blackflame.com.zymepro.ui.profile.updateuser;

public class UserPresenter {

  View view;
  public  UserPresenter(View v){
    this.view=v;
  }


  public void validateUser(String name,String mobile){

    if(name!=null &&  name.length() <0){
      view.setNameError();
    }else if (mobile != null && mobile.length() != 10 || mobile.contains("+91")) {
      view.setMobileError();
    }else {
      view.updateUser(name,mobile);
    }

  }
  public void validatePassword(String old_password,String new_password,String re_password){

    if(old_password != null && old_password.length() <6){
      view.setOldPasswordError();

    }else if(new_password != null && new_password.length() <6){
      view.setNewPasswordError();

    }else if(re_password != null &&  !re_password.equals(new_password)){
      view.rePasswordError();
    }else{
      view.updatePassword(old_password,new_password);

    }

  }



  interface View{
    void setNameError();
    void setMobileError();
    void setOldPasswordError();
    void setNewPasswordError();
    void rePasswordError();
    void updateUser(String name,String mobile);
    void updatePassword(String old,String new_password);
  }

}
