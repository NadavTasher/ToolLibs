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

import nadav.tasher.lightool.parts.Peer;
import nadav.tasher.lightool.parts.Tower;

public class BluetoothSession extends AsyncTask<String, String, Boolean> {
    private Context context;
    private String address;
    private Tower<String> incomingTower, outgoingTower;
    private BluetoothSocket socket;
    private int sample;

    public BluetoothSession(Context context, String address, int samplingRateHz) {
        this.context = context;
        this.address = address;
        this.sample = 1000 / samplingRateHz;
        incomingTower = new Tower<>();
        outgoingTower = new Tower<>();
    }

    public void send(String s) {
        outgoingTower.tell(s);
    }

    public void close() {
        if (socket != null) {
            if (socket.isConnected()) {
                try {
                    socket.getOutputStream().flush();
                    socket.getOutputStream().close();
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public void registerIncoming(Peer<String> peer) {
        incomingTower.addPeer(peer);
    }

    public void unregisterIncoming(Peer<String> peer) {
        incomingTower.removePeer(peer);
    }

    public boolean isConnected() {
        if (socket != null) {
            return socket.isConnected();
        } else {
            return false;
        }
    }

    @Override
    protected Boolean doInBackground(String... tunnels) {
        BluetoothAdapter blueAdapter;
        BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            blueAdapter = manager.getAdapter();
            if (blueAdapter.isEnabled()) {
                blueAdapter.cancelDiscovery();
                final BluetoothDevice device = blueAdapter.getRemoteDevice(address);
                UUID uuid = device.getUuids()[0].getUuid();
                try {
                    socket = device.createRfcommSocketToServiceRecord(uuid);
                    try {
                        socket.connect();
                        while (!socket.isConnected()) ;
                        outgoingTower.addPeer(new Peer<>(new Peer.OnPeer<String>() {
                            @Override
                            public boolean onPeer(String data) {
                                if (socket != null) {
                                    if (socket.isConnected()) {
                                        try {
                                            socket.getOutputStream().write(data.getBytes());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                return true;
                            }
                        }));
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
                                    incomingTower.tell(caught.toString());
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
                    } catch (IOException e) {
                    }
                } catch (IOException ignored) {
                }
            }
        }
        return true;
    }
}

