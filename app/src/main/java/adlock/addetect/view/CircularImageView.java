package adlock.addetect.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import adlock.addetect.R;


@SuppressLint("AppCompatCustomView")
public class CircularImageView extends ImageView {
    private Paint mPaint;

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CircularImageView(Context context) {
        super(context);
        initialize(context);
    }

    private void initialize(Context context) {
        this.mPaint = new Paint(1);
        this.mPaint.setColor(getResources().getColor(R.color.teal));
    }

    private void setCircularDrawable(Bitmap bitmap) {
        setImageDrawable(new CircularDrawable(bitmap));
    }

    public void setImageBitmap(Bitmap bitmap) {
        setCircularDrawable(bitmap);
    }

    public void setImageResource(int resId) {
        super.setImageResource(resId);
        Drawable drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {
            setCircularDrawable(((BitmapDrawable) drawable).getBitmap());
        }
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (drawable instanceof BitmapDrawable) {
            setCircularDrawable(((BitmapDrawable) drawable).getBitmap());
        }
    }

    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        if (getDrawable() instanceof CircularDrawable) {
            super.onDraw(canvas);
        }
    }

    private void drawBackground(Canvas canvas) {
        int width = getMeasuredWidth() / 2;
        int height = getMeasuredHeight() / 2;
        canvas.drawCircle((float) width, (float) height, (float) Math.min(width, height), this.mPaint);
    }
}
