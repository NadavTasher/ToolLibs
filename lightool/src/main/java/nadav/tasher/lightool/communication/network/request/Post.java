package nadav.tasher.lightool.communication.network.request;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import nadav.tasher.lightool.communication.OnFinish;
import nadav.tasher.lightool.communication.SessionStatus;

public class Post extends AsyncTask<SessionStatus.SessionStatusTower, SessionStatus.SessionStatusTower, SessionStatus> {
    private String phpurl;
    private ArrayList<RequestParameter> parms;
    private OnFinish op;

    public Post(String url, RequestParameter[] parameters, OnFinish onFinish) {
        this.phpurl = url;
        parms = new ArrayList<>(Arrays.asList(parameters));
        op = onFinish;
    }

    private void sendStatus(SessionStatus ss, SessionStatus.SessionStatusTower[] tns) {
        for (int t = 0; t < tns.length; t++) {
            tns[t].send(ss);
        }
    }

    @Override
    protected SessionStatus doInBackground(SessionStatus.SessionStatusTower... tunnels) {
        SessionStatus currentStatus = new SessionStatus();
        sendStatus(currentStatus, tunnels);
        currentStatus.setStatus(SessionStatus.STARTING);
        sendStatus(currentStatus, tunnels);
        String response = null;
        StringBuilder data = new StringBuilder();
        BufferedReader reader = null;
        HttpURLConnection conn = null;
        try {
            currentStatus.setStatus(SessionStatus.IN_PROGRESS);
            sendStatus(currentStatus, tunnels);
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
            currentStatus.setStatus(SessionStatus.IN_PROGRESS);
            sendStatus(currentStatus, tunnels);
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
            currentStatus.setStatus(SessionStatus.NOT_FINISHED_FAILED);
            sendStatus(currentStatus, tunnels);
        } finally {
            currentStatus.setStatus(SessionStatus.FINISHING);
            sendStatus(currentStatus, tunnels);
            try {
                if (reader != null) {
                    reader.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                currentStatus.setStatus(SessionStatus.FINISHING_FAILED);
                sendStatus(currentStatus, tunnels);
            }
        }
        currentStatus.setStatus(SessionStatus.FINISHED_SUCCESS);
        currentStatus.setExtra(response);
        sendStatus(currentStatus, tunnels);
        return currentStatus;
    }

    @Override
    protected void onPostExecute(SessionStatus s) {
        super.onPostExecute(s);
        if (op != null) {
            op.onFinish(s);
        }
    }
}

