package com.onetapgaming.onetapwinning;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Jason on 9/12/2014.
 */
public class CustomDrawableView extends ImageView implements View.OnClickListener {

    private ShapeDrawable mDrawable;
    int numTapsLeft;
    int timeRemaining;
    Context context;
    Random rR;
    Random rG;
    Random rB;
    Random taps;

    public CustomDrawableView(Context context, int x, int y) {
        super(context);
        int width = 100;
        int height = 100;
        rR = new Random();
        rG = new Random();
        rB = new Random();
        taps = new Random();
        int r = rR.nextInt(255);
        int g = rG.nextInt(255);
        int b = rB.nextInt(255);
        numTapsLeft = taps.nextInt(5)+1;

        if(r > g && r > b)
        {
            r = 255;
        }
        else if(g > r && g > b)
        {
            g = 255;
        }
        else
        {
            b = 255;
        }

        mDrawable = new ShapeDrawable(new RectShape());
        mDrawable.getPaint().setARGB(255,r,g,b);
        mDrawable.setBounds(x, y, x + width, y + height);
        setOnClickListener(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.topMargin = y;
        params.leftMargin = x;
        setLayoutParams(params);
        this.setPadding(20,20,20,20);
      /*  v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLICKY CLICK CLICK");
                if (numTapsLeft > 0) {
                    numTapsLeft--;
                    invalidate();
                } else {
                    // mDrawable.setAlpha(0);
                }
            }
        });*/
        /*v.setOnTouchListener(new OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                 System.out.println("CLICKY CLICK CLICK");
                 if (numTapsLeft > 0) {
                     numTapsLeft--;
                     invalidate();
                 } else {
                     // mDrawable.setAlpha(0);
                 }
                 return true;
             }
         }
        );*/

        mDrawable.setIntrinsicWidth(width);
        mDrawable.setIntrinsicHeight(height);
        //this.setBackgroundColor(Color.GREEN);
        //maybe?
        this.setHapticFeedbackEnabled(true);
        this.context = context;

        setBackground(mDrawable);
        //this.setMinimumHeight(100);
        //this.setMinimumWidth(100);
    }

    protected void onDraw(Canvas canvas) {
        //this.draw(canvas);
        TextPaint paint = new TextPaint();
        //Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(Color.BLUE);
        paint.setTextSize(48);
        canvas.drawText(String.valueOf(numTapsLeft), mDrawable.getBounds().centerX(), mDrawable.getBounds().centerY(), paint);
        ////this.setTextSize(48);
        ////this.setTextColor(Color.BLUE);
        ////this.setText(String.valueOf(numTapsLeft));
    }

    @Override
    public void onClick(View v) {
        if (numTapsLeft > 1) {
            numTapsLeft--;
            invalidate();
        } else {
            //this.setVisibility(View.INVISIBLE);
            ((FrameLayout)((Activity)this.context).findViewById(R.id.tryFullScreenContent)).removeView(v);
        }
    }
}
