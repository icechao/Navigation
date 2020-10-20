package com.icechao.navigation;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class HexagonView extends View {

    private Path mPath;

    private Paint paint;
    private int mAlpha = 150;
    private int strokeWidth = 10;
    private float radius = 10;
    private Paint shadowPaint;
    private ValueAnimator valueAnimator;
    private PathMeasure mPathMeasure = new PathMeasure();
    private Path shadowPath = new Path();
    private Path shadowPathInit = new Path();
    private float mFloatPos;
    int lineLength = 200;

    public HexagonView(Context context) {
        super(context);
    }

    public HexagonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public HexagonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HexagonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {
        CornerPathEffect cornerPathEffect = new CornerPathEffect(radius);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setPathEffect(cornerPathEffect);

        shadowPaint = new Paint();
        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setStrokeWidth(strokeWidth);
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(Color.RED);
        shadowPaint.setPathEffect(cornerPathEffect);
//        SweepGradient shader = new SweepGradient(getMeasuredWidth()/2, getMeasuredHeight()/2,
//                new int[]{Color.RED, Color.TRANSPARENT},
//                new float[]{
//                        1f, 0.25f
//                });
//        Matrix matrix = new Matrix();
//        matrix.setRotate(130, getMeasuredWidth()/2, getMeasuredHeight()/2);
//        shader.setLocalMatrix(matrix);
//        shadowPaint.setShader(shader);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (mPath == null) {
            initHexagonPath();
        }
        canvas.drawPath(mPath, paint);
        initHexagonShadowPath(canvas);
    }


    private void initHexagonPath() {
        mPath = new Path();
        float l = (float) (getMeasuredWidth() / 2);
        float h = (float) (Math.sqrt(3) * l);
        float top = (getMeasuredHeight() - h) / 2;
        mPath.reset();
        mPath.moveTo(l / 2, top);
        mPath.lineTo(strokeWidth, h / 2 + top);
        mPath.lineTo(l / 2, h + top);
        mPath.lineTo((float) (l * 1.5), h + top);
        mPath.lineTo(2 * l - strokeWidth, h / 2 + top);
        mPath.lineTo((float) (l * 1.5), top);
        mPath.lineTo(l / 2, top);
        mPath.close();
    }

    private void initHexagonShadowPath(Canvas canvas) {
        mPathMeasure.setPath(mPath, true);
        float mLength = mPathMeasure.getLength();
        // 每次重新绘制之前将mDest重置
        shadowPath.reset();
        shadowPathInit.reset();
        float mStop = mFloatPos * mLength;
        // 截取mPath中从mStart起点到mStop终点的片段，到mDest里
        float mStart = 0f;
        if (mStop < lineLength) {
            mStart = 0;
        } else {
            mStart = mStop - lineLength;
        }
        mPathMeasure.getSegment(mStart, mStop, shadowPath, true);
        canvas.drawPath(shadowPath, shadowPaint);

        if (mStop < lineLength) {
            float mStart2 = mLength - (lineLength - mStop);
            mPathMeasure.getSegment(mStart2, mLength, shadowPathInit, true);
            canvas.drawPath(shadowPathInit, shadowPaint);
        }

    }

    public void startAnim() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mFloatPos = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.setDuration(4000);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.start();
        }
    }

    public void stopAnim() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }

}
