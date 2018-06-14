package nadav.tasher.lightool.parts;

import java.util.ArrayList;

public class Peer<T> {

    public ArrayList<T> datas = new ArrayList<>();

    public T currentData;

    public OnPeer<T> onPeer;

    public Peer() {
    }

    public Peer(T initial) {
        this.datas.add(initial);
        this.currentData = initial;
    }

    public Peer(OnPeer<T> onPeer) {
        this.onPeer = onPeer;
    }

    public Peer(T initial, OnPeer<T> onPeer) {
        this.onPeer = onPeer;
        this.datas.add(initial);
        this.currentData = initial;
    }

    public boolean ask(T data) {
        if (onPeer != null) {
            boolean result = onPeer.onPeer(data);
            if (result) {
                datas.add(data);
                currentData = data;
            }
            return result;
        }
        return false;
    }

    public void tell(T data) {
        datas.add(data);
        currentData = data;
        if (onPeer != null) {
            onPeer.onPeer(data);
        }
    }

    public void setOnPeer(OnPeer<T> onPeer){
        this.onPeer=onPeer;
    }

    public ArrayList<T> getDatas() {
        return datas;
    }

    public T getCurrentData() {
        return currentData;
    }

    public interface OnPeer<T> {
        boolean onPeer(T data);
    }
}
