package nadav.tasher.lightool.graphics.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.widget.SeekBar;

public class ColorPicker extends SeekBar {
    private int defaultColor = 0xFFFFFFFF, currentColor = defaultColor;
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
                currentColor = Color.rgb(r, g, b);
                drawThumb();
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

    private void drawThumb(){
        int size=0;
        if(getLayoutParams()!=null) {
            size = getLayoutParams().height - getPaddingTop() - getPaddingBottom();
        }
        LayerDrawable layerDrawable=new LayerDrawable(new Drawable[]{Utils.getCoaster(Color.WHITE,16,10,size/2,size),Utils.getCoaster(currentColor,16,10,(int)(size/2.25),(int)(size/1.25))});
        setThumb(layerDrawable);
    }

    public interface OnColorChanged {
        void onColorChange(int color);
    }
}

