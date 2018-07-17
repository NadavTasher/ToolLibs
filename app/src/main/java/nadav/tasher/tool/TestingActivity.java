/*
 * This file contains test code for the library.
 */

package nadav.tasher.tool;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        final AppView view=new AppView(getApplicationContext(),0xFF123321);
        CornerView cornNav=new CornerView(getApplicationContext());
        Corner c=new Corner(getApplicationContext(), Device.screenX(getApplicationContext())/5,0xFF123987);
        c.setDrawable(getDrawable(R.drawable.ic_add),0.8);
        c.addOnState(new Corner.OnState() {
                         @Override
                         public void onOpen() {
                             view.getDrawer().open(false,0.7);
                         }

                         @Override
                         public void onClose() {
                             view.getDrawer().close(false);

                         }

                         @Override
                         public void onBoth(boolean isOpened) {
                         }
                     }
        );
        cornNav.setTopRight(c);
        view.setNavigationView(cornNav);
        view.setBackgroundColor(Color.GREEN);
        TextView myTextA=new TextView(getApplicationContext());
        myTextA.setText("This Is A Text.");
        TextView myTextB=new TextView(getApplicationContext());
        myTextA.setGravity(Gravity.CENTER);
        myTextB.setGravity(Gravity.CENTER);
        myTextB.setText("This Is Another Text.");
//        myTextB.setPadding(0,30,0,30);
        myTextB.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Device.screenY(getApplicationContext())/13));
        ExpandingView ev=new ExpandingView(getApplicationContext(),500,Device.screenY(getApplicationContext())/13,myTextA,myTextB);
        ev.setBackground(Utils.getCoaster(Color.WHITE,32,10));
        view.setContent(ev);
        setContentView(view);
    }
}

