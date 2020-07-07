package blackflame.com.zymepro.view.custom;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ZymeProViewPager extends ViewPager {
  public ZymeProViewPager(Context context) {
    super(context);
  }
  private boolean isPagingEnabled = true;



  public ZymeProViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return this.isPagingEnabled && super.onTouchEvent(event);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    return this.isPagingEnabled && super.onInterceptTouchEvent(event);
  }

  public void setPagingEnabled(boolean b) {
    this.isPagingEnabled = b;
  }
}
