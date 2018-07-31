package nadav.tasher.lightool.graphics.views.appview.navigation.drawer;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

import nadav.tasher.lightool.info.Device;

public class Drawer extends LinearLayout {
    private ObjectAnimator animation;
    private FrameLayout drawerView;
    private View closer;
    private ArrayList<OnState> onstates = new ArrayList<>();

    public Drawer(Context context) {
        super(context);
        init();
    }

    private void init() {
        // Log.i("DrawerInfo","Current Height: "+drawer.getLayoutParams().height+" Current Y: "+getY()+" IsOpened: "+isOpen());
        drawerView = new FrameLayout(getContext());
        drawerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        closer = new View(getContext());
        closer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        closer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen()) close();
            }
        });
        closer.setVisibility(View.GONE);
        setOrientation(LinearLayout.VERTICAL);
        setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER);
        addView(drawerView);
        addView(closer);
    }

    public void open(double percent) {
        if (!isAnimating()) {
            percent = Math.abs(percent);
            closer.setVisibility(View.VISIBLE);
            drawerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Device.screenY(getContext()) * percent)));
            animation = ObjectAnimator.ofFloat(Drawer.this, View.TRANSLATION_Y, -drawerView.getLayoutParams().height, 0);
            animation.setDuration(300);
            animation.setInterpolator(new LinearInterpolator());
            animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (isOpen()) {
                        for (int a = 0; a < onstates.size(); a++) {
                            onstates.get(a).onOpen();
                        }
                        for (int a = 0; a < onstates.size(); a++) {
                            onstates.get(a).onBoth(isOpen());
                        }
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animation.start();
        }
    }

    public void close() {
        if (!isAnimating()) {
            closer.setVisibility(View.GONE);
            animation = ObjectAnimator.ofFloat(Drawer.this, View.TRANSLATION_Y, 0, -drawerView.getLayoutParams().height);
            animation.setDuration(300);
            animation.setInterpolator(new LinearInterpolator());
            animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    drawerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                    if (!isOpen()) {
                        for (int a = 0; a < onstates.size(); a++) {
                            onstates.get(a).onClose();
                        }
                        for (int a = 0; a < onstates.size(); a++) {
                            onstates.get(a).onBoth(isOpen());
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animation.start();
        }
    }

    public boolean isAnimating() {
        return animation != null && animation.isRunning();
    }

    public boolean isOpen() {
        return drawerView.getLayoutParams() != null && drawerView.getLayoutParams().height != 0;
    }

    public FrameLayout getDrawerView() {
        return drawerView;
    }

    public View getContent() {
        if (drawerView.getChildCount() > 0) {
            return drawerView.getChildAt(0);
        } else {
            return null;
        }
    }

    public void setContent(View v) {
        drawerView.removeAllViews();
        drawerView.addView(v);
    }

    public void addOnState(OnState onState) {
        onstates.add(onState);
    }

    public void removeOnState(OnState onState) {
        onstates.remove(onState);
    }

    public void removeAllOnStates() {
        onstates.clear();
    }

    public void emptyContent() {
        drawerView.removeAllViews();
    }

    public int getStatusBarColor(int colorA, int colorB) {
        int redA = Color.red(colorA);
        int greenA = Color.green(colorA);
        int blueA = Color.blue(colorA);
        int redB = Color.red(colorB);
        int greenB = Color.green(colorB);
        int blueB = Color.blue(colorB);
        int combineRed = redA - (redA - redB) / 2, combineGreen = greenA - (greenA - greenB) / 2, combineBlue = blueA - (blueA - blueB) / 2;
        return Color.rgb(combineRed, combineGreen, combineBlue);
    }

    public interface OnState {
        void onOpen();

        void onClose();

        void onBoth(boolean isOpened);
    }
}