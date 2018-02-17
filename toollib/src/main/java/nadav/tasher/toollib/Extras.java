package nadav.tasher.toollib;

import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;
import android.view.WindowManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Extras {
	public static boolean isOnline(Context c) {
		return ((ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo()!=null;
	}
	public static boolean isWifi(Context c) {
		ConnectivityManager connectivityManager=((ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE));
		Network[] networks=connectivityManager.getAllNetworks();
		if(networks==null){
			return false;
		}else{
			for(Network network : networks){
				NetworkInfo info=connectivityManager.getNetworkInfo(network);
				if(info!=null&&info.getType()==ConnectivityManager.TYPE_WIFI){
					if(info.isAvailable()&&info.isConnected()){
						return true;
					}
				}
			}
		}
		return false;
	}
	public static boolean isInstalled(Context con, String packageName) {
		try{
			con.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			return true;
		}catch(PackageManager.NameNotFoundException e){
			return false;
		}
	}
	public static int getVersionCode(Context con, String packagename) {
		try{
			return con.getPackageManager().getPackageInfo(packagename, PackageManager.GET_ACTIVITIES).versionCode;
		}catch(PackageManager.NameNotFoundException e){
			e.printStackTrace();
			return 0;
		}
	}
	public static String getVersionName(Context con, String packagename) {
		try{
			return con.getPackageManager().getPackageInfo(packagename, PackageManager.GET_ACTIVITIES).versionName;
		}catch(PackageManager.NameNotFoundException e){
			e.printStackTrace();
			return null;
		}
	}
	public static int screenX(Context con) {
		Display display=((WindowManager) con.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point size=new Point();
		display.getSize(size);
		return size.x;
	}
	public static int screenY(Context con) {
		Display display=((WindowManager) con.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point size=new Point();
		display.getSize(size);
		return size.y;
	}
	public static boolean isTablet(Context con) {
		return screenX(con)>screenY(con);
	}
	public static void copyToClipboard(Context con, String s) {
		ClipboardManager cm=(ClipboardManager) con.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip=ClipData.newPlainText("label", s);
		cm.setPrimaryClip(clip);
	}
	public static void openUrl(Context con, String s) {
		Intent i;
		String url=s;
		if(!s.startsWith("http://")&&!s.startsWith("https://")){
			url="http://" + s;
		}
		i=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		con.startActivity(i);
	}
	public static void setWallpaper(Context con, Bitmap b) {
		WallpaperManager wp=WallpaperManager.getInstance(con);
		try{
			wp.setBitmap(b);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void setWallpaper(Context con, int b) {
		WallpaperManager wp=WallpaperManager.getInstance(con);
		try{
			wp.setResource(b);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void setRingtone(Context con, String fname, String title) {
		ContentValues values=new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, fname);
		values.put(MediaStore.MediaColumns.TITLE, title);
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_ALARM, true);
		Uri uri=MediaStore.Audio.Media.getContentUriForPath(fname);
		Uri newUri=con.getContentResolver().insert(uri, values);
		RingtoneManager.setActualDefaultRingtoneUri(con, RingtoneManager.TYPE_RINGTONE, newUri);
	}
	public static void setNotificationSound(Context con, String fname, String selectionName) {
		ContentValues values=new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, fname);
		values.put(MediaStore.MediaColumns.TITLE, selectionName);
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/ogg");
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
		Uri uri=MediaStore.Audio.Media.getContentUriForPath(fname);
		Uri newUri=con.getContentResolver().insert(uri, values);
		RingtoneManager.setActualDefaultRingtoneUri(con, RingtoneManager.TYPE_NOTIFICATION, newUri);
	}

	public static class Numerals {
		public static final String ONE="I";
		public static final String TWO="II";
		public static final String THREE="III";
		public static final String FOUR="IV";
		public static final String FIVE="V";
		public static final String SIX="VI";
		public static final String SEVEN="VII";
		public static final String EIGHT="VIII";
		public static final String NINE="IX";
		public static final String TEN="X";
		public static final String TWENTY="XX";
		public static final String THIRTY="XXX";
		public static final String FOURTY="XL";
		public static final String FIFTY="L";
		public static final String SIXTY="LX";
		public static final String SEVENTY="LXX";
		public static final String EIGHTY="LXXX";
		public static final String NINETY="XC";
		public static final String HUNDRED="C";
		public static final String TWO_HUNDRED="CC";
		public static final String THREE_HUNDRED="CCC";
		public static final String FOUR_HUNDRED="CD";
		public static final String FIVE_HUNDRED="D";
		public static final String SIX_HUNDRED="DC";
		public static final String SEVEN_HUNDRED="DCC";
		public static final String EIGHT_HUNDRED="DCCC";
		public static final String NINE_HUNDRED="CM";
		public static final String THOUSEND="M";
		public static final String TWO_THOUSEND="MM";
		public static final String THREE_THOUSEND="MMM";
		public static final String FOUR_THOUSEND="MMMM";
		static private final int ONES=-1;
		static private final int TENS=-2;
		static private final int HUNS=-3;
		static private final int TOUS=-4;
		private static String text="";
		static private int num=ONES;
		public static String get(int i) {
			if(i<5000){
				text="";
				String base="" + i;
				for(int c=base.length() - 1; c>=0; c--){
					int before=0;
					if(c!=base.length() - 1){
						before=Integer.parseInt(base.charAt(c + 1) + "");
					}
					int current=Integer.parseInt(base.charAt(c) + "");
					switch(num){
						case ONES:
							ones(current, before);
							break;
						case TENS:
							tens(current, before);
							break;
						case HUNS:
							huns(current, before);
							break;
						case TOUS:
							tous(current, before);
							break;
					}
					if(num!=TOUS){
						add("-");
						num--;
					}
				}
				return organized(text);
			}else{
				return "Number Is Larger Then 4999";
			}
		}
		static private void ones(int c, int l) {
			switch(c){
				case 1:
					add(ONE);
					break;
				case 2:
					add(TWO);
					break;
				case 3:
					add(THREE);
					break;
				case 4:
					add(FOUR);
					break;
				case 5:
					add(FIVE);
					break;
				case 6:
					add(SIX);
					break;
				case 7:
					add(SEVEN);
					break;
				case 8:
					add(EIGHT);
					break;
				case 9:
					add(NINE);
					break;
			}
		}
		static private void tens(int c, int l) {
			switch(c){
				case 1:
					add(TEN);
					break;
				case 2:
					add(TWENTY);
					break;
				case 3:
					add(THIRTY);
					break;
				case 4:
					add(FOURTY);
					break;
				case 5:
					add(FIFTY);
					break;
				case 6:
					add(SIXTY);
					break;
				case 7:
					add(SEVENTY);
					break;
				case 8:
					add(EIGHTY);
					break;
				case 9:
					add(NINETY);
					break;
			}
		}
		static private void huns(int c, int l) {
			switch(c){
				case 1:
					add(HUNDRED);
					break;
				case 2:
					add(TWO_HUNDRED);
					break;
				case 3:
					add(THREE_HUNDRED);
					break;
				case 4:
					add(FOUR_HUNDRED);
					break;
				case 5:
					add(FIVE_HUNDRED);
					break;
				case 6:
					add(SIX_HUNDRED);
					break;
				case 7:
					add(SEVEN_HUNDRED);
					break;
				case 8:
					add(EIGHT_HUNDRED);
					break;
				case 9:
					add(NINE_HUNDRED);
					break;
			}
		}
		static private void tous(int c, int l) {
			switch(c){
				case 1:
					add(THOUSEND);
					break;
				case 2:
					add(TWO_THOUSEND);
					break;
				case 3:
					add(THREE_THOUSEND);
					break;
				case 4:
					add(FOUR_THOUSEND);
					break;
			}
		}
		static private String organized(String s) {
			String ns="";
			ArrayList<String> strings=Stringer.cutOnEvery(s, "-");
			for(int o=strings.size() - 1; o>=0; o--){
				ns=ns + strings.get(o);
			}
			return ns;
		}
		static private void add(String s) {
			text=text + s;
		}
	}

	public static class Coin {
		public static boolean flip() {
			int i=new Random().nextInt(2);
			return i!=0;
		}
	}
}
