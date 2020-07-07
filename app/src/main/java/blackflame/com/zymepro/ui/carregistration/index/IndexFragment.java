package blackflame.com.zymepro.ui.carregistration.index;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.common.CommonFragment;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.ui.carregistration.CarRegistration;

public class IndexFragment extends CommonFragment {

  Button btn_index_next;
  @Nullable
  @Override

  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.index_fragment, container, false);
    btn_index_next= v.findViewById(R.id.btn_carinfo_start);
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F);
    btn_index_next.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(buttonClick);
        ((CarRegistration)GlobalReferences.getInstance().baseActivity).mViewPager.setCurrentItem(1);
      }
    });


    return v;
  }


  public static IndexFragment newInstance(String text) {

    IndexFragment f = new IndexFragment();
    Bundle b = new Bundle();
    b.putString("msg", text);

    f.setArguments(b);

    return f;
  }

  @Override
  public void onResume() {
    super.onResume();

    // register connection status listener
  }
}
