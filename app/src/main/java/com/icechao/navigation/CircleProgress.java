package com.icechao.navigation;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.navigation
 * @FileName     : CircleProgress.java
 * @Author       : chao
 * @Date         : 2019-09-12
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *
 *                    .::::.
 *                  .::::::::.
 *                 :::::::::::  
 *             ..:::::::::::'
 *           '::::::::::::'
 *             .::::::::::
 *        '::::::::::::::..
 *             ..::::::::::::.
 *           ``::::::::::::::::
 *            ::::``:::::::::'        .:::.
 *           ::::'   ':::::'       .::::::::.
 *         .::::'      ::::     .:::::::'::::.
 *        .:::'       :::::  .:::::::::' ':::::.
 *       .::'        :::::.:::::::::'      ':::::.
 *      .::'         ::::::::::::::'         ``::::.
 *  ...:::           ::::::::::::'              ``::.
 * ```` ':.          ':::::::::'                  ::::..
 *                    '.:::::'                    ':'````..
 *************************************************************************/
public class CircleProgress extends View implements ValueAnimator.AnimatorUpdateListener {


    private Paint backGroudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float radiu = 150;

    private float xPoint, yPoint;
    private RectF rectF;
    private int endColor;
    private int startColor;
    private float progressAngle;


    public void setRadiu(float radiu) {
        this.radiu = radiu;
    }

    public void setWidth(float widht) {
        backGroudPaint.setStrokeWidth(widht);
        progressPaint.setStrokeWidth(widht);
    }

    public CircleProgress(Context context) {
        super(context);
        initView(context);
    }


    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);

    }

    private void initView(Context context) {
        setWillNotDraw(false);
        setWidth(40);
        backGroudPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStyle(Paint.Style.STROKE);
        backGroudPaint.setAntiAlias(true);
        progressPaint.setAntiAlias(true);
        setProgressColor(Color.GRAY, Color.BLUE);
    }

    private void setProgressColor(int startColor, int endColor) {
        this.endColor = endColor;
        this.startColor = startColor;
    }

    @Override
    public void setBackgroundColor(int color) {
//        super.setBackgroundColor(color);
        backGroudPaint.setColor(color);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        xPoint = getMeasuredWidth() / 2f;
        yPoint = getMeasuredHeight() / 2f;
        rectF = new RectF(xPoint - radiu, yPoint - radiu, xPoint + radiu, yPoint + radiu);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(xPoint, yPoint, radiu, backGroudPaint);
        canvas.save();
        canvas.rotate(90, xPoint, yPoint);
        progressPaint.setShader(new SweepGradient(xPoint, yPoint, startColor, endColor));
        canvas.drawArc(rectF, 0, progressAngle, false, progressPaint);
        canvas.restore();
    }

    public void setProgress(int progress) {
        if (progress >= 0 && progress <= 100) {
            float temp = 3.6f * progress;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(this.progressAngle, temp);
            valueAnimator.addUpdateListener(this);
            valueAnimator.setDuration(500);
            valueAnimator.start();
        }

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        this.progressAngle = (float) animation.getAnimatedValue();
        invalidate();
    }
}
