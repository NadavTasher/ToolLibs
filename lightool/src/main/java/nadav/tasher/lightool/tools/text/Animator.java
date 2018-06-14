package nadav.tasher.lightool.tools.text;

import android.app.Activity;
import android.widget.TextView;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Animator {
    public static final String STOP_ANIMATION = "TEXT_ANIMATION_ACTION_STOP";

    public static Thread animateAppend(final Activity a, final TextView tv, final int millispace) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                final String all = tv.getText().toString();
                String sofar = "";
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(null);
                    }
                });
                for (int c = 0; c < all.length(); c++) {
                    if (!tv.getText().toString().equals(STOP_ANIMATION)) {
                        sofar += all.charAt(c);
                        final String finalSofar = sofar;
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(finalSofar);
                            }
                        });
                        try {
                            sleep(millispace);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        break;
                    }
                }
            }
        });
    }

    public static Thread animateTypeEffect(final Activity a, final TextView tv, final int millispace) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                final String all = tv.getText().toString();
                String sofar = "";
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(null);
                    }
                });
                for (int c = 0; c < all.length(); c++) {
                    if (!tv.getText().toString().equals(STOP_ANIMATION)) {
                        sofar += all.charAt(c);
                        final String finalSofar = sofar;
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(finalSofar);
                            }
                        });
                        int time = new Random().nextInt(millispace);
                        time = time + millispace / 2;
                        try {
                            sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        break;
                    }
                }
            }
        });
    }
}
