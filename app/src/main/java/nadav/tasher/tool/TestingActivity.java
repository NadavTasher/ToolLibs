/*
 * This file contains test code for the library.
 */

package nadav.tasher.tool;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;

import nadav.tasher.lightool.communication.SessionStatus;
import nadav.tasher.lightool.communication.Tunnel;
import nadav.tasher.lightool.communication.network.request.Get;
import nadav.tasher.lightool.communication.network.request.RequestParameter;
import nadav.tasher.lightool.graphics.views.AppView;
import nadav.tasher.lightool.graphics.views.DragNavigation;

public class TestingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AppView myApp=new AppView(getApplicationContext(),null,0xFFFFFFFF);
        myApp.setBackgroundColor(Color.BLUE);
        myApp.overlaySelf(getWindow());
        myApp.getDragNavigation().setOnStateChangedListener(new DragNavigation.OnStateChangedListener() {
            @Override
            public void onOpen() {
                myApp.getDragNavigation().close(true);
            }

            @Override
            public void onClose() {
                myApp.getDragNavigation().open(false);
            }
        });
        Tunnel<SessionStatus> ss=new Tunnel<>();
        String[] a=new String[]{""};
        new Get("http://google.com",new RequestParameter[0],null).execute(new SessionStatus.SessionStatusTunnel());
        setContentView(myApp);
    }
}

