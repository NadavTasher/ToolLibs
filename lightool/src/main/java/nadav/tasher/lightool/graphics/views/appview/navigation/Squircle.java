package nadav.tasher.lightool.graphics.views.appview.navigation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
        removeAllViews();
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

    public void setTypeface(Typeface t) {
        typeface = t;
    }

    public int getMaxXY() {
        return maxXY;
    }

    public void setMaxXY(int maxXY) {
        this.maxXY = maxXY;
        contentXY = (int) (maxXY * 0.9);
        imageXY = (int) (maxXY * 0.6);
        inside.setLayoutParams(new LayoutParams(maxXY, maxXY));
        setLayoutParams(new LayoutParams(maxXY, maxXY));
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

    public void setText(TextView... texts) {
        inside.removeAllViews();
        for (TextView tv : texts) {
            inside.addView(resetView(tv, maxXY, texts.length));
        }
    }

    public void setState(boolean state) {
        isOpened = state;
    }

    private TextView resetView(TextView tv, int maxSize, int tvs) {
        tv.setGravity(Gravity.CENTER);
        if (typeface != null)
            tv.setTypeface(typeface);
        tv.setLayoutParams(new LinearLayout.LayoutParams(maxSize, maxSize / tvs));
        return tv;
    }

    public static TextView getTextView(Context c,String t, int size,final int textColor) {
        final TextView v = new TextView(c);
        v.setTextColor(textColor);
        v.setTextSize(size);
        v.setText(t);
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

    public static class SquircleView extends FrameLayout {

        private LinearLayout topLeft, topRight, bottomLeft, bottomRight;

        public SquircleView(Context context) {
            super(context);
            init();
        }

        private void init() {
            // Init Views
            topLeft = new LinearLayout(getContext());
            topRight = new LinearLayout(getContext());
            bottomLeft = new LinearLayout(getContext());
            bottomRight = new LinearLayout(getContext());
            // Set Orientation
            topLeft.setOrientation(LinearLayout.VERTICAL);
            topRight.setOrientation(LinearLayout.VERTICAL);
            bottomLeft.setOrientation(LinearLayout.VERTICAL);
            bottomRight.setOrientation(LinearLayout.VERTICAL);
            // Set Gravity
            topLeft.setGravity(Gravity.TOP | Gravity.START);
            topRight.setGravity(Gravity.TOP | Gravity.END);
            bottomLeft.setGravity(Gravity.BOTTOM | Gravity.START);
            bottomRight.setGravity(Gravity.BOTTOM | Gravity.END);
            // Set Size
            LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            topLeft.setLayoutParams(parameters);
            topRight.setLayoutParams(parameters);
            bottomLeft.setLayoutParams(parameters);
            bottomRight.setLayoutParams(parameters);
            // Set Padding
            setPadding(10, 10, 10, 10);
            // Add To Layout
            addView(topLeft);
            addView(topRight);
            addView(bottomLeft);
            addView(bottomRight);
        }

        public void setTopLeft(Squircle s) {
            if (s == null) {
                topLeft.removeAllViews();
            } else {
                topLeft.removeAllViews();
                topLeft.addView(s);
            }
        }

        public void setTopRight(Squircle s) {
            if (s == null) {
                topRight.removeAllViews();
            } else {
                topRight.removeAllViews();
                topRight.addView(s);
            }
        }

        public void setBottomLeft(Squircle s) {
            if (s == null) {
                bottomLeft.removeAllViews();
            } else {
                bottomLeft.removeAllViews();
                bottomLeft.addView(s);
            }
        }

        public void setBottomRight(Squircle s) {
            if (s == null) {
                bottomRight.removeAllViews();
            } else {
                bottomRight.removeAllViews();
                bottomRight.addView(s);
            }
        }
    }
}
