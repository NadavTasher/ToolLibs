package nadav.tasher.lightool.communication.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;

import nadav.tasher.lightool.communication.OnFinish;
import nadav.tasher.lightool.communication.SessionStatus;
import nadav.tasher.lightool.communication.Tunnel;

public class BluetoothQuickSession extends AsyncTask<SessionStatus.SessionStatusTunnel, SessionStatus.SessionStatusTunnel, SessionStatus> {
    private Context context;
    private String address, data;
    private OnFinish onFinish;

    public BluetoothQuickSession(Context context, String address, String data, OnFinish onFinish) {
        this.context = context;
        this.onFinish = onFinish;
        this.address = address;
        this.data = data;
    }

    private void sendStatus(SessionStatus ss, Tunnel<SessionStatus>[] tns) {
        for (int t = 0; t < tns.length; t++) {
            tns[t].send(ss);
        }
    }

    @Override
    protected SessionStatus doInBackground(SessionStatus.SessionStatusTunnel... tunnels) {
        SessionStatus currentStatus = new SessionStatus();
        sendStatus(currentStatus, tunnels);
        currentStatus.setStatus(SessionStatus.STARTING);
        sendStatus(currentStatus, tunnels);
        BluetoothAdapter blueAdapter;
        BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            currentStatus.setStatus(SessionStatus.IN_PROGRESS);
            sendStatus(currentStatus, tunnels);
            blueAdapter = manager.getAdapter();
            if (blueAdapter.isEnabled()) {
                currentStatus.setStatus(SessionStatus.IN_PROGRESS);
                sendStatus(currentStatus, tunnels);
                blueAdapter.cancelDiscovery();
                final BluetoothDevice device = blueAdapter.getRemoteDevice(address);
                UUID uuid = device.getUuids()[0].getUuid();
                currentStatus.setStatus(SessionStatus.IN_PROGRESS);
                sendStatus(currentStatus, tunnels);
                try {
                    final BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
                    currentStatus.setStatus(SessionStatus.IN_PROGRESS);
                    sendStatus(currentStatus, tunnels);
                    try {
                        socket.connect();
                        while (!socket.isConnected())
                            currentStatus.setStatus(SessionStatus.IN_PROGRESS);
                        sendStatus(currentStatus, tunnels);
                        if (socket.isConnected()) {
                            currentStatus.setStatus(SessionStatus.FINISHING);
                            sendStatus(currentStatus, tunnels);
                            socket.getOutputStream().write(data.getBytes());
                            currentStatus.setStatus(SessionStatus.FINISHING);
                            sendStatus(currentStatus, tunnels);
                            try {
                                socket.getOutputStream().flush();
                                socket.getOutputStream().close();
                                socket.close();
                            } catch (IOException ignored) {
                                currentStatus.setStatus(SessionStatus.FINISHING_FAILED);
                                sendStatus(currentStatus, tunnels);
                            }
                            currentStatus.setStatus(SessionStatus.FINISHED_SUCCESS);
                            sendStatus(currentStatus, tunnels);
                        } else {
                            currentStatus.setStatus(SessionStatus.FINISHED_FAILED);
                            sendStatus(currentStatus, tunnels);
                        }
                    } catch (IOException e) {
                        currentStatus.setStatus(SessionStatus.NOT_FINISHED_FAILED);
                        sendStatus(currentStatus, tunnels);
                    }
                } catch (IOException e) {
                    currentStatus.setStatus(SessionStatus.NOT_FINISHED_FAILED);
                    sendStatus(currentStatus, tunnels);
                }
            } else {
                currentStatus.setStatus(SessionStatus.STARTING_FAILED);
                sendStatus(currentStatus, tunnels);
            }
        } else {
            currentStatus.setStatus(SessionStatus.STARTING_FAILED);
            sendStatus(currentStatus, tunnels);
        }
        return currentStatus;
    }

    @Override
    protected void onPostExecute(SessionStatus status) {
        super.onPostExecute(status);
        if (onFinish != null) onFinish.onFinish(status);
    }
}