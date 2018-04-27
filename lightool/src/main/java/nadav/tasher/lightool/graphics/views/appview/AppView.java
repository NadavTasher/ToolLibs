package nadav.tasher.lightool.graphics.views.appview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import nadav.tasher.lightool.graphics.views.appview.navigation.Drag;
import nadav.tasher.lightool.graphics.views.appview.navigation.bar.Bar;
import nadav.tasher.lightool.graphics.views.appview.navigation.bar.Squircle;

public class AppView extends FrameLayout {
    private FrameLayout content;
    private LinearLayout scrolly;
    private LinearLayout barHolder;
    private Drag drag;
    private Bar bar;
    private int currentTopColor = Color.WHITE;
    private int currentBottomColor = currentTopColor;

    public AppView(Context context, Drawable icon, int dragColor, Squircle mainSquircle) {
        super(context);
        drag = new Drag(context, icon, dragColor);
        bar = new Bar(context, mainSquircle);
        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        content = new FrameLayout(context);
        scrolly = new LinearLayout(context);
        barHolder = new LinearLayout(context);
        scrolly.setOrientation(LinearLayout.VERTICAL);
        scrolly.setGravity(Gravity.CENTER);
        barHolder.setOrientation(LinearLayout.VERTICAL);
        barHolder.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        barHolder.addView(bar);
        scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.addView(scrolly);
        scrolly.addView(new View(context), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, drag.spacerSize()));
        scrolly.addView(content);
        addView(scrollView);
        addView(barHolder);
        addView(drag);
    }

    public AppView(Context context, Drawable icon, int dragColor) {
        super(context);
        drag = new Drag(context, icon, dragColor);
        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        content = new FrameLayout(context);
        scrolly = new LinearLayout(context);
        scrolly.setOrientation(LinearLayout.VERTICAL);
        scrolly.setGravity(Gravity.CENTER);
        scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.addView(scrolly);
        scrolly.addView(new View(context), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, drag.spacerSize()));
        scrolly.addView(content);
        addView(scrollView);
        addView(drag);
    }

    public Drag getDrag() {
        return drag;
    }

    public Bar getBar() {
        return bar;
    }

    public void setContent(View v) {
        if (content != null) {
            content.removeAllViews();
            content.addView(v);
        }
    }

    public int getBackgroundColor() {
        return currentTopColor;
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        currentTopColor = color;
        currentBottomColor = color;
    }

    public void overlaySelf(Window w) {
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(drag.calculateOverlayedColor(currentTopColor));
        w.setNavigationBarColor(currentBottomColor);
    }

    public int getTopColor() {
        return currentTopColor;
    }

    public void setTopColor(int color) {
        currentTopColor = color;
    }

    public int getBottomColor() {
        return currentBottomColor;
    }

    public void setBottomColor(int color) {
        currentBottomColor = color;
    }
}
