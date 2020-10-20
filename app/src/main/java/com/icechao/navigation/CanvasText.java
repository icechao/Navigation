package com.icechao.navigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;


/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.navigation
 * @FileName     : CanvasText.java
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
public class CanvasText extends TextView {

    private int width;
    private int height;

    public CanvasText(Context context) {
        super(context);
    }

    public CanvasText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CanvasText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(0, 0, width, height);
        Paint paint = new Paint();
        paint.setAlpha(10);
        paint.setColor(Color.GREEN);
        canvas.drawRect(rectF, paint);

        canvas.save();
        canvas.scale(0.5f, 0.5f, getWidth() / 2, getHeight() / 2);
        paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(rectF, paint);
        canvas.restore();

    }
}
