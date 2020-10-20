package com.icechao.navigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.navigation
 * @FileName     : HorizentalProgress.java
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
public class HorizentalProgress extends View {

    private Paint leftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint rightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int progress = -1;

    private int pointCount = 5;
    private float pointRadiu = 20;
    private int width;
    private float centerY;
    private float animProgress = -1;

    public void setProgressLineWidth(float width) {
        leftPaint.setStrokeWidth(width);
        rightPaint.setStrokeWidth(width);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (animProgress != progress) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(animProgress, progress);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    animProgress = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.setDuration(500);
            valueAnimator.start();
        }
    }

    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }

    public void setPointRadiu(float pointRadiu) {
        this.pointRadiu = pointRadiu;
    }

    public HorizentalProgress(Context context) {
        super(context);
        initView(context);
    }

    public HorizentalProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HorizentalProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HorizentalProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        setWillNotDraw(false);
        setProgressLineWidth(10);
        setBackgroundColor(Color.GREEN);
        setProgressColor(Color.BLUE);
    }

    @Override
    public void setBackgroundColor(int color) {
        rightPaint.setColor(color);
    }

    public void setProgressColor(int color) {
        leftPaint.setColor(color);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        centerY = getMeasuredHeight() / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = this.width - 2 * pointRadiu;
        float tempWidth = (width / (pointCount - 1));
        if (progress >= 0 && progress <= 100) {
            float progressWidht = (int) (animProgress * width / 100);
            //底部线绘制
            canvas.drawLine(pointRadiu, centerY, pointRadiu + progressWidht, centerY, leftPaint);
            canvas.drawLine(progressWidht, centerY, this.width - pointRadiu, centerY, rightPaint);

            //绘制点
            canvas.drawCircle(pointRadiu, centerY, pointRadiu, leftPaint);
            for (int i = 1; i < pointCount; i++) {
                if (progressWidht >= tempWidth * i) {
                    canvas.drawCircle(pointRadiu + tempWidth * i, centerY, pointRadiu, leftPaint);
                } else {
                    canvas.drawCircle(pointRadiu + tempWidth * i, centerY, pointRadiu, rightPaint);
                }
            }
        } else {
            canvas.drawLine(pointRadiu, centerY, this.width - pointRadiu, centerY, rightPaint);
            for (int i = 0; i < pointCount; i++) {
                canvas.drawCircle(pointRadiu + tempWidth * i, centerY, pointRadiu, rightPaint);
            }
        }

    }

}
