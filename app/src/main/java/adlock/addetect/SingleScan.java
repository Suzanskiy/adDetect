package adlock.addetect;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adlock.addetect.control.Module;
import adlock.addetect.model.ApplicationItem;

public class SingleScan extends AsyncTask<Void, Void, Void> {
    private String packageName;
    private PackageInfo packageInfo;
    private PackageManager packageManager;
    private Context mContext;
    ApplicationItem applicationItem;
    private static List<Module> sModuleList = new ArrayList();

    public SingleScan(Context ctx, String packageName) {
        this.mContext = ctx;
        this.packageName = packageName;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (applicationItem != null) {
            Log.v("Asynk2", applicationItem.getPackageInfo().packageName + "" + applicationItem.getModuleSet().toString());

     /*       Intent notificationIntent = new Intent(mContext, SingleScan.class);
            PendingIntent contentIntent = PendingIntent.getActivity(mContext,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationManager nm = (NotificationManager) mContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Resources res = mContext.getResources();
            Notification.Builder builder = new Notification.Builder(mContext);

            builder.setContentIntent(contentIntent)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.icon_logo))
                    .setTicker("Последнее китайское предупреждение!")
                    // .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(applicationItem.getPackageInfo().toString())
                    .setContentText("Рекламный модуль:" + applicationItem.getModuleSet().toString()); // Текст уведомления

            Notification n = builder.getNotification();

            if (nm != null) {
                nm.notify(4561, n);
            }*/

            Toast.makeText(mContext,"App:"+
                    applicationItem.getPackageInfo().packageName+"\n"+
                    applicationItem.getModuleSet().toString(),Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        packageManager = mContext.getPackageManager();

        try {
            packageInfo = packageManager.getPackageInfo(packageName,7 );
            int flag = packageInfo.applicationInfo.flags;
            if ((flag & 1) == 0 && (flag & 128) == 0 && this.packageManager.checkPermission("android.permission.INTERNET", packageInfo.packageName) == 0) {
                parsePackage(packageInfo);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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


    private void parsePackage(PackageInfo packageInfo) {
        PackageItemInfo[] activityInfoArray = packageInfo.activities;
        PackageItemInfo[] serviceInfoArray = packageInfo.services;
      //  PackageItemInfo[] receiverInfoArray = packageInfo.receivers;
        if (activityInfoArray.length > 0) {
            checkModle(packageInfo, activityInfoArray);
            // Log.v("Brod2","package info "+ activityInfoArray.toString());
        }
        if (serviceInfoArray.length > 0) {
            checkModle(packageInfo, serviceInfoArray);
            //  Log.v("Brod2","serviceInfoArray "+ serviceInfoArray.toString());

        }
      /*  if (receiverInfoArray.length > 0) {
            checkModle(packageInfo, receiverInfoArray);
            // Log.v("Brod2","receiverInfoArray "+ receiverInfoArray.toString());
        }*/
    }

    private void checkModle(PackageInfo packageInfo, PackageItemInfo[] pakcageItemInfoArray) {
        for (PackageItemInfo packageItemInfo : pakcageItemInfoArray) {
            for (int i = 0; i < sModuleList.size(); i++) {
                Module module = sModuleList.get(i);
                if (packageItemInfo.name.indexOf(module.getName()) >= 0) {
                   // applicationItem = null;
                    if (applicationItem == null) {
                        applicationItem = new ApplicationItem(packageInfo);
                        //  this.mApplicationItemMap.put(packageInfo.packageName, applicationItem);
                    }
                    //  Log.v("Brod2", applicationItem.getPackageInfo().toString());
                    Log.v("Brod2", applicationItem.getPackageInfo().packageName + " " + applicationItem.getModuleSet().toString());
                    applicationItem.addModule(module);
                }
            }
        }
    }
}
