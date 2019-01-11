package blackflame.com.zymepro.ui.carregistration;

import android.os.Bundle;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.view.custom.StepIndicator;
import blackflame.com.zymepro.view.custom.ZymeProViewPager;

public class CarRegistration extends BaseActivity {
  public ZymeProViewPager mViewPager;
  boolean doubleBackToExitPressedOnce=false;
  int [] layouts;
  CarinfoViewPagerAdapter viewPagerAdapter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_car_registration);
    GlobalReferences.getInstance().baseActivity=this;
    initViews();

  }

  private void initViews(){
    mViewPager= findViewById(R.id.viewPager_car_info);

    viewPagerAdapter =new CarinfoViewPagerAdapter(getSupportFragmentManager());
    mViewPager.setAdapter(viewPagerAdapter);
    mViewPager.setPagingEnabled(false);
    int currentItem=   getIntent().getIntExtra("currentItem",0);
    if(currentItem==2){
      mViewPager.setCurrentItem(2);
    }


    StepIndicator stepIndicator = findViewById(R.id.step_indicator_car_info);
    stepIndicator.setupWithViewPager(mViewPager);

    // Enable | Disable click on step number
    stepIndicator.setClickable(false);
  }
  @Override
  public void onBackPressed() {
    if (getFragmentManager().getBackStackEntryCount() > 0 ){
      getFragmentManager().popBackStackImmediate();
    }
    System.exit(0);
  }


}
