package nadav.tasher.lightool.graphics.views.appview.navigation.bar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import nadav.tasher.lightool.parts.Peer;

public class Squircle extends FrameLayout {
    private int imageXY,
            contentXY,
            maxXY,
            color;
    private ArrayList<OnState> onstates = new ArrayList<>();
    private Peer<Integer> colorPeer = new Peer<>(), textSizePeer = new Peer<>(), textColorPeer = new Peer<>();
    private Typeface typeface = null;
    private boolean isOpened = false;
    private LinearLayout inside;

    public Squircle(Context context, int size, int color) {
        super(context);
        this.maxXY = size;
        init();
        setColor(color);
    }

    private void init() {
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpened = !isOpened;
                if (isOpened) {
                    for (int a = 0; a < onstates.size(); a++) {
                        if (onstates.get(a) != null) onstates.get(a).onOpen();
                    }
                } else {
                    for (int a = 0; a < onstates.size(); a++) {
                        if (onstates.get(a) != null) onstates.get(a).onClose();
                    }
                }
                for (int a = 0; a < onstates.size(); a++) {
                    if (onstates.get(a) != null) onstates.get(a).onBoth(isOpened);
                }
            }
        };
        contentXY = (int) (maxXY * 0.9);
        imageXY = (int) (maxXY * 0.6);
        inside = new LinearLayout(getContext());
        inside.setOrientation(LinearLayout.VERTICAL);
        inside.setGravity(Gravity.CENTER);
        inside.setLayoutParams(new LayoutParams(maxXY, maxXY));
        setLayoutParams(new LayoutParams(maxXY, maxXY));
        setOnClickListener(onClickListener);
        colorPeer.setOnPeer(new Peer.OnPeer<Integer>() {
            @Override
            public boolean onPeer(Integer data) {
                setColor(data);
                return true;
            }
        });
        addView(inside);
    }

    private void squircle() {
        int radii = 32;
        float[] rad = new float[]{
                radii,
                radii,
                radii,
                radii,
                radii,
                radii,
                radii,
                radii
        };
        ShapeDrawable oval = new ShapeDrawable(new RoundRectShape(rad, null, null));
        oval.setIntrinsicHeight(maxXY);
        oval.setIntrinsicWidth(maxXY);
        oval.getPaint().setColor(color);
        setBackground(oval);
    }

    public int getContentXY() {
        return contentXY;
    }

    public int getMaxXY() {
        return maxXY;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        this.color = Color.argb(128, Color.red(color), Color.green(color), Color.blue(color));
        squircle();
    }

    public Peer<Integer> getColorPeer() {
        return colorPeer;
    }

    public Peer<Integer> getTextColorPeer() {
        return textColorPeer;
    }

    public Peer<Integer> getTextSizePeer() {
        return textSizePeer;
    }

    public void setDrawable(Drawable d) {
        inside.removeAllViews();
        ImageView iv = new ImageView(getContext());
        iv.setLayoutParams(new LinearLayout.LayoutParams(imageXY, imageXY));
        iv.setImageDrawable(d);
        inside.addView(iv);
    }

    public void setTextColor(int i) {
        textColorPeer.tell(i);
    }

    public void setTextSize(int i) {
        textSizePeer.tell(i);
    }

    public void setText(int color, int size, String upper, String lower) {
        if (upper.length() > 4) {
            upper = upper.substring(0, 4);
        }
        inside.removeAllViews();
        inside.addView(getTextView(upper, size + 4, maxXY, color));
        inside.addView(getTextView(lower, size, contentXY, color));
    }

    public void setState(boolean state) {
        isOpened = state;
    }

    private TextView getTextView(String t, int s, int par, final int textColor) {
        final TextView v = new TextView(getContext());
        v.setTextColor(textColor);
        v.setTextSize(s);
        v.setText(t);
        v.setGravity(Gravity.CENTER);
        if (typeface != null)
            v.setTypeface(typeface);
        v.setLayoutParams(new LinearLayout.LayoutParams(par, par / 2));
        textColorPeer.setOnPeer(new Peer.OnPeer<Integer>() {
            @Override
            public boolean onPeer(Integer data) {
                v.setTextColor(data);
                return true;
            }
        });
        textSizePeer.setOnPeer(new Peer.OnPeer<Integer>() {
            @Override
            public boolean onPeer(Integer data) {
                v.setTextSize(data);
                return true;
            }
        });
        return v;
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

    public interface OnState {
        void onOpen();

        void onClose();

        void onBoth(boolean isOpened);
    }

    public static class Holder extends LinearLayout {

        private final double pad = 0.05;
        private Squircle squircle;

        public Holder(Context context, Squircle squircle) {
            super(context);
            this.squircle = squircle;
            init();
        }

        private void init() {
            int padding = (int) (squircle.getMaxXY() * pad);
            setOrientation(VERTICAL);
            setGravity(Gravity.CENTER);
            setLayoutParams(new LayoutParams(squircle.getMaxXY() + 2 * padding, squircle.getMaxXY() + 2 * padding));
            setPadding(padding, padding, padding, padding);
            addView(squircle);
        }

        public int getSidePad() {
            return (int) (pad * squircle.getMaxXY());
        }

        public void disableRight() {
            setPadding(getPaddingLeft(), getPaddingTop(), 0, getPaddingBottom());
        }

        public void disableLeft() {
            setPadding(0, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
    }
}
