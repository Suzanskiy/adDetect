package adlock.addetect.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import adlock.addetect.R;


public class ModuleScanDialog extends Dialog {
    private Context mContext;
    private ProgressBar mDownloadProgress;
    private TextView mIndexView;
    private TextView mPercentView;
    private TextView mSizeView;
    private TextView mTitleView;

    public ModuleScanDialog(Context context) {
        super(context);
        requestWindowFeature(1);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        LayoutParams windowParams = getWindow().getAttributes();
        windowParams.dimAmount = 0.7f;
        getWindow().setAttributes(windowParams);
        getWindow().setFlags(2, 2);
        setContentView(R.layout.dialog_scan);
        this.mContext = context;
        this.mTitleView = findViewById(R.id.text_title);
        this.mIndexView = findViewById(R.id.text_index);
        this.mSizeView = findViewById(R.id.text_size);
        this.mPercentView = findViewById(R.id.text_percent);
        this.mPercentView.setText("0%");
        this.mDownloadProgress = findViewById(R.id.progress_download);
        this.mDownloadProgress.setIndeterminate(false);
        this.mDownloadProgress.setMax(100);
    }

    public void dismiss() {
        super.dismiss();
    }

    public void setTitle(String title) {
        this.mTitleView.setText(title);
    }

    public void setTitle(int resourceId) {
        this.mTitleView.setText(this.mContext.getString(resourceId));
    }

    @SuppressLint("WrongConstant")
    public void setIndex(long index, long size) {
        if (size > 0) {
            this.mIndexView.setText("(" + index + "/" + size + ")");
            this.mIndexView.setVisibility(0);
            return;
        }
        this.mIndexView.setVisibility(8);

    }

    public void setProgress(int index, int size) {
        int progress = (index * 100) / size;
        this.mSizeView.setText(String.format("%d/%d", Integer.valueOf(index), Integer.valueOf(size)));
        this.mDownloadProgress.setProgress(progress);
        this.mPercentView.setText(String.valueOf(progress) + "%");
    }
}
