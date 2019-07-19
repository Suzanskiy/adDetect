package adlock.addetect.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adlock.addetect.R;
import adlock.addetect.model.ApplicationItem;
import adlock.addetect.view.LinkableTextView;


 public class AppListAdapter extends BaseAdapter {
    private static SimpleDateFormat sSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
    private ArrayList mApplicationItemList = new ArrayList();
    private LayoutInflater mLayoutInflater;
    private LinkableTextView.OnLinkClickListener mLinkClickListener;
    private PackageManager mPackageManager;

    public AppListAdapter(Context context) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mPackageManager = context.getPackageManager();
    }

    public void setApplicationItemList(List<ApplicationItem> applicationItemList) {
        this.mApplicationItemList.clear();
        this.mApplicationItemList.addAll(applicationItemList);
    }

    public ArrayList getApplicationItemList() {
        return this.mApplicationItemList;
    }

    public void setOnLinkClickListener(LinkableTextView.OnLinkClickListener linkClickListener) {
        this.mLinkClickListener = linkClickListener;
    }

    public int getCount() {
        return this.mApplicationItemList.size();
    }

    public Object getItem(int position) {
        return this.mApplicationItemList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    @SuppressLint({"ViewTag", "InflateParams"})
    public View getView(int position, View convertView, ViewGroup parent) {
        ApplicationItem applicationItem = (ApplicationItem) this.mApplicationItemList.get(position);
        View view = convertView;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_application, null);
            view.setTag(R.id.image_thumbnail, view.findViewById(R.id.image_thumbnail));
            view.setTag(R.id.text_title, view.findViewById(R.id.text_title));
            view.setTag(R.id.text_content, view.findViewById(R.id.text_content));
            view.setTag(R.id.text_size, view.findViewById(R.id.text_size));
            view.setTag(R.id.text_date, view.findViewById(R.id.text_date));
            view.setTag(R.id.text_module, view.findViewById(R.id.text_module));
        }
        view.setTag(applicationItem);
        PackageInfo packageInfo = applicationItem.getPackageInfo();
        File file = new File(packageInfo.applicationInfo.sourceDir);
        ((ImageView) view.getTag(R.id.image_thumbnail)).setImageDrawable(packageInfo.applicationInfo.loadIcon(this.mPackageManager));
        ((TextView) view.getTag(R.id.text_title)).setText(packageInfo.applicationInfo.loadLabel(this.mPackageManager));
        ((TextView) view.getTag(R.id.text_content)).setText(packageInfo.packageName);
        ((TextView) view.getTag(R.id.text_size)).setText(getFileSize(file));
        ((TextView) view.getTag(R.id.text_date)).setText(getLastModifiedDate(file));
        LinkableTextView moduleView = (LinkableTextView) view.getTag(R.id.text_module);
        moduleView.setOnLinkClickListener(this.mLinkClickListener);
        moduleView.gatherLinksForText(applicationItem.getModuleList());
        return view;
    }

    private static String getFileSize(File object) {
        long l;
        long l2 = l = 0L;
        if (((File)object).exists()) {
            try {
                l2 = ((File)object).length();
            }
            catch (Exception exception) {
                l2 = l;
            }
        }

               int n = 0;
        double d = 0.0;
        int n2 = 0;
        do {
            if (!((double)l2 / 1024.0 > 0.0)) {
                return String.valueOf(d) + new String[]{"Byte", "KB", "MB"}[n];
            }
            n = n2++;
            d = l2;
            l2 = (long)((double)l2 / 1024.0);
        } while (true);

    }


        private static String getLastModifiedDate(File file) {
            String lastModifiedDate = null;
            if (file.exists()) {
                try {
                    lastModifiedDate = sSimpleDateFormat.format(new Date(file.lastModified()));
                } catch (Exception ignored) {
                }
            }
            return lastModifiedDate;
        }
    }
