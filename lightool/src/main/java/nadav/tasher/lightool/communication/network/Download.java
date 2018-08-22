package nadav.tasher.lightool.communication.network;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Download extends AsyncTask<String, String, Boolean> {

    private String source;
    private File destination;
    private Callback callback;
    private Exception exception;

    public Download(String source, File destination, Callback callback) {
        this.source = source;
        this.destination = destination;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            Response response = new OkHttpClient().newCall(new Request.Builder().url(source).build()).execute();
            if (response.code() != 200) {
                exception = new Exception("The server responded with " + response.code());
                if (response.body() != null) {
                    response.body().close();
                }
                return false;
            } else {
                if (response.body() != null) {
                    FileOutputStream outputStream = new FileOutputStream(destination);
                    outputStream.write(response.body().bytes());
                    outputStream.close();
                    response.body().close();
                    return true;
                } else {
                    exception = new Exception("The response body was empty");
                    return false;
                }
            }
        } catch (IOException e) {
            this.exception = e;
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        if (callback != null) {
            if (success) {
                callback.onSuccess(destination);
            } else {
                callback.onFailure(exception);
            }
        }
    }

    public interface Callback {
        void onSuccess(File file);

        void onFailure(Exception e);
    }
}
