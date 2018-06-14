package nadav.tasher.lightool.communication.network.file;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Reader extends AsyncTask<String, String, String> {
    private Reader.OnEnd one;
    private String fi;

    public Reader(String file, Reader.OnEnd oe) {
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
