package nadav.tasher.lightool.graphics.views.appview.navigation.squircle;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SquircleView extends FrameLayout {

    private LinearLayout topLeft, topRight, bottomLeft, bottomRight;

    public SquircleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        // Init Views
        topLeft = new LinearLayout(getContext());
        topRight = new LinearLayout(getContext());
        bottomLeft = new LinearLayout(getContext());
        bottomRight = new LinearLayout(getContext());
        // Set Orientation
        topLeft.setOrientation(LinearLayout.VERTICAL);
        topRight.setOrientation(LinearLayout.VERTICAL);
        bottomLeft.setOrientation(LinearLayout.VERTICAL);
        bottomRight.setOrientation(LinearLayout.VERTICAL);
        // Set Gravity
        topLeft.setGravity(Gravity.TOP | Gravity.START);
        topRight.setGravity(Gravity.TOP | Gravity.END);
        bottomLeft.setGravity(Gravity.BOTTOM | Gravity.START);
        bottomRight.setGravity(Gravity.BOTTOM | Gravity.END);
        // Set Size
        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        topLeft.setLayoutParams(parameters);
        topRight.setLayoutParams(parameters);
        bottomLeft.setLayoutParams(parameters);
        bottomRight.setLayoutParams(parameters);
        // Set Padding
        setPadding(10, 10, 10, 10);
        // Add To Layout
        addView(topLeft);
        addView(topRight);
        addView(bottomLeft);
        addView(bottomRight);
    }

    public void setPadding(int padding){
        setPadding(padding, padding, padding, padding);
    }

    public void setTopLeft(Squircle s) {
        if (s == null) {
            topLeft.removeAllViews();
        } else {
            topLeft.removeAllViews();
            topLeft.addView(s);
        }
    }

    public void setTopRight(Squircle s) {
        if (s == null) {
            topRight.removeAllViews();
        } else {
            topRight.removeAllViews();
            topRight.addView(s);
        }
    }

    public void setBottomLeft(Squircle s) {
        if (s == null) {
            bottomLeft.removeAllViews();
        } else {
            bottomLeft.removeAllViews();
            bottomLeft.addView(s);
        }
    }

    public void setBottomRight(Squircle s) {
        if (s == null) {
            bottomRight.removeAllViews();
        } else {
            bottomRight.removeAllViews();
            bottomRight.addView(s);
        }
    }
}