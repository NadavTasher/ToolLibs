package nadav.tasher.lightool.parts.communication;

import java.util.ArrayList;

public class Tower<T> {
    public ArrayList<T> datas = new ArrayList<>();
    public ArrayList<Peer<T>> peers = new ArrayList<>();

    public Tower() {
    }

    public Tower(T initial) {
        tell(initial);
    }

    public void addPeer(Peer<T> peer) {
        peers.add(peer);
    }

    public void removePeer(Peer<T> peer) {
        peers.remove(peer);
    }

    public void tell(T s) {
        datas.add(s);
        for (int a = 0; a < peers.size(); a++) {
            if (peers.get(a) != null) {
                peers.get(a).tell(s);
            }
        }
    }

    public T getLast() {
        if (datas.size() > 0) {
            return datas.get(datas.size() - 1);
        } else {
            return null;
        }
    }

    public ArrayList<T> getDatas() {
        return datas;
    }
}
