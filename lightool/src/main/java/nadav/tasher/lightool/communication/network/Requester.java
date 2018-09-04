package nadav.tasher.lightool.communication.network;

import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Requester extends AsyncTask<String, String, Response> {

    private Callback callback;
    private Request request;

    public Requester(Request request, Callback callback) {
        this.request = request;
        this.callback = callback;
    }

    @Override
    protected Response doInBackground(String... strings) {
        try {
            return new OkHttpClient().newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if (callback != null) {
            if (response != null) {
                callback.onCall(response);
            } else {
                callback.onCall(new Response.Builder().build());
            }
        }
        if (response != null && response.body() != null) response.body().close();
    }

    public interface Callback {
        void onCall(Response response);
    }
}
