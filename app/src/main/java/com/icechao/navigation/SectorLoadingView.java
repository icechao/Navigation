package com.icechao.navigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.SweepGradient;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.navigation
 * @FileName     : SectorLoadingView.java
 * @Author       : chao
 * @Date         : 2020/6/17
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
public class SectorLoadingView extends android.support.v7.widget.AppCompatTextView implements ValueAnimator.AnimatorUpdateListener {

    private int width;
    private int height;

    public SectorLoadingView(Context context) {
        super(context);
    }

    public SectorLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SectorLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setDuration(3000);
        animator.setRepeatCount(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(this);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        TextPaint paint = getPaint();
        SweepGradient shader = new SweepGradient(width / 2, height / 2,
                new int[]{Color.TRANSPARENT, Color.BLUE},
                new float[]{
                        0f, 1f
                });
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, width / 2, height / 2);
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
        canvas.drawCircle(width / 2, height / 2, width / 2, paint);
    }

    private float degrees;

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        degrees = (float) animation.getAnimatedValue();
        invalidate();
    }
}
