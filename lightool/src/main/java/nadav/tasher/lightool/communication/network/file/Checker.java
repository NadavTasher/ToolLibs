package nadav.tasher.lightool.communication.network.file;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Checker extends AsyncTask<String, String, String> {
    private long kbs;
    private String addr;
    private boolean available;
    private Checker.OnFile of;

    public Checker(String url, Checker.OnFile onFile) {
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