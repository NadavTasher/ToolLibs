/*
 * This file contains test code for the library.
 */

package nadav.tasher.tool;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import nadav.tasher.lightool.graphics.views.AppView;

public class TestingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppView myApp=new AppView(getApplicationContext(),null,0xFF333333);
        myApp.setBackgroundColor(Color.MAGENTA);
        myApp.overlaySelf(getWindow());
        setContentView(myApp);
    }
}

