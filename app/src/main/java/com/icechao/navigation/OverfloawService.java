package com.icechao.navigation;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class OverfloawService extends Service implements View.OnTouchListener, View.OnLongClickListener, ValueAnimator.AnimatorUpdateListener {


    WindowManager.LayoutParams params;
    private LinearLayout view;
    private WindowManager windowManager;
    private int screenWidth, screenHeight, stateBarHeight;
    private ValueAnimator valueAnimator;
    private long animationLong = 100L;
    private int flowWidth = 400, itemHeight = 200;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        FlowCoinManager.getInstance().bindService(this);
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        //Android8.0行为变更，对8.0进行适配https://developer.android.google.cn/about/versions/oreo/android-8.0-changes#o-apps

        //  changed by wangzhaofei at 2019.04.04 周四 解决语音通话进入后台后弹出浮动图标时，程序异常闪退问题
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){//6.0+
//            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        }else {
//            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        }

        Display defaultDisplay = windowManager.getDefaultDisplay();
        screenWidth = defaultDisplay.getWidth();
        screenHeight = defaultDisplay.getHeight();
        stateBarHeight = getStatusBarHeight();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= 24) {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.LEFT | Gravity.TOP;

        //设置悬浮窗口长宽数据.
        params.width = flowWidth;
        //获取浮动窗口视图所在布局.
        view = new LinearLayout(getApplicationContext());
        view.setOrientation(LinearLayout.VERTICAL);
        view.setBackgroundResource(R.drawable.shape_market_flow_bg);
        view.setOnLongClickListener(this);
        view.setOnTouchListener(this);
        //添加toucherlayout
        windowManager.addView(view, params);


        FlowCoinManager.getInstance().addCoinFlow(this, "BTC", "USDT");
        FlowCoinManager.getInstance().addCoinFlow(this, "ETH", "USDT");
        FlowCoinManager.getInstance().addCoinFlow(this, "BTH", "USDT");


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private float innerX = 0, innerY = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                innerX = event.getX();
                innerY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isLongPress) {
                    float moveX = event.getRawX();
                    float moveY = event.getRawY();

                    params.x = (int) (moveX - innerX);
                    params.y = (int) (moveY - innerY) - stateBarHeight;

                    windowManager.updateViewLayout(view, params);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                isLongPress = false;

                if (false) {//移除逻辑

                } else if (params.x + params.width / 2 > screenWidth / 2) {
                    animViewToSide(screenWidth - params.width);
                } else {
                    animViewToSide(0);
                }
                break;

        }


        return isLongPress;

    }

    private void animViewToSide(int i) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(params.x, i);
        valueAnimator.setDuration(animationLong);
        valueAnimator.addUpdateListener(this);
        valueAnimator.start();
    }

    private boolean isLongPress = false;

    @Override
    public boolean onLongClick(View v) {
        isLongPress = true;
        return false;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        params.x = (int) animation.getAnimatedValue();
        windowManager.updateViewLayout(view, params);
    }

    private int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public void addNewFlow(View childView) {
        view.addView(childView);
        resetSize();
    }


    public void removeFlow(View childView) {
        view.removeView(childView);
        resetSize();
    }

    private void resetSize() {
        params.height = view.getChildCount() * itemHeight;
        windowManager.updateViewLayout(view, params);
    }
}
