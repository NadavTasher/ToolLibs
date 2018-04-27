/*
 * This file contains test code for the library.
 */

package nadav.tasher.tool;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import nadav.tasher.lightool.communication.SessionStatus;
import nadav.tasher.lightool.graphics.views.appview.navigation.bar.Squircle;
import nadav.tasher.lightool.info.Device;
import nadav.tasher.lightool.parts.Tower;
import nadav.tasher.lightool.communication.bluetooth.BluetoothSession;
import nadav.tasher.lightool.graphics.views.appview.AppView;
import nadav.tasher.lightool.graphics.views.appview.navigation.Drag;

public class TestingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Squircle s=new Squircle(getApplicationContext(), Device.screenX(getApplicationContext())/5,0x123456);
        Squircle s2=new Squircle(getApplicationContext(), Device.screenX(getApplicationContext())/3,0x128956);
        final AppView myApp=new AppView(getApplicationContext(),null,0x333333,s);
        myApp.setBackgroundColor(Color.BLUE);
        myApp.overlaySelf(getWindow());
        myApp.getBar().addSquircle(s2);
        setContentView(myApp);
    }
}

