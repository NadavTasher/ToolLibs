package nadav.tasher.lightool.graphics.views;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;

public class Utils {
    public static Drawable getCoaster(int color, int radii, int padding){
        GradientDrawable coaster=new GradientDrawable();
        coaster.setColor(color);
        coaster.setCornerRadius(radii);
        return new InsetDrawable(coaster,padding,padding,padding,padding);
    }
}
