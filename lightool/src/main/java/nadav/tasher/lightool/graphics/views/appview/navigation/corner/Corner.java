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

import nadav.tasher.lightool.parts.Peer;
import nadav.tasher.lightool.parts.Tower;

public class Corner extends FrameLayout {

    public static final int TOP_LEFT=0;
    public static final int TOP_RIGHT=1;
    public static final int BOTTOM_LEFT=2;
    public static final int BOTTOM_RIGHT=3;

    private int
            contentXY,
            maxXY,
            color,
            location=TOP_LEFT;
    private ArrayList<OnState> onstates = new ArrayList<>();
    private Peer<Integer> colorPeer = new Peer<>(), textSizePeer = new Peer<>(), textColorPeer = new Peer<>();
    private Tower<Integer> innerColor = new Tower<>();
    private Tower<Integer> innerSize = new Tower<>();
    private Typeface typeface = null;
    private boolean isOpened = false;
    private int radii=256;
    private int alpha=128;
    private LinearLayout inside;

    public Corner(Context context, int size, int color) {
        super(context);
        this.maxXY = size;
        init();
        setRadii(maxXY);
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
        textColorPeer.setOnPeer(new Peer.OnPeer<Integer>() {
            @Override
            public boolean onPeer(Integer data) {
                innerColor.tell(data);
                return false;
            }
        });
        textSizePeer.setOnPeer(new Peer.OnPeer<Integer>() {
            @Override
            public boolean onPeer(Integer data) {
                innerSize.tell(data);
                return false;
            }
        });
        addView(inside);
    }

    public void setRadii(int rad){
        radii=rad;
        corner();
    }

    public void setColorAlpha(int a){
        alpha=a;
        corner();
    }

    private void corner() {

        float[] rad = new float[]{
                radii*value(location,BOTTOM_RIGHT),
                radii*value(location,BOTTOM_RIGHT),
                radii*value(location,BOTTOM_LEFT),
                radii*value(location,BOTTOM_LEFT),
                radii*value(location,TOP_LEFT),
                radii*value(location,TOP_LEFT),
                radii*value(location,TOP_RIGHT),
                radii*value(location,TOP_RIGHT)
        };
        ShapeDrawable oval = new ShapeDrawable(new RoundRectShape(rad, null, null));
        oval.setIntrinsicHeight(maxXY);
        oval.setIntrinsicWidth(maxXY);
        oval.getPaint().setColor(color);
        setBackground(oval);
    }

    private int value(int corner,int wanted){
        if(corner==wanted)return 1;
        return 0;
//        if(corner>1)
//        return 1;
//        return 0;
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
        inside.setLayoutParams(new LayoutParams(maxXY, maxXY));
        setLayoutParams(new LayoutParams(maxXY, maxXY));
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        this.color= Color.argb(alpha,Color.red(color),Color.green(color),Color.blue(color));
        corner();
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

    public void setDrawable(Drawable d, double sizePrecent) {
        sizePrecent = Math.abs(sizePrecent);
        inside.removeAllViews();
        ImageView iv = new ImageView(getContext());
        iv.setLayoutParams(new LinearLayout.LayoutParams((int) (contentXY * sizePrecent), (int) (contentXY * sizePrecent)));
        iv.setImageDrawable(d);
        inside.addView(iv);
    }

    public void setText(double sizePrecent,TextPiece... texts) {
        inside.removeAllViews();
        for (TextPiece tv : texts) {
            inside.addView(generateView(tv, (int)(contentXY * sizePrecent), texts.length));
        }
    }


    public void setState(boolean state) {
        isOpened = state;
    }

    public void setLocation(int location){
        this.location=location;
        int g;
        if(location==TOP_RIGHT||location==TOP_LEFT){
            g=Gravity.TOP;
        }else{
            g=Gravity.BOTTOM;
        }
        if(location==TOP_LEFT||location==BOTTOM_LEFT){
            g=g|Gravity.START;
        }else{
            g=g|Gravity.END;
        }
        inside.setGravity(g);
        corner();
    }

    private TextView generateView(final TextPiece tp, int maxSize, int tvs) {
        final TextView tv = new TextView(getContext());
        tv.setTextColor(tp.textColor);
        tv.setText(tp.text);
        tv.setGravity(Gravity.CENTER);
        if (typeface != null)
            tv.setTypeface(typeface);
        tv.setLayoutParams(new LinearLayout.LayoutParams(maxSize, maxSize / tvs));
        innerColor.addPeer(new Peer<>(new Peer.OnPeer<Integer>() {
            @Override
            public boolean onPeer(Integer data) {
                tv.setTextColor(data);
                return false;
            }
        }));
        innerSize.addPeer(new Peer<>(new Peer.OnPeer<Integer>() {
            @Override
            public boolean onPeer(Integer data) {
                tv.setTextSize((int) (tp.fontSizeRatio * data));
                return false;
            }
        }));
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

    public static class TextPiece {
        private String text;
        private double fontSizeRatio;
        private int textColor;

        public TextPiece(String text, double size, int color) {
            this.text = text;
            this.fontSizeRatio = size;
            this.textColor = color;
        }
    }
}