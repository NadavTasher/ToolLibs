package nadav.tasher.toollib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class DrawableFactory {
	public static final int FLAG_FLIP_VERTICAL=1;
	public static final int FLAG_FLIP_HORIZONTAL=2;
	public static Drawable resize(Context ctx, Drawable dr, int size) {
		return new BitmapDrawable(ctx.getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) dr).getBitmap(), size, size, true));
	}
	public static Drawable resize(Context ctx, Drawable dr, int sizex, int sizey) {
		return new BitmapDrawable(ctx.getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) dr).getBitmap(), sizex, sizey, true));
	}
	public static Drawable flip(Context c, Drawable d, int flag) {
		Matrix matrix=new Matrix();
		switch(flag){
			case FLAG_FLIP_VERTICAL:
				matrix.preScale(1.0f, -1.0f);
				break;
			case FLAG_FLIP_HORIZONTAL:
				matrix.preScale(-1.0f, 1.0f);
				break;
			default:
				matrix.preScale(-1.0f, 1.0f);
				break;
		}
		return new BitmapDrawable(c.getResources(), Bitmap.createBitmap(((BitmapDrawable) d).getBitmap(), 0, 0, ((BitmapDrawable) d).getBitmap().getWidth(), ((BitmapDrawable) d).getBitmap().getHeight(), matrix, true));
	}
}
