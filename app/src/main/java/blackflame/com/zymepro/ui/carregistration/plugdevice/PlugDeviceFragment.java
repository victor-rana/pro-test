package blackflame.com.zymepro.ui.carregistration.plugdevice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.common.CommonFragment;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.ui.carregistration.CarRegistration;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
public class PlugDeviceFragment extends CommonFragment {
  Button btn_progress;
  TextView textView_obd;
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.layout_plug_device, container, false);
    btn_progress= v.findViewById(R.id.btn_progress);
    ImageView imageView = v.findViewById(R.id.image_plugdevice);
    GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
    Glide.with(this).load(R.mipmap.device_plug).into(imageViewTarget);
    textView_obd= v.findViewById(R.id.cannotfindobd);
    textView_obd.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        new AwesomeInfoDialog(getActivity())
            .setTitle("Can't find port?")
            .setMessage("Please drop us a mail on support@getzyme.com with your car make and model and we will get back to you with the port location shortly.")
            .setColoredCircle(R.color.colorAccent)
            .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
            .setCancelable(true)
            .setPositiveButtonText("Ok")
            .setPositiveButtonbackgroundColor(R.color.colorAccent)
            .setPositiveButtonTextColor(R.color.white)
            .setPositiveButtonClick(new Closure() {
              @Override
              public void exec() {
              }
            })
            .show();
      }
    });
    btn_progress.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ((CarRegistration) GlobalReferences.getInstance().baseActivity).mViewPager.setCurrentItem(3);
      }
    });
    return v;
  }



}
