package com.icechao.navigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

/*************************************************************************
 * Description   : 方形边上有个移动的线
 *
 * @PackageName  : com.icechao.navigation
 * @FileName     : RayView.java
 * @Author       : chao
 * @Date         : 2020/6/15
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
public class RayView extends androidx.appcompat.widget.AppCompatImageView implements ValueAnimator.AnimatorUpdateListener {

    private float radius = 50;

    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint movePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PathMeasure pathMeasure;
    private float pathLength;


    public RayView(Context context) {
        super(context);
    }

    public RayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        borderPaint.setColor(Color.YELLOW);
        borderPaint.setStrokeWidth(30);
        borderPaint.setStyle(Paint.Style.STROKE);

        movePaint.setColor(Color.RED);
        movePaint.setStrokeWidth(30);
        movePaint.setStyle(Paint.Style.STROKE);
        movePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public RayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        float right = (float) w - getPaddingRight();
        float bottom = (float) h - getPaddingBottom();
        //构件外框
        path.moveTo(paddingLeft + radius, paddingTop);
        path.lineTo(right - radius, paddingTop);
        path.arcTo(new RectF(right - radius, paddingTop, right, paddingTop + radius), 270, 90);
        path.lineTo(right, bottom - radius);
        path.arcTo(new RectF(right - radius, bottom - radius, right, bottom), 0, 90);
        path.lineTo(paddingLeft + radius, bottom);
        path.arcTo(new RectF(paddingLeft, bottom - radius, paddingLeft + radius, bottom), 90, 90);
        path.lineTo(paddingLeft, paddingTop + radius);
        path.arcTo(new RectF(paddingLeft, paddingTop, paddingLeft + radius, paddingTop + radius), 180, 90);
        path.close();
        pathMeasure = new PathMeasure(path, false);
        pathLength = pathMeasure.getLength();
        //执行动画
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        valueAnimator.setDuration(10000).addUpdateListener(this);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(10000);
        valueAnimator.start();
    }

    private float startX;
    private float moveLength = 200;
    Path path = new Path();
    Path movePath = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, borderPaint);

        float stopD = startX + moveLength;
        if (stopD > pathLength) {
            //如果是移动的结束点大于path长度
            movePath.reset();
            pathMeasure.getSegment(0, stopD - pathLength, movePath, true);
            canvas.drawPath(movePath, movePaint);
        }
        //截取移动长度的进行绘制
        movePath.reset();
        pathMeasure.getSegment(startX, stopD, movePath, true);
        canvas.drawPath(movePath, movePaint);

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        startX = (float) animation.getAnimatedValue();
        invalidate();
    }
}
