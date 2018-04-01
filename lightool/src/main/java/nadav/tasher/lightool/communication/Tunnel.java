package nadav.tasher.lightool.communication;

import java.util.ArrayList;

public class Tunnel<T>{
    public ArrayList<T> comm=new ArrayList<>();
    public ArrayList<OnTunnel<T>> onTunnel=new ArrayList<>();

    public Tunnel(){

    }

    public Tunnel(T initial){
        send(initial);
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

    public T getLast(){
        if(comm.size()>0) {
            return comm.get(comm.size() - 1);
        }else{
            return null;
        }
    }

    public interface OnTunnel<T>{
        void onReceive(T response);
    }
}
