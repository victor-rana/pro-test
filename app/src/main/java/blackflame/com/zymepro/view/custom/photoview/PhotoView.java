package blackflame.com.zymepro.view.custom.photoview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.widget.ImageView;
import blackflame.com.zymepro.view.custom.photoview.listener.OnMatrixChangedListener;
import blackflame.com.zymepro.view.custom.photoview.listener.OnOutsidePhotoTapListener;
import blackflame.com.zymepro.view.custom.photoview.listener.OnPhotoTapListener;
import blackflame.com.zymepro.view.custom.photoview.listener.OnScaleChangedListener;
import blackflame.com.zymepro.view.custom.photoview.listener.OnSingleFlingListener;


public class PhotoView extends ImageView {

  private PhotoViewAttacher attacher;

  public PhotoView(Context context) {
    this(context, null);
  }

  public PhotoView(Context context, AttributeSet attr) {
    this(context, attr, 0);
  }

  public PhotoView(Context context, AttributeSet attr, int defStyle) {
    super(context, attr, defStyle);
    init();
  }

  @TargetApi(21)
  public PhotoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init() {
    attacher = new PhotoViewAttacher(this);
    //We always pose as a Matrix scale type, though we can change to another scale type
    //via the attacher
    super.setScaleType(ScaleType.MATRIX);
  }

  /**
   * Get the current {@link PhotoViewAttacher} for this view. Be wary of holding on to references
   * to this attacher, as it has a reference to this view, which, if a reference is held in the
   * wrong place, can cause memory leaks.
   *
   * @return the attacher.
   */
  public PhotoViewAttacher getAttacher() {
    return attacher;
  }

  @Override
  public ScaleType getScaleType() {
    return attacher.getScaleType();
  }

  @Override
  public Matrix getImageMatrix() {
    return attacher.getImageMatrix();
  }

  @Override
  public void setOnLongClickListener(OnLongClickListener l) {
    attacher.setOnLongClickListener(l);
  }

  @Override
  public void setOnClickListener(OnClickListener l) {
    attacher.setOnClickListener(l);
  }

  @Override
  public void setScaleType(ScaleType scaleType) {
    if (attacher != null) {
      attacher.setScaleType(scaleType);
    }
  }

  @Override
  public void setImageDrawable(Drawable drawable) {
    super.setImageDrawable(drawable);
    // setImageBitmap calls through to this method
    if (attacher != null) {
      attacher.update();
    }
  }

  @Override
  public void setImageResource(int resId) {
    super.setImageResource(resId);
    if (attacher != null) {
      attacher.update();
    }
  }

  @Override
  public void setImageURI(Uri uri) {
    super.setImageURI(uri);
    if (attacher != null) {
      attacher.update();
    }
  }

  @Override
  protected boolean setFrame(int l, int t, int r, int b) {
    boolean changed = super.setFrame(l, t, r, b);
    if (changed) {
      attacher.update();
    }
    return changed;
  }

  public void setRotationTo(float rotationDegree) {
    attacher.setRotationTo(rotationDegree);
  }

  public void setRotationBy(float rotationDegree) {
    attacher.setRotationBy(rotationDegree);
  }

  public boolean isZoomEnabled() {
    return attacher.isZoomEnabled();
  }

  public RectF getDisplayRect() {
    return attacher.getDisplayRect();
  }

  public void getDisplayMatrix(Matrix matrix) {
    attacher.getDisplayMatrix(matrix);
  }

  public boolean setDisplayMatrix(Matrix finalRectangle) {
    return attacher.setDisplayMatrix(finalRectangle);
  }

  public float getMinimumScale() {
    return attacher.getMinimumScale();
  }

  public float getMediumScale() {
    return attacher.getMediumScale();
  }

  public float getMaximumScale() {
    return attacher.getMaximumScale();
  }

  public float getScale() {
    return attacher.getScale();
  }

  public void setAllowParentInterceptOnEdge(boolean allow) {
    attacher.setAllowParentInterceptOnEdge(allow);
  }

  public void setMinimumScale(float minimumScale) {
    attacher.setMinimumScale(minimumScale);
  }

  public void setMediumScale(float mediumScale) {
    attacher.setMediumScale(mediumScale);
  }

  public void setMaximumScale(float maximumScale) {
    attacher.setMaximumScale(maximumScale);
  }

  public void setScaleLevels(float minimumScale, float mediumScale, float maximumScale) {
    attacher.setScaleLevels(minimumScale, mediumScale, maximumScale);
  }

  public void setOnMatrixChangeListener(OnMatrixChangedListener listener) {
    attacher.setOnMatrixChangeListener(listener);
  }

  public void setOnPhotoTapListener(OnPhotoTapListener listener) {
    attacher.setOnPhotoTapListener(listener);
  }

  public void setOnOutsidePhotoTapListener(OnOutsidePhotoTapListener listener) {
    attacher.setOnOutsidePhotoTapListener(listener);
  }

  public void setScale(float scale) {
    attacher.setScale(scale);
  }

  public void setScale(float scale, boolean animate) {
    attacher.setScale(scale, animate);
  }

  public void setScale(float scale, float focalX, float focalY, boolean animate) {
    attacher.setScale(scale, focalX, focalY, animate);
  }

  public void setZoomable(boolean zoomable) {
    attacher.setZoomable(zoomable);
  }

  public void setZoomTransitionDuration(int milliseconds) {
    attacher.setZoomTransitionDuration(milliseconds);
  }

  public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener newOnDoubleTapListener) {
    attacher.setOnDoubleTapListener(newOnDoubleTapListener);
  }

  public void setOnScaleChangeListener(OnScaleChangedListener onScaleChangedListener) {
    attacher.setOnScaleChangeListener(onScaleChangedListener);
  }

  public void setOnSingleFlingListener(OnSingleFlingListener onSingleFlingListener) {
    attacher.setOnSingleFlingListener(onSingleFlingListener);
  }
}
