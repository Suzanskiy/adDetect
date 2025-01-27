package adlock.addetect.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

public class CircularDrawable extends Drawable {
    private final Bitmap mBitmap;
    private final int mBitmapHeight;
    private final int mBitmapWidth;
    private final Paint mPaint = new Paint();
    private final RectF mRectF = new RectF();

    public CircularDrawable(Bitmap bitmap) {
        this.mBitmap = bitmap;
        BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setShader(shader);
        this.mBitmapWidth = this.mBitmap.getWidth();
        this.mBitmapHeight = this.mBitmap.getHeight();
    }

    public void draw(Canvas canvas) {
        canvas.drawOval(this.mRectF, this.mPaint);
    }

    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        try {
            int radius = Math.min(bounds.width(), bounds.height()) / 2;
            this.mRectF.left = (float) (bounds.centerX() - radius);
            this.mRectF.right = (float) (bounds.centerX() + radius);
            this.mRectF.top = (float) (bounds.centerY() - radius);
            this.mRectF.bottom = (float) (bounds.centerY() + radius);
        } catch (Exception e) {
            this.mRectF.set(bounds);
        }
    }

    public void setAlpha(int alpha) {
        if (this.mPaint.getAlpha() != alpha) {
            this.mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }

    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }

    public void setAntiAlias(boolean aa) {
        this.mPaint.setAntiAlias(aa);
        invalidateSelf();
    }

    public void setFilterBitmap(boolean filter) {
        this.mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    public void setDither(boolean dither) {
        this.mPaint.setDither(dither);
        invalidateSelf();
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }
}
