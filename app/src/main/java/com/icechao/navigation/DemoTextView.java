package com.icechao.navigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.navigation
 * @FileName     : DemoTextView.java
 * @Author       : chao
 * @Date         : 2020/4/9
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
public class DemoTextView extends TextView {
    //
    private Paint leftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint rightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DemoTextView(Context context) {
        super(context);
    }

    public DemoTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        leftPaint.setTextSize(30);
        rightPaint.setTextSize(30);
        centerPaint.setTextSize(30);
        leftPaint.setTextAlign(Paint.Align.LEFT);
        rightPaint.setTextAlign(Paint.Align.RIGHT);
        centerPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawColor(Color.YELLOW);


        canvas.save();
        canvas.clipRect(new Rect(10, 0, 80, canvas.getHeight() / 2));
        canvas.drawColor(Color.BLUE);
        canvas.clipRect(new Rect(10, 0, 80, canvas.getHeight()));
        canvas.restore();

        TextPaint paint = getPaint();
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

//        paint.baselineShift
        float ascent = fontMetrics.ascent;
        paint.setColor(Color.YELLOW);
        canvas.drawLine(0, ascent, getWidth(), ascent, paint);

        float descent = fontMetrics.descent;
        paint.setColor(Color.BLUE);
        canvas.drawLine(0, descent, getWidth(), descent, paint);

        float bottom = fontMetrics.bottom;
        paint.setColor(Color.GREEN);
        canvas.drawLine(0, bottom, getWidth(), bottom, paint);

        float top = fontMetrics.top;
        paint.setColor(Color.GREEN);
        canvas.drawLine(0, top, getWidth(), top, paint);

        float leading = fontMetrics.leading;
        paint.setColor(Color.GREEN);
        canvas.drawLine(0, bottom, getWidth(), bottom, paint);
    }
}


