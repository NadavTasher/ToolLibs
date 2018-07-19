package nadav.tasher.lightool.graphics.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ExpandingView extends LinearLayout {
    private View topView, bottomView;
    private boolean isOpened = false, isAnimating = false;
    private int duration, minimalSize;

    public ExpandingView(Context c, int duration, int minimalSize, View topView, View bottomView) {
        super(c);
        this.topView = topView;
        this.bottomView = bottomView;
        this.duration = duration;
        this.minimalSize = minimalSize;
        init();
    }

    public ExpandingView(Context c, Drawable back, int duration, int minimalSize, View topView, View bottomView) {
        super(c);
        this.setBackground(back);
        this.topView = topView;
        this.bottomView = bottomView;
        this.duration = duration;
        this.minimalSize = minimalSize;
        init();
    }

    public void setPadding(int horizontal, int vertical) {
        setPadding(horizontal, vertical, horizontal, vertical);
    }

    public int getVerticalPadding() {
        return getPaddingBottom() + getPaddingTop();
    }

    public int getHorizontalPadding() {
        return getPaddingLeft() + getPaddingRight();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.START);
        setPadding(30, 20);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAnimating) {
                    if (!isOpened) {
                        animate(true);
                    } else {
                        animate(false);
                    }
                    isOpened = !isOpened;
                }
            }
        });
        topView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, minimalSize));
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, minimalSize + getVerticalPadding()));
        addView(topView);
        addView(bottomView);
    }

    private void animate(final boolean open) {
        int bottomHeight=bottomView.getLayoutParams().height;
        if(bottomHeight == ViewGroup.LayoutParams.MATCH_PARENT || bottomHeight == ViewGroup.LayoutParams.WRAP_CONTENT){
            bottomView.measure(ViewGroup.LayoutParams.MATCH_PARENT, bottomHeight);
            bottomHeight=bottomView.getMeasuredHeight();
        }
        int first, second;
        if (open) {
            first = minimalSize + getVerticalPadding();
            second = minimalSize + bottomHeight + getVerticalPadding();
        } else {
            first = minimalSize + bottomHeight + getVerticalPadding();
            second = minimalSize + getVerticalPadding();
        }
        ValueAnimator animator = ValueAnimator.ofInt(first, second);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = val;
                setLayoutParams(layoutParams);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.setDuration(duration);
        animator.start();
    }
}
