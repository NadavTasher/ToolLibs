package nadav.tasher.lightool.graphics.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.SeekBar;

public class ColorPicker extends SeekBar {
    private int currentColor = Color.WHITE;
    private OnColorChanged onColor = null;

    public ColorPicker(Context context) {
        super(context);
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{
                0xFF000000,
                0xFF0000FF,
                0xFF00FF00,
                0xFF00FFFF,
                0xFFFF0000,
                0xFFFF00FF,
                0xFFFFFF00,
                0xFFFFFFFF
        });
        gradient.setCornerRadius(8);
        setBackground(gradient);
        setProgressDrawable(null);
        setMax(256 * 7 - 1);
        setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentColor = getColorFromProgress(progress);
                drawThumb();
                if (onColor != null) onColor.onColorChange(currentColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        drawThumb();
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        drawThumb();
    }

    private void drawThumb() {
        int size = 0;
        if (getLayoutParams() != null) {
            size = getLayoutParams().height - getPaddingTop() - getPaddingBottom();
        }
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                Utils.getSizedCoaster(Color.WHITE, 16, 10, size / 2, size),
                Utils.getSizedCoaster(currentColor, 16, 10, (int) (size / 2.25), (int) (size / 1.25))
        });
        setThumb(layerDrawable);
    }

    public int getColor() {
        return currentColor;
    }

    public void setColor(int color) {
        setProgress(getProgressFromColor(color));
        drawThumb();
    }

    public void setOnColor(OnColorChanged onColor) {
        this.onColor = onColor;
    }

    private int getProgressFromColor(int color) {
        int progress = 0;
        for (int i = 0; i <= getMax(); i++) {
            if (getColorFromProgress(i) == color) progress = i;
        }
        return progress;
    }

    private int getColorFromProgress(int progress) {
        int r = 0;
        int g = 0;
        int b = 0;
        if (progress < 256) {
            b = progress;
        } else if (progress < 256 * 2) {
            g = progress % 256;
            b = 256 - progress % 256;
        } else if (progress < 256 * 3) {
            g = 255;
            b = progress % 256;
        } else if (progress < 256 * 4) {
            r = progress % 256;
            g = 256 - progress % 256;
            b = 256 - progress % 256;
        } else if (progress < 256 * 5) {
            r = 255;
            g = 0;
            b = progress % 256;
        } else if (progress < 256 * 6) {
            r = 255;
            g = progress % 256;
            b = 256 - progress % 256;
        } else if (progress < 256 * 7) {
            r = 255;
            g = 255;
            b = progress % 256;
        }
        return Color.rgb(r, g, b);
    }

    public interface OnColorChanged {
        void onColorChange(int color);
    }
}

