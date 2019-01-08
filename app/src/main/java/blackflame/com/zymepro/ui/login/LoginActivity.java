package blackflame.com.zymepro.ui.login;
import android.os.Bundle;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.ui.login.fragment.loginfragment.LoginFragment;
import blackflame.com.zymepro.ui.login.fragment.signupfragment.SignUpFragment;
import blackflame.com.zymepro.view.custom.SwitchMultiButton;

public class LoginActivity extends BaseActivity {
  SwitchMultiButton switchMultiButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    GlobalReferences.getInstance().baseActivity=this;
    CommonPreference.initializeInstance(this);
    //GlobalReferences.getInstance().pref=this;
    initViews();

  }
  private void initViews(){
    switchMultiButton=findViewById(R.id.switch_login);
    switchMultiButton.setText("Signup", "Login").setOnSwitchListener(onSwitchListener);
    addFragmentWithBackStack(new SignUpFragment(),R.id.fragment_login,false);
  }


  private SwitchMultiButton.OnSwitchListener onSwitchListener = new SwitchMultiButton.OnSwitchListener() {
    @Override
    public void onSwitch(int position, String tabText,boolean isUser) {

      switch (position){
        case 0:

          addFragmentWithBackStack(new SignUpFragment(),R.id.fragment_login,false);

          break;
        case 1:
          addFragmentWithBackStack(new LoginFragment(),R.id.fragment_login,false);


          break;


      }

    }
  };
}
