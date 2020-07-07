package blackflame.com.zymepro.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.common.CommonFragment;
import blackflame.com.zymepro.common.GlobalReferences;


public class BaseActivityParent extends AppCompatActivity {

  private static final String TAG = BaseActivity.class.getCanonicalName();
  protected FragmentManager fragmentManager;
  protected FragmentTransaction fragmentTransaction;
  @Override
  public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);
    //fragmentManager =getSupportFragmentManager();

  }

  public void addFragmentWithBackStack(Fragment fragment,int id, boolean addToBackStack){
    try {
      GlobalReferences.getInstance().mCommonFragment = (CommonFragment) fragment;
      String backStateName = fragment.getClass().getName();
      String fragmentTag = backStateName;
      final FragmentManager manager = getSupportFragmentManager();
      if(manager!=null) {

        Log.e("manager.findFra)", manager.findFragmentByTag(fragmentTag) + "");

        Fragment fragment1 = manager.findFragmentByTag(fragmentTag);


        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //barber.flycuts.com.flycutsbarber.fragment not in back stack, create it.

          FragmentTransaction ft = manager.beginTransaction();
          //  ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
          ft.replace(id, fragment, fragmentTag);

          // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
          if (addToBackStack)
            ft.addToBackStack(backStateName);
          ft.commitAllowingStateLoss();
        } else {
          // Fragment fragment1 = manager.findFragmentByTag(fragmentTag);
          Log.e("alreadyyy", "aleadyyy");
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }



  public void addFragmentWithBackStack(Fragment fragment, boolean addToBackStack,Bundle bundle){
    try {
      String backStateName = fragment.getClass().getName();
      String fragmentTag = backStateName;
      Log.e(TAG, "addFragmentWithBackStack: name"+fragmentTag );

      final FragmentManager manager = getSupportFragmentManager();
      if(manager!=null) {

        Log.e("manager.findFra)", manager.findFragmentByTag(fragmentTag) + "");

        Fragment fragment1 = manager.findFragmentByTag(fragmentTag);


        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.

          FragmentTransaction ft = manager.beginTransaction();
          if (bundle!=null) {
            fragment.setArguments(bundle);
          }
          //  ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
          ft.replace(R.id.singlecarfragment, fragment, fragmentTag);

          // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
          if (addToBackStack)
            ft.addToBackStack(backStateName);
          ft.commitAllowingStateLoss();
        } else {
          // Fragment fragment1 = manager.findFragmentByTag(fragmentTag);

          if (fragmentTag.equals("blackflame.com.zymepro.FragmentSingleCar")){
            FragmentTransaction ft = manager.beginTransaction();
            if (bundle!=null) {
              fragment.setArguments(bundle);
            }
            //  ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
            ft.replace(R.id.singlecarfragment, fragment, fragmentTag);

            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (addToBackStack)
              ft.addToBackStack(backStateName);
            ft.commitAllowingStateLoss();
          }


          Log.e("alreadyyy", "aleadyyy");
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }




  @Override
  protected void onResume() {
    super.onResume();
    fragmentManager =getSupportFragmentManager();
    Log.e("manager",getSupportFragmentManager()+"");
  }

  public void replaceFragmentWithAnimation(Fragment fragment, String tag){
    Fragment currentFragment = (Fragment)GlobalReferences.getInstance().mCommonFragment;
    FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
    fragTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.enter_from_right, R.anim.enter_from_right,R.anim.enter_from_right);
    fragTransaction.detach(currentFragment);
    fragTransaction.attach(currentFragment);
    fragTransaction.commitAllowingStateLoss();
  }


  public  void hideSoftKeyboard() {
    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
  }
}
