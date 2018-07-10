/*
 * This file contains test code for the library.
 */

package nadav.tasher.tool;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import nadav.tasher.lightool.graphics.views.appview.navigation.Squircle;
import nadav.tasher.lightool.info.Device;
import nadav.tasher.lightool.graphics.views.appview.AppView;

public class TestingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Squircle s=new Squircle(getApplicationContext(), Device.screenX(getApplicationContext())/5,0x123456);
        s.setText(Squircle.getTextView(getApplicationContext(),"App",32,0xFF123321));
        Squircle s2=new Squircle(getApplicationContext(), Device.screenX(getApplicationContext())/5,0x128956);
        s2.setDrawable(getDrawable(R.drawable.ic_delete));
        final AppView myApp=new AppView(getApplicationContext(),0x80654321);
        myApp.setBackgroundColor(new AppView.Gradient(0xFF123456,Color.WHITE));
        myApp.setWindow(getWindow());
        myApp.getSquircleView().setTopLeft(s);
        myApp.getSquircleView().setBottomRight(s2);
        s2.addOnState(new Squircle.OnState() {
            @Override
            public void onOpen() {
                myApp.getDrawer().open(true,0.6);
            }

            @Override
            public void onClose() {
                myApp.getDrawer().close(true);
            }

            @Override
            public void onBoth(boolean isOpened) {
            }
        });
        setContentView(myApp);
    }
}

