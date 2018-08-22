/*
 * This file contains test code for the library.
 */

package nadav.tasher.tool;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nadav.tasher.lightool.graphics.views.ExpandingView;
import nadav.tasher.lightool.graphics.views.Utils;
import nadav.tasher.lightool.graphics.views.appview.AppView;
import nadav.tasher.lightool.graphics.views.appview.navigation.corner.Corner;
import nadav.tasher.lightool.info.Device;
import nadav.tasher.lightool.parts.Peer;

public class TestingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AppView view = new AppView(this);
        new Peer<String>().
                view.getDrawer().getDrawerView().setBackground(Utils.getCoaster(Color.BLACK, 64, 20));
        view.getDrawer().setAnimationTime(1000);
        Corner c = new Corner(getApplicationContext(), Device.screenX(getApplicationContext()) / 5, 0xFF123987);
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageDrawable(getDrawable(R.drawable.ic_add));
        c.setView(imageView, 0.8);
        c.addOnState(new Corner.OnState() {
                         @Override
                         public void onOpen() {
                         }

                         @Override
                         public void onClose() {
                         }

                         @Override
                         public void onBoth(boolean isOpened) {
                             if (view.getDrawer().isOpen()) {
                                 view.getDrawer().close();
                             } else {
                                 view.getDrawer().open(0.5);
                             }
                         }
                     }
        );
        view.getCornerView().setTopRight(c);
        Corner c2 = new Corner(getApplicationContext(), Device.screenX(getApplicationContext()) / 5, 0xFF123987);
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
        myTextB.setTextSize(32);
        myTextB.setText("This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.\nThis Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.\nThis Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.This Is A Text.\nThis Is A Text.");
        myTextB.measure(ViewGroup.LayoutParams.MATCH_PARENT, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i("BHO",""+myTextB.getMeasuredHeight());

        //        myTextB.setPadding(0,30,0,30);
//        myTextB.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Device.screenY(getApplicationContext()) / 13));
        ExpandingView ev = new ExpandingView(getApplicationContext(), Utils.getCoaster(Color.WHITE, 32, 10),500, Device.screenY(getApplicationContext()) / 13, myTextA, myTextB);
        LinearLayout ll=new LinearLayout(getApplicationContext());
        ll.setGravity(Gravity.CENTER);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(ev);
        view.getScrolly().setView(ll);
        setContentView(view);
    }
}

