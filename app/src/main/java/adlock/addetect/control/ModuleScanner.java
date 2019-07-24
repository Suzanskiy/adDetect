package adlock.addetect.control;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adlock.addetect.R;
import adlock.addetect.control.decompile.Extractor;
import adlock.addetect.model.ApplicationItem;
import adlock.addetect.view.dialog.ModuleScanDialog;


public class ModuleScanner extends AsyncTask<Void, Object, Boolean> {
    private static List<Module> sModuleList = new ArrayList();
    private Map<String, ApplicationItem> mApplicationItemMap = new HashMap();
    private OnCallbackListener mCallbackListener;
    private Context mContext;
    private ModuleScanDialog mDialog;
    private Extractor mExtract = new Extractor();
    private boolean mIsCancelled;
    private PackageManager mPackageManager;

    /* renamed from: krow.dev.addetector.control.ModuleScanner$1 */
    class C00981 implements OnCancelListener {
        C00981() {
        }

        public void onCancel(DialogInterface dialog) {
            if (ModuleScanner.this.mCallbackListener != null) {
                ModuleScanner.this.mCallbackListener.onCancel();
            }
            ModuleScanner.this.mIsCancelled = true;
            ModuleScanner.this.cancel(true);
        }
    }

    public interface OnCallbackListener {
        void onCancel();

        void onCompletion(List<ApplicationItem> list);

        void onProgress(String str, int i, int i2);

        void onStart();
    }

    static {
        sModuleList.add(Module.TAd);
        sModuleList.add(Module.Adam);
        sModuleList.add(Module.AdMob);
        sModuleList.add(Module.AdMobService);
        sModuleList.add(Module.Dawin);
        sModuleList.add(Module.InMobi);
        sModuleList.add(Module.Adlib);
        sModuleList.add(Module.AppLift);
        sModuleList.add(Module.TapJoy);
        sModuleList.add(Module.MillennialMedia);
        sModuleList.add(Module.AppFlood);
        sModuleList.add(Module.MoPub);
        sModuleList.add(Module.ShallWeAd);
        sModuleList.add(Module.Mojise);
        sModuleList.add(Module.AirPush);
        sModuleList.add(Module.LeadBolt);
        sModuleList.add(Module.Moolah);
        sModuleList.add(Module.SendDroid);
        sModuleList.add(Module.AppLovin);
        sModuleList.add(Module.Appboy);
        sModuleList.add(Module.Amazon);
    }

    public ModuleScanner(Activity activity) {
        this.mContext = activity;
    }

    public void setOnCallbackListener(OnCallbackListener callbackListener) {
        this.mCallbackListener = callbackListener;
    }

    public void stop() {
        if (this.mCallbackListener != null) {
            this.mCallbackListener.onCancel();
        }
        this.mIsCancelled = true;
        cancel(true);
    }

    @Override
    protected void onPreExecute() {
        if (this.mCallbackListener != null) {
            this.mCallbackListener.onStart();
        }
        mPackageManager = mContext.getPackageManager();
        this.mDialog = new ModuleScanDialog(this.mContext);
        this.mDialog.setTitle(this.mContext.getString(R.string.text_scan));
        this.mDialog.setCanceledOnTouchOutside(false);
        this.mDialog.setOnCancelListener(new C00981());
        this.mDialog.show();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (this.mDialog != null) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean isSucceed = true;
        List<PackageInfo> packageInfoList = this.mPackageManager.getInstalledPackages(0);
        int size = packageInfoList.size();
        for (int i = 0; i < size; i++) {
            if (this.mIsCancelled) {
                isSucceed = false;
                break;
            }
            try {
                PackageInfo packageInfo = this.mPackageManager.getPackageInfo(packageInfoList.get(i).packageName, 7);
                publishProgress(packageInfo.packageName, Integer.valueOf(i + 1), Integer.valueOf(size));
                int flag = packageInfo.applicationInfo.flags;
                if ((flag & 1) == 0 && (flag & 128) == 0 && this.mPackageManager.checkPermission("android.permission.INTERNET", packageInfo.packageName) == 0) {
                    parsePackage(packageInfo);
                }
            } catch (Exception ignored) {
            }
        }
       /* try {
            Thread.sleep(500);
        } catch (Exception ignored) {
        }*/
        return isSucceed;
    }

    @Override
    protected void onProgressUpdate(Object... items) {
        try {
            String name = (String) items[0];
            int index = (Integer) items[1];
            int size = (Integer) items[2];
            if (this.mCallbackListener != null) {
                this.mCallbackListener.onProgress(name, index, size);
            }
            if (this.mDialog != null) {
                this.mDialog.setProgress(index, size);
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result && this.mCallbackListener != null) {
            this.mCallbackListener.onCompletion(new ArrayList(this.mApplicationItemMap.values()));
        }
        if (this.mDialog != null) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }

    private void parsePackage(PackageInfo packageInfo) {
        PackageItemInfo[] activityInfoArray = packageInfo.activities;
        PackageItemInfo[] serviceInfoArray = packageInfo.services;
        PackageItemInfo[] receiverInfoArray = packageInfo.receivers;
        if (activityInfoArray.length > 0) {
            checkModle(packageInfo, activityInfoArray);
           // Log.v("Brod2","package info "+ activityInfoArray.toString());
        }
        if (serviceInfoArray.length > 0) {
            checkModle(packageInfo, serviceInfoArray);
          //  Log.v("Brod2","serviceInfoArray "+ serviceInfoArray.toString());

        }
        if (receiverInfoArray.length > 0) {
            checkModle(packageInfo, receiverInfoArray);
           // Log.v("Brod2","receiverInfoArray "+ receiverInfoArray.toString());
        }
    }

    private void checkModle(PackageInfo packageInfo, PackageItemInfo[] pakcageItemInfoArray) {
        for (PackageItemInfo packageItemInfo : pakcageItemInfoArray) {
            for (int i = 0; i < sModuleList.size(); i++) {
                Module module = sModuleList.get(i);
                if (packageItemInfo.name.indexOf(module.getName()) >= 0) {
                    ApplicationItem applicationItem = this.mApplicationItemMap.get(packageInfo.packageName);
                    if (applicationItem == null) {
                        applicationItem = new ApplicationItem(packageInfo);
                        this.mApplicationItemMap.put(packageInfo.packageName, applicationItem);
                    }
                  //  Log.v("Brod2", applicationItem.getPackageInfo().toString());
Log.v("Brod2", applicationItem.getPackageInfo().toString()+" "+ applicationItem.getModuleSet().toString());
                    applicationItem.addModule(module);
                }
            }
        }
    }

    @Deprecated
    private void parse(PackageInfo packageInfo) throws Exception {
        String path = packageInfo.applicationInfo.sourceDir;
        if (path != null && new File(path).exists()) {
            checkRule(packageInfo, this.mExtract.getManifest(path));
        }
    }

    @Deprecated
    private void checkRule(PackageInfo packageInfo, String xml) {
        String[] stringArray = xml.split(System.getProperty("line.separator"));
        for (String value : stringArray) {
            for (int j = 0; j < sModuleList.size(); j++) {
                Module module = sModuleList.get(j);
                if (value.indexOf(module.getName()) > 0) {
                    ApplicationItem applicationItem = this.mApplicationItemMap.get(packageInfo.packageName);
                    if (applicationItem == null) {
                        applicationItem = new ApplicationItem(packageInfo);
                        this.mApplicationItemMap.put(packageInfo.packageName, applicationItem);
                    }
                    applicationItem.addModule(module);
                }
            }
        }
    }
}
