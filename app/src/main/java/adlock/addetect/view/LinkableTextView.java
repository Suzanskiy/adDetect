package adlock.addetect.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import adlock.addetect.control.Module;


@SuppressLint("AppCompatCustomView")
public class LinkableTextView extends TextView {
    private OnLinkClickListener mLinkClickListener;

    private class Hyperlink {
        private int end;
        private InternalURLSpan span;
        private int start;

        private Hyperlink() {
        }
    }

    public class InternalURLSpan extends ClickableSpan {
        private Module mModule;

        public InternalURLSpan(Module module) {
            this.mModule = module;
        }

        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(true);
        }

        public void onClick(View textView) {
            if (LinkableTextView.this.mLinkClickListener != null) {
                LinkableTextView.this.mLinkClickListener.onTextLinkClick(textView, this.mModule);
            }
        }
    }

    public interface OnLinkClickListener {
        void onTextLinkClick(View view, Module module);
    }

    public LinkableTextView(Context context) {
        super(context);
    }

    public LinkableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinkableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        CharSequence object = getText();
        if (!(object instanceof Spanned)) {
            return false;
        }
        Spanned spanned = (Spanned) object;
        int action = event.getAction();
        if (action != 1 && action != 0) {
            return false;
        }
        int x = (((int) event.getX()) - getTotalPaddingLeft()) + getScrollX();
        int y = (((int) event.getY()) - getTotalPaddingTop()) + getScrollY();
        Layout layout = getLayout();
        int offset = layout.getOffsetForHorizontal(layout.getLineForVertical(y), (float) x);
        ClickableSpan[] link = spanned.getSpans(offset, offset, ClickableSpan.class);
        if (link.length == 0) {
            return false;
        }
        if (action == 1) {
            link[0].onClick(this);
        }
        return true;
    }

    public void gatherLinksForText(List<Module> moduleList) {
        int i;
        List<Hyperlink> hyperLinkList = new ArrayList();
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        for (i = 0; i < moduleList.size(); i++) {
            Hyperlink hyperlink = new Hyperlink();
            Module module = moduleList.get(i);
            String tag = module.toString();
            hyperlink.span = new InternalURLSpan(module);
            hyperlink.start = index;
            hyperlink.end = tag.length() + index;
            stringBuilder.append(tag);
            index = hyperlink.end;
            if (i < moduleList.size() - 1) {
                stringBuilder.append(", ");
                index += 2;
            }
            hyperLinkList.add(hyperlink);
        }
        SpannableString spannableString = new SpannableString(stringBuilder.toString());
        for (i = 0; i < hyperLinkList.size(); i++) {
           Hyperlink hyperlink = hyperLinkList.get(i);
            spannableString.setSpan(hyperlink.span, hyperlink.start, hyperlink.end, 33);
        }
        setText(spannableString);
    }

    public void setOnLinkClickListener(OnLinkClickListener linkClickListener) {
        this.mLinkClickListener = linkClickListener;
    }
}
