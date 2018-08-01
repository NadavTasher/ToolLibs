package nadav.tasher.lightool.graphics.views.appview.navigation.corner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Corner extends LinearLayout {

    public static final int TOP_LEFT = 0;
    public static final int TOP_RIGHT = 1;
    public static final int BOTTOM_LEFT = 2;
    public static final int BOTTOM_RIGHT = 3;

    private int
            size = 256,
            color=Color.WHITE,
            radii = size,
            alpha = 128,
            location = TOP_LEFT;

    private ArrayList<OnState> onstates = new ArrayList<>();
    private boolean isOpened = false;

    public Corner(Context context, int size, int color) {
        super(context);
        this.size = size;
        this.color=color;
        init();
    }

    private void init() {
        super.removeAllViews();
        setOnClickListener(new OnClickListener() {
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
        });
        initSize();
        setColor(color);
    }

    private void initSize() {
        setLayoutParams(new LayoutParams(size, size));
        setRadii(size);
    }

    private void setRadii(int rad) {
        radii = rad;
        corner();
    }

    public void setView(View v, double percent) {
        super.removeAllViews();
        if (v != null) {
            v.setLayoutParams(new LinearLayout.LayoutParams((int) (size * percent), (int) (size * percent)));
            super.addView(v);
        }
    }

    @Override
    @Deprecated
    public void addView(View v) {
    }

    @Override
    @Deprecated
    public void removeAllViews() {
    }

    public void setColorAlpha(int a) {
        alpha = a;
        corner();
    }

    private void corner() {
        float[] rad = new float[]{
                radii * value(location, BOTTOM_RIGHT),
                radii * value(location, BOTTOM_RIGHT),
                radii * value(location, BOTTOM_LEFT),
                radii * value(location, BOTTOM_LEFT),
                radii * value(location, TOP_LEFT),
                radii * value(location, TOP_LEFT),
                radii * value(location, TOP_RIGHT),
                radii * value(location, TOP_RIGHT)
        };
        ShapeDrawable oval = new ShapeDrawable(new RoundRectShape(rad, null, null));
        oval.setIntrinsicHeight(size);
        oval.setIntrinsicWidth(size);
        oval.getPaint().setColor(color);
        setBackground(oval);
    }

    private int value(int corner, int wanted) {
        if (corner == wanted) return 1;
        return 0;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        initSize();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        this.color = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
        corner();
    }

    public void setState(boolean state) {
        isOpened = state;
    }

    public void setLocation(int location) {
        this.location = location;
        int g;
        if (location == TOP_RIGHT || location == TOP_LEFT) {
            g = Gravity.TOP;
        } else {
            g = Gravity.BOTTOM;
        }
        if (location == TOP_LEFT || location == BOTTOM_LEFT) {
            g = g | Gravity.START;
        } else {
            g = g | Gravity.END;
        }
        setGravity(g);
        corner();
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
}