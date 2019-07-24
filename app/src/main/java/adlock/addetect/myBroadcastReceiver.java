package adlock.addetect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

 public class myBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String packageName = getPackageNameFromIntent(intent);
        SingleScan singleScan = new SingleScan(context,packageName);
        singleScan.execute();
    }
    private String getPackageNameFromIntent(Intent intent) {
        if (intent.getData() == null) {
            return null;
        }
        return intent.getData().getSchemeSpecificPart();
    }


}
