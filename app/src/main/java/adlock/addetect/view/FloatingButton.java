package adlock.addetect.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;

import adlock.addetect.util.DisplayUtil;


@SuppressLint("AppCompatCustomView")
public class FloatingButton extends Button {

    private Bitmap mBitmap;
    private Paint mButtonPaint;
    private float mCurrentY;
    private Paint mDrawablePaint;
    private boolean mHidden = true;
    private int mScreenHeight;
    private Animator.AnimatorListener mShowAnimatorListener = new animListener();

    /* renamed from: krow.dev.addetector.view.FloatingButton$1 */
    @SuppressLint("NewApi")
    class animListener implements Animator.AnimatorListener {
        animListener() {
        }

        public void onAnimationStart(Animator animation) {
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
            if (VERSION.SDK_INT < 11) {
                FloatingButton.this.clearAnimation();
                int size = DisplayUtil.getDimensionToPixel(FloatingButton.this.getContext(), 72.0f);
                int margin = DisplayUtil.getDimensionToPixel(FloatingButton.this.getContext(), 16.0f);
                LayoutParams layoutParams = new LayoutParams(size, size);
                layoutParams.addRule(11);
                layoutParams.addRule(12);
                layoutParams.rightMargin = margin;
                layoutParams.bottomMargin = margin;
                FloatingButton.this.setLayoutParams(layoutParams);
            }
        }

        public void onAnimationCancel(Animator animation) {
        }
    }

    public FloatingButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context);
    }

    public FloatingButton(Context context) {
        super(context);
        initialize(context);
    }

    public void setColor(int color) {
        this.mButtonPaint.setColor(color);
        invalidate();
    }

    public void setDrawable(Drawable drawable) {
        this.mBitmap = ((BitmapDrawable) drawable).getBitmap();
        invalidate();
    }

    public void setCurrentY(int y) {
        this.mCurrentY = (float) y;
    }

    @TargetApi(11)
    public void initialize(Context context) {
        if (VERSION.SDK_INT >= 11) {
            setLayerType(1, null);
        }
        setWillNotDraw(false);
        setBackgroundColor(0);
        this.mButtonPaint = new Paint(1);
        this.mButtonPaint.setColor(-1);
        this.mButtonPaint.setStyle(Style.FILL);
        this.mButtonPaint.setShadowLayer(10.0f, 0.0f, 3.5f, Color.argb(100, 0, 0, 0));
        this.mDrawablePaint = new Paint(1);
        invalidate();
        this.mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    protected void onDraw(Canvas canvas) {
        setClickable(true);
        canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (float) (((double) getWidth()) / 2.6d), this.mButtonPaint);
        canvas.drawBitmap(this.mBitmap, (float) ((getWidth() - this.mBitmap.getWidth()) / 2), (float) ((getHeight() - this.mBitmap.getHeight()) / 2), this.mDrawablePaint);
    }

   /* @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 1) {
                    ViewHelper.setAlpha(this, 1.0f);
        } else if (event.getAction() == 0) {
            ViewHelper.setAlpha(this, 0.6f);
        }
        return super.onTouchEvent(event);
    }*/

    public int dpToPx(int dp) {
        return Math.round(((float) dp) * (getContext().getResources().getDisplayMetrics().xdpi / 160.0f));
    }

    @SuppressLint("NewApi")
    public void hide() {
        if (!this.mHidden) {
            getLocationOnScreen(new int[2]);
            @SuppressLint("ObjectAnimatorBinding") ObjectAnimator hideAnimator = ObjectAnimator.ofFloat((Object) this, "y", (float) this.mScreenHeight);
            hideAnimator.setInterpolator(new AccelerateInterpolator());
            hideAnimator.start();
            this.mHidden = true;
        }
    }

    @SuppressLint("NewApi")
    public void show() {
        if (this.mHidden) {
            @SuppressLint("ObjectAnimatorBinding") ObjectAnimator showAnimator = ObjectAnimator.ofFloat((Object) this, "y", this.mCurrentY);
            showAnimator.setInterpolator(new DecelerateInterpolator());
            showAnimator.addListener(this.mShowAnimatorListener);
            showAnimator.start();
            this.mHidden = false;
        }
    }
}
