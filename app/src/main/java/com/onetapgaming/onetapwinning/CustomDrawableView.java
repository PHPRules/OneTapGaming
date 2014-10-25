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
    int r, g, b;
    Random taps;

    public CustomDrawableView(Context context, int x, int y, int size) {
        super(context);
        rR = new Random();
        rG = new Random();
        rB = new Random();
        taps = new Random();
        this.r = rR.nextInt(200);
        this.g = rG.nextInt(200);
        this.b = rB.nextInt(200);
        numTapsLeft = taps.nextInt(5)+1;

        if(r > g && r > b)
        {
            r = 200;
        }
        else if(g > r && g > b)
        {
            g = 200;
        }
        else
        {
            b = 200;
        }

        mDrawable = new ShapeDrawable(new RectShape());
        mDrawable.getPaint().setARGB(255,r,g,b);
        mDrawable.setBounds(x, y, x + size, y + size);
        setOnClickListener(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
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

        mDrawable.setIntrinsicWidth(size);
        mDrawable.setIntrinsicHeight(size);
        //this.setBackgroundColor(Color.GREEN);
        //maybe?
        this.setHapticFeedbackEnabled(true);
        this.context = context;

        setBackground(mDrawable);
        //this.setMinimumHeight(100);
        //this.setMinimumWidth(100);
    }

    // This is actually being called every millisecond
    protected void onDraw(Canvas canvas) {
        //this.draw(canvas);
        TextPaint paint = new TextPaint();
        //Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(Color.WHITE);
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
            this.setBackgroundColor(Color.argb((105  + (numTapsLeft * 30)), r, g, b));
        } else {
            //this.setVisibility(View.INVISIBLE);
            ((FrameLayout)((Activity)this.context).findViewById(R.id.tryFullScreenContent)).removeView(v);
        }
    }
}
