package nadav.tasher.lightool.graphics.views.appview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import nadav.tasher.lightool.graphics.views.appview.navigation.drawer.Drawer;

public class AppView extends FrameLayout {
    private FrameLayout content;
    private LinearLayout scrolly;
    private Window window;
    private FrameLayout navigationView;
    private Drawer drawer;
    private Scrolly scrollView;
    private boolean colorChangeNav = true, colorChangeStat = true;
    private Gradient backgroundColors = new Gradient(Color.WHITE);
    private Drawer.PersistantOnState persistantOnState;

    public AppView(Context context, int drawerColor) {
        super(context);
        init(drawerColor);
    }

    public AppView(Context context) {
        super(context);
        init(0x80333333);
    }

    private void init(final int drawerColor) {
        drawer = new Drawer(getContext(), drawerColor);
        persistantOnState = new Drawer.PersistantOnState() {
            @Override
            public void onOpen() {
                setWindowColors(new Gradient(drawer.getStatusBarColor(backgroundColors.colorTop, drawerColor), backgroundColors.getColorBottom()));
            }

            @Override
            public void onClose() {
                setWindowColors(backgroundColors);
            }

            @Override
            public void onBoth(boolean isOpened) {
            }
        };
        drawer.addOnState(persistantOnState);
        scrollView = new Scrolly(getContext());
        scrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        content = new FrameLayout(getContext());
        navigationView = new FrameLayout(getContext());
        scrolly = new LinearLayout(getContext());
        scrolly.setOrientation(LinearLayout.VERTICAL);
        scrolly.setGravity(Gravity.CENTER);
        scrollView.addView(scrolly);
        scrolly.addView(content);
        addView(scrollView);
        addView(navigationView);
        addView(drawer);
    }

    public void setNavigationView(FrameLayout frameLayout) {
        navigationView.removeAllViews();
        navigationView.addView(frameLayout);
    }

    public void removeNavigation() {
        navigationView.removeAllViews();
    }

    public Drawer getDrawer() {
        return drawer;
    }

    public void setContent(View v) {
        if (content != null) {
            content.removeAllViews();
            content.addView(v);
        }
    }

    public Scrolly getScrolly() {
        return scrollView;
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
            if(!drawer.isOpen()) {
                setWindowColors(backgroundColors);
            }else{
                persistantOnState.onOpen();
            }
        }
    }

    public void setWindow(Window w) {
        this.window = w;
        if (window != null) {
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
            if (colorChangeNav) window.setNavigationBarColor(g.colorBottom);
            if (colorChangeStat) window.setStatusBarColor(g.colorTop);
        }
    }

    public void setColorChangeNavigation(boolean newVal) {
        this.colorChangeNav = newVal;
    }

    public void setColorChangeStatus(boolean newVal) {
        this.colorChangeStat = newVal;
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
