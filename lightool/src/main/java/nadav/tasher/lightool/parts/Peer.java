package nadav.tasher.lightool.parts;

import java.util.ArrayList;

public class Peer<T> {

    ArrayList<T> data = new ArrayList<>();

    OnPeer<T> onPeer;

    public Peer() {
    }

    public Peer(T initial) {
        this.data.add(initial);
    }

    public Peer(OnPeer<T> onPeer) {
        this.onPeer = onPeer;
    }

    public Peer(T initial, OnPeer<T> onPeer) {
        this.onPeer = onPeer;
        this.data.add(initial);
    }

    public boolean ask(T newData) {
        if (onPeer != null) {
            boolean result = onPeer.onPeer(newData);
            if (result) {
                data.add(newData);
            }
            return result;
        }
        return false;
    }

    public void tell(T newData) {
        data.add(newData);
        if (onPeer != null) {
            onPeer.onPeer(newData);
        }
    }

    public void setOnPeer(OnPeer<T> onPeer) {
        this.onPeer = onPeer;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public T getLast() {
        return (data.isEmpty()) ? null : data.get(data.size() - 1);
    }

    public interface OnPeer<T> {
        boolean onPeer(T data);
    }
}
