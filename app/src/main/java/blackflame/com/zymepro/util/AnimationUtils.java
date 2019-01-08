package blackflame.com.zymepro.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

public final class AnimationUtils {

  /**
   * Don't let anyone instantiate this class.
   */
  private AnimationUtils() {
    throw new Error("Do not need instantiate!");
  }

  /**
   * Default animation duration
   */
  public static final long DEFAULT_ANIMATION_DURATION = 400;

  /**
   * Get a rotation animation
   *
   * @param fromDegrees       Starting angle
   * @param toDegrees         End angle
   * @param pivotXType      Rotation center point X-axis coordinate relative type
   * @param pivotXValue Rotation center point X-axis coordinate
   * @param pivotYType Rotation center point Y-axis coordinate relative type
   * @param pivotYValue Rotation center point Y-axis coordinate
   * @param durationMillis duration
   * @param animationListener Animation listener
   * @return 一Rotating animation
   */
  public static RotateAnimation getRotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue, long durationMillis, AnimationListener animationListener) {
    RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue);
    rotateAnimation.setDuration(durationMillis);
    if (animationListener != null) {
      rotateAnimation.setAnimationListener(animationListener);
    }
    return rotateAnimation;
  }

  /**
   *
   * Get an animation that rotates according to the center point of the view itself
   *
   * @param durationMillis    Animation duration
   * @param animationListener Animation listener
   * @return 一
   * An animation that rotates according to the center point
   */
  public static RotateAnimation getRotateAnimationByCenter(long durationMillis, AnimationListener animationListener) {
    return getRotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, durationMillis, animationListener);
  }

  /**
   *
   * Get an animation that rotates according to the center point
   *
   * @param duration
   * Animation duration
   * @return 一
   * An animation that rotates according to the center point
   */
  public static RotateAnimation getRotateAnimationByCenter(long duration) {
    return getRotateAnimationByCenter(duration, null);
  }

  /**
   * Get an animation that rotates according to the center point of the view itself
   *
   * @param animationListener
   * Animation listener
   * @return
   * Animation based on the rotation of the center point
   */
  public static RotateAnimation getRotateAnimationByCenter(AnimationListener animationListener) {
    return getRotateAnimationByCenter(DEFAULT_ANIMATION_DURATION, animationListener);
  }

  /**
   *
   * Get an animation that rotates according to the center point
   *
   * @return 一
   * An animation that rotates based on a center point. The default duration is DEFAULT_ANIMATION_DURATION
   */
  public static RotateAnimation getRotateAnimationByCenter() {
    return getRotateAnimationByCenter(DEFAULT_ANIMATION_DURATION, null);
  }

  /**
   *
   * Get a transparency gradient animation
   *
   * @param fromAlpha         Initial transparency
   * @param toAlpha           Transparency at the end
   * @param durationMillis    duration
   * @param animationListener Animation listener
   * @return a transparency gradient animation
   */
  public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, long durationMillis, AnimationListener animationListener) {
    AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
    alphaAnimation.setDuration(durationMillis);
    if (animationListener != null) {
      alphaAnimation.setAnimationListener(animationListener);
    }
    return alphaAnimation;
  }

  /**
   *
   * Get a transparency gradient animation
   *
   * @param fromAlpha
   * Initial transparency
   * @param toAlpha        Transparency at the end
   * @param durationMillis
   * duration
   * @return
   * a transparency gradient animation
   */
  public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, long durationMillis) {
    return getAlphaAnimation(fromAlpha, toAlpha, durationMillis, null);
  }

  /**
   *
   * Get a transparency gradient animation
   *
   * @param fromAlpha         Initial transparency
   * @param toAlpha
   * Transparency at the end
   * @param animationListener
   * Animation listener
   * @return 一
   * Transparency gradient animation with a default duration of DEFAULT_ANIMATION_DURATION
   */
  public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, AnimationListener animationListener) {
    return getAlphaAnimation(fromAlpha, toAlpha, DEFAULT_ANIMATION_DURATION, animationListener);
  }

  /**
   *
   * Get a transparency gradient animation
   *
   * @param fromAlpha
   * Initial transparency
   * @param toAlpha
   * Transparency at the end
   * @return 一
   * Transparency gradient animation with a default duration o DEFAULT_ANIMATION_DURATION
   */
  public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha) {
    return getAlphaAnimation(fromAlpha, toAlpha, DEFAULT_ANIMATION_DURATION, null);
  }

  /**
   * Get a transparency gradient animation that changes from full to invisible
   *
   * @param durationMillis    duration
   * @param animationListener Animation listener
   * @return
   * A transparency gradient animation that changes from full to invisible
   */
  public static AlphaAnimation getHiddenAlphaAnimation(long durationMillis, AnimationListener animationListener) {
    return getAlphaAnimation(1.0f, 0.0f, durationMillis, animationListener);
  }

  /**
   *
   * Get a transparency gradient animation that changes from full to invisible
   *
   * @param durationMillis duration
   * @return 一
   * Transparency gradient animation that changes from full to invisible
   */
  public static AlphaAnimation getHiddenAlphaAnimation(long durationMillis) {
    return getHiddenAlphaAnimation(durationMillis, null);
  }

  /**
   *
   * Get a transparency gradient animation that changes from full to invisible
   *
   * @param animationListener
   * Animation listener
   * @return
   * A transparency gradient animation that changes from full to invisible, with a default duration of DEFAULT_ANIMATION_DURATION
   */
  public static AlphaAnimation getHiddenAlphaAnimation(AnimationListener animationListener) {
    return getHiddenAlphaAnimation(DEFAULT_ANIMATION_DURATION, animationListener);
  }

  /**
   *
   * Get a transparency gradient animation that changes from full to invisible
   *
   * @return 一A transparency gradient animation that changes from full to invisible, with a default duration of DEFAULT_ANIMATION_DURATION
   */
  public static AlphaAnimation getHiddenAlphaAnimation() {
    return getHiddenAlphaAnimation(DEFAULT_ANIMATION_DURATION, null);
  }

  /**
   *
   * Get a transparency gradient animation that changes from invisible to full display
   *
   * @param durationMillis
   * duration
   * @param animationListener
   *Animation listener
   * @return 一
   * Transparency gradient animation from invisible to full display
   */
  public static AlphaAnimation getShowAlphaAnimation(long durationMillis, AnimationListener animationListener) {
    return getAlphaAnimation(0.0f, 1.0f, durationMillis, animationListener);
  }

  /**
   * Get a transparency gradient animation that changes from invisible to full display
   *
   * @param durationMillis
   * duration
   * @return
   * A transparency gradient animation that changes from invisible to fully displayed
   */
  public static AlphaAnimation getShowAlphaAnimation(long durationMillis) {
    return getAlphaAnimation(0.0f, 1.0f, durationMillis, null);
  }

  /**
   *
   * Get a transparency gradient animation that changes from invisible to full display
   *
   * @param animationListener
   * Animation listener
   * @return
   * A transparency gradient animation that changes from invisible to fully displayed, with a default duration of DEFAULT_ANIMATION_DURATION
   */
  public static AlphaAnimation getShowAlphaAnimation(AnimationListener animationListener) {
    return getAlphaAnimation(0.0f, 1.0f, DEFAULT_ANIMATION_DURATION, animationListener);
  }

  /**
   *
   * Get a transparency gradient animation that changes from invisible to full display
   *
   * @return 一
   * A transparency gradient animation that changes from invisible to fully displayed, with a default duration of DEFAULT_ANIMATION_DURATION
   */
  public static AlphaAnimation getShowAlphaAnimation() {
    return getAlphaAnimation(0.0f, 1.0f, DEFAULT_ANIMATION_DURATION, null);
  }

  /**
   * Get a reduced animation
   *
   * @param durationMillis
   * @param animationListener
   * @return
   */
  public static ScaleAnimation getLessenScaleAnimation(long durationMillis, AnimationListener animationListener) {
    ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, ScaleAnimation.RELATIVE_TO_SELF, ScaleAnimation.RELATIVE_TO_SELF);
    scaleAnimation.setDuration(durationMillis);
    scaleAnimation.setAnimationListener(animationListener);
    return scaleAnimation;
  }

  /**
   *
   * Get a reduced animation
   *
   * @param durationMillis
   * @return
   */
  public static ScaleAnimation getLessenScaleAnimation(long durationMillis) {
    return getLessenScaleAnimation(durationMillis, null);
  }

  /**
   *
   * Get a reduced animation
   *
   * @param animationListener
   * @return
   */
  public static ScaleAnimation getLessenScaleAnimation(AnimationListener animationListener) {
    return getLessenScaleAnimation(DEFAULT_ANIMATION_DURATION, animationListener);
  }

  /**
   *
   * Get a reduced animation
   *
   * @param durationMillis
   * @param animationListener
   * @return
   */
  public static ScaleAnimation getAmplificationAnimation(long durationMillis, AnimationListener animationListener) {
    ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, ScaleAnimation.RELATIVE_TO_SELF, ScaleAnimation.RELATIVE_TO_SELF);
    scaleAnimation.setDuration(durationMillis);
    scaleAnimation.setAnimationListener(animationListener);
    return scaleAnimation;
  }

  /**
   *
   * Get a magnified animation
   *
   * @param durationMillis
   * @return
   */
  public static ScaleAnimation getAmplificationAnimation(long durationMillis) {
    return getAmplificationAnimation(durationMillis, null);
  }

  /**
   * Get a magnified animation
   *
   * @param animationListener
   * @return
   */
  public static ScaleAnimation getAmplificationAnimation(AnimationListener animationListener) {
    return getAmplificationAnimation(DEFAULT_ANIMATION_DURATION, animationListener);
  }

}