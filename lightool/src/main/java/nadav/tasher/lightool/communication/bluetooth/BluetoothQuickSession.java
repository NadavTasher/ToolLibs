package nadav.tasher.lightool.communication.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;

public class BluetoothQuickSession extends AsyncTask<String, String, Boolean> {
    private Context context;
    private String address, data;
    private Callback callback;

    public BluetoothQuickSession(Context context, String address, String data, Callback callback) {
        this.context = context;
        this.callback = callback;
        this.address = address;
        this.data = data;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        BluetoothAdapter blueAdapter;
        BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            blueAdapter = manager.getAdapter();
            if (blueAdapter.isEnabled()) {
                blueAdapter.cancelDiscovery();
                final BluetoothDevice device = blueAdapter.getRemoteDevice(address);
                UUID uuid = device.getUuids()[0].getUuid();
                try {
                    final BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
                    try {
                        socket.connect();
                        while (!socket.isConnected()) ;
                        if (socket.isConnected()) {
                            socket.getOutputStream().write(data.getBytes());
                            try {
                                socket.getOutputStream().flush();
                                socket.getOutputStream().close();
                                socket.close();
                            } catch (IOException ignored) {
                            }
                            return true;
                        } else {
                            return false;
                        }
                    } catch (IOException e) {
                        return false;
                    }
                } catch (IOException e) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        if (callback != null) callback.onFinish(success);
    }

    public interface Callback {
        void onFinish(Boolean success);
    }
}