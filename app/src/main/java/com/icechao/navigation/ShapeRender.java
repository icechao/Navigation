package com.icechao.navigation;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;

import static android.view.GestureDetector.*;

/**
 * 绘图容器
 */
public class ShapeRender extends androidx.appcompat.widget.AppCompatImageView implements OnGestureListener, View.OnTouchListener {


    ParallelLineShape shape = new ParallelLineShape();


    private GestureDetectorCompat gestureDetector;

    public ShapeRender(Context context) {
        super(context);
    }

    public ShapeRender(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShapeRender(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs) {
//        setWillNotDraw(false);
        gestureDetector = new GestureDetectorCompat(context, this, new Handler());
        gestureDetector.setIsLongpressEnabled(false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        shape.drawShape(canvas);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        getParent().requestDisallowInterceptTouchEvent(true);
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {

        shape.setPoint(e.getX(), e.getY());

        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        invalidate();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        shape.updatePoint(e2.getX(), e2.getY(), distanceX, distanceY);

        invalidate();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
