package com.icechao.navigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.navigation
 * @FileName     : Navigation.java
 * @Author       : chao
 * @Date         : 2019-07-30
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class Navigation extends View implements ViewPager.OnPageChangeListener {

    private boolean navigationFitX;
    private int selectItem, lastSelect = -1;

    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context context;

    public Navigation(Context context) {
        super(context);
        this.context = context;
        initView(null);
    }

    public Navigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }

    public Navigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Navigation(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initView(attrs);
    }


    private int navigationCount = 4;
    private float[] itemWidths;
    private String[] itemTexts;
    private int[] itemImagesNormal;
    private int[] itemImagesSelected;
    private Paint[] itemImagePaints;


    private void initView(AttributeSet attrs) {
        setWillNotDraw(false);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.Navigation);
        int count = array.getInteger(R.styleable.Navigation_navigationCount, -1);
        setNavigationCount(count);
        setFitX(array.getBoolean(R.styleable.Navigation_navigationFit, true));
        String[] textItems = context.getResources().getStringArray(array.getResourceId(R.styleable.Navigation_navigationTextItem, 0));
        int[] imageItemsNormal = context.getResources().getIntArray(array.getResourceId(R.styleable.Navigation_navigationImagesNormal, 0));
        int[] imageItemsSelected = context.getResources().getIntArray(array.getResourceId(R.styleable.Navigation_navigationImagesSelected, 0));
        setTextSize(array.getDimension(R.styleable.Navigation_navigationTextSize, 30));
        setTextColor(array.getColor(R.styleable.Navigation_navigationTextColor, Color.DKGRAY));
        setItemImageHeight(array.getDimension(R.styleable.Navigation_navigationImageHeight, 80));
        if (textItems.length > 0) {
            setItemText(textItems);
        }
        if (imageItemsNormal.length > 0) {
            setItemImagesNormal(imageItemsNormal, imageItemsSelected);
        }
        textPaint.setTextAlign(Paint.Align.CENTER);
        array.recycle();
    }


    public void setTextColor(int color) {
        textPaint.setColor(color);
    }

    public void setItemImageHeight(float height) {
        this.itemImageHeight = height;
    }

    public void setItemImagesNormal(int[] imageItemsNormal, int[] imageItemsSelected) {
        this.itemImagesNormal = imageItemsNormal;
        this.itemImagesSelected = imageItemsSelected;
        int length = imageItemsNormal.length;
        itemImagePaints = new Paint[length];
        for (int i = 0; i < length; i++) {
            itemImagePaints[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
    }

    public void setItemText(String[] itemTexts) {
        this.itemTexts = itemTexts;
    }

    public void setFitX(boolean fitX) {
        navigationFitX = fitX;
    }


    public void setNavigationCount(int count) {
        this.navigationCount = count;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (navigationFitX) {
            float temp = (float) (w / navigationCount);
            itemWidths = new float[navigationCount];
            for (int i = 0; i < navigationCount; i++) {
                itemWidths[i] = temp;
            }
        }
        this.viewHeight = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        try {
            if (navigationCount <= 0 || null == itemWidths || itemImagesNormal.length != itemImagesSelected.length) {
                return;
            }
            float sumWidth = 0, itemXCenter;
            if (pading == 0) {
                pading = (viewHeight - textHeight - itemImageHeight) / 2;
            }
            for (int i = 0; i < navigationCount; i++) {
                itemXCenter = (sumWidth * 2 + itemWidths[i]) / 2;
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), selectItem == i ? itemImagesSelected[i] : itemImagesNormal[i]);
                int height = bitmap.getHeight();
                float halfImageWidth = itemImageHeight / height * bitmap.getWidth() / 2;
                canvas.drawBitmap(bitmap, null, new RectF(itemXCenter - halfImageWidth, pading, itemXCenter + halfImageWidth, pading + itemImageHeight), itemImagePaints[i]);
                canvas.drawText(itemTexts[i], itemXCenter, fixTextYBaseBottom(pading + itemImageHeight) + textHeight / 2, textPaint);
                sumWidth += itemWidths[i];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修复text位置
     */
    protected float fixTextYBaseBottom(float y) {
        return y + (textHeight) / 2 - textDecent;
    }

    private float pading, viewHeight, textHeight, textDecent, itemImageHeight;

    /**
     * 统一设置设置文字大小
     */
    public void setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        textHeight = fm.descent - fm.ascent;
        textDecent = fm.descent;
    }


    public void bindViewPager(ViewPager viewPager) {

        viewPager.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        //position 当前所在页面
        //positionOffset 当前所在页面偏移百分比
        //positionOffsetPixels 当前所在页面偏移量
        if (v > 0) {
            if (v > 0.5) {
                itemImagePaints[i].setAlpha((int) (255 - 255 * v * 2));
                itemImagePaints[i + 1].setAlpha((int) (255 * v));
                lastSelect = selectItem;
                selectItem = i + 1;
            } else {
                itemImagePaints[i].setAlpha((int) (255 * v * 2));
                itemImagePaints[i + 1].setAlpha((int) (255 - 255 * v * 2));
                lastSelect = selectItem;
                selectItem = i;
            }
        }
    }

    @Override
    public void onPageSelected(int i) {
        if (i != selectItem) {
            lastSelect = selectItem;
            selectItem = i;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    public interface OnSelectedChange {
        /**
         * 选中变化
         *
         * @param newSelected 新选中
         * @param oldSelected 老选中,第一次调用些值为-1
         */
        void onSelectedChange(int newSelected, int oldSelected);
    }
}
