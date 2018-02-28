package nadav.tasher.lightool;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.awt.font.NumericShaper;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Light {
    public static class Net {
        public static class NetFile {
            public static class FileChecker extends AsyncTask<String, String, String> {
                private long kbs;
                private String addr;
                private boolean available;
                private FileChecker.OnFile of;

                public FileChecker(String url, FileChecker.OnFile onFile) {
                    addr = url;
                    of = onFile;
                }

                private boolean check() {
                    try {
                        HttpURLConnection.setFollowRedirects(false);
                        HttpURLConnection con = (HttpURLConnection) new URL(addr).openConnection();
                        con.setRequestMethod("HEAD");
                        return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                protected String doInBackground(String... strings) {
                    if (check()) {
                        available = true;
                        try {
                            HttpURLConnection con = (HttpURLConnection) new URL(addr).openConnection();
                            con.connect();
                            kbs = con.getContentLength() / 1024;
                            con.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        available = false;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    if (of != null)
                        of.onFinish(kbs, available);
                    super.onPostExecute(s);
                }

                public interface OnFile {
                    void onFinish(long fileInKB, boolean isAvailable);
                }
            }

            public static class FileDownloader extends AsyncTask<String, String, String> {
                private String furl;
                private File fdpath;
                private boolean available;
                private FileDownloader.OnDownload oe;

                public FileDownloader(String url, File path, FileDownloader.OnDownload onfile) {
                    oe = onfile;
                    furl = url;
                    fdpath = path;
                }

                private boolean check() {
                    try {
                        HttpURLConnection.setFollowRedirects(false);
                        HttpURLConnection con = (HttpURLConnection) new URL(furl).openConnection();
                        con.setRequestMethod("HEAD");
                        return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                protected String doInBackground(String... comment) {
                    int perc = 0;
                    if (check()) {
                        available = true;
                        int count;
                        try {
                            URL url = new URL(furl);
                            URLConnection conection = url.openConnection();
                            conection.connect();
                            int lenghtOfFile = conection.getContentLength();
                            InputStream input = new BufferedInputStream(url.openStream(), 8192);
                            OutputStream output = new FileOutputStream(fdpath);
                            byte data[] = new byte[1024];
                            long total = 0;
                            while ((count = input.read(data)) != -1) {
                                Log.i("FileDownloader", "File Download: " + furl + " " + total * 100 / lenghtOfFile);
                                output.write(data, 0, count);
                                total += count;
                                if (perc < (int) (total * 100 / lenghtOfFile)) {
                                    perc++;
                                    oe.onProgressChanged(fdpath, (int) (total * 100 / lenghtOfFile));
                                }
                            }
                            output.flush();
                            output.close();
                            input.close();
                        } catch (Exception e) {
                            Log.e("Error: ", e.getMessage());
                        }
                    } else {
                        available = false;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String file_url) {
                    if (oe != null) {
                        oe.onFinish(fdpath, available);
                    }
                }

                public interface OnDownload {
                    void onFinish(File output, boolean isAvailable);

                    void onProgressChanged(File output, int percent);
                }
            }

            public static class FileReader extends AsyncTask<String, String, String> {
                private FileReader.OnEnd one;
                private String fi;

                public FileReader(String file, FileReader.OnEnd oe) {
                    one = oe;
                    fi = file;
                }

                @Override
                protected String doInBackground(String... params) {
                    StringBuilder s = new StringBuilder();
                    try {
                        URL url = new URL(fi);
                        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                        String str;
                        while ((str = in.readLine()) != null) {
                            s.append(str).append("\n");
                        }
                        in.close();
                    } catch (IOException e) {
                        s = null;
                    }
                    if (s != null) {
                        return s.toString();
                    } else {
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(String content) {
                    if (one != null) {
                        one.onFileRead(content);
                    }
                }

                interface OnEnd {
                    void onFileRead(String content);
                }
            }
        }

        public static class Pinger extends AsyncTask<String, String, Boolean> {
            private Pinger.OnEnd onEnd;
            private int tmout = 2000;
            private String addr;

            public Pinger(String url, int timeout, Pinger.OnEnd e) {
                onEnd = e;
                tmout = timeout;
                addr = url;
            }

            @Override
            protected Boolean doInBackground(String... strings) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(addr).openConnection();
                    connection.setConnectTimeout(tmout);
                    connection.connect();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (onEnd != null) {
                    onEnd.onPing(result);
                }
            }

            public interface OnEnd {
                void onPing(boolean result);
            }
        }

        public static class Request {
            public static class Post extends AsyncTask<String, String, String> {
                private String phpurl;
                private ArrayList<RequestParameter> parms;
                private OnPost op;
                private String result;

                public Post(String url, RequestParameter[] parameters, OnPost onpost) {
                    this.phpurl = url;
                    parms = new ArrayList<>(Arrays.asList(parameters));
                    op = onpost;
                }

                @Override
                protected String doInBackground(String... comments) {
                    String response = "";
                    StringBuilder data = new StringBuilder();
                    BufferedReader reader = null;
                    HttpURLConnection conn = null;
                    try {
                        URL url = new URL(phpurl);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                        for (int v = 0; v < parms.size(); v++) {
                            data.append("&").append(URLEncoder.encode(parms.get(v).getName(), "UTF-8")).append("=").append(URLEncoder.encode(parms.get(v).getValue(), "UTF-8"));
                        }
                        wr.write(data.toString());
                        wr.flush();
                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        boolean first = true;
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!first) {
                                sb.append("\n");
                            } else {
                                first = false;
                            }
                            sb.append(line);
                        }
                        response = sb.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (reader != null) {
                                reader.close();
                            }
                            if (conn != null) {
                                conn.disconnect();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    result = response;
                    return response;
                }

                @Override
                protected void onPostExecute(String s) {
                    if (op != null) {
                        op.onPost(result);
                    }
                    super.onPostExecute(s);
                }

                public interface OnPost {
                    void onPost(String response);
                }
            }

            public static class RequestParameter {
                private String name;
                private String value;

                public RequestParameter(String n, String v) {
                    name = n;
                    value = v;
                }

                public String getName() {
                    return name;
                }

                public void setName(String newname) {
                    name = newname;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String newvalue) {
                    value = newvalue;
                }
            }
        }
    }

    public static class Device {
        public static boolean isOnline(Context c) {
            return ((ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
        }

        public static boolean isWifi(Context c) {
            ConnectivityManager connectivityManager = ((ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE));
            Network[] networks = connectivityManager.getAllNetworks();
            if (networks == null) {
                return false;
            } else {
                for (Network network : networks) {
                    NetworkInfo info = connectivityManager.getNetworkInfo(network);
                    if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
                        if (info.isAvailable() && info.isConnected()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public static boolean isInstalled(Context con, String packageName) {
            try {
                con.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
        }

        public static int getVersionCode(Context con, String packagename) {
            try {
                return con.getPackageManager().getPackageInfo(packagename, PackageManager.GET_ACTIVITIES).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return 0;
            }
        }

        public static String getVersionName(Context con, String packagename) {
            try {
                return con.getPackageManager().getPackageInfo(packagename, PackageManager.GET_ACTIVITIES).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static int screenX(Context con) {
            Display display = ((WindowManager) con.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size.x;
        }

        public static int screenY(Context con) {
            Display display = ((WindowManager) con.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size.y;
        }
    }

    public static class Stringer {
        public static String reversed(String s) {
            String news = "";
            for (int i = s.length() - 1; i >= 0; i--) {
                news = news + s.charAt(i);
            }
            return news;
        }

        public static class Encryptor {
            public static String CONTROL = "\u0001\u0002\u0003\u0004\u0005\u0006\u0007\n\b\t\u000B\f\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F ";
            public static String LATIN = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007F\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F ¡¢£¤¥¦§¨©ª«¬\u00AD®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿĀāĂăĄąĆćĈĉĊċČčĎďĐđĒēĔĕĖėĘęĚěĜĝĞğĠġĢģĤĥĦħĨĩĪīĬĭĮįİıĲĳĴĵĶķĸĹĺĻļĽľĿŀŁłŃńŅņŇňŉŊŋŌōŎŏŐőŒœŔŕŖŗŘřŚśŜŝŞşŠšŢţŤťŦŧŨũŪūŬŭŮůŰűŲųŴŵŶŷŸŹźŻżŽžſƀƁƂƃƄƅƆƇƈƉƊƋƌƍƎƏƐƑƒƓƔƕƖƗƘƙƚƛƜƝƞƟƠơƢƣƤƥƦƧƨƩƪƫƬƭƮƯưƱƲƳƴƵƶƷƸƹƺƻƼƽƾƿǀǁǂǃǄǅǆǇǈǉǊǋǌǍǎǏǐǑǒǓǔǕǖǗǘǙǚǛǜǝǞǟǠǡǢǣǤǥǦǧǨǩǪǫǬǭǮǯǰǱǲǳǴǵǶǷǸǹǺǻǼǽǾǿȀȁȂȃȄȅȆȇȈȉȊȋȌȍȎȏȐȑȒȓȔȕȖȗȘșȚțȜȝȞȟȠȡȢȣȤȥȦȧȨȩȪȫȬȭȮȯȰȱȲȳȴȵȶȷȸȹȺȻȼȽȾȿɀɁɂɃɄɅɆɇɈɉɊɋɌɍɎɏ";
            public static String GREEK = "ͰͱͲͳʹ͵Ͷͷ\u0378\u0379ͺͻͼͽ;\u037F\u0380\u0381\u0382\u0383΄΅Ά·ΈΉΊ\u038BΌ\u038DΎΏΐΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡ\u03A2ΣΤΥΦΧΨΩΪΫάέήίΰαβγδεζηθικλμνξοπρςστυφχψωϊϋόύώϏϐϑϒϓϔϕϖϗϘϙϚϛϜϝϞϟϠϡϢϣϤϥϦϧϨϩϪϫϬϭϮϯϰϱϲϳϴϵ϶ϷϸϹϺϻϼϽϾϿ";
            public static String HEBREW = "\u0590ְֱֲֳִֵֶַָֹֺֻּֽ֑֖֛֢֣֤֥֦֧֪֚֭֮֒֓֔֕֗֘֙֜֝֞֟֠֡֨֩֫֬֯־ֿ׀ׁׂ׃ׅׄ׆ׇ\u05C8\u05C9\u05CA\u05CB\u05CC\u05CD\u05CE\u05CFאבגדהוזחטיךכלםמןנסעףפץצקרשת\u05EB\u05EC\u05ED\u05EE\u05EFװױײ׳״\u05F5\u05F6\u05F7\u05F8\u05F9\u05FA\u05FB\u05FC\u05FD\u05FE";
            public static String LATINEXTENTION = "ḀḁḂḃḄḅḆḇḈḉḊḋḌḍḎḏḐḑḒḓḔḕḖḗḘḙḚḛḜḝḞḟḠḡḢḣḤḥḦḧḨḩḪḫḬḭḮḯḰḱḲḳḴḵḶḷḸḹḺḻḼḽḾḿṀṁṂṃṄṅṆṇṈṉṊṋṌṍṎṏṐṑṒṓṔṕṖṗṘṙṚṛṜṝṞṟṠṡṢṣṤṥṦṧṨṩṪṫṬṭṮṯṰṱṲṳṴṵṶṷṸṹṺṻṼṽṾṿẀẁẂẃẄẅẆẇẈẉẊẋẌẍẎẏẐẑẒẓẔẕẖẗẘẙẚẛẜẝẞẟẠạẢảẤấẦầẨẩẪẫẬậẮắẰằẲẳẴẵẶặẸẹẺẻẼẽẾếỀềỂểỄễỆệỈỉỊịỌọỎỏỐốỒồỔổỖỗỘộỚớỜờỞởỠỡỢợỤụỦủỨứỪừỬửỮữỰựỲỳỴỵỶỷỸỹỺỻỼỽỾỿ";
            public static String SYMBOLIC = "‐‑‒–—―‖‗‘’‚‛“”„‟†‡•‣․‥…‧\u2028\u2029\u202A\u202B\u202C\u202D\u202E‰‱′″‴‵‶‷‸‹›※‼‽‾‿⁀⁁⁂⁃⁄⁅⁆⁇⁈⁉⁊⁋⁌⁍⁎⁏⁐⁑⁒⁓⁔⁕⁖⁗⁘⁙⁚⁛⁜⁝⁞\u2060\u2061\u2062\u2063\u2064\u2065\u2066\u2067\u2068\u2069\u206A\u206B\u206C\u206D\u206E\u206F";
            public static String SUPERANDSUB = "⁰ⁱ\u2072\u2073⁴⁵⁶⁷⁸⁹⁺⁻⁼⁽⁾ⁿ₀₁₂₃₄₅₆₇₈₉₊₋₌₍₎\u208Fₐₑₒₓₔₕₖₗₘₙₚₛₜ\u209D\u209E\u209F";
            public static String CURRENCIES = "₠₡₢₣₤₥₦₧₨₩₪₫€₭₮₯₰₱₲₳₴₵₶₷₸₹₺\u20BB\u20BC\u20BD\u20BE\u20BF\u20C0\u20C1\u20C2\u20C3\u20C4\u20C5\u20C6\u20C7\u20C8\u20C9\u20CA\u20CB\u20CC\u20CD\u20CE\u20CF";
            public static String LETTERLIKE = "℀℁ℂ℃℄℅℆ℇ℈℉ℊℋℌℍℎℏℐℑℒℓ℔ℕ№℗℘ℙℚℛℜℝ℞℟℠℡™℣ℤ℥Ω℧ℨ℩KÅℬℭ℮ℯℰℱℲℳℴℵℶℷℸℹ℺℻ℼℽℾℿ⅀⅁⅂⅃⅄ⅅⅆⅇⅈⅉ⅊⅋⅌⅍ⅎ⅏";
            public static String ALL = CONTROL + LATIN + GREEK + HEBREW + LATINEXTENTION + SYMBOLIC + SUPERANDSUB + CURRENCIES + LETTERLIKE;
            private static String DEFAULT = ALL;

            public static class EncryptionV1 {
                public static String encrypt(String key, String text) {
                    if (key != null && key.length() > 0) {
                        int keyPart = 0;
                        String encrypted = "";
                        for (int l = 0; l < text.length(); l++) {
                            int keyChar = findChar(key.charAt(keyPart), DEFAULT);
                            int currentChar = findChar(text.charAt(l), DEFAULT);
                            if (currentChar == -1) {
                                encrypted = encrypted + text.charAt(l);
                            } else {
                                if (currentChar + keyChar >= DEFAULT.length()) {
                                    encrypted = encrypted + DEFAULT.charAt(currentChar + keyChar - DEFAULT.length());
                                } else {
                                    encrypted = encrypted + DEFAULT.charAt(currentChar + keyChar);
                                }
                            }
                            if (keyPart == key.length() - 1) {
                                keyPart = 0;
                            } else {
                                keyPart++;
                            }
                        }
                        return encrypted;
                    }
                    return text;
                }

                public static String decrypt(String key, String text) {
                    if (key != null && key.length() > 0) {
                        int keyPart = 0;
                        String encrypted = "";
                        for (int l = 0; l < text.length(); l++) {
                            int keyChar = findChar(key.charAt(keyPart), DEFAULT);
                            int currentChar = findChar(text.charAt(l), DEFAULT);
                            if (currentChar == -1) {
                                encrypted = encrypted + text.charAt(l);
                            } else {
                                if (currentChar - keyChar < 0) {
                                    encrypted = encrypted + DEFAULT.charAt(currentChar - keyChar + DEFAULT.length());
                                } else {
                                    encrypted = encrypted + DEFAULT.charAt(currentChar - keyChar);
                                }
                            }
                            if (keyPart == key.length() - 1) {
                                keyPart = 0;
                            } else {
                                keyPart++;
                            }
                        }
                        return encrypted;
                    }
                    return text;
                }

                public static String encrypt(String key, String text, String mask) {
                    if (key != null && key.length() > 0) {
                        int keyPart = 0;
                        String encrypted = "";
                        for (int l = 0; l < text.length(); l++) {
                            int keyChar = findChar(key.charAt(keyPart), mask);
                            int currentChar = findChar(text.charAt(l), mask);
                            if (currentChar == -1) {
                                encrypted = encrypted + text.charAt(l);
                            } else {
                                if (currentChar + keyChar >= mask.length()) {
                                    encrypted = encrypted + mask.charAt(currentChar + keyChar - mask.length());
                                } else {
                                    encrypted = encrypted + mask.charAt(currentChar + keyChar);
                                }
                            }
                            if (keyPart == key.length() - 1) {
                                keyPart = 0;
                            } else {
                                keyPart++;
                            }
                        }
                        return encrypted;
                    }
                    return text;
                }

                public static String decrypt(String key, String text, String mask) {
                    if (key != null && key.length() > 0) {
                        int keyPart = 0;
                        String encrypted = "";
                        for (int l = 0; l < text.length(); l++) {
                            int keyChar = findChar(key.charAt(keyPart), mask);
                            int currentChar = findChar(text.charAt(l), mask);
                            if (currentChar == -1) {
                                encrypted = encrypted + text.charAt(l);
                            } else {
                                if (currentChar - keyChar < 0) {
                                    encrypted = encrypted + mask.charAt(currentChar - keyChar + mask.length());
                                } else {
                                    encrypted = encrypted + mask.charAt(currentChar - keyChar);
                                }
                            }
                            if (keyPart == key.length() - 1) {
                                keyPart = 0;
                            } else {
                                keyPart++;
                            }
                        }
                        return encrypted;
                    }
                    return text;
                }

                private static int findChar(char s, String mask) {
                    for (int cu = 0; cu < mask.length(); cu++) {
                        if (s == mask.charAt(cu)) {
                            return cu;
                        }
                    }
                    return -1;
                }
            }

            public static class EncryptionV2 {
                public static String encrypt(@NonNull String key, @NonNull String text) {
                    int[] keyNode = new int[key.length()];
                    int[] node1 = new int[text.length()];
                    int[] node2 = new int[text.length()];
                    char[] node3 = new char[text.length()];
                    boolean up = true;
                    int keyPart = 0;
                    for (int kp = 0; kp < key.length(); kp++) {
                        keyNode[kp] = key.charAt(kp);
                    }
                    for (int l = 0; l < text.length(); l++) {
                        node1[l] = findChar(text.charAt(l), DEFAULT);
                    }
                    for (int l = 0; l < text.length(); l++) {
                        if (keyPart < keyNode.length - 1) {
                            keyPart++;
                        } else {
                            keyPart = 0;
                        }
                        if (up) {
                            node2[l] = revalueInt(node1[l] + l * l + keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * keyPart * keyPart * keyNode[rIA(l, keyNode)], DEFAULT);
                        } else {
                            node2[l] = revalueInt(node1[l] - l * l - keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * l * keyPart, DEFAULT);
                        }
                        up = !up;
                    }
                    for (int l = 0; l < text.length(); l++) {
                        node3[l] = findInt(node2[l], DEFAULT);
                    }
                    return String.copyValueOf(node3);
                }

                public static String decrypt(@NonNull String key, @NonNull String text) {
                    int[] keyNode = new int[key.length()];
                    int[] node1 = new int[text.length()];
                    int[] node2 = new int[text.length()];
                    char[] node3 = new char[text.length()];
                    boolean up = true;
                    int keyPart = 0;
                    for (int kp = 0; kp < key.length(); kp++) {
                        keyNode[kp] = key.charAt(kp);
                    }
                    for (int l = 0; l < text.length(); l++) {
                        node1[l] = findChar(text.charAt(l), DEFAULT);
                    }
                    for (int l = 0; l < text.length(); l++) {
                        if (keyPart < keyNode.length - 1) {
                            keyPart++;
                        } else {
                            keyPart = 0;
                        }
                        if (up) {
                            node2[l] = revalueInt(node1[l] - l * l - keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * keyPart * keyPart * keyNode[rIA(l, keyNode)], DEFAULT);
                        } else {
                            node2[l] = revalueInt(node1[l] + l * l + keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * l * keyPart, DEFAULT);
                        }
                        up = !up;
                    }
                    for (int l = 0; l < text.length(); l++) {
                        node3[l] = findInt(node2[l], DEFAULT);
                    }
                    return String.copyValueOf(node3);
                }

                public static String encrypt(@NonNull String key, @NonNull String text, @NonNull String map) {
                    int[] keyNode = new int[key.length()];
                    int[] node1 = new int[text.length()];
                    int[] node2 = new int[text.length()];
                    char[] node3 = new char[text.length()];
                    boolean up = true;
                    int keyPart = 0;
                    for (int kp = 0; kp < key.length(); kp++) {
                        keyNode[kp] = key.charAt(kp);
                    }
                    for (int l = 0; l < text.length(); l++) {
                        node1[l] = findChar(text.charAt(l), map);
                    }
                    for (int l = 0; l < text.length(); l++) {
                        if (keyPart < keyNode.length - 1) {
                            keyPart++;
                        } else {
                            keyPart = 0;
                        }
                        if (up) {
                            node2[l] = revalueInt(node1[l] + l * l + keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * keyPart * keyPart * keyNode[rIA(l, keyNode)], DEFAULT);
                        } else {
                            node2[l] = revalueInt(node1[l] - l * l - keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * l * keyPart, DEFAULT);
                        }
                        up = !up;
                    }
                    for (int l = 0; l < text.length(); l++) {
                        node3[l] = findInt(node2[l], map);
                    }
                    return String.copyValueOf(node3);
                }

                public static String decrypt(@NonNull String key, @NonNull String text, @NonNull String map) {
                    int[] keyNode = new int[key.length()];
                    int[] node1 = new int[text.length()];
                    int[] node2 = new int[text.length()];
                    char[] node3 = new char[text.length()];
                    boolean up = true;
                    int keyPart = 0;
                    for (int kp = 0; kp < key.length(); kp++) {
                        keyNode[kp] = key.charAt(kp);
                    }
                    for (int l = 0; l < text.length(); l++) {
                        node1[l] = findChar(text.charAt(l), map);
                    }
                    for (int l = 0; l < text.length(); l++) {
                        if (keyPart < keyNode.length - 1) {
                            keyPart++;
                        } else {
                            keyPart = 0;
                        }
                        if (up) {
                            node2[l] = revalueInt(node1[l] - l * l - keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * keyPart * keyPart * keyNode[rIA(l, keyNode)], DEFAULT);
                        } else {
                            node2[l] = revalueInt(node1[l] + l * l + keyNode[keyPart] * keyNode[rIA(keyPart - 1, keyNode)] * keyNode[rIA(keyPart - 2, keyNode)] * keyNode[rIA(keyPart + l, keyNode)] * l * keyPart, DEFAULT);
                        }
                        up = !up;
                    }
                    for (int l = 0; l < text.length(); l++) {
                        node3[l] = findInt(node2[l], map);
                    }
                    return String.copyValueOf(node3);
                }

                private static int findChar(char s, String mask) {
                    for (int cu = 0; cu < mask.length(); cu++) {
                        if (s == mask.charAt(cu)) {
                            return cu;
                        }
                    }
                    return -1;
                }

                public static char findInt(int s, String mask) {
                    return mask.charAt(revalueInt(s, mask));
                }

                public static int revalueInt(int s, String mask) {
                    while (s > mask.length() - 1) {
                        s = s - (mask.length() - 1);
                    }
                    while (s < 0) {
                        s = s + (mask.length() - 1);
                    }
                    return s;
                }

                public static int rIA(int s, int[] array) {
                    while (s > array.length - 1) {
                        s = s - (array.length - 1);
                    }
                    while (s < 0) {
                        s = s + (array.length - 1);
                    }
                    return s;
                }
            }
        }

        public static class TextAnimator {
            public static final String STOP_ANIMATION = "TEXT_ANIMATION_ACTION_STOP";

            public static Thread animateAppend(final Activity a, final TextView tv, final int millispace) {
                return new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String all = tv.getText().toString();
                        String sofar = "";
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(null);
                            }
                        });
                        for (int c = 0; c < all.length(); c++) {
                            if (!tv.getText().toString().equals(STOP_ANIMATION)) {
                                sofar += all.charAt(c);
                                final String finalSofar = sofar;
                                a.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv.setText(finalSofar);
                                    }
                                });
                                try {
                                    sleep(millispace);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                break;
                            }
                        }
                    }
                });
            }

            public static Thread animateTypeEffect(final Activity a, final TextView tv, final int millispace) {
                return new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String all = tv.getText().toString();
                        String sofar = "";
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(null);
                            }
                        });
                        for (int c = 0; c < all.length(); c++) {
                            if (!tv.getText().toString().equals(STOP_ANIMATION)) {
                                sofar += all.charAt(c);
                                final String finalSofar = sofar;
                                a.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv.setText(finalSofar);
                                    }
                                });
                                int time = new Random().nextInt(millispace);
                                time = time + millispace / 2;
                                try {
                                    sleep(time);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                break;
                            }
                        }
                    }
                });
            }
        }
    }

    public static class Animations {
        public static final float[] VIBRATE_SMALL = new float[]{0, 1, -1, 2, -2, 3, -3, 4, -4, 5, -5, 6, -6, 5, -5, 4, -4, 3, -3, 2, -2, 1, -1, 0};
        public static final float[] VIBRATE_BIG = new float[]{0, 10, -10, 20, -20, 30, -30, 30, -20, 20, -10, 10, 0};
        public static final float[] JUMP_SMALL = new float[]{0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 7, 7, 7, 7, 6, 6, 5, 5, 4, 4, 3, 3, 2, 2, 1, 1, 0, 0};
        public static final float[] JUMP_BIG = new float[]{0, 0, 0, 0, 10, 10, 20, 20, 30, 30, 40, 40, 40, 40, 30, 30, 20, 20, 10, 10, 0, 0, 0, 0};
        public static final float[] INVISIBLE_TO_VISIBLE = new float[]{0.0f, 1.0f};
        public static final float[] VISIBLE_TO_INVISIBLE = new float[]{1.0f, 0.0f};

        public static float[] getSlideRight(Context c) {
            return new float[]{0, Device.screenX(c)};
        }

        public static float[] getSlideLeft(Context c) {
            return new float[]{0, -Device.screenX(c)};
        }

        public static float[] getSlideUp(Context c) {
            return new float[]{0, Device.screenY(c)};
        }

        public static float[] getSlideDown(Context c) {
            return new float[]{0, -Device.screenY(c)};
        }
    }

    public static class Filer {
        public static String readFile(File file) {
            try {
                StringBuilder text = new StringBuilder();
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String str;
                while ((str = in.readLine()) != null) {
                    text.append(str);
                    text.append('\n');
                }
                in.close();
                return text.toString();
            } catch (IOException e) {
                return null;
            }
        }

        public static void writeToFile(File file, String lines) {
            try {
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
                writer.write(lines);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void log(File f, String s) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss @ ");
            String time = dateFormat.format(new Date());
            String current = Filer.readFile(f);
            if (current != null) {
                String all = current + time + s;
                Filer.writeToFile(f, all);
            } else {
                String start = time + "Start Log\n";
                String all = start + time + s;
                Filer.writeToFile(f, all);
            }
            Log.i("Logger", s);
        }

        public static void logWarning(File f, String s) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss @ ");
            String time = dateFormat.format(new Date()) + "Warning: ";
            String current = Filer.readFile(f);
            if (current != null) {
                String all = current + time + s;
                Filer.writeToFile(f, all);
            } else {
                String start = time + "Start Log\n";
                String all = start + time + s;
                Filer.writeToFile(f, all);
            }
            Log.i("Logger", s);
        }

        public static void logError(File f, String s) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss @ ");
            String time = dateFormat.format(new Date()) + "Error!: ";
            String current = Filer.readFile(f);
            if (current != null) {
                String all = current + time + s;
                Filer.writeToFile(f, all);
            } else {
                String start = time + "Start Log\n";
                String all = start + time + s;
                Filer.writeToFile(f, all);
            }
            Log.i("Logger", s);
        }
    }

    public static class Graphics {
        public static class DragNavigation extends LinearLayout {
            private Drawable icon;
            private FrameLayout upContent;
            private ImageView iconHolder;
            private LinearLayout.LayoutParams iconParams, navigationParms;
            private int smallNavigation, backgroundColor;
            private boolean touchable = true;
            private boolean isOpen = false;
            private OnStateChangedListener onstate;
            private LinearLayout pullOff;

            public DragNavigation(Context context) {
                super(context);
                icon = null;
                backgroundColor = Color.BLACK;
                init();
            }

            public DragNavigation(Context context, Drawable icon, int backgroundColor) {
                super(context);
                this.icon = icon;
                this.backgroundColor = backgroundColor;
                init();
            }

            private void init() {
                final int y = Device.screenY(getContext());
                final int logoSize = (y / 8) - (y / 30);
                smallNavigation = y / 8;
                final int navFullY = (y / 3) * 2;
                iconParams = new LinearLayout.LayoutParams(logoSize, logoSize);
                navigationParms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, navFullY);
                iconHolder = new ImageView(getContext());
                upContent = new FrameLayout(getContext());
                pullOff = new LinearLayout(getContext());
                iconHolder.setLayoutParams(iconParams);
                iconHolder.setImageDrawable(icon);
                upContent.setPadding(20, 20, 20, 20);
                upContent.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, navFullY - smallNavigation));
                ViewGroup.LayoutParams pullOffParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, smallNavigation);
                pullOff.setLayoutParams(pullOffParams);
                pullOff.setGravity(Gravity.CENTER);
                pullOff.setOrientation(HORIZONTAL);
                setPadding(20, 0, 20, 0);
                setOrientation(LinearLayout.VERTICAL);
                setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                setLayoutParams(navigationParms);
                setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                setBackgroundColor(backgroundColor);
                addView(upContent);
                addView(pullOff);
                pullOff.addView(iconHolder);
                final float completeZero = -navFullY + smallNavigation;
                setY(completeZero);
                setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        v.performClick();
                        if (touchable) {
                            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                                float l = event.getRawY() - v.getHeight();
                                if (l >= completeZero && l <= 0) {
                                    setY(l);
                                } else if (l < completeZero) {
                                    setY(completeZero);
                                } else if (l > 0) {
                                    setY(0);
                                }
                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                //                                Log.i("Y", String.valueOf(getY()));
                                if (getY() >= (-getHeight() / 2) + smallNavigation / 2) {
                                    ObjectAnimator oa = ObjectAnimator.ofFloat(DragNavigation.this, View.TRANSLATION_Y, getY(), 0);
                                    oa.setDuration(300);
                                    oa.setInterpolator(new LinearInterpolator());
                                    oa.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {
                                            touchable = false;
                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            touchable = true;
                                            if (!isOpen) {
                                                if (onstate != null) onstate.onOpen();
                                            }
                                            isOpen = true;
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {
                                        }
                                    });
                                    oa.start();
                                } else {
                                    ObjectAnimator oa = ObjectAnimator.ofFloat(DragNavigation.this, View.TRANSLATION_Y, getY(), completeZero);
                                    oa.setDuration(300);
                                    oa.setInterpolator(new LinearInterpolator());
                                    oa.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {
                                            touchable = false;
                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            touchable = true;
                                            if (isOpen) {
                                                if (onstate != null) onstate.onClose();
                                            }
                                            isOpen = false;
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {
                                        }
                                    });
                                    oa.start();
                                }
                            }
                        }
                        return true;
                    }
                });
            }

            public void setIcon(Drawable icon) {
                this.icon = icon;
                iconHolder.setImageDrawable(this.icon);
            }

            public void setColor(int color) {
                this.backgroundColor = color;
            }

            public void setContent(View v) {
                upContent.removeAllViews();
                upContent.addView(v);
            }

            public void setOnIconClick(View.OnClickListener ocl) {
                iconHolder.setOnClickListener(ocl);
            }

            public void setOnStateChangedListener(OnStateChangedListener osc) {
                onstate = osc;
            }

            public void emptyContent() {
                upContent.removeAllViews();
            }

            public int spacerSize() {
                return smallNavigation;
            }

            public int calculateOverlayedColor(int parentViewColor){
                int calculatedA=convertColor(parentViewColor)+ convertColor(backgroundColor);
                return colorRange(parentViewColor,backgroundColor);
            }

            private int convertColor(int color){
                return Color.argb(80,Color.red(color),Color.green(color),Color.blue(color));
            }

            public int colorFix(int color){
                int red=Color.red(color);
                int green=Color.green(color);
                int blue=Color.blue(color);
                if(red%2!=0){
                    red-=1;
                }
                if(green%2!=0){
                    green-=1;
                }
                if(green%2!=0){
                    green-=1;
                }
                return Color.argb(Color.alpha(color),red,green,blue);
            }

            public int colorRange(int colorA,int colorB){
                int redA=Color.red(colorA);
                int greenA=Color.green(colorA);
                int blueA=Color.blue(colorA);
                int redB=Color.red(colorB);
                int greenB=Color.green(colorB);
                int blueB=Color.blue(colorB);
                int combineRed=redA-(redA-redB)/2,combineGreen=greenA-(greenA-greenB)/2,combineBlue=blueA-(blueA-blueB)/2;
                return Color.rgb(combineRed,combineGreen,combineBlue);
            }

            @Override
            public boolean performClick() {
                super.performClick();
                return true;
            }

            public interface OnStateChangedListener {
                void onOpen();

                void onClose();
            }
        }

        public static class ColorFadeAnimation {
            private ColorState onChange;
            private int colorA, colorB;

            public ColorFadeAnimation(int start, int end, ColorState colorState) {
                onChange = colorState;
                colorA = start;
                colorB = end;
            }

            public void start(int milliseconds) {
                final int rOffset = Color.red(colorB) - Color.red(colorA);
                final int gOffset = Color.green(colorB) - Color.green(colorA);
                final int bOffset = Color.blue(colorB) - Color.blue(colorA);
                int total = (Math.abs(rOffset) + Math.abs(gOffset) + Math.abs(bOffset));
                if (total == 0) {
                    total = 1;
                }
                final double maxTimePerColor = milliseconds / total;
                final int rA = Color.red(colorA);
                final int gA = Color.green(colorA);
                final int bA = Color.blue(colorA);
                final int rB = Color.red(colorB);
                final int gB = Color.green(colorB);
                final int bB = Color.blue(colorB);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int r;
                        int g = gA;
                        int b = bA;
                        if (rOffset < 0) {
                            for (r = rA; r > rB; r--) {
                                onChange.onColor(Color.rgb(r, g, b));
                                try {
                                    Thread.sleep((long) maxTimePerColor);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            for (r = rA; r < rB; r++) {
                                onChange.onColor(Color.rgb(r, g, b));
                                try {
                                    Thread.sleep((long) maxTimePerColor);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (gOffset < 0) {
                            for (g = gA; g > gB; g--) {
                                onChange.onColor(Color.rgb(r, g, b));
                                try {
                                    Thread.sleep((long) maxTimePerColor);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            for (g = gA; g < gB; g++) {
                                onChange.onColor(Color.rgb(r, g, b));
                                try {
                                    Thread.sleep((long) maxTimePerColor);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (bOffset < 0) {
                            for (b = bA; b > bB; b--) {
                                onChange.onColor(Color.rgb(r, g, b));
                                try {
                                    Thread.sleep((long) maxTimePerColor);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            for (b = bA; b < bB; b++) {
                                onChange.onColor(Color.rgb(r, g, b));
                                try {
                                    Thread.sleep((long) maxTimePerColor);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }).start();
            }

            public interface ColorState {
                void onColor(int color);
            }
        }
    }

    public static class ModularJSON {
        public static class Module {
            ArrayList<Module> subModules = null;
            String name = null, value = null;
            String[] options = null;

            public static class OptionModule extends Module {
                OptionModule(String name, String[] options) {
                    super.name = name;
                    super.options = options;
                }
            }

            public static class ValueModule extends Module {
                ValueModule(String name, String value) {
                    super.name = name;
                    super.value = value;
                }
            }

            public static class MasterModule extends Module {
                MasterModule(JSONObject masterObject) {
                    super.subModules = new ArrayList<>();
                    try {
                        Iterator<String> types = masterObject.keys();
                        while (types.hasNext()) {
                            String name = types.next();
                            if (masterObject.get(name) instanceof String) {
                                super.subModules.add(new ValueModule(name, masterObject.getString(name)));
                            } else if (masterObject.get(name) instanceof JSONArray) {
                                JSONArray convertFrom = masterObject.getJSONArray(name);
                                String[] converted = new String[convertFrom.length()];
                                for (int p = 0; p < convertFrom.length(); p++) {
                                    converted[p] = convertFrom.getString(p);
                                }
                                super.subModules.add(new OptionModule(name, converted));
                            } else if (masterObject.get(name) instanceof JSONObject) {
                                super.subModules.add(new MasterModule(masterObject.getJSONObject(name)));
                            }
                        }
                    } catch (JSONException ignored) {
                    }
                }
            }
        }
    }
}
