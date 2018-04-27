package nadav.tasher.lightool.tools.transfer;

import java.util.ArrayList;

public class Clip<T>{

    private ArrayList<T> history=new ArrayList<>();

    private T current;

    public Clip(){}

    public Clip(T initial){
        history.add(initial);
        current=initial;
    }

    public void set(T t){
        history.add(t);
        current=t;
    }

    public T get(){
        return current;
    }

    public ArrayList<T> getHistory() {
        return history;
    }
}
