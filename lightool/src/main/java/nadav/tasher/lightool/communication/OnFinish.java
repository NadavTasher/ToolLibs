package nadav.tasher.lightool.communication;

public interface OnFinish {
    void onFinish(SessionStatus sessionStatus);
    void onFail(SessionStatus sessionStatus);
}
