/*
 * This file contains test code for the library.
 */

package nadav.tasher.tool;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

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
        setContentView(myApp);
    }
}

