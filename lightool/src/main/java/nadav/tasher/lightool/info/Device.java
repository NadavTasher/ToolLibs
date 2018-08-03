package nadav.tasher.lightool.info;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

public class Device {
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

    static boolean isJobServiceRunning(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            JobScheduler scheduler = context.getSystemService(JobScheduler.class);
            boolean hasBeenScheduled = false;
            if (scheduler != null) {
                for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
                    if (jobInfo.getId() == id) {
                        hasBeenScheduled = true;
                        break;
                    }
                }
            }
            return hasBeenScheduled;
        } else {
            return false;
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
