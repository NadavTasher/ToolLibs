package nadav.tasher.lightool.graphics.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class AppView extends FrameLayout {
    private FrameLayout content;
    private LinearLayout scrolly;
    private DragNavigation dragNavigation;
    private int currentTopColor = Color.WHITE;
    private int currentBottomColor = currentTopColor;

    public AppView(Context context, Drawable icon, int dragColor) {
        super(context);
        dragNavigation = new DragNavigation(context, icon, dragColor);
        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        content = new FrameLayout(context);
        scrolly = new LinearLayout(context);
        scrolly.setOrientation(LinearLayout.VERTICAL);
        scrolly.setGravity(Gravity.CENTER);
        scrollView.addView(scrolly);
        scrolly.addView(new View(context), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dragNavigation.spacerSize()));
        scrolly.addView(content);
        addView(scrollView);
        addView(dragNavigation);
    }

    public DragNavigation getDragNavigation(){
        return dragNavigation;
    }

    public void setContent(View v){
        if(content!=null){
            content.removeAllViews();
            content.addView(v);
        }
    }

    public int getBackgroundColor(){
        return currentTopColor;
    }

    public void overlaySelf(Window w) {
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(dragNavigation.calculateOverlayedColor(currentTopColor));
        w.setNavigationBarColor(currentBottomColor);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        currentTopColor = color;
        currentBottomColor = color;
    }

    public void setBottomColor(int color){
        currentBottomColor=color;
    }

    public void setTopColor(int color){
        currentTopColor=color;
    }

    public int getTopColor(){
        return currentTopColor;
    }

    public int getBottomColor(){
        return currentBottomColor;
    }
}
