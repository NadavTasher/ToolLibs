package nadav.tasher.toollibpacked;

import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Tool {
}

class Sounder {
	static void playSound(Context context, String asset, boolean looping) {
		MediaPlayer m=new MediaPlayer();
		try{
			AssetFileDescriptor descriptor=context.getAssets().openFd(asset);
			m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			m.prepare();
			m.setVolume(1f, 1f);
			m.setLooping(looping);
			m.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

class Stringer {
	static ArrayList<String> cutOnEvery(String s, String xc) {
		ArrayList<String> list;
		if(s!=null){
			if(s.contains(xc)){
				String[] to=s.split("\\" + xc);
				list=new ArrayList<>(Arrays.asList(to));
			}else{
				ArrayList<String> al=new ArrayList<>();
				al.add(s);
				return al;
			}
		}else{
			return null;
		}
		return list;
	}
	static String replaceEvery(String s, char toreplace, char replaced) {
		String f="";
		for(int cc=0; cc<s.length(); cc++){
			if(s.charAt(cc)==toreplace){
				f=f + replaced;
			}else{
				f=f + s.charAt(cc);
			}
		}
		s=f;
		return s;
	}
	static String reversed(String s) {
		String news="";
		for(int i=s.length() - 1; i>=0; i--){
			news=news + s.charAt(i);
		}
		return news;
	}
}

class ApplicationManager {
	static final int FLAG_SHOW_ONLY_RUNABLES=1;
	static final int FLAG_SHOW_ALL=0;
	static final int FLAG_ONLY_SYSTEM=2;
	static ArrayList<App> getApps(Context cx, int flags) {
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
						}catch(NameNotFoundException e){
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
					}catch(NameNotFoundException e){
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
						}catch(NameNotFoundException e){
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
	static ArrayList<String> getAppNames(Context cx, int flags) {
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
	static class App {
		private String appName;
		private String packageName;
		private int flags;
		private int targetSDKversion;
		private Drawable appIcon;
		private Intent launchIntent;
		private String versionName;
		private int versionCode;
		App(String appName, String packageName, int flags, int targetSDKversion, Drawable appIcon, Intent launchIntent, int versionCode, String versionName) {
			this.appName=appName;
			this.packageName=packageName;
			this.flags=flags;
			this.targetSDKversion=targetSDKversion;
			this.appIcon=appIcon;
			this.launchIntent=launchIntent;
			this.versionCode=versionCode;
			this.versionName=versionName;
		}
		String getName() {
			return appName;
		}
		String getPackage() {
			return packageName;
		}
		int getFlags() {
			return flags;
		}
		Drawable getIcon() {
			return appIcon;
		}
		Intent getIntent() {
			return launchIntent;
		}
		int getTargetSDK() {
			return targetSDKversion;
		}
		int getVersionCode() {
			return versionCode;
		}
		String getVersionName() {
			return versionName;
		}
	}
}

class ListViewFactory {
	static ListView setupListView(Context c, ArrayList<String> adp) {
		ListView l=new ListView(c);
		ArrayAdapter<String> apt=new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, android.R.id.text1, adp);
		l.setAdapter(apt);
		return l;
	}
	static ListView setupListViewWithCustomAdaptar(Context c, ArrayList<String> adp, int layout, int id) {
		ListView l=new ListView(c);
		ArrayAdapter<String> apt=new ArrayAdapter<>(c, layout, id, adp);
		l.setAdapter(apt);
		return l;
	}
	static ListView setupListViewWithCustomAdaptar(Context c, String[] adp, int layout, int id) {
		ListView l=new ListView(c);
		ArrayAdapter<String> apt=new ArrayAdapter<>(c, layout, id, adp);
		l.setAdapter(apt);
		return l;
	}
	static ListView setupListView(Context c, String[] adp) {
		ListView l=new ListView(c);
		ArrayAdapter<String> apt=new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, android.R.id.text1, adp);
		l.setAdapter(apt);
		return l;
	}
}

class Quick {
	static ImageView quickImageView(Context cont, Drawable image) {
		ImageView iv=new ImageView(cont);
		iv.setImageDrawable(image);
		return iv;
	}
	static ScrollView quickScrollView(Context cont, View v) {
		ScrollView sv=new ScrollView(cont);
		sv.addView(v);
		return sv;
	}
	static Button quickButton(Context cont, String text) {
		Button b=new Button(cont);
		b.setText(text);
		return b;
	}
	static TextView quickTextView(Context cont, String text) {
		TextView tv=new TextView(cont);
		tv.setGravity(Gravity.CENTER);
		tv.setText(text);
		return tv;
	}
	static TextView quickTextView(Context cont, String text, int size) {
		TextView tv=new TextView(cont);
		tv.setGravity(Gravity.CENTER);
		tv.setText(text);
		tv.setTextSize((float) size);
		return tv;
	}
	static ListView quickListView(Context cont, ArrayList<String> adp) {
		ListView q=new ListView(cont);
		ArrayAdapter<String> apt=new ArrayAdapter<>(cont, android.R.layout.simple_list_item_1, android.R.id.text1, adp);
		q.setAdapter(apt);
		return q;
	}
	static ListView quickListView(Context cont, String s, String cutter) {
		ListView q=new ListView(cont);
		ArrayAdapter<String> apt=new ArrayAdapter<>(cont, android.R.layout.simple_list_item_1, android.R.id.text1, Stringer.cutOnEvery(s, cutter));
		q.setAdapter(apt);
		return q;
	}
	static LinearLayout quickLinearLayoutVertical(Context cont) {
		LinearLayout ll=new LinearLayout(cont);
		ll.setGravity(Gravity.CENTER);
		ll.setOrientation(LinearLayout.VERTICAL);
		return ll;
	}
	static LinearLayout quickLinearLayoutHorizontal(Context cont) {
		LinearLayout ll=new LinearLayout(cont);
		ll.setGravity(Gravity.CENTER);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		return ll;
	}
}

class FloatFactory {
	static void update(Context c, View v, int sizeX, int sizeY, int positionX, int positionY) {
		WindowManager wm=(WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		final LayoutParams parameters=new LayoutParams(sizeX, sizeY, LayoutParams.TYPE_PHONE, LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
		parameters.x=positionX;
		parameters.y=positionY;
		wm.updateViewLayout(v, parameters);
	}
	static void start(Context c, final View v, int sizeX, int sizeY, int positionX, int positionY, final OnMove om) {
		WindowManager wm=(WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		LayoutParams layoutParameteres=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		v.setLayoutParams(layoutParameteres);
		final LayoutParams parameters=new LayoutParams(sizeX, sizeY, LayoutParams.TYPE_PHONE, LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
		parameters.gravity=Gravity.CENTER;
		parameters.x=positionX;
		parameters.y=positionY;
		wm.addView(v, parameters);
	}
	static void remove(Context c, View v) {
		WindowManager wm=(WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		wm.removeView(v);
	}
	interface OnMove {
		void run(double x, double y, View v);
	}
}

class XMLFactory {
	static final int ASSETS=1;
	static final int INTERNAL_STORAGE=0;
	static ArrayList<XMLTag> read(Context context, File file, ArrayList<String> tags, int flag) {
		XmlPullParserFactory pullParserFactory;
		try{
			pullParserFactory=XmlPullParserFactory.newInstance();
			XmlPullParser parser=pullParserFactory.newPullParser();
			FileInputStream fileInputStream=new FileInputStream(file);
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(fileInputStream, null);
			switch(flag){
				case ASSETS:
					parser.setInput(context.getAssets().open(file.toString()), null);
					break;
				case INTERNAL_STORAGE:
					parser.setInput(fileInputStream, null);
					break;
				default:
					break;
			}
			return parse(parser, tags);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	static ArrayList<XMLTag> read(Context context, InputStream input, ArrayList<String> tags) {
		XmlPullParserFactory pullParserFactory;
		try{
			pullParserFactory=XmlPullParserFactory.newInstance();
			XmlPullParser parser=pullParserFactory.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(input, null);
			return parse(parser, tags);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	static private ArrayList<XMLTag> parse(XmlPullParser parser, ArrayList<String> tags) throws Exception {
		ArrayList<XMLTag> products=null;
		XMLTag currentProduct=null;
		int eventType=parser.getEventType();
		while(eventType!=XmlPullParser.END_DOCUMENT){
			String name=null;
			switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					products=new ArrayList<>();
					break;
				case XmlPullParser.START_TAG:
					name=parser.getName();
					if(tags!=null){
						for(int i=0; i<tags.size(); i++){
							if(name.equals(tags.get(i))){
								currentProduct=new XMLTag();
								currentProduct.tag=tags.get(i);
								currentProduct.data=parser.nextText();
								products.add(currentProduct);
							}
						}
					}else{
						currentProduct=new XMLTag();
						currentProduct.tag=name;
						currentProduct.data=parser.nextText();
						products.add(currentProduct);
					}
					break;
				case XmlPullParser.END_TAG:
					name=parser.getName();
			}
			eventType=parser.next();
		}
		return products;
	}
	static class XMLTag {
		String tag;
		String data;
	}
}

class ZipFactory {
	static void extract(File file, String extractto) {
		try{
			un(file, new File(extractto));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	static private void un(File z, File d) throws IOException {
		ZipInputStream zis=new ZipInputStream(new BufferedInputStream(new FileInputStream(z)));
		try{
			ZipEntry ze;
			int count;
			byte[] buffer=new byte[8192];
			while((ze=zis.getNextEntry())!=null){
				File file=new File(d, ze.getName());
				File dir=ze.isDirectory() ? file : file.getParentFile();
				if(!dir.isDirectory()&&!dir.mkdirs())
					throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
				if(ze.isDirectory())
					continue;
				FileOutputStream fout=new FileOutputStream(file);
				try{
					while((count=zis.read(buffer))!=-1)
						fout.write(buffer, 0, count);
				}finally{
					fout.close();
				}
			}
		}finally{
			zis.close();
		}
	}
}

class Extras {
	static boolean isInstalled(Context con, String packageName) {
		try{
			con.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			return true;
		}catch(NameNotFoundException e){
			return false;
		}
	}
	static int getVersionCode(Context con, String packagename) {
		try{
			return con.getPackageManager().getPackageInfo(packagename, PackageManager.GET_ACTIVITIES).versionCode;
		}catch(NameNotFoundException e){
			e.printStackTrace();
			return 0;
		}
	}
	static String getVersionName(Context con, String packagename) {
		try{
			return con.getPackageManager().getPackageInfo(packagename, PackageManager.GET_ACTIVITIES).versionName;
		}catch(NameNotFoundException e){
			e.printStackTrace();
			return null;
		}
	}
	static int screenX(Context con) {
		Display display=((WindowManager) con.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point size=new Point();
		display.getSize(size);
		return size.x;
	}
	static int screenY(Context con) {
		Display display=((WindowManager) con.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point size=new Point();
		display.getSize(size);
		return size.y;
	}
	static boolean isTablet(Context con) {
		return screenX(con)>screenY(con);
	}
	static void copyToClipboard(Context con, String s) {
		ClipboardManager cm=(ClipboardManager) con.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip=ClipData.newPlainText("label", s);
		cm.setPrimaryClip(clip);
	}
	static void openUrl(Context con, String s) {
		Intent i;
		String url=s;
		if(!s.startsWith("http://")&&!s.startsWith("https://")){
			url="http://" + s;
		}
		i=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		con.startActivity(i);
	}
	static void setWallpaper(Context con, Bitmap b) {
		WallpaperManager wp=WallpaperManager.getInstance(con);
		try{
			wp.setBitmap(b);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	static void setWallpaper(Context con, int b) {
		WallpaperManager wp=WallpaperManager.getInstance(con);
		try{
			wp.setResource(b);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	static void setRingtone(Context con, String fname, String title) {
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
	static void setNotificationSound(Context con, String fname, String selectionName) {
		ContentValues values=new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, fname);
		values.put(MediaStore.MediaColumns.TITLE, selectionName);
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/ogg");
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
		Uri uri=MediaStore.Audio.Media.getContentUriForPath(fname);
		Uri newUri=con.getContentResolver().insert(uri, values);
		RingtoneManager.setActualDefaultRingtoneUri(con, RingtoneManager.TYPE_NOTIFICATION, newUri);
	}
	static class EncryptionFactory {
		private static String[] allletters=new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", " ", "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "-", "=", ":", "/", ";", "'", "|", "[", "]", "{", ".", "?", "<", ">", "`", ",", "}"};
		private static int le=allletters.length;
		private static int le2=le;
		static String encrypt(String key, String text) {
			boolean eUD=true;
			int eLAP=0;
			String ns="";
			if(key.length()>0){
				for(int l=0; l<text.length(); l++){
					if(eUD){
						int letternum=findLetter(text.charAt(l) + "");
						int codepart=findLetter(key.charAt(eLAP) + "");
						if(letternum + codepart>=le){
							ns=ns + allletters[letternum + codepart - le2];
						}else{
							ns=ns + allletters[letternum + codepart];
						}
						eLAP++;
						if(eLAP==key.length()){
							eLAP=0;
						}
						eUD=!eUD;
					}else{
						int letternum=findLetter(text.charAt(l) + "");
						int codepart=findLetter(key.charAt(eLAP) + "");
						if(letternum - codepart<=0){
							ns=ns + allletters[letternum - codepart + le2];
						}else{
							ns=ns + allletters[letternum - codepart];
						}
						eLAP++;
						if(eLAP==key.length()){
							eLAP=0;
						}
						eUD=!eUD;
					}
				}
			}
			return ns;
		}
		private static int findLetter(String s) {
			int tr=-1;
			for(int cu=0; cu<(le); cu++){
				if(s.equals(allletters[cu])){
					tr=cu;
				}
			}
			return tr;
		}
		static String decrypt(String key, String text) {
			boolean deUD=true;
			int deLAP=0;
			String ns="";
			if(key.length()>0){
				for(int l=0; l<text.length(); l++){
					if(deUD){
						int letternum=findLetter(text.charAt(l) + "");
						int codepart=findLetter(key.charAt(deLAP) + "");
						if(letternum - codepart<0){
							ns=ns + allletters[letternum - codepart + le2];
						}else{
							ns=ns + allletters[letternum - codepart];
						}
						deLAP++;
						if(deLAP==key.length()){
							deLAP=0;
						}
						deUD=!deUD;
					}else{
						int letternum=findLetter(text.charAt(l) + "");
						int codepart=findLetter(key.charAt(deLAP) + "");
						if(letternum + codepart>=le){
							ns=ns + allletters[letternum + codepart - le2];
						}else{
							ns=ns + allletters[letternum + codepart];
						}
						deLAP++;
						if(deLAP==key.length()){
							deLAP=0;
						}
						deUD=!deUD;
					}
				}
			}
			return ns;
		}
	}

	static class Numerals {
		static final String ONE="I";
		static final String TWO="II";
		static final String THREE="III";
		static final String FOUR="IV";
		static final String FIVE="V";
		static final String SIX="VI";
		static final String SEVEN="VII";
		static final String EIGHT="VIII";
		static final String NINE="IX";
		static final String TEN="X";
		static final String TWENTY="XX";
		static final String THIRTY="XXX";
		static final String FOURTY="XL";
		static final String FIFTY="L";
		static final String SIXTY="LX";
		static final String SEVENTY="LXX";
		static final String EIGHTY="LXXX";
		static final String NINETY="XC";
		static final String HUNDRED="C";
		static final String TWO_HUNDRED="CC";
		static final String THREE_HUNDRED="CCC";
		static final String FOUR_HUNDRED="CD";
		static final String FIVE_HUNDRED="D";
		static final String SIX_HUNDRED="DC";
		static final String SEVEN_HUNDRED="DCC";
		static final String EIGHT_HUNDRED="DCCC";
		static final String NINE_HUNDRED="CM";
		static final String THOUSEND="M";
		static final String TWO_THOUSEND="MM";
		static final String THREE_THOUSEND="MMM";
		static final String FOUR_THOUSEND="MMMM";
		private final int ONES=-1;
		private final int TENS=-2;
		private final int HUNS=-3;
		private final int TOUS=-4;
		private String text="";
		private int num=ONES;
		String get(int i) {
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
		private void ones(int c, int l) {
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
		private void tens(int c, int l) {
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
		private void huns(int c, int l) {
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
		private void tous(int c, int l) {
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
		private String organized(String s) {
			String ns="";
			ArrayList<String> strings=Stringer.cutOnEvery(s, "-");
			for(int o=strings.size() - 1; o>=0; o--){
				ns=ns + strings.get(o);
			}
			return ns;
		}
		private void add(String s) {
			text=text + s;
		}
	}

	static class Coin {
		static boolean flip() {
			int i=new Random().nextInt(2);
			return i!=0;
		}
	}
}

class DrawableFactory {
	static final int FLAG_FLIP_VERTICAL=1;
	static final int FLAG_FLIP_HORIZONTAL=2;
	static Drawable resize(Context ctx, Drawable dr, int size) {
		return new BitmapDrawable(ctx.getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) dr).getBitmap(), size, size, true));
	}
	static Drawable resize(Context ctx, Drawable dr, int sizex, int sizey) {
		return new BitmapDrawable(ctx.getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) dr).getBitmap(), sizex, sizey, true));
	}
	static Drawable flip(Context c, Drawable d, int flag) {
		Matrix matrix=new Matrix();
		switch(flag){
			case FLAG_FLIP_VERTICAL:
				matrix.preScale(1.0f, -1.0f);
				break;
			case FLAG_FLIP_HORIZONTAL:
				matrix.preScale(-1.0f, 1.0f);
				break;
			default:
				matrix.preScale(-1.0f, 1.0f);
				break;
		}
		return new BitmapDrawable(c.getResources(), Bitmap.createBitmap(((BitmapDrawable) d).getBitmap(), 0, 0, ((BitmapDrawable) d).getBitmap().getWidth(), ((BitmapDrawable) d).getBitmap().getHeight(), matrix, true));
	}
}

class FileFactory {
	static String readFile(File file) {
		try{
			StringBuilder text=new StringBuilder();
			BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String str;
			while((str=in.readLine())!=null){
				text.append(str);
				text.append('\n');
			}
			in.close();
			return text.toString();
		}catch(IOException e){
			return null;
		}
	}
	static void writeToFile(File file, String lines) {
		try{
			OutputStreamWriter writer=new OutputStreamWriter(new FileOutputStream(file));
			writer.write(lines);
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	static void appendToFile(File file, String lines) {
		try{
			OutputStreamWriter writer=new OutputStreamWriter(new FileOutputStream(file));
			String all=readFile(file) + lines;
			writer.write(all);
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	static void log(File f, String s) {
		DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss @ ");
		String time=dateFormat.format(new Date());
		String current=FileFactory.readFile(f);
		if(current!=null){
			String all=current + time + s;
			FileFactory.writeToFile(f, all);
		}else{
			String start=time + "Start Log\n";
			String all=start + time + s;
			FileFactory.writeToFile(f, all);
		}
		Log.i("Logger", s);
	}
	static void warning(File f, String s) {
		DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss @ ");
		String time=dateFormat.format(new Date()) + "Warning: ";
		String current=FileFactory.readFile(f);
		if(current!=null){
			String all=current + time + s;
			FileFactory.writeToFile(f, all);
		}else{
			String start=time + "Start Log\n";
			String all=start + time + s;
			FileFactory.writeToFile(f, all);
		}
		Log.i("Logger", s);
	}
	static void error(File f, String s) {
		DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss @ ");
		String time=dateFormat.format(new Date()) + "Error!: ";
		String current=FileFactory.readFile(f);
		if(current!=null){
			String all=current + time + s;
			FileFactory.writeToFile(f, all);
		}else{
			String start=time + "Start Log\n";
			String all=start + time + s;
			FileFactory.writeToFile(f, all);
		}
		Log.i("Logger", s);
	}
}

class Animations {
	static final float[] VIBRATE_SMALL=new float[]{0, 1, -1, 2, -2, 3, -3, 4, -4, 5, -5, 6, -6, 5, -5, 4, -4, 3, -3, 2, -2, 1, -1, 0};
	static final float[] VIBRATE_BIG=new float[]{0, 10, -10, 20, -20, 30, -30, 30, -20, 20, -10, 10, 0};
	static final float[] JUMP_SMALL=new float[]{0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 7, 7, 7, 7, 6, 6, 5, 5, 4, 4, 3, 3, 2, 2, 1, 1, 0, 0};
	static final float[] JUMP_BIG=new float[]{0, 0, 0, 0, 10, 10, 20, 20, 30, 30, 40, 40, 40, 40, 30, 30, 20, 20, 10, 10, 0, 0, 0, 0};
	static final float[] INVISIBLE_TO_VISIBLE=new float[]{0.0f, 1.0f};
	static final float[] VISIBLE_TO_INVISIBLE=new float[]{1.0f, 0.0f};
	static float[] getSlideRight(Context c) {
		return new float[]{0, Extras.screenX(c)};
	}
	static float[] getSlideLeft(Context c) {
		return new float[]{0, -Extras.screenX(c)};
	}
	static float[] getSlideUp(Context c) {
		return new float[]{0, Extras.screenY(c)};
	}
	static float[] getSlideDown(Context c) {
		return new float[]{0, -Extras.screenY(c)};
	}
}

class FileDownloader extends AsyncTask<String, String, String> {
	private String furl;
	private String fdpath;
	private boolean available;
	private OnDownload oe;
	public FileDownloader(String url, String path, OnDownload onfile) {
		oe=onfile;
		furl=url;
		fdpath=path;
	}
	private boolean check() {
		try{
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con=(HttpURLConnection) new URL(furl).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode()==HttpURLConnection.HTTP_OK);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	@Override
	protected String doInBackground(String... comment) {
		int perc=0;
		if(check()){
			available=true;
			int count;
			try{
				URL url=new URL(furl);
				URLConnection conection=url.openConnection();
				conection.connect();
				int lenghtOfFile=conection.getContentLength();
				InputStream input=new BufferedInputStream(url.openStream(), 8192);
				OutputStream output=new FileOutputStream(fdpath);
				byte data[]=new byte[1024];
				long total=0;
				while((count=input.read(data))!=-1){
					Log.i("FileDownloader", "File Download: " + furl + " " + total * 100 / lenghtOfFile);
					output.write(data, 0, count);
					total+=count;
					if(perc<(int) (total * 100 / lenghtOfFile)){
						perc++;
						oe.onProgressChanged(new File(fdpath), (int) (total * 100 / lenghtOfFile));
					}
				}
				output.flush();
				output.close();
				input.close();
			}catch(Exception e){
				Log.e("Error: ", e.getMessage());
			}
		}else{
			available=false;
		}
		return null;
	}
	@Override
	protected void onPostExecute(String file_url) {
		if(oe!=null){
			oe.onFinish(new File(fdpath), available);
		}
	}
	interface OnDownload {
		void onFinish(File output, boolean isAvailable);
		void onProgressChanged(File output, int percent);
	}
}

class FileChecker extends AsyncTask<String, String, String> {
	private long kbs;
	private String addr;
	private boolean available;
	private OnFile of;
	FileChecker(String url, OnFile onFile) {
		addr=url;
		of=onFile;
	}
	private boolean check() {
		try{
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con=(HttpURLConnection) new URL(addr).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode()==HttpURLConnection.HTTP_OK);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	@Override
	protected String doInBackground(String... strings) {
		if(check()){
			available=true;
			try{
				HttpURLConnection con=(HttpURLConnection) new URL(addr).openConnection();
				con.connect();
				kbs=con.getContentLength() / 1024;
				con.disconnect();
			}catch(IOException e){
				e.printStackTrace();
			}
		}else{
			available=false;
		}
		return null;
	}
	@Override
	protected void onPostExecute(String s) {
		if(of!=null)
			of.onFinish(kbs, available);
		super.onPostExecute(s);
	}
	interface OnFile {
		void onFinish(long fileInKB, boolean isAvailable);
	}
}

class FileReader extends AsyncTask<String, String, String> {
	private InputStream is;
	private OnEnd one;
	private String fi;
	public FileReader(String file, OnEnd oe) {
		one=oe;
		fi=file;
	}
	@Override
	protected String doInBackground(String... params) {
		try{
			URL url=new URL(fi);
			is=url.openStream();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(String file_url) {
		if(one!=null){
			one.onFileRead(is);
		}
	}
	interface OnEnd {
		void onFileRead(InputStream stream);
	}
}

class Pinger extends AsyncTask<String, String, Boolean> {
	private OnEnd onEnd;
	private int tmout=2000;
	private String addr;
	public Pinger(int timeout, OnEnd e) {
		onEnd=e;
		tmout=timeout;
	}
	@Override
	protected Boolean doInBackground(String... strings) {
		addr=strings[0];
		try{
			HttpURLConnection connection=(HttpURLConnection) new URL(strings[0]).openConnection();
			connection.setConnectTimeout(tmout);
			connection.connect();
			return true;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	@Override
	protected void onPostExecute(Boolean result) {
		if(onEnd!=null){
			onEnd.onPing(addr, result);
		}
	}
	interface OnEnd {
		void onPing(String url, boolean result);
	}
}