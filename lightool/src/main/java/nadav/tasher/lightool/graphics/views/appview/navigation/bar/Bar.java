package nadav.tasher.lightool.graphics.views.appview.navigation.bar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

public class Bar extends LinearLayout {

    private HorizontalScrollView hsv;
    private LinearLayout squircles;
    private Squircle mainSquircle;
    private Squircle.OnState onState;
    private Squircle.Holder mainHolder;
    private ArrayList<Squircle> squircleList;
    private boolean isOpen = false;

    public Bar(Context context, Squircle main) {
        super(context);
        this.mainSquircle = main;
        makeMain();
        init();
    }

    public Squircle getMainSquircle() {
        return mainSquircle;
    }

    private void makeMain() {
        mainSquircle.removeAllOnStates();
        mainSquircle.addOnState(new Squircle.OnState() {
            @Override
            public void onOpen() {
                open(true);
            }

            @Override
            public void onClose() {
                close(true);
            }

            @Override
            public void onBoth(boolean isOpened) {
            }
        });
    }

    private void init() {
        squircleList = new ArrayList<>();
        squircles = new LinearLayout(getContext());
        squircles.setOrientation(HORIZONTAL);
        squircles.setGravity(Gravity.CENTER);
        hsv = new HorizontalScrollView(getContext());
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        setLayoutDirection(LAYOUT_DIRECTION_RTL);
        mainHolder = new Squircle.Holder(getContext(), mainSquircle);
        mainHolder.disableRight();
        addView(mainHolder);
        hsv.addView(squircles);
        hsv.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        hsv.setHorizontalScrollBarEnabled(false);
        hsv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(hsv);
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setPadding(mainHolder.getSidePad(),0,mainHolder.getSidePad(),0);
        close(false);
    }

    public void addSquircle(Squircle squircle) {
        squircle.setMaxXY(mainSquircle.getMaxXY());
        squircleList.add(squircle);
        squircles.addView(new Squircle.Holder(getContext(), squircle));
    }

    public void addSquircles(Squircle[] squircle) {
        for (Squircle s : squircle) {
            addSquircle(s);
        }
    }

    public void addSquircles(ArrayList<Squircle> squircle) {
        for (Squircle s : squircle) {
            addSquircle(s);
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open(boolean animate) {
        isOpen = true;
        hsv.setAlpha(0);
        if (animate) {
            ObjectAnimator slide = ObjectAnimator.ofFloat(hsv, View.TRANSLATION_X, -hsv.getWidth(), 0);
            slide.setDuration(500);
            ObjectAnimator transparancy = ObjectAnimator.ofFloat(hsv, View.ALPHA, 0, 1);
            transparancy.setDuration(500);
            transparancy.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mainSquircle.setClickable(false);
                    hsv.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mainSquircle.setClickable(true);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            slide.start();
            transparancy.start();
        } else {
            hsv.setX(0);
            hsv.setVisibility(View.VISIBLE);
        }
    }

    public void close(boolean animate) {
        isOpen = false;
        hsv.setAlpha(1);
        if (animate) {
            ObjectAnimator slide = ObjectAnimator.ofFloat(hsv, View.TRANSLATION_X, 0, -hsv.getWidth());
            slide.setDuration(500);
            ObjectAnimator transparancy = ObjectAnimator.ofFloat(hsv, View.ALPHA, 1, 0);
            transparancy.setDuration(500);
            transparancy.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mainSquircle.setClickable(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mainSquircle.setClickable(true);
                    hsv.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            slide.start();
            transparancy.start();
        } else {
            hsv.setX(-hsv.getWidth());
            hsv.setVisibility(View.GONE);
        }
    }
}
