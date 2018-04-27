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
import nadav.tasher.lightool.parts.Tower;
import nadav.tasher.lightool.communication.bluetooth.BluetoothSession;
import nadav.tasher.lightool.graphics.views.AppView;
import nadav.tasher.lightool.graphics.views.DragNavigation;

public class TestingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AppView myApp=new AppView(getApplicationContext(),null,0x333333);
        myApp.setBackgroundColor(Color.BLUE);
        myApp.overlaySelf(getWindow());

        final BluetoothSession session=new BluetoothSession(getApplicationContext(),"20:15:03:16:01:96",10);
        session.registerIncoming(new Tower.OnTunnel<String>() {
            @Override
            public void onReceive(String response) {
                Log.i("BT",response);
            }
        });
        Button b=new Button(this);
        myApp.setContent(b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionStatus.SessionStatusTower sst=new SessionStatus.SessionStatusTower();
                sst.addReceiver(new Tower.OnTunnel<SessionStatus>() {
                    @Override
                    public void onReceive(SessionStatus response) {
                        Log.i("BTS",""+response.getExtra());
                    }
                });
                session.execute(sst);
            }
        });
        myApp.getDragNavigation().setOnStateChangedListener(new DragNavigation.OnStateChangedListener() {
            @Override
            public void onOpen() {
                session.send("Hi");

            }

            @Override
            public void onClose() {
                session.close();
            }
        });
        setContentView(myApp);
    }
}

