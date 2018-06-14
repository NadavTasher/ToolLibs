package nadav.tasher.lightool.parts;

import java.util.ArrayList;

public class Tube<T> {
    public static final int SUCCESS = -1;

    public ArrayList<T> datas = new ArrayList<>();
    public ArrayList<Peer<T>> peers = new ArrayList<>();

    public Tube() {
    }

    public void addPeer(Peer<T> peer) {
        peers.add(peer);
    }

    public void removePeer(Peer<T> peer) {
        peers.remove(peer);
    }

    public int blow(T s) {
        datas.add(s);
        for (int a = 0; a < peers.size(); a++) {
            if (peers.get(a) != null) {
                boolean result = peers.get(a).ask(s);
                if (!result) {
                    return a;
                }
            }
        }
        return SUCCESS;
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
