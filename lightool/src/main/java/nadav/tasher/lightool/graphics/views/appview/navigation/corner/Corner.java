package nadav.tasher.lightool.graphics.views.appview.navigation.corner;

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

public class Corner extends FrameLayout {

    public static final int TOP_LEFT = 0;
    public static final int TOP_RIGHT = 1;
    public static final int BOTTOM_LEFT = 2;
    public static final int BOTTOM_RIGHT = 3;

    private int
            size,
            color,
            location = TOP_LEFT;

    private ArrayList<TextView> views=new ArrayList<>();

    private ArrayList<OnState> onstates = new ArrayList<>();
    private boolean isOpened = false;
    private int radii = 256;
    private int alpha = 128;
    private LinearLayout inside;

    public Corner(Context context, int size, int color) {
        super(context);
        this.size = size;
        init();
        setRadii(size);
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
        inside = new LinearLayout(getContext());
        inside.setOrientation(LinearLayout.VERTICAL);
        inside.setGravity(Gravity.CENTER);

        setOnClickListener(onClickListener);
        addView(inside);
    }

    private void initSize(){
        inside.setLayoutParams(new LayoutParams(size, size));
        setLayoutParams(new LayoutParams(size, size));
    }

    public void setRadii(int rad) {
        radii = rad;
        corner();
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

public int getSize(){
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

    public void setDrawable(Drawable d, double percent) {
        int maxSize =(int)(((double) size)*percent);
        inside.removeAllViews();
        ImageView iv = new ImageView(getContext());
        iv.setLayoutParams(new LinearLayout.LayoutParams(maxSize, maxSize));
        iv.setImageDrawable(d);
        inside.addView(iv);
    }

    public void addText(String text, int textColor, int textSize, Typeface typeface){
        views.add(generateView(text,textColor,textSize,typeface));
    }

    public void removeAllTexts(){
        views.clear();
        inside.removeAllViews();
    }

    public void renderText(double percent) {
        int maxSize =(int)(((double) size)*percent);
        inside.removeAllViews();
        for(TextView tv:views){
            tv.setLayoutParams(new LinearLayout.LayoutParams(maxSize, maxSize / views.size()));
            inside.addView(tv);
        }
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
        inside.setGravity(g);
        corner();
    }

    private TextView generateView(String text, int textColor, int textSize, Typeface typeface) {
        final TextView tv = new TextView(getContext());
        tv.setTextColor(textColor);
        tv.setText(text);
        tv.setTextSize(textSize);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(typeface);
        return tv;
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