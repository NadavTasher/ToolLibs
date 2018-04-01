package nadav.tasher.lightool.graphics.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class ColorPicker extends LinearLayout {
    private int defaultColor = 0xFFFFFFFF, currentColor = defaultColor;
    private SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    private OnColorChanged onColor = null;

    public ColorPicker(Context context) {
        super(context);
        addViews();
    }

    public ColorPicker(Context context, int defaultColor) {
        super(context);
        this.defaultColor = defaultColor;
        currentColor = defaultColor;
        addViews();
    }

    private void addViews() {
        SeekBar.OnSeekBarChangeListener onChange = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentColor = Color.rgb(redSeekBar.getProgress(), greenSeekBar.getProgress(), blueSeekBar.getProgress());
                drawThumbs(currentColor);
                setCoasterColor(currentColor);
                if (onColor != null) onColor.onColorChange(currentColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setLayoutDirection(LAYOUT_DIRECTION_LTR);
        setPadding(15, 15, 15, 15);
        GradientDrawable redDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xFF000000, 0xFFFF0000});
        GradientDrawable greenDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xFF000000, 0xFF00FF00});
        GradientDrawable blueDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xFF000000, 0xFF0000FF});
        redDrawable.setCornerRadius(8);
        greenDrawable.setCornerRadius(8);
        blueDrawable.setCornerRadius(8);
        redSeekBar = new SeekBar(getContext());
        greenSeekBar = new SeekBar(getContext());
        blueSeekBar = new SeekBar(getContext());
        redSeekBar.setPadding(10, 10, 10, 10);
        greenSeekBar.setPadding(10, 10, 10, 10);
        blueSeekBar.setPadding(10, 10, 10, 10);
        redSeekBar.setProgressDrawable(redDrawable);
        greenSeekBar.setProgressDrawable(greenDrawable);
        blueSeekBar.setProgressDrawable(blueDrawable);
        redSeekBar.setMax(255);
        greenSeekBar.setMax(255);
        blueSeekBar.setMax(255);
        addView(redSeekBar);
        addView(greenSeekBar);
        addView(blueSeekBar);
        redSeekBar.setProgress(Color.red(defaultColor));
        greenSeekBar.setProgress(Color.green(defaultColor));
        blueSeekBar.setProgress(Color.blue(defaultColor));
        redSeekBar.setOnSeekBarChangeListener(onChange);
        greenSeekBar.setOnSeekBarChangeListener(onChange);
        blueSeekBar.setOnSeekBarChangeListener(onChange);
        drawThumbs(defaultColor);
        setCoasterColor(defaultColor);
    }

    public void setOnColorChanged(OnColorChanged onc) {
        onColor = onc;
    }

    private void drawThumbs(int color) {
        int redAmount = Color.red(color);
        int greenAmount = Color.green(color);
        int blueAmount = Color.blue(color);
        int xy = ((redSeekBar.getLayoutParams().height - redSeekBar.getPaddingTop() - redSeekBar.getPaddingBottom()) + (greenSeekBar.getLayoutParams().height - greenSeekBar.getPaddingTop() - greenSeekBar.getPaddingBottom()) + (blueSeekBar.getLayoutParams().height - blueSeekBar.getPaddingTop() - blueSeekBar.getPaddingBottom())) / 3;
        redSeekBar.setThumb(getRoundedRect(Color.rgb(redAmount, 0, 0), xy));
        greenSeekBar.setThumb(getRoundedRect(Color.rgb(0, greenAmount, 0), xy));
        blueSeekBar.setThumb(getRoundedRect(Color.rgb(0, 0, blueAmount), xy));
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams l) {
        LinearLayout.LayoutParams l2;
        if (l instanceof LinearLayout.LayoutParams) {
            l2 = ((LinearLayout.LayoutParams) l);
            l2.setMargins(0, 10, 0, 10);
            super.setLayoutParams(l2);
        } else {
            super.setLayoutParams(l);
        }
        redSeekBar.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, l.height / 4));
        greenSeekBar.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, l.height / 4));
        blueSeekBar.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, l.height / 4));
        drawThumbs(currentColor);
    }

    private void setCoasterColor(int color) {
        float corner = 16;
        float[] corners = new float[]{corner, corner, corner, corner, corner, corner, corner, corner};
        RoundRectShape shape = new RoundRectShape(corners, new RectF(), corners);
        ShapeDrawable coaster = new ShapeDrawable(shape);
        coaster.getPaint().setColor(color);
        setBackground(coaster);
    }

    private LayerDrawable getRoundedRect(int color, int size) {
        float corner = 16;
        float[] corners = new float[]{corner, corner, corner, corner, corner, corner, corner, corner};
        RoundRectShape shape = new RoundRectShape(corners, new RectF(), corners);
        RoundRectShape shape2 = new RoundRectShape(corners, new RectF(8, 8, 8, 8), corners);
        ShapeDrawable rectBack = new ShapeDrawable(shape2);
        rectBack.setIntrinsicHeight(size);
        rectBack.setIntrinsicWidth(size);
        rectBack.getPaint().setColor(Color.WHITE);
        ShapeDrawable rect = new ShapeDrawable(shape);
        rect.setIntrinsicHeight((size));
        rect.setIntrinsicWidth((size));
        rect.getPaint().setColor(color);
        LayerDrawable ld = new LayerDrawable(new Drawable[]{rect, rectBack});
        return ld;
    }

    public interface OnColorChanged {
        void onColorChange(int color);
    }
}

