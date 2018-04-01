package nadav.tasher.lightool.graphics;

import android.graphics.Color;

public class ColorFadeAnimation {
    private ColorFadeAnimation.ColorState onChange;
    private int colorA, colorB;

    public ColorFadeAnimation(int start, int end, ColorFadeAnimation.ColorState colorState) {
        onChange = colorState;
        colorA = start;
        colorB = end;
    }

    public void start(int milliseconds) {
        final int rOffset = Color.red(colorB) - Color.red(colorA);
        final int gOffset = Color.green(colorB) - Color.green(colorA);
        final int bOffset = Color.blue(colorB) - Color.blue(colorA);
        int total = (Math.abs(rOffset) + Math.abs(gOffset) + Math.abs(bOffset));
        if (total == 0) {
            total = 1;
        }
        final double maxTimePerColor = milliseconds / total;
        final int rA = Color.red(colorA);
        final int gA = Color.green(colorA);
        final int bA = Color.blue(colorA);
        final int rB = Color.red(colorB);
        final int gB = Color.green(colorB);
        final int bB = Color.blue(colorB);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int r;
                int g = gA;
                int b = bA;
                if (rOffset < 0) {
                    for (r = rA; r > rB; r--) {
                        onChange.onColor(Color.rgb(r, g, b));
                        try {
                            Thread.sleep((long) maxTimePerColor);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    for (r = rA; r < rB; r++) {
                        onChange.onColor(Color.rgb(r, g, b));
                        try {
                            Thread.sleep((long) maxTimePerColor);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (gOffset < 0) {
                    for (g = gA; g > gB; g--) {
                        onChange.onColor(Color.rgb(r, g, b));
                        try {
                            Thread.sleep((long) maxTimePerColor);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    for (g = gA; g < gB; g++) {
                        onChange.onColor(Color.rgb(r, g, b));
                        try {
                            Thread.sleep((long) maxTimePerColor);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (bOffset < 0) {
                    for (b = bA; b > bB; b--) {
                        onChange.onColor(Color.rgb(r, g, b));
                        try {
                            Thread.sleep((long) maxTimePerColor);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    for (b = bA; b < bB; b++) {
                        onChange.onColor(Color.rgb(r, g, b));
                        try {
                            Thread.sleep((long) maxTimePerColor);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public interface ColorState {
        void onColor(int color);
    }
}