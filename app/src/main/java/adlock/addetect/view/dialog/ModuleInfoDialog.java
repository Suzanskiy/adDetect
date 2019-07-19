package adlock.addetect.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import adlock.addetect.R;


public class ModuleInfoDialog extends Dialog implements OnClickListener {
    private TextView mContentView;
    private TextView mTitleView;

    public ModuleInfoDialog(Context context) {
        super(context);
        requestWindowFeature(1);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        LayoutParams windowParams = getWindow().getAttributes();
        windowParams.dimAmount = 0.7f;
        getWindow().setAttributes(windowParams);
        getWindow().setFlags(2, 2);
        setContentView(R.layout.dialog_info);
        this.mTitleView = findViewById(R.id.text_title);
        this.mContentView = findViewById(R.id.text_content);
        findViewById(R.id.button_confirm).setOnClickListener(this);
    }

    public void dismiss() {
        super.dismiss();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_confirm:
                dismiss();
                return;
            default:
                return;
        }
    }

    public void setTitle(String title) {
        this.mTitleView.setText(title);
    }

    public void setContent(String content) {
        this.mContentView.setText(content);
    }
}
