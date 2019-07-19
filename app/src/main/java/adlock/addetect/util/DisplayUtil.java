package adlock.addetect.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import adlock.addetect.R;


public class DisplayUtil {
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getNavigationBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getActionBarHeight(Context context) {
        int height = 0;
        try {
            height = context.getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height);
        } catch (Exception e) {
        }
        return height;
    }

    public static int getPixelToDimension(Context context, int pixel) {
        return pixel / (context.getResources().getDisplayMetrics().densityDpi / 160);
    }

    public static int getDimensionToPixel(Context context, float dip) {
        return (int) TypedValue.applyDimension(1, dip, context.getResources().getDisplayMetrics());
    }

    public static int getDimensionToId(Context context, int id) {
        try {
            return (int) context.getResources().getDimension(id);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getDisplayWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getDisplayHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}
