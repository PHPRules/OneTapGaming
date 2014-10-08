package com.onetapgaming.onetapwinning;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.Random;

/**
 * Created by Jason on 9/15/2014.
 */
public class DrawShapes implements Runnable {

    Random randX, randY, rS;
    View destination;

    double theta;

    public DrawShapes(View view) {
        randX = new Random();
        randY = new Random();
        rS = new Random();
        destination = view;
    }

    public void run() {
        WindowManager wm = (WindowManager) destination.findViewById(R.id.tryFullScreenContent).getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        CustomDrawableView customDrawableView;
        int size = rS.nextInt(250 - 125) + 125;
        int xPos = randX.nextInt(p.x-size);
        int yPos = randY.nextInt(p.y-size);
        customDrawableView = new CustomDrawableView(destination.findViewById(R.id.tryFullScreenContent).getContext(), xPos, (yPos > 80? yPos : (yPos + 100)), size);
        ((FrameLayout)destination.findViewById(R.id.tryFullScreenContent)).addView(customDrawableView);
    }
}