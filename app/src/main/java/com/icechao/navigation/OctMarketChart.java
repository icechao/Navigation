package com.icechao.navigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.huobi.otc.widget
 * @FileName     : OctMarketChart.java
 * @Author       : chao
 * @Date         : 2020/4/24
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
public class OctMarketChart extends FrameLayout {

    private GestureDetector detector;
    private Float[] datas;
    private float itemWidth;
    private ValueAnimator valueAnimator;

    public OctMarketChart(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public OctMarketChart(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public OctMarketChart(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OctMarketChart(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        setWillNotDraw(false);
        detector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                showSelected = true;
                int index = xToIndex(e.getX());
                if (selectedIndex != index) {
                    if (null != selectedListener) {
                        selectedListener.onSelectedChange(index, datas[index * 2 + 1]);
                    }
                    selectedIndex = index;
                }
                invalidate();
            }
        });
//        detector.setIsLongpressEnabled(true);
        gridPaint.setColor(Color.RED);
        gridPaint.setStrokeWidth(2);
        chartLinePaint.setColor(Color.GREEN);
        chartLinePaint.setStrokeWidth(5);
        chartLinePaint.setStyle(Paint.Style.STROKE);
        chartFillPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        chartEndCircleFillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        chartEndCircleStrokePaint.setStyle(Paint.Style.STROKE);

        chartEndCircleFillPaint.setColor(Color.BLUE);
        chartEndCircleStrokePaint.setColor(Color.YELLOW);

        yLabelPaint.setTextSize(16);
        xLabelPaint.setTextSize(16);

        selectedLinePaint.setStrokeWidth(2);
        selectedLinePaint.setColor(Color.YELLOW);

        selectedTextPaint.setColor(Color.RED);
        selectedLinePaint.setTextSize(16);
        selectedTextPaint.setTextAlign(Paint.Align.CENTER);

        selectedCrossCirclePaint.setColor(Color.WHITE);
        selectedCrossCircleBorderPaint.setStyle(Paint.Style.STROKE);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initChartRect(w, h);
        setDatas();
    }

    private Rect chartRect;

