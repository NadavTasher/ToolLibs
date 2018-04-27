package nadav.tasher.lightool.communication.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import nadav.tasher.lightool.communication.SessionStatus;
import nadav.tasher.lightool.parts.Tower;

public class BluetoothSession extends AsyncTask<SessionStatus.SessionStatusTower, SessionStatus.SessionStatusTower, SessionStatus> {
    private Context context;
    private String address;
    private Tower<String> incomingTower, outgoingTower;
    private BluetoothSocket socket;
    private SessionStatus currentStatus;
    private int sample;
    private SessionStatus.SessionStatusTower[] tunnels;

    public BluetoothSession(Context context, String address, int samplingRateHz) {
        this.context = context;
        this.address = address;
        this.sample = 1000 / samplingRateHz;
        incomingTower = new Tower<>();
        outgoingTower = new Tower<>();
    }

    private void sendStatus() {
        for (int t = 0; t < tunnels.length; t++) {
            tunnels[t].send(currentStatus);
        }
    }

    public void send(String s) {
        outgoingTower.send(s);
    }

    public void close() {
        if (socket != null) {
            if (socket.isConnected()) {
                try {
                    socket.getOutputStream().flush();
                    socket.getOutputStream().close();
                    socket.close();
                    currentStatus.setStatus(SessionStatus.FINISHING);
                    sendStatus();
                } catch (IOException ignored) {
                    currentStatus.setStatus(SessionStatus.FINISHING_FAILED);
                    sendStatus();
                }
                currentStatus.setStatus(SessionStatus.FINISHED_SUCCESS);
                sendStatus();
            }
        }
    }

    public void registerIncoming(Tower.OnTunnel<String> onTunnel) {
        incomingTower.addReceiver(onTunnel);
    }

    public void unregisterIncoming(Tower.OnTunnel<String> onTunnel) {
        incomingTower.removeReceiver(onTunnel);
    }

    public boolean isConnected() {
        if (socket != null) {
            return socket.isConnected();
        } else {
            return false;
        }
    }

    @Override
    protected SessionStatus doInBackground(SessionStatus.SessionStatusTower... tunnels) {
        this.tunnels = tunnels;
        currentStatus = new SessionStatus();
        sendStatus();
        currentStatus.setStatus(SessionStatus.STARTING);
        sendStatus();
        BluetoothAdapter blueAdapter;
        BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            currentStatus.setStatus(SessionStatus.IN_PROGRESS);
            sendStatus();
            blueAdapter = manager.getAdapter();
            if (blueAdapter.isEnabled()) {
                currentStatus.setStatus(SessionStatus.IN_PROGRESS);
                sendStatus();
                blueAdapter.cancelDiscovery();
                final BluetoothDevice device = blueAdapter.getRemoteDevice(address);
                UUID uuid = device.getUuids()[0].getUuid();
                currentStatus.setStatus(SessionStatus.IN_PROGRESS);
                sendStatus();
                try {
                    socket = device.createRfcommSocketToServiceRecord(uuid);
                    currentStatus.setStatus(SessionStatus.IN_PROGRESS);
                    sendStatus();
                    try {
                        socket.connect();
                        while (!socket.isConnected())
                            currentStatus.setStatus(SessionStatus.IN_PROGRESS);
                        sendStatus();
                        outgoingTower.addReceiver(new Tower.OnTunnel<String>() {
                            @Override
                            public void onReceive(String response) {
                                if (socket != null) {
                                    if (socket.isConnected()) {
                                        try {
                                            socket.getOutputStream().write(response.getBytes());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        });
                        currentStatus.setStatus(SessionStatus.CONNECTED);
                        sendStatus();
                        BufferedReader r;
                        r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        StringBuilder caught = new StringBuilder();
                        while (socket.isConnected()) {
                            try {
                                StringBuilder total = new StringBuilder();
                                while (r.ready()) {
                                    total.append(((char) r.read()));
                                }
                                if (!(caught.toString() + total.toString()).equals(caught.toString())) {
                                    caught.append(total.toString());
                                    incomingTower.send(caught.toString());
                                    currentStatus.setStatus(SessionStatus.IDLE);
                                    sendStatus();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                Thread.sleep(sample);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        currentStatus.setStatus(SessionStatus.DISCONNECTED);
                        sendStatus();
                    } catch (IOException e) {
                        currentStatus.setStatus(SessionStatus.FINISHED_FAILED);
                        sendStatus();
                    }
                } catch (IOException ignored) {
                    currentStatus.setStatus(SessionStatus.STARTING_FAILED);
                    sendStatus();
                }
            } else {
                currentStatus.setStatus(SessionStatus.STARTING_FAILED);
                sendStatus();
            }
        } else {
            currentStatus.setStatus(SessionStatus.STARTING_FAILED);
            sendStatus();
        }
        return currentStatus;
    }
}

