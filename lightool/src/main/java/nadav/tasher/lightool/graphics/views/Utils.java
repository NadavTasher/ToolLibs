package nadav.tasher.lightool.graphics.views;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

public class Utils {
    public static Drawable getCoaster(int color, int radii, int padding){
        GradientDrawable coaster=new GradientDrawable();
        coaster.setColor(color);
        coaster.setCornerRadius(radii);
        return new InsetDrawable(coaster,padding,padding,padding,padding);
    }

    public static Drawable getCoaster(int color, int radii, int paddingX,int paddingY){
        GradientDrawable coaster=new GradientDrawable();
        coaster.setColor(color);
        coaster.setCornerRadius(radii);
        return new InsetDrawable(coaster,paddingX,paddingY,paddingX,paddingY);
    }

    public static Drawable getCoaster(int color, int radii, int left,int right,int top,int bottom){
        GradientDrawable coaster=new GradientDrawable();
        coaster.setColor(color);
        coaster.setCornerRadius(radii);
        return new InsetDrawable(coaster,left,top,right,bottom);
    }

    public static Drawable getSizedCoaster(int color,int radii, int padding,int size){
        float[] corners = new float[]{radii, radii, radii, radii, radii, radii, radii, radii};
        RoundRectShape coasterShape = new RoundRectShape(corners, new RectF(), corners);
        ShapeDrawable coaster = new ShapeDrawable(coasterShape);
        coaster.setIntrinsicHeight(size);
        coaster.setIntrinsicWidth(size);
        coaster.getPaint().setColor(color);
        return new InsetDrawable(coaster,padding,padding,padding,padding);
    }

    public static Drawable getSizedCoaster(int color,int radii, int padding,int sizeX,int sizeY){
        float[] corners = new float[]{radii, radii, radii, radii, radii, radii, radii, radii};
        RoundRectShape coasterShape = new RoundRectShape(corners, new RectF(), corners);
        ShapeDrawable coaster = new ShapeDrawable(coasterShape);
        coaster.setIntrinsicHeight(sizeY);
        coaster.setIntrinsicWidth(sizeX);
        coaster.getPaint().setColor(color);
        return new InsetDrawable(coaster,padding,padding,padding,padding);
    }
}
