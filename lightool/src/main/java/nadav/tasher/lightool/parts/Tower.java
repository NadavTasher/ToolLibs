package nadav.tasher.lightool.parts;

import java.util.ArrayList;

public class Tower<T> extends Peer<T> {
    private ArrayList<Peer<T>> peers = new ArrayList<>();

    public void addPeer(Peer<T> peer) {
        peers.add(peer);
    }

    public void removePeer(Peer<T> peer) {
        peers.remove(peer);
    }

    @Override
    public void tell(T s) {
        data.add(s);
        for (int a = 0; a < peers.size(); a++) {
            if (peers.get(a) != null) {
                peers.get(a).tell(s);
            }
        }
    }

    @Override
    public boolean ask(T s) {
        return true;
    }
}
