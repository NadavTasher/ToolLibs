package nadav.tasher.lightool.graphics.views;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Utils {
    public static Drawable getCoaster(int color, int radii, int padding) {
        GradientDrawable coaster = new GradientDrawable();
        coaster.setColor(color);
        coaster.setCornerRadius(radii);
        return new InsetDrawable(coaster, padding, padding, padding, padding);
    }

    public static Drawable getCoaster(int color, int radii, int paddingX, int paddingY) {
        GradientDrawable coaster = new GradientDrawable();
        coaster.setColor(color);
        coaster.setCornerRadius(radii);
        return new InsetDrawable(coaster, paddingX, paddingY, paddingX, paddingY);
    }

    public static Drawable getCoaster(int color, int radii, int left, int right, int top, int bottom) {
        GradientDrawable coaster = new GradientDrawable();
        coaster.setColor(color);
        coaster.setCornerRadius(radii);
        return new InsetDrawable(coaster, left, top, right, bottom);
    }

    public static Drawable getSizedCoaster(int color, int radii, int padding, int size) {
        float[] corners = new float[]{
                radii,
                radii,
                radii,
                radii,
                radii,
                radii,
                radii,
                radii
        };
        RoundRectShape coasterShape = new RoundRectShape(corners, new RectF(), corners);
        ShapeDrawable coaster = new ShapeDrawable(coasterShape);
        coaster.setIntrinsicHeight(size);
        coaster.setIntrinsicWidth(size);
        coaster.getPaint().setColor(color);
        return new InsetDrawable(coaster, padding, padding, padding, padding);
    }

    public static Drawable getSizedCoaster(int color, int radii, int padding, int sizeX, int sizeY) {
        float[] corners = new float[]{
                radii,
                radii,
                radii,
                radii,
                radii,
                radii,
                radii,
                radii
        };
        RoundRectShape coasterShape = new RoundRectShape(corners, new RectF(), corners);
        ShapeDrawable coaster = new ShapeDrawable(coasterShape);
        coaster.setIntrinsicHeight(sizeY);
        coaster.setIntrinsicWidth(sizeX);
        coaster.getPaint().setColor(color);
        return new InsetDrawable(coaster, padding, padding, padding, padding);
    }

    public static void measure(final View view) {
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        final int horizontalMode;
        final int horizontalSize;
        switch (layoutParams.width) {
            case ViewGroup.LayoutParams.MATCH_PARENT:
                horizontalMode = View.MeasureSpec.EXACTLY;
                if (view.getParent() instanceof LinearLayout
                        && ((LinearLayout) view.getParent()).getOrientation() == LinearLayout.VERTICAL) {
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                    horizontalSize = ((View) view.getParent()).getMeasuredWidth() - lp.leftMargin - lp.rightMargin;
                } else {
                    horizontalSize = ((View) view.getParent()).getMeasuredWidth();
                }
                break;
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                horizontalMode = View.MeasureSpec.UNSPECIFIED;
                horizontalSize = 0;
                break;
            default:
                horizontalMode = View.MeasureSpec.EXACTLY;
                horizontalSize = layoutParams.width;
                break;
        }
        final int horizontalMeasureSpec = View.MeasureSpec
                .makeMeasureSpec(horizontalSize, horizontalMode);
        final int verticalMode;
        final int verticalSize;
        switch (layoutParams.height) {
            case ViewGroup.LayoutParams.MATCH_PARENT:
                verticalMode = View.MeasureSpec.EXACTLY;
                if (view.getParent() instanceof LinearLayout
                        && ((LinearLayout) view.getParent()).getOrientation() == LinearLayout.HORIZONTAL) {
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                    verticalSize = ((View) view.getParent()).getMeasuredHeight() - lp.topMargin - lp.bottomMargin;
                } else {
                    verticalSize = ((View) view.getParent()).getMeasuredHeight();
                }
                break;
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                verticalMode = View.MeasureSpec.UNSPECIFIED;
                verticalSize = 0;
                break;
            default:
                verticalMode = View.MeasureSpec.EXACTLY;
                verticalSize = layoutParams.height;
                break;
        }
        final int verticalMeasureSpec = View.MeasureSpec.makeMeasureSpec(verticalSize, verticalMode);
        view.measure(horizontalMeasureSpec, verticalMeasureSpec);
    }
}
