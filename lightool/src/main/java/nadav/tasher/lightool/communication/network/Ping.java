package nadav.tasher.lightool.communication.network;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Ping extends AsyncTask<String, String, Boolean> {
    private Ping.OnEnd onEnd;
    private int tmout = 2000;
    private String addr;

    public Ping(String url, int timeout, Ping.OnEnd e) {
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
