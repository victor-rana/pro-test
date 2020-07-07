package blackflame.com.zymepro.ui.carregistration;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import blackflame.com.zymepro.ui.carregistration.carInfo.CarInfoFragment;
import blackflame.com.zymepro.ui.carregistration.index.IndexFragment;
import blackflame.com.zymepro.ui.carregistration.plugdevice.PlugDeviceFragment;
import blackflame.com.zymepro.ui.carregistration.qrscan.QrscanFragment;

public class CarinfoViewPagerAdapter extends FragmentPagerAdapter {
  private LayoutInflater layoutInflater;

  public CarinfoViewPagerAdapter(FragmentManager fm) {
    super(fm);
  }


  @Override
  public Fragment getItem(int position) {
    switch (position) {

      case 0:
        return new IndexFragment();
      case 1:
        return new QrscanFragment();
      case 2:
        return new PlugDeviceFragment();
      case 3:
        return new CarInfoFragment();
      default: return new IndexFragment();
    }

  }

  @Override
  public int getCount() {
    return 4;
  }

}
