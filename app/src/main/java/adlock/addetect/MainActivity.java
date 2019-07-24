package adlock.addetect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.Objects;

import adlock.addetect.control.Module;
import adlock.addetect.control.ModuleScanner;
import adlock.addetect.model.ApplicationItem;
import adlock.addetect.util.DisplayUtil;
import adlock.addetect.view.FloatingButton;
import adlock.addetect.view.LinkableTextView;


import adlock.addetect.view.adapter.AppListAdapter;
import adlock.addetect.view.dialog.ModuleInfoDialog;
import krow.dev.addetector.util.SystemBarTintManager;


public class MainActivity extends AppCompatActivity {
    private OnItemClickListener mApplicationItemClickListener = new C00952();
    private AppListAdapter mApplicationListAdapter;
    private View mFooterview;
    private View mHeaderView;
    private LinkableTextView.OnLinkClickListener mLinkClickListener = (LinkableTextView.OnLinkClickListener) new onClickListener();
    private ModuleScanner.OnCallbackListener mModuleCallbackListener = (ModuleScanner.OnCallbackListener) new onCallbackListener();
    private ModuleScanner mModuleScanner;
    private FloatingButton mRefreshButton;
    private OnClickListener mRefreshClickListener = new C00964();
    private List<ApplicationItem> mApplicationItemList;

    class C00952 implements OnItemClickListener {
        C00952() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (view.getTag() != null) {
                PackageInfo packageInfo = ((ApplicationItem) view.getTag()).getPackageInfo();
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", packageInfo.packageName, null));
                    MainActivity.this.startActivity(intent);
                } catch (Exception ignored) {
                }
            }
        }
    }


    class C00964 implements OnClickListener {
        C00964() {
        }

        public void onClick(View view) {
            Log.e("test", "refresh click!!");
            MainActivity.this.refresh();
        }
    }


    class onCallbackListener implements ModuleScanner.OnCallbackListener {
        onCallbackListener() {
        }

        public void onStart() {
        }

        public void onProgress(String name, int index, int size) {
        }

        public void onCancel() {
            MainActivity.this.mRefreshButton.setVisibility(View.VISIBLE);
        }

        public void onCompletion(List<ApplicationItem> applicationItemList) {

            MainActivity.this.mApplicationListAdapter.setApplicationItemList(applicationItemList);
            MainActivity.this.mApplicationListAdapter.notifyDataSetChanged();
            MainActivity.this.mRefreshButton.setVisibility(View.VISIBLE);
            mApplicationItemList = applicationItemList;
        }
    }


    class onClickListener implements LinkableTextView.OnLinkClickListener {
        onClickListener() {
        }

        @Override
        public void onTextLinkClick(View textView, Module module) {
            ModuleInfoDialog moduleInfoDialog = new ModuleInfoDialog(MainActivity.this);
            moduleInfoDialog.setTitle(module.toString());
            moduleInfoDialog.setContent(MainActivity.this.getString(module.getDescriptionId()));
            moduleInfoDialog.setCanceledOnTouchOutside(true);
            moduleInfoDialog.show();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // getApplicationContext().unregisterReceiver(br);
    }

    BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  setupActionbar();
        initializeView();


        br = new myBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        getApplicationContext().registerReceiver(br, filter);


    }


    private void initializeView() {
        int headerHeight = DisplayUtil.getStatusBarHeight(getApplicationContext()) + DisplayUtil.getActionBarHeight(getApplicationContext());
        int footerHeight = DisplayUtil.getNavigationBarHeight(getApplicationContext());
        boolean isOverlay = false;
        if (VERSION.SDK_INT >= 19) {
            isOverlay = true;
          /*  this.mHeaderView = new View(getApplicationContext());
            this.mHeaderView.setLayoutParams(new LayoutParams(-1, headerHeight));*/
          /*  this.mFooterview = new View(getApplicationContext());
            this.mFooterview.setLayoutParams(new LayoutParams(-1, footerHeight));*/
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setNavigationBarTintEnabled(true);
            systemBarTintManager.setTintColor(getResources().getColor(R.color.teal700));
        }
        if (isOverlay) {
            headerHeight = 0;
        }
        bindRefreshButton(headerHeight);
        this.mApplicationListAdapter = new AppListAdapter(getApplicationContext());

        mApplicationListAdapter.setOnLinkClickListener(this.mLinkClickListener);//
        ListView mApplicationListView = (ListView) findViewById(R.id.list_package);
        mApplicationListView.setOnItemClickListener(this.mApplicationItemClickListener);
       /* mApplicationListView.addFooterView(getLayoutInflater().inflate(R.layout.footer_dummy, null));
        if (this.mHeaderView != null) {
            mApplicationListView.addHeaderView(this.mHeaderView);
        }
        if (this.mFooterview != null) {
            mApplicationListView.addFooterView(this.mFooterview);
        }*/
        mApplicationListView.setAdapter(this.mApplicationListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.mApplicationListAdapter.getCount() <= 0) {
            this.mRefreshButton.hide();
            scanModule();
        }
    }

    @Override
    protected void onStop() {
        stopScan();
        super.onStop();
    }

    private void bindRefreshButton(int headerSize) {
        Log.e("test", "header size: " + headerSize);
        int size = DisplayUtil.getDimensionToPixel(getApplicationContext(), 72.0f);
        int margin = DisplayUtil.getDimensionToPixel(getApplicationContext(), 16.0f);
        int height = DisplayUtil.getDisplayHeight(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(size, size);
        layoutParams.addRule(11);
        layoutParams.addRule(12);
        layoutParams.rightMargin = margin;
        layoutParams.bottomMargin = -size;
        Log.e("test", "height: " + height + " margin: " + margin + " size: " + size);
        this.mRefreshButton = new FloatingButton(getApplicationContext());
        this.mRefreshButton.setOnClickListener(this.mRefreshClickListener);
        this.mRefreshButton.setColor(getResources().getColor(R.color.teal));
        this.mRefreshButton.setDrawable(getResources().getDrawable(R.drawable.icon_refresh));
        this.mRefreshButton.setCurrentY(height - ((margin + size) + headerSize));
        this.mRefreshButton.setLayoutParams(layoutParams);
        ((ViewGroup) findViewById(R.id.layout_frame)).addView(this.mRefreshButton);
    }

    private void scanModule() {
        this.mModuleScanner = new ModuleScanner(this);
        this.mModuleScanner.setOnCallbackListener(this.mModuleCallbackListener);
        this.mModuleScanner.execute();
    }

    private void stopScan() {
        if (this.mModuleScanner != null) {
            this.mModuleScanner.stop();
            this.mModuleScanner = null;
        }
    }

    private void refresh() {
        if (this.mModuleScanner != null) {
            this.mModuleScanner.stop();
            this.mModuleScanner = null;
        }
        this.mRefreshButton.hide();
        scanModule();
    }
}
