package nadav.tasher.lightool;

import java.util.ArrayList;

public class Tunnel<T>{
    public ArrayList<T> comm=new ArrayList<>();
    public ArrayList<OnTunnel<T>> onTunnel=new ArrayList<>();

    public Tunnel(){

    }

    public void addReceiver(OnTunnel<T> tunnel){
        onTunnel.add(tunnel);
    }

    public void removeReceiver(OnTunnel<T> tunnel){
        onTunnel.remove(tunnel);
    }

    public void send(T s){
        comm.add(s);
        for(int a=0;a<onTunnel.size();a++){
            onTunnel.get(a).onReceive(s);
        }
    }

    public interface OnTunnel<T>{
        void onReceive(T response);
    }
}
