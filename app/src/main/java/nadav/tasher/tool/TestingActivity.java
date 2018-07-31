/*
 * This file contains test code for the library.
 */

package nadav.tasher.tool;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import nadav.tasher.lightool.graphics.views.ColorPicker;
import nadav.tasher.lightool.graphics.views.ExpandingView;
import nadav.tasher.lightool.graphics.views.Utils;
import nadav.tasher.lightool.graphics.views.appview.AppView;
import nadav.tasher.lightool.graphics.views.appview.navigation.corner.Corner;
import nadav.tasher.lightool.graphics.views.appview.navigation.corner.CornerView;
import nadav.tasher.lightool.info.Device;

public class TestingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AppView view = new AppView(this);
        view.getDrawer().getDrawerView().setBackground(Utils.getCoaster(Color.BLACK,64,20));
        Corner c = new Corner(getApplicationContext(), Device.screenX(getApplicationContext()) / 5, 0xFF123987);
        c.setDrawable(getDrawable(R.drawable.ic_add), 0.8);
        c.addOnState(new Corner.OnState() {
                         @Override
                         public void onOpen() {
                         }

                         @Override
                         public void onClose() {
                         }

                         @Override
                         public void onBoth(boolean isOpened) {
                             if(view.getDrawer().isOpen()){
                                 view.getDrawer().close();
                             }else{
                                 view.getDrawer().open(0.5);
                             }
                         }
                     }
        );
        view.getCornerView().setTopRight(c);
        Corner c2 = new Corner(getApplicationContext(), Device.screenX(getApplicationContext()) / 5, 0xFF123987);
        c2.setDrawable(getDrawable(R.drawable.ic_add), 0.8);
        c2.addOnState(new Corner.OnState() {
                         @Override
                         public void onOpen() {
                         }

                         @Override
                         public void onClose() {
                         }

                         @Override
                         public void onBoth(boolean isOpened) {
                             view.getDrawer().open(0.8);
                         }
                     }
        );
        view.getCornerView().setBottomLeft(c2);
        view.setBackgroundColor(Color.GRAY);
        TextView myTextA = new TextView(getApplicationContext());
        myTextA.setText("This Is A Text.");
        TextView myTextB = new TextView(getApplicationContext());
        myTextA.setGravity(Gravity.CENTER);
        myTextB.setGravity(Gravity.CENTER);
        myTextB.setText("This Is Another Text.");
        //        myTextB.setPadding(0,30,0,30);
        myTextB.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Device.screenY(getApplicationContext()) / 13));
        ExpandingView ev = new ExpandingView(getApplicationContext(), 500, Device.screenY(getApplicationContext()) / 13, myTextA, myTextB);
        ev.setBackground(Utils.getCoaster(Color.WHITE, 32, 10));
        ColorPicker colorPicker = new ColorPicker(getApplicationContext());
        colorPicker.setLayoutParams(new LinearLayout.LayoutParams((int) (Device.screenX(getApplicationContext()) * 0.9), Device.screenY(getApplicationContext()) / 12));
        colorPicker.setColor(Color.rgb(255,255,110));
        colorPicker.setOnColor(new ColorPicker.OnColorChanged() {
            @Override
            public void onColorChange(int color) {
                Log.i("Color", "" + color);
                Log.i("RED",String.valueOf(Color.red(color)));
                Log.i("GREEN",String.valueOf(Color.green(color)));
                Log.i("BLUE",String.valueOf(Color.blue(color)));
            }
        });
        view.getScrolly().setView(colorPicker);
        setContentView(view);
    }
}

