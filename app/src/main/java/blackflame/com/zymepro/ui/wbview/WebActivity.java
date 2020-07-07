package blackflame.com.zymepro.ui.wbview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import blackflame.com.zymepro.R;

public class WebActivity extends AppCompatActivity {
  WebView webview;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_web);
    setWebview();

    Bundle bundle=getIntent().getBundleExtra("bundle");
    if (bundle!=null){
      String url=bundle.getString("link");
      Log.e("URL", "onCreate: "+bundle.getString("link") );
      webview.loadUrl(url);
    }else{
      String url=getIntent().getStringExtra("url");
      webview.loadUrl(url);
    }
  }


  private void setWebview(){
    webview= findViewById(R.id.wv);
    //webView_Security.getSettings().setJavaScriptEnabled(true);
    webview.getSettings().setPluginState(WebSettings.PluginState.ON);
    webview.getSettings().setAllowFileAccess(true);
    webview.getSettings().setJavaScriptEnabled(true);
    webview.getSettings().setLoadWithOverviewMode(true);
    webview.getSettings().setUseWideViewPort(true);
    webview.setWebViewClient(new WebViewClient(){

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);

        return true;
      }
      @Override
      public void onPageFinished(WebView view, final String url) {

      }
    });
  }
}
