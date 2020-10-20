package com.icechao.navigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.navigation
 * @FileName     : PanelView.java
 * @Author       : chao
 * @Date         : 2019-08-06
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
public class PanelView extends View implements ValueAnimator.AnimatorUpdateListener {

    private Paint borderPaint;

    public PanelView(Context context) {
        super(context);
    }

    public PanelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PanelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PanelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        setWillNotDraw(false);
        centerX = padding + radiu;
        centerY = radiu + padding;

        centerTopPaint.setColor(Color.WHITE);
        centerTopPaint.setStrokeWidth(2);
        centerTopPaint.setStyle(Paint.Style.STROKE);

        centerBottomPaint.setStrokeWidth(10);
        centerBottomPaint.setColor(Color.YELLOW);
        centerBottomPaint.setStyle(Paint.Style.STROKE);
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStrokeWidth(3);
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);

        innerLineLongPaint.setStyle(Paint.Style.STROKE);
        innerLineLongPaint.setStrokeWidth(35);
        innerLineLongPaint.setColor(Color.WHITE);
        innerLinePaint.setColor(Color.WHITE);
        innerLinePaint.setStrokeWidth(20);
        innerLinePaint.setStyle(Paint.Style.STROKE);

        innerCircleSidePaint.setStyle(Paint.Style.STROKE);
        innerCircleSidePaint.setStrokeWidth(innerCircleSideWidth);
        innerCircleSidePaint.setColor(Color.YELLOW);

        smallCirclePaint.setStyle(Paint.Style.STROKE);
        smallCirclePaint.setColor(Color.WHITE);
        smallCirclePaint.setStrokeWidth(smallCircleWidth);
        indexLinePaint.setStyle(Paint.Style.STROKE);
        indexLinePaint.setColor(Color.BLUE);
        indexPaint.setStyle(Paint.Style.FILL);
        indexPaint.setColor(Color.BLUE);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(25);

    }

    private float radiu = 400;
    private float centerRadiu = 80;
    private float smallCircleRadiu = 20;
    private float padding = 20;
    private float textMargin = 50;
    private float innerPadding = 20;
    private float centerX, centerY;
    private float innerCircleSideWidth = 20;
    private float smallCircleWidth = 4;
    private Paint indexPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint indexLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint innerLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint innerLineLongPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint innerCircleSidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint smallCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint centerBottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint centerTopPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float startAngle = 125, sweepAngle = 290;
    private float idnexStartAngle = 135, idnexSweepAngle = 270;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float textX, textY;
        //背景部分
        canvas.drawArc(new RectF(centerX - radiu, centerY - radiu, centerX + radiu, centerY + radiu), startAngle, sweepAngle, false, borderPaint);
        float radiu = this.radiu - innerPadding;
        RectF rectFShort = new RectF(centerX - radiu, centerY - radiu, centerX + radiu, centerY + radiu);
        radiu = this.radiu - innerPadding - textMargin;
        for (int i = 0; i <= 180; i++) {
            float i1 = idnexSweepAngle / 180f;
            float startAngle = idnexStartAngle + i1 * i;
            if (i % 10 == 0) {

                canvas.drawArc(rectFShort, startAngle - 0.25f, 0.5f, false, innerLineLongPaint);
                if (startAngle <= 90) {
                    textX = (float) (radiu + Math.cos(Math.toRadians(90 - startAngle)) * radiu) + padding + innerPadding + textMargin;
                    textY = (float) (radiu + Math.sin(Math.toRadians(90 - startAngle)) * radiu) + padding + innerPadding + textMargin;
                } else if (startAngle <= 180) {
                    textX = (float) (radiu - Math.cos(Math.toRadians(180 - startAngle)) * radiu) + padding + innerPadding + textMargin;
                    textY = (float) (radiu + Math.sin(Math.toRadians(180 - startAngle)) * radiu) + padding + innerPadding + textMargin;
                } else if (startAngle <= 270) {
                    textX = (float) (radiu - Math.cos(Math.toRadians(startAngle - 180)) * radiu) + padding + innerPadding + textMargin;
                    textY = (float) (radiu - Math.sin(Math.toRadians(startAngle - 180)) * radiu + padding + innerPadding) + textMargin;
                } else {
                    textX = (float) (radiu + Math.cos(Math.toRadians(360 - startAngle)) * radiu) + padding + innerPadding + textMargin;
                    textY = (float) (radiu - Math.sin(Math.toRadians(360 - startAngle)) * radiu) + padding + innerPadding + textMargin;
                }
                textPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(i + "", textX, textY, textPaint);
            } else {
                canvas.drawArc(rectFShort, startAngle - 0.25f, 0.5f, false, innerLinePaint);
            }

        }


        //中间部分
        RectF centerCircle = new RectF(centerX - centerRadiu, centerY - centerRadiu, centerX + centerRadiu, centerY + centerRadiu);
        float v = startAngle + sweepAngle - 360;
        canvas.drawArc(centerCircle, v, startAngle - v, false, centerBottomPaint);
        canvas.drawArc(centerCircle, v + startAngle - v + 10 + idnexSweepAngle / 2 + rotate, 360 - (startAngle - v) - 20 - idnexSweepAngle / 2 - rotate, false, centerTopPaint);
        canvas.drawArc(centerCircle, v - 0.25f - 10, 0.5f, false, innerCircleSidePaint);
        canvas.drawArc(centerCircle, startAngle + 10 - 0.25f, 0.5f, false, innerCircleSidePaint);

        canvas.drawArc(centerCircle, v + startAngle - v + 10, idnexSweepAngle / 2 + rotate, false, indexLinePaint);

        canvas.save();
        canvas.rotate(rotate, centerX, centerY);
        Path path = new Path();
        path.moveTo(centerX - 5, centerY);
        path.lineTo(centerX - 1, centerY - this.radiu);
        path.lineTo(centerX + 1, centerY - this.radiu);
        path.lineTo(centerX + 5, centerY);
        path.close();
        canvas.drawPath(path, indexPaint);
        canvas.restore();


        canvas.drawArc(new RectF(centerX - smallCircleRadiu, centerY - smallCircleRadiu, centerX + smallCircleRadiu, centerY + smallCircleRadiu), 0, 360, false, smallCirclePaint);


    }

    private float rotate;

    public void setCurrent(float index) {

        generatorAnim(rotate, idnexSweepAngle / 180 * (index - 90));

    }

    private void generatorAnim(float start, float end) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
        valueAnimator.addUpdateListener(this);
        valueAnimator.start();
    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        rotate = (float) animation.getAnimatedValue();
        invalidate();
    }
}
