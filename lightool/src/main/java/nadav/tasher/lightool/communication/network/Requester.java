package nadav.tasher.lightool.communication.network;

import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Requester extends AsyncTask<String, String, Response> {

    private Callback callback;
    private Request.Builder requestBuilder;

    public Requester(Request.Builder requestBuilder, Callback callback) {
        this.requestBuilder = requestBuilder;
        this.callback = callback;
    }

    @Override
    protected Response doInBackground(String... strings) {
        try {
            return new OkHttpClient().newCall(requestBuilder.build()).execute();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if (callback != null) {
                callback.onCall(response);
            }
        try {
            if (response != null && response.body() != null) response.body().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface Callback {
        void onCall(Response response);
    }
}
