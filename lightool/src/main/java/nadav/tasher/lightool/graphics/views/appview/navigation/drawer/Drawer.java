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
    private FrameLayout upContent;
    private View currentContent;
    private LinearLayout.LayoutParams navigationParms;
    private ArrayList<OnState> onstates = new ArrayList<>();
    private int backgroundColor;
    private boolean isOpen = false;
    private float completeZero;

    public Drawer(Context context) {
        super(context);
        backgroundColor = Color.BLACK;
        init();
    }

    public Drawer(Context context, int backgroundColor) {
        super(context);
        this.backgroundColor = backgroundColor;
        init();
    }

    private void init() {
        //        backgroundColor = Color.argb(128, Color.red(backgroundColor), Color.green(backgroundColor), Color.blue(backgroundColor));
        final int y = Device.screenY(getContext());
        navigationParms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, y);
        upContent = new FrameLayout(getContext());
        upContent.setPadding(20, 20, 20, 20);
        upContent.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, y));
        setPadding(20, 0, 20, 0);
        setOrientation(LinearLayout.VERTICAL);
        setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setLayoutParams(navigationParms);
        setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        setBackgroundColor(backgroundColor);
        addView(upContent);
        completeZero = -y;
        setY(completeZero);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open(final boolean runAction, double precent) {
        precent = Math.abs(precent);
        ObjectAnimator oa = ObjectAnimator.ofFloat(Drawer.this, View.TRANSLATION_Y, getY(), -(int) (getHeight() * (1 - precent)));
        oa.setDuration(300);
        oa.setInterpolator(new LinearInterpolator());
        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (!isOpen) {
                    for (int a = 0; a < onstates.size(); a++) {
                        if (onstates.get(a) != null && (runAction || onstates.get(a) instanceof PersistantOnState))
                            onstates.get(a).onOpen();
                    }
                    for (int a = 0; a < onstates.size(); a++) {
                        if (onstates.get(a) != null && (runAction || onstates.get(a) instanceof PersistantOnState))
                            onstates.get(a).onBoth(isOpen);
                    }
                }
                isOpen = true;
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
        oa.start();
    }

    public void close(final boolean runAction) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(Drawer.this, View.TRANSLATION_Y, getY(), completeZero);
        oa.setDuration(300);
        oa.setInterpolator(new LinearInterpolator());
        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isOpen) {
                    for (int a = 0; a < onstates.size(); a++) {
                        if (onstates.get(a) != null && (runAction || onstates.get(a) instanceof PersistantOnState))
                            onstates.get(a).onClose();
                    }
                    for (int a = 0; a < onstates.size(); a++) {
                        if (onstates.get(a) != null && (runAction || onstates.get(a) instanceof PersistantOnState))
                            onstates.get(a).onBoth(isOpen);
                    }
                }
                isOpen = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        oa.start();
    }

    public void setColor(int color) {
        this.backgroundColor = color;
        setBackgroundColor(backgroundColor);
    }

    public View getContent() {
        return currentContent;
    }

    public void setContent(View v) {
        upContent.removeAllViews();
        currentContent = v;
        upContent.addView(currentContent);
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
        upContent.removeAllViews();
    }

    public int getStatusBarColor(int colorA, int colorB) {
        int redA = Color.red(colorA);
        int greenA = Color.green(colorA);
        int blueA = Color.blue(colorA);
        int redB = Color.red(colorB);
        int greenB = Color.green(colorB);
        int blueB = Color.blue(colorB);
        int alphaA = Color.alpha(colorA);
        int alphaB = Color.alpha(colorB);
        int combineRed = redA - (redA - redB) / 2, combineGreen = greenA - (greenA - greenB) / 2, combineBlue = blueA - (blueA - blueB) / 2;
        int combineAlpha = alphaA - (alphaA - alphaB) / 2;
        return Color.rgb(combineRed, combineGreen, combineBlue);
    }

    public interface OnState {
        void onOpen();

        void onClose();

        void onBoth(boolean isOpened);
    }

    public interface PersistantOnState extends OnState {

    }
}