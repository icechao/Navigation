package com.icechao.navigation.kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.huobi.otc.widget
 * @FileName     : SlideButton.java
 * @Author       : chao
 * @Date         : 2020/5/8
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
public class SlideButton : AppCompatTextView {


    var viewPading = 1f;
    var itemMargin = 2f;
    var itemWidth = 0f;
    var backGroundColor = Color.TRANSPARENT
    var itemSelectedColor = Color.BLUE
    var itemSelectedTextColor = Color.BLACK
    var itemNormalTextColor = Color.GRAY
    private var radius = 0f

    var backGroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        backGroundPaint.style = Paint.Style.FILL_AND_STROKE
        backGroundPaint.color = Color.BLUE
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = (h / 2).toFloat();
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.RED)
        canvas.drawArc(RectF(0f, 0f, radius * 2, radius * 2), 90f, 270f, true, backGroundPaint)
    }
}