    private void initChartRect(int w, int h) {
        chartRect = new Rect(0, 0, w - 100, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        renderBackGround(canvas);
        renderGrid(canvas);
        renderChart(canvas);
        renderYLabel(canvas);
        renderXLabel(canvas);
        renderSelected(canvas);
    }

    private Paint selectedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint selectedTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float selectedTextMarging = 10;
    private float selectedLineMargingBottom = 40;
    private float selectedTextMarginTop = 40;
    private float selectedCrossCircleRadius = 10;
    private Paint selectedCrossCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint selectedCrossCircleBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private void renderSelected(Canvas canvas) {
        if (showSelected) {
            if (selectedIndex >= 0) {
                if (selectedIndex >= datas.length / 2) {
                    selectedIndex = datas.length / 2 - 1;
                }
                float x = itemWidth * selectedIndex * 2;
                String text = selectedDateFromater(datas[selectedIndex * 2].longValue());
                Paint.FontMetrics fontMetrics = selectedTextPaint.getFontMetrics();
                float y = fontMetrics.bottom - fontMetrics.top;
                float halfTextWidth = selectedTextPaint.measureText(text) / 2;

                canvas.drawLine(x, y + selectedTextMarginTop, x, chartRect.height() - selectedLineMargingBottom, selectedLinePaint);
                canvas.drawCircle(x, valueToY(datas[selectedIndex * 2 + 1]), selectedCrossCircleRadius, selectedCrossCirclePaint);
                canvas.drawCircle(x, valueToY(datas[selectedIndex * 2 + 1]), selectedCrossCircleRadius, selectedCrossCircleBorderPaint);

                if (x < halfTextWidth + selectedTextMarging) {
                    x = halfTextWidth + selectedTextMarging;
                } else {
                    float maxX = chartRect.width() - halfTextWidth - selectedTextMarging;
                    if (x > maxX) {
                        x = maxX;
                    }
                }
                canvas.drawText(text, x, fitTextY(y, selectedTextPaint) + selectedTextMarginTop, selectedTextPaint);
            }
        }
    }

    private Paint xLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float xLabelMarginBottom = 10;

    private void renderXLabel(Canvas canvas) {
        xLabelPaint.setTextAlign(Paint.Align.CENTER);
        float width = canvas.getWidth();
        float y = fitTextY(canvas.getHeight(), xLabelPaint) - xLabelMarginBottom;
        float space = width / (gridColumnCount - 1);
        for (int i = 0; i < gridColumnCount; i++) {
            float x = i * space + chartRect.left;
            int index = xToIndex(x);
            if (index < datas.length / 2) {
                String label = formatterDate(datas[index * 2].longValue());
                canvas.drawText(label, x, y, xLabelPaint);
            }
        }
    }

    private int xToIndex(float x) {
        return (int) (x / itemWidth / 2 + 0.5f);
    }

    private Paint yLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float yLabelMarginRight = 10;

    private void renderYLabel(Canvas canvas) {
        if (gridRowCount > 1) {
            float space = (chartMax - chartMin) / (gridRowCount - 1);
            for (int i = 1; i < gridRowCount - 1; i++) {
                float v = space * i;
                float value = chartMin + v;
                String labelText = formatterValue(value);
                float chartY = valueToY(value) + chartRect.top;
                float textWidth = yLabelPaint.measureText(labelText);
                canvas.drawText(labelText, canvas.getWidth() - textWidth - yLabelMarginRight, fitTextY(chartY, yLabelPaint), yLabelPaint);
            }
        }
    }

    private float fitTextY(float y, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return y + (fontMetrics.descent + fontMetrics.ascent) / 2;

    }

    private Paint chartFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint chartLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float chartEndPointRadius = 10;
    private float chartEndPointLampMaxRadius = 100;
    private float chartEndPointLampRadius = 10;
    private Paint chartEndCircleFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint chartEndCircleStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint chartEndLampPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private void renderChart(Canvas canvas) {
        float width = chartRect.width();
        float height = chartRect.height();
        LinearGradient linearGradient = new LinearGradient(0, 0, 0, height, Color.parseColor("#6533eebb"), Color.parseColor("#65336678"), Shader.TileMode.CLAMP);
        chartFillPaint.setShader(linearGradient);
        int length = datas.length;
        itemWidth = width / (length - 2f);
        Path fillPath = new Path();
        Path linePath = new Path();
        fillPath.moveTo(0, height);
        for (int i = 0; i < length; i += 2) {
            float x = i * itemWidth;
            float y = valueToY(datas[i + 1]);
            fillPath.lineTo(x, y);
            if (0 == i) {
                linePath.moveTo(x, y);
            } else {
                linePath.lineTo(x, y);
            }
        }
        fillPath.lineTo(width, height);
        canvas.drawPath(fillPath, chartFillPaint);
        chartLinePaint.setPathEffect(new CornerPathEffect(3));
        canvas.drawPath(linePath, chartLinePaint);

        int cx = chartRect.left + chartRect.width();
        float cy = valueToY(datas[datas.length - 1]);

        chartEndLampPaint.setShader(new RadialGradient(cx, cy, chartEndPointLampRadius, Color.RED, Color.TRANSPARENT, Shader.TileMode.CLAMP));
        canvas.drawCircle(cx, cy, chartEndPointLampRadius, chartEndLampPaint);
        canvas.drawCircle(cx, cy, chartEndPointRadius, chartEndCircleFillPaint);
        canvas.drawCircle(cx, cy, chartEndPointRadius, chartEndCircleStrokePaint);
        if (null == valueAnimator || !valueAnimator.isRunning()) {
            valueAnimator = ValueAnimator.ofFloat(chartEndPointRadius, chartEndPointLampMaxRadius);
            valueAnimator.setRepeatCount(1000);
            valueAnimator.setDuration(500);
            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    chartEndPointLampRadius = (float) animation.getAnimatedValue();

                    invalidate();
                }
            });
            valueAnimator.start();
        }

    }

    private float valueToY(float value) {
        return (float) ((chartMax - value) * charScaleY);
    }


    private void calculateValues() {
        int length = datas.length;
    }

    private Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int gridRowCount = 5, gridColumnCount = 5;

    private void renderGrid(Canvas canvas) {

        float strokeWidth = gridPaint.getStrokeWidth();
        float width = canvas.getWidth() - strokeWidth;
        float height = canvas.getHeight() - strokeWidth;

        float rowSpace = height / (gridRowCount - 1);
        float columnSpace = width / (gridColumnCount - 1);
        float halfWidth = gridPaint.getStrokeWidth() / 2;
        canvas.drawLine(0, halfWidth, width, halfWidth, gridPaint);
        for (int i = 1; i < gridRowCount - 1; i++) {
            float y = rowSpace * i;
            canvas.drawLine(0, y, width, y, gridPaint);
        }
        canvas.drawLine(0, height - halfWidth, width, height - halfWidth, gridPaint);
        canvas.drawLine(halfWidth, 0, halfWidth, height, gridPaint);
        for (int i = 1; i < gridColumnCount - 1; i++) {
            float x = columnSpace * i;
            canvas.drawLine(x, 0, x, height, gridPaint);
        }
        canvas.drawLine(width - halfWidth, 0, width - halfWidth, height, gridPaint);


    }

    private void renderBackGround(Canvas canvas) {

    }

    private double charScaleY;
    private float maxValue;
    private float minValue;
    private int maxIndex;
    private int minIndex;

    private float chartMax;
    private float chartMin;

    public void setDatas() {

        chartMax = Float.MIN_VALUE;
        chartMin = Float.MAX_VALUE;
        maxValue = Float.MIN_VALUE;
        minValue = Float.MAX_VALUE;

        datas = new Float[120];
        for (int i = 0; i < 120; i += 2) {
            datas[i] = 1563638400 + i * 60f * 1000;
            float random = (float) (new Random().nextFloat() * 100 - new Random().nextDouble() * 100);

            float value = 10000 + random;
            datas[i + 1] = value;
            if (value > maxValue) {
                maxValue = value;
                maxIndex = i;
            }
            if (value < minValue) {
                minValue = value;
                minIndex = i;
            }
        }
        chartMax = maxValue * 1.1f;
        chartMin = minValue * 0.9f;
        charScaleY = chartRect.height() * 1f / (chartMax - chartMin);
    }


    private String formatterValue(float value) {
        return String.format("%.02f", value);
    }


    private String formatterDate(long date) {
        return new SimpleDateFormat("hh:mm ss").format(new Date(date));
    }


    private String selectedDateFromater(long date) {
        return new SimpleDateFormat("hh:mm yyyy/MM/dd").format(new Date(date));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        if (!detector.onTouchEvent(event)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if (showSelected) {
                        int selectedIndex = xToIndex(event.getX());
                        if (this.selectedIndex != selectedIndex) {
                            if (null != selectedListener) {
                                selectedListener.onSelectedChange(selectedIndex, datas[selectedIndex * 2 + 1]);
                            }
                            this.selectedIndex = selectedIndex;
                        }
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    showSelected = false;
                    selectedIndex = -1;
                    invalidate();
                    break;
            }
        }
        Log.e("tag", " getAction : " + event.getAction());
        return true;
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    private boolean showSelected;
    private int selectedIndex;


    private SelectedListener selectedListener;



    private interface SelectedListener {
        void onSelectedChange(int index, float value);
    }


    public void setSelectedTextMarging(float selectedTextMarging) {
        this.selectedTextMarging = selectedTextMarging;
    }

    public void setSelectedLineMargingBottom(float selectedLineMargingBottom) {
        this.selectedLineMargingBottom = selectedLineMargingBottom;
    }

    public void setSelectedTextMarginTop(float selectedTextMarginTop) {
        this.selectedTextMarginTop = selectedTextMarginTop;
    }

    public void setSelectedCrossCircleRadius(float selectedCrossCircleRadius) {
        this.selectedCrossCircleRadius = selectedCrossCircleRadius;
    }

    public void setxLabelMarginBottom(float xLabelMarginBottom) {
        this.xLabelMarginBottom = xLabelMarginBottom;
    }

    public void setyLabelMarginRight(float yLabelMarginRight) {
        this.yLabelMarginRight = yLabelMarginRight;
    }

    public void setChartEndPointRadius(float chartEndPointRadius) {
        this.chartEndPointRadius = chartEndPointRadius;
    }

    public void setChartEndPointLampMaxRadius(float chartEndPointLampMaxRadius) {
        this.chartEndPointLampMaxRadius = chartEndPointLampMaxRadius;
    }

    public void setChartEndPointLampRadius(float chartEndPointLampRadius) {
        this.chartEndPointLampRadius = chartEndPointLampRadius;
    }

    public void setGridRowCount(int gridRowCount) {
        this.gridRowCount = gridRowCount;
    }

    public void setGridColumnCount(int gridColumnCount) {
        this.gridColumnCount = gridColumnCount;
    }

    public void setSelectedListener(SelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }
}
