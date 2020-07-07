package blackflame.com.zymepro.ui.about;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.ui.wbview.WebActivity;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.ToastUtils;
import java.util.List;

public class AboutUs extends BaseActivity implements OnClickListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about_us);
    GlobalReferences.getInstance().baseActivity=this;
    initViews();
  }


  private void initViews(){
    Toolbar toolbar= findViewById(R.id.toolbar_common);
    TextView title= findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"About us");
    ImageView iv_facebook= findViewById(R.id.iv_facebook);
    ImageView   iv_linkedin= findViewById(R.id.iv_linkedin);
    ImageView    iv_twitter= findViewById(R.id.iv_twitter);
    ImageView    iv_youtube= findViewById(R.id.iv_youtube);
    TextView   tv_website= findViewById(R.id.website);
    TextView   tv_email= findViewById(R.id.mail);
    iv_facebook.setOnClickListener(this);
    iv_linkedin.setOnClickListener(this);
    iv_twitter.setOnClickListener(this);
    iv_youtube.setOnClickListener(this);
    tv_website.setOnClickListener(this);
    tv_email.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    boolean isConnected = NetworkUtils.isConnected();
    switch(v.getId()){
      case R.id.iv_facebook:
        if(isConnected) {
          try {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookPageURL(AboutUs.this);
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
          }catch (Exception ex){

          }


        }else{
          ToastUtils.showShort("No internet connection.");

        }

        break ;
      case R.id.iv_linkedin:
        if(isConnected) {
          Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://in.linkedin.com/company/zyme-technologies"));
          final PackageManager packageManager = getPackageManager();
          final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
          if (list.isEmpty()) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=you"));
          }
          startActivity(intent);
        }else{
          ToastUtils.showShort("No internet connection.");
          }
        break;
      case R.id.iv_twitter:
        if(isConnected) {
          Intent intent = null;
          try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=getzyme"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/getzyme"));
          }
          startActivity(intent);

        }else{
          ToastUtils.showShort("No internet connection.");
        }
        break;
      case R.id.iv_youtube:
        if(isConnected) {
          Intent intent = new Intent(AboutUs.this, WebActivity.class);
          intent.putExtra("url", "https://www.youtube.com/channel/UCviRvLe7TeHKujRIJrRpInQ");
          startActivity(intent);
        }else{
          ToastUtils.showShort("No internet connection.");
        }
        break;
      case R.id.website:
        if(isConnected) {
          Intent intent = new Intent(this, WebActivity.class);
          intent.putExtra("url", "http://www.getzyme.com/");
          startActivity(intent);
        }else{
          ToastUtils.showShort("No internet connection.");
        }
        break;
      case R.id.mail:

        if (isConnected) {
          Intent i = new Intent(Intent.ACTION_SEND);
          i.setType("message/rfc822");
          i.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@getzyme.com"});

          try {
            startActivity(Intent.createChooser(i, "Send mail..."));
          } catch (android.content.ActivityNotFoundException ex) {
            ToastUtils.showShort("There are no email clients installed.");
          }
        }else{
          ToastUtils.showShort("No internet");
        }

        break;
    }


  }


  private String getFacebookPageURL(Context context) {
    String FACEBOOK_PAGE_ID = "Zyme";
    String FACEBOOK_URL = "https://www.facebook.com/getzyme/";
    PackageManager packageManager = context.getPackageManager();
    try {
      int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
      if (versionCode >= 3002850) { //newer versions of fb app
        return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
      } else { //older versions of fb app
        return "fb://page/" + FACEBOOK_PAGE_ID;
      }
    } catch (PackageManager.NameNotFoundException e) {
      return FACEBOOK_URL; //normal web url
    }
  }

  @Override
  public void indexScreen() {
    Analytics.index(AboutUs.this,"Aboutus");
  }
}
