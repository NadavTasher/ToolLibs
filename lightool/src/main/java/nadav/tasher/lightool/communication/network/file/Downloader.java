package nadav.tasher.lightool.communication.network.file;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Downloader extends AsyncTask<String, String, String> {
    private String furl;
    private File fdpath;
    private boolean available;
    private Downloader.OnDownload oe;

    public Downloader(String url, File path, Downloader.OnDownload onfile) {
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
