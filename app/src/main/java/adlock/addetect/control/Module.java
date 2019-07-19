package adlock.addetect.control;

import java.util.HashMap;
import java.util.Map;

import adlock.addetect.R;


public enum Module {
    TAd("com.skplanet.tad", R.string.description_tad),
    Adam("net.daum.adam.publisher", R.string.description_adam),
    Dawin("com.incross.dawin", R.string.description_dawin),
    AdMob("com.google.ads", R.string.description_admob),
    AdMobService("com.google.android.gms.ads", R.string.description_admob),
    InMobi("com.inmobi.androidsdk", R.string.description_inmobi),
    Adlib("com.mocoplex.adlib", R.string.description_ablib),
    AppLift("com.hf.appliftsdk", R.string.description_applift),
    TapJoy("com.tapjoy", R.string.description_tapjoy),
    MillennialMedia("com.millennialmedia", R.string.description_millennialmedia),
    AppFlood("com.appflood", R.string.description_appflood),
    MoPub("com.mopub.mobileads", R.string.description_mopub),
    ShallWeAd("com.jm.co.shallwead.sdk", R.string.description_shallwead),
    Mojise("kr.com.mojise.sdk", R.string.description_mojise),
    AirPush("com.airpush", R.string.description_airpush),
    LeadBolt("com.leadBolt", R.string.description_leadbolt),
    Moolah("com.adnotify", R.string.description_moolah),
    SendDroid("com.senddroid", R.string.description_senddroid),
    AppLovin("com.applovin", R.string.description_applovin),
    Appboy("com.appboy", R.string.description_appboy),
    Amazon("com.amazon.device.ads", R.string.description_amazon);

    public static Map<String, Module> ModuleStore = null;
    private int mDescriptionId;
    private String mName;

    static {
        ModuleStore = new HashMap();
        ModuleStore.put(TAd.mName, TAd);
        ModuleStore.put(Adam.mName, Adam);
        ModuleStore.put(Dawin.mName, Dawin);
        ModuleStore.put(AdMob.mName, AdMob);
        ModuleStore.put(AdMobService.mName, AdMobService);
        ModuleStore.put(InMobi.mName, InMobi);
        ModuleStore.put(Adlib.mName, Adlib);
        ModuleStore.put(AppLift.mName, AppLift);
        ModuleStore.put(TapJoy.mName, TapJoy);
        ModuleStore.put(MillennialMedia.mName, MillennialMedia);
        ModuleStore.put(AppFlood.mName, AppFlood);
        ModuleStore.put(MoPub.mName, MoPub);
        ModuleStore.put(ShallWeAd.mName, ShallWeAd);
        ModuleStore.put(Mojise.mName, Mojise);
        ModuleStore.put(AirPush.mName, AirPush);
        ModuleStore.put(LeadBolt.mName, LeadBolt);
        ModuleStore.put(Moolah.mName, Moolah);
        ModuleStore.put(SendDroid.mName, SendDroid);
        ModuleStore.put(AppLovin.mName, AppLovin);
        ModuleStore.put(Appboy.mName, Appboy);
    }

    Module(String name, int descriptionId) {
        this.mName = name;
        this.mDescriptionId = descriptionId;
    }

    public static Module getModule(String name) {
        return ModuleStore.get(name);
    }

    public String getName() {
        return this.mName;
    }

    public int getDescriptionId() {
        return this.mDescriptionId;
    }
}



/*
package adlock.addetect.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Module {
    private String name;
    private String packageName;

    public static List<Module> moduleStore = new ArrayList<>();

    public Module(String name, String pkgName) {
        this.name = name;
        this.packageName = pkgName;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }


    public void init() {
        moduleStore.add(new Module("Adam", "net.daum.adam.publisher"));
        moduleStore.add(new Module("Dawin", "com.incross.dawin"));
        moduleStore.add(new Module("AdMob", "com.google.ads"));
        moduleStore.add(new Module("AdMobService", "com.google.android.gms.ads"));
        moduleStore.add(new Module("InMobi", "com.inmobi.androidsdk"));
        moduleStore.add(new Module("Adlib", "com.mocoplex.adlib"));
        moduleStore.add(new Module("AppLift", "com.hf.appliftsdk"));
        moduleStore.add(new Module("TapJoy", "com.tapjoy"));
        moduleStore.add(new Module("MillennialMedia", "com.millennialmedia"));
        moduleStore.add(new Module("AppFlood", "com.appflood"));
        moduleStore.add(new Module("MoPub", "com.mopub.mobileads"));
        moduleStore.add(new Module("ShallWeAd", "com.jm.co.shallwead.sdk"));
        moduleStore.add(new Module("Mojise", "kr.com.mojise.sdk"));
        moduleStore.add(new Module("AirPush", "com.airpush"));
        moduleStore.add(new Module("LeadBolt", "com.leadBolt"));
        moduleStore.add(new Module("Moolah", "com.adnotify"));
        moduleStore.add(new Module("SendDroid", "com.senddroid"));
        moduleStore.add(new Module("AppLovin", "com.applovin"));
        moduleStore.add(new Module("Appboy", "com.appboy"));
        moduleStore.add(new Module("Amazon", "com.amazon.device.ads"));
        moduleStore.add(new Module("TAd", "com.skplanet.tad"));

    }

}
*/
