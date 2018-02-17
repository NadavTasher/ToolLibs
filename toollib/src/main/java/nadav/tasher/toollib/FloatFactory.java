package nadav.tasher.toollib;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class FloatFactory {
	public static void update(Context c, View v, int sizeX, int sizeY, int positionX, int positionY) {
		WindowManager wm=(WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		final WindowManager.LayoutParams parameters=new WindowManager.LayoutParams(sizeX, sizeY, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
		parameters.x=positionX;
		parameters.y=positionY;
		wm.updateViewLayout(v, parameters);
	}
	public static void start(Context c, final View v, int sizeX, int sizeY, int positionX, int positionY, final OnMove om) {
		WindowManager wm=(WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams layoutParameteres=new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
		v.setLayoutParams(layoutParameteres);
		final WindowManager.LayoutParams parameters=new WindowManager.LayoutParams(sizeX, sizeY, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
		parameters.gravity=Gravity.CENTER;
		parameters.x=positionX;
		parameters.y=positionY;
		wm.addView(v, parameters);
	}
	public static void remove(Context c, View v) {
		WindowManager wm=(WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		wm.removeView(v);
	}
	public interface OnMove {
		void run(double x, double y, View v);
	}
}
