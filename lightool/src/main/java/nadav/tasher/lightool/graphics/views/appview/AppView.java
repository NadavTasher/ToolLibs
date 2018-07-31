package nadav.tasher.lightool.graphics.views.appview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import nadav.tasher.lightool.graphics.views.appview.navigation.corner.CornerView;
import nadav.tasher.lightool.graphics.views.appview.navigation.drawer.Drawer;

public class AppView extends FrameLayout {
    public static final int ORDER_NAVIGATION_ON_TOP = 0;
    public static final int ORDER_NAVIGATION_ON_BOTTOM = 1;
    private Window window;
    private Drawer drawer;
    private CornerView cornerView;
    private Scrolly scroll;
    private int order = ORDER_NAVIGATION_ON_TOP;
    private boolean drawNavigation = true, drawStatusbar = true;
    private Gradient backgroundColors = new Gradient(Color.WHITE);

    public AppView(Activity activity) {
        super(activity);
        setWindow(activity.getWindow());
        init();
    }

    private void init() {
        drawer = new Drawer(getContext());
        scroll = new Scrolly(getContext());
        cornerView = new CornerView(getContext());
        initOrder();
    }

    public void setOrder(int order) {
        this.order = order;
        initOrder();
    }

    private void initOrder() {
        addView(scroll);
        if (order == ORDER_NAVIGATION_ON_TOP) {
            addView(drawer);
            addView(cornerView);
        } else {
            addView(cornerView);
            addView(drawer);
        }
    }

    public Drawer getDrawer() {
        return drawer;
    }

    public Scrolly getScrolly() {
        return scroll;
    }

    public CornerView getCornerView() {
        return cornerView;
    }

    public Gradient getBackgroundColor() {
        return backgroundColors;
    }

    public void setBackgroundColor(Gradient color) {
        backgroundColors = color;
        updateColors();
    }

    @Override
    public void setBackgroundColor(int color) {
        backgroundColors = new Gradient(color);
        updateColors();
    }

    private void updateColors() {
        setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{
                backgroundColors.getColorTop(),
                backgroundColors.getColorBottom()
        }));
        if (window != null) {
            setWindowColors(backgroundColors);
        }
    }

    private void setWindow(Window window) {
        this.window = window;
        if (this.window != null) {
            setFlags();
            setWindowColors(backgroundColors);
        }
    }

    private void setFlags() {
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    private void setWindowColors(Gradient g) {
        if (window != null) {
            if (drawNavigation) window.setNavigationBarColor(g.colorBottom);
            if (drawStatusbar) window.setStatusBarColor(g.colorTop);
        }
    }

    public void setDrawStatusbar(boolean drawStatusbar) {
        this.drawStatusbar = drawStatusbar;
    }

    public void setDrawNavigation(boolean drawNavigation) {
        this.drawNavigation = drawNavigation;
    }

    public static class Gradient {
        private int colorTop, colorBottom;

        public Gradient(int color) {
            colorTop = color;
            colorBottom = color;
        }

        public Gradient(int colorTop, int colorBottom) {
            this.colorTop = colorTop;
            this.colorBottom = colorBottom;
        }

        public int getColorBottom() {
            return colorBottom;
        }

        public int getColorTop() {
            return colorTop;
        }
    }

    public static class Scrolly extends ScrollView {

        private OnScroll list;

        public Scrolly(Context context) {
            super(context);
            setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
            setVerticalScrollBarEnabled(false);
        }

        public void setView(View v) {
            super.removeAllViews();
            super.addView(v);
        }

        @Deprecated
        @Override
        public void addView(View view) {
        }

        public void setOnScroll(OnScroll os) {
            list = os;
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            if (list != null) {
                list.onScroll(l, t, oldl, oldt);
            }
            super.onScrollChanged(l, t, oldl, oldt);
        }

        public interface OnScroll {
            void onScroll(int l, int t, int ol, int ot);
        }
    }
}
