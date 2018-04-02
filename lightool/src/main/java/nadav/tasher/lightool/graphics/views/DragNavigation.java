package nadav.tasher.lightool.graphics.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import nadav.tasher.lightool.info.Device;

public class DragNavigation extends LinearLayout {
    private Drawable icon;
    private FrameLayout upContent;
    private ImageView iconHolder;
    private LinearLayout.LayoutParams iconParams, navigationParms;
    private int smallNavigation, backgroundColor;
    private boolean touchable = true;
    private boolean isOpen = false;
    private OnStateChangedListener onstate;
    private LinearLayout pullOff;
    private float completeZero;

    public DragNavigation(Context context) {
        super(context);
        icon = null;
        backgroundColor = Color.BLACK;
        init();
    }

    public DragNavigation(Context context, Drawable icon, int backgroundColor) {
        super(context);
        this.icon = icon;
        this.backgroundColor = backgroundColor;
        init();
    }

    private void init() {
        backgroundColor = Color.argb(128, Color.red(backgroundColor), Color.green(backgroundColor), Color.blue(backgroundColor));
        final int y = Device.screenY(getContext());
        final int logoSize = (y / 8) - (y / 30);
        smallNavigation = y / 8;
        final int navFullY = (y / 3) * 2;
        iconParams = new LinearLayout.LayoutParams(logoSize, logoSize);
        navigationParms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, navFullY);
        iconHolder = new ImageView(getContext());
        upContent = new FrameLayout(getContext());
        pullOff = new LinearLayout(getContext());
        iconHolder.setLayoutParams(iconParams);
        iconHolder.setImageDrawable(icon);
        upContent.setPadding(20, 20, 20, 20);
        upContent.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, navFullY - smallNavigation));
        ViewGroup.LayoutParams pullOffParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, smallNavigation);
        pullOff.setLayoutParams(pullOffParams);
        pullOff.setGravity(Gravity.CENTER);
        pullOff.setOrientation(HORIZONTAL);
        setPadding(20, 0, 20, 0);
        setOrientation(LinearLayout.VERTICAL);
        setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setLayoutParams(navigationParms);
        setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        setBackgroundColor(backgroundColor);
        addView(upContent);
        addView(pullOff);
        pullOff.addView(iconHolder);
        completeZero = -navFullY + smallNavigation;
        setY(completeZero);
        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                if (touchable) {
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        float l = event.getRawY() - v.getHeight();
                        if (l >= completeZero && l <= 0) {
                            setY(l);
                        } else if (l < completeZero) {
                            setY(completeZero);
                        } else if (l > 0) {
                            setY(0);
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        //                                Log.i("Y", String.valueOf(getY()));
                        toggle(true);
                    }
                }
                return true;
            }
        });
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void toggle(final boolean runAction) {
        if (getY() >= (-getHeight() / 2) + smallNavigation / 2) {
            open(runAction);
        } else {
            close(runAction);
        }
    }

    public void open(final boolean runAction) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(DragNavigation.this, View.TRANSLATION_Y, getY(), 0);
        oa.setDuration(300);
        oa.setInterpolator(new LinearInterpolator());
        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                touchable = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                touchable = true;
                if (!isOpen && runAction) {
                    if (onstate != null) onstate.onOpen();
                }
                isOpen = true;
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
        ObjectAnimator oa = ObjectAnimator.ofFloat(DragNavigation.this, View.TRANSLATION_Y, getY(), completeZero);
        oa.setDuration(300);
        oa.setInterpolator(new LinearInterpolator());
        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                touchable = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                touchable = true;
                if (isOpen && runAction) {
                    if (onstate != null) onstate.onClose();
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

    public void setIcon(Drawable icon) {
        this.icon = icon;
        iconHolder.setImageDrawable(this.icon);
    }

    public void setColor(int color) {
        this.backgroundColor = color;
    }

    public void setContent(View v) {
        upContent.removeAllViews();
        upContent.addView(v);
    }

    public void setOnIconClick(View.OnClickListener ocl) {
        iconHolder.setOnClickListener(ocl);
    }

    public void setOnStateChangedListener(OnStateChangedListener osc) {
        onstate = osc;
    }

    public void emptyContent() {
        upContent.removeAllViews();
    }

    public int spacerSize() {
        return smallNavigation;
    }

    public int calculateOverlayedColor(int parentViewColor) {
        int calculatedA = convertColor(parentViewColor) + convertColor(backgroundColor);
        return colorRange(parentViewColor, backgroundColor);
    }

    private int convertColor(int color) {
        return Color.argb(80, Color.red(color), Color.green(color), Color.blue(color));
    }

    public int colorFix(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        if (red % 2 != 0) {
            red -= 1;
        }
        if (green % 2 != 0) {
            green -= 1;
        }
        if (green % 2 != 0) {
            green -= 1;
        }
        return Color.argb(Color.alpha(color), red, green, blue);
    }

    public int colorRange(int colorA, int colorB) {
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

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public interface OnStateChangedListener {
        void onOpen();

        void onClose();
    }
}