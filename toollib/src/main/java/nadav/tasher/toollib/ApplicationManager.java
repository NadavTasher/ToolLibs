package nadav.tasher.toollib;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class ApplicationManager {
	public static final int FLAG_SHOW_ONLY_RUNABLES=1;
	public static final int FLAG_SHOW_ALL=0;
	public static final int FLAG_ONLY_SYSTEM=2;
	public static ArrayList<App> getApps(Context cx, int flags) {
		PackageManager pm=cx.getPackageManager();
		ArrayList<ApplicationInfo> apps=new ArrayList<>(pm.getInstalledApplications(0));
		switch(flags){
			case FLAG_ONLY_SYSTEM:
				ArrayList<App> realapps=new ArrayList<>();
				for(int app=0; app<apps.size(); app++){
					if(apps.get(app).flags!=ApplicationInfo.FLAG_SYSTEM){
						ApplicationInfo a=apps.get(app);
						Drawable ic=null;
						try{
							ic=pm.getApplicationIcon(a.packageName);
						}catch(PackageManager.NameNotFoundException e){
							e.printStackTrace();
						}
						realapps.add(new App((String) pm.getApplicationLabel(a), a.packageName, a.flags, a.targetSdkVersion, ic, pm.getLaunchIntentForPackage(a.packageName), Extras.getVersionCode(cx, a.packageName), Extras.getVersionName(cx, a.packageName)));
					}
				}
				return realapps;
			case FLAG_SHOW_ALL:
				ArrayList<App> realapps2=new ArrayList<>();
				for(int app=0; app<apps.size(); app++){
					ApplicationInfo a=apps.get(app);
					Drawable ic=null;
					try{
						ic=pm.getApplicationIcon(a.packageName);
					}catch(PackageManager.NameNotFoundException e){
						e.printStackTrace();
					}
					realapps2.add(new App((String) pm.getApplicationLabel(a), a.packageName, a.flags, a.targetSdkVersion, ic, pm.getLaunchIntentForPackage(a.packageName), Extras.getVersionCode(cx, a.packageName), Extras.getVersionName(cx, a.packageName)));
				}
				return realapps2;
			case FLAG_SHOW_ONLY_RUNABLES:
				ArrayList<App> realapps3=new ArrayList<>();
				for(int app=0; app<apps.size(); app++){
					if(pm.getLaunchIntentForPackage(apps.get(app).packageName)!=null){
						ApplicationInfo a=apps.get(app);
						Drawable ic=null;
						try{
							ic=pm.getApplicationIcon(a.packageName);
						}catch(PackageManager.NameNotFoundException e){
							e.printStackTrace();
						}
						realapps3.add(new App((String) pm.getApplicationLabel(a), a.packageName, a.flags, a.targetSdkVersion, ic, pm.getLaunchIntentForPackage(a.packageName), Extras.getVersionCode(cx, a.packageName), Extras.getVersionName(cx, a.packageName)));
					}
				}
				return realapps3;
			default:
				return new ArrayList<>();
		}
	}
	public static ArrayList<String> getAppNames(Context cx, int flags) {
		PackageManager pm=cx.getPackageManager();
		ArrayList<ApplicationInfo> apps=new ArrayList<>(pm.getInstalledApplications(0));
		switch(flags){
			case FLAG_ONLY_SYSTEM:
				ArrayList<String> realapps=new ArrayList<>();
				for(int app=0; app<apps.size(); app++){
					if(apps.get(app).flags!=ApplicationInfo.FLAG_SYSTEM){
						ApplicationInfo a=apps.get(app);
						realapps.add((String) pm.getApplicationLabel(a));
					}
				}
				return realapps;
			case FLAG_SHOW_ALL:
				ArrayList<String> realapps2=new ArrayList<>();
				for(int app=0; app<apps.size(); app++){
					ApplicationInfo a=apps.get(app);
					realapps2.add((String) pm.getApplicationLabel(a));
				}
				return realapps2;
			case FLAG_SHOW_ONLY_RUNABLES:
				ArrayList<String> realapps3=new ArrayList<>();
				for(int app=0; app<apps.size(); app++){
					if(pm.getLaunchIntentForPackage(apps.get(app).packageName)!=null){
						ApplicationInfo a=apps.get(app);
						realapps3.add((String) pm.getApplicationLabel(a));
					}
				}
				return realapps3;
			default:
				return new ArrayList<>();
		}
	}
	public static class App {
		private String appName;
		private String packageName;
		private int flags;
		private int targetSDKversion;
		private Drawable appIcon;
		private Intent launchIntent;
		private String versionName;
		private int versionCode;
		public App(String appName, String packageName, int flags, int targetSDKversion, Drawable appIcon, Intent launchIntent, int versionCode, String versionName) {
			this.appName=appName;
			this.packageName=packageName;
			this.flags=flags;
			this.targetSDKversion=targetSDKversion;
			this.appIcon=appIcon;
			this.launchIntent=launchIntent;
			this.versionCode=versionCode;
			this.versionName=versionName;
		}
		public String getName() {
			return appName;
		}
		public String getPackage() {
			return packageName;
		}
		public int getFlags() {
			return flags;
		}
		public Drawable getIcon() {
			return appIcon;
		}
		public Intent getIntent() {
			return launchIntent;
		}
		public int getTargetSDK() {
			return targetSDKversion;
		}
		public int getVersionCode() {
			return versionCode;
		}
		public String getVersionName() {
			return versionName;
		}
	}
}
