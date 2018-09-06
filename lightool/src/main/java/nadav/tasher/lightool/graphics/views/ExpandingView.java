package nadav.tasher.lightool.graphics.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class ExpandingView extends LinearLayout {
    private FrameLayout topHolder, bottomHolder;
    private boolean isOpened = false, isAnimating = false;
    private int duration = 500;

    public ExpandingView(Context context) {
        super(context);
        init();
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        initLayout();
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
        topHolder = new FrameLayout(getContext());
        bottomHolder = new FrameLayout(getContext());
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
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
        addView(topHolder);
        addView(bottomHolder);
        initLayout();
    }

    public FrameLayout getBottomHolder() {
        return bottomHolder;
    }

    public FrameLayout getTopHolder() {
        return topHolder;
    }

    public void setBottom(View v) {
        bottomHolder.removeAllViews();
        bottomHolder.addView(v);
        initLayout();
    }

    public void setTop(View v) {
        topHolder.removeAllViews();
        topHolder.addView(v);
        initLayout();
    }

    private void initLayout() {
        Utils.measure(topHolder);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, topHolder.getMeasuredHeight() + getVerticalPadding()));
        setPadding(getHorizontalPadding() / 2, getVerticalPadding() / 2);
    }

    private void animate(final boolean open) {
        Utils.measure(topHolder);
        Utils.measure(bottomHolder);

        int bottomHeight = bottomHolder.getMeasuredHeight();
        int topHeight = topHolder.getMeasuredHeight();
        int first, second;
        if (open) {
            first = topHeight + getVerticalPadding();
            second = topHeight + bottomHeight + getVerticalPadding();
        } else {
            first = topHeight + bottomHeight + getVerticalPadding();
            second = topHeight + getVerticalPadding();
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
