package nadav.tasher.lightool.communication;

public class SessionStatus {
    public static final int IDLE=0;
    public static final int STARTING=1;
    public static final int IN_PROGRESS=2;
    public static final int FINISHING=3;
    public static final int NOT_FINISHED_FAILED=4;
    public static final int FINISHED_SUCCESS=5;
    public static final int FINISHED_FAILED=6;
    public static final int FINISHING_FAILED=7;
    public static final int STARTING_FAILED=7;
    private int status=IDLE;
    private String extra=null;
    public SessionStatus(){}
    public SessionStatus(int initial){
        status=initial;
    }
    public void setStatus(int newstatus){
        status=newstatus;
    }
    public int getStatus(){
        return status;
    }
    public void setExtra(String s){
        extra=s;
    }
    public String getExtra(){
        return extra;
    }
}