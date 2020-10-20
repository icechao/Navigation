package com.icechao.navigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.Nullable;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.navigation
 * @FileName     : PolygonProgressView.java
 * @Author       : chao
 * @Date         : 2020/6/16
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
public class PolygonProgressView extends androidx.appcompat.widget.AppCompatTextView implements ValueAnimator.AnimatorUpdateListener {

    private Paint borderPolygonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint movePointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint movePointShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path borderPolygonPath;
    private PathMeasure borderPolygonMeasure;
    private float moveLength;


    private float movePointRadius = 30;
    private float movePointShadowRadius = 50;
    private float perimeter;

    public PolygonProgressView(Context context) {
        super(context);
    }

    public PolygonProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        borderPolygonPaint.setStyle(Paint.Style.STROKE);
        borderPolygonPaint.setStrokeWidth(20);
        borderPolygonPaint.setColor(Color.YELLOW);
        borderPolygonPaint.setPathEffect(new CornerPathEffect(30));

        movePointPaint.setColor(Color.parseColor("#00ff00"));
        movePointShadowPaint.setColor(Color.parseColor("#5500ff00"));
    }

    public PolygonProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float leftX, slidTopY, slidBottomY, topY, centerX, centerY, bottomY, rightX;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        float temp = (h - paddingTop - paddingBottom) >> 2;
        leftX = paddingLeft;
        rightX = w - paddingRight;
        topY = paddingTop;
        bottomY = h - paddingBottom;
        slidTopY = paddingTop + temp;
        slidBottomY = h - paddingBottom - temp;
        centerX = w >> 1;
        centerY = h >> 1;

        borderPolygonPath = new Path();
        borderPolygonPath.moveTo(rightX, h >> 1);
        borderPolygonPath.lineTo(rightX, slidBottomY);
        borderPolygonPath.lineTo(centerX, bottomY);
        borderPolygonPath.lineTo(leftX, slidBottomY);
        borderPolygonPath.lineTo(leftX, slidTopY);
        borderPolygonPath.lineTo(centerX, topY);
        borderPolygonPath.lineTo(rightX, slidTopY);
        borderPolygonPath.close();


        borderPolygonMeasure = new PathMeasure(borderPolygonPath, true);
        perimeter = borderPolygonMeasure.getLength();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, perimeter);
        valueAnimator.setRepeatCount(1000);
        valueAnimator.setDuration(10000);
        valueAnimator.addUpdateListener(this);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();

    }

    SweepGradient shader = new SweepGradient(centerX, centerY,
            new int[]{Color.YELLOW, Color.TRANSPARENT, Color.YELLOW, Color.TRANSPARENT, Color.YELLOW},
            new float[]{
                    0f, 0.25f, 0.5f, 0.75f, 1f
            });
    Matrix matrix = new Matrix();

     float[] firstPointPosition = new float[2];
     float[] secPointPosition = new float[2];

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        borderPolygonMeasure.getPosTan(moveLength, firstPointPosition, null);
        float senDistance = moveLength + perimeter / 2;
        if (senDistance <= perimeter) {
            borderPolygonMeasure.getPosTan(senDistance, secPointPosition, null);
        } else {
            borderPolygonMeasure.getPosTan(senDistance - perimeter, secPointPosition, null);
        }

        matrix.setRotate(360 * moveLength / perimeter, centerX, centerY);
        shader.setLocalMatrix(matrix);

        borderPolygonPaint.setShader(shader);
        canvas.drawPath(borderPolygonPath, borderPolygonPaint);
        canvas.drawCircle(firstPointPosition[0], firstPointPosition[1], movePointShadowRadius, movePointShadowPaint);
        canvas.drawCircle(firstPointPosition[0], firstPointPosition[1], movePointRadius, movePointPaint);
        canvas.drawCircle(secPointPosition[0], secPointPosition[1], movePointShadowRadius, movePointShadowPaint);
        canvas.drawCircle(secPointPosition[0], secPointPosition[1], movePointRadius, movePointPaint);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        moveLength = (float) animation.getAnimatedValue();
        invalidate();
    }
}
