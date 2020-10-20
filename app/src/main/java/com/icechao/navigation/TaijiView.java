package com.icechao.navigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;


/*************************************************************************
 * Description   : 太极图
 *
 * @PackageName  : com.icechao.navigation
 * @FileName     : TaijiView.java
 * @Author       : chao
 * @Date         : 2020/6/18
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
public class TaijiView extends androidx.appcompat.widget.AppCompatTextView {

    private float jiPointRadius = 20;

    private Paint yinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint yangPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path yinPath, yangPath;
    private float centerX;
    private float radius;
    private float top;
    private float bottom;

    public TaijiView(Context context) {
        super(context);
    }

    public TaijiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        yinPaint.setColor(Color.BLACK);
        yangPaint.setColor(Color.WHITE);

    }

    public TaijiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int left = getPaddingLeft();
        top = getPaddingTop();
        int right = w - getPaddingRight();
        bottom = h - getPaddingBottom();
        centerX = w / 2;
        float centerY = h / 2;
        if (w > h) {
            radius = h / 4;
        } else {
            radius = w / 4;
        }
        yangPath = new Path();
        yangPath.moveTo(centerX, top);
        yangPath.arcTo(new RectF(left, top, right, bottom), -90, 180);
        yangPath.arcTo(new RectF(centerX - radius, centerY, centerX + radius, bottom), 90, 180);
        yangPath.arcTo(new RectF(centerX - radius, top, centerX + radius, centerY), 90, -180);
        yangPath.close();
        yinPath = new Path();
        yinPath.moveTo(centerX, bottom);
        yinPath.arcTo(new RectF(left, top, right, bottom), 90, 180);
        yinPath.arcTo(new RectF(centerX - radius, top, centerX + radius, centerY), -90, 180);
        yinPath.arcTo(new RectF(centerX - radius, centerY, centerX + radius, bottom), 270, -180);
        yinPath.close();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(yinPath, yinPaint);
        canvas.drawPath(yangPath, yangPaint);
        canvas.drawCircle(centerX, top + radius, jiPointRadius, yangPaint);
        canvas.drawCircle(centerX, bottom - radius, jiPointRadius, yinPaint);
    }
}
