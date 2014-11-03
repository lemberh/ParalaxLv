package com.example.roman.paralaxlv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Roman on 31.10.2014.
 */
public class ParallaxView extends ImageView {

    private int mCurrentTranslation;

    public ParallaxView(Context context) {
        super(context);
//        setBackgroundColor(Color.TRANSPARENT);
    }

    public ParallaxView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setBackgroundColor(Color.TRANSPARENT);
    }

    public ParallaxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setBackgroundColor(Color.TRANSPARENT);
    }

    public void setCurrentTranslation(int currentTranslation) {
        mCurrentTranslation = currentTranslation;
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(0, -(getHeight() - mCurrentTranslation) / 2);
        super.draw(canvas);
        canvas.restore();
    }
}