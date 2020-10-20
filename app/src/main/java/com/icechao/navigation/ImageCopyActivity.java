package com.icechao.navigation;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.didichuxing.doraemonkit.DoraemonKit;

public class ImageCopyActivity extends Activity {

    private Bitmap bitmap;
    private LinearLayout linearLayout;
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_copy);
//        linearLayout.setDrawingCacheEnabled(true);
//        linearLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        linearLayout.layout(0, 0, linearLayout.getMeasuredWidth(), linearLayout.getMeasuredHeight());
//        linearLayout.buildDrawingCache();
//        bitmap = Bitmap.createBitmap(linearLayout.getDrawingCache());
//        linearLayout.setDrawingCacheEnabled(false);
//        linearLayout.setGravity(Gravity.CENTER);
//        linearLayout1 = findViewById(R.id.linear_layout_1);
//        linearLayout2 = findViewById(R.id.linear_layout_2);

//        try {
//            ViewUtil.saveView(this, linearLayout, Environment.getDownloadCacheDirectory() + "/1.jgp");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        DoraemonKit.show();
    }

    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
//                    .WRITE_EXTERNAL_STORAGE)) {
//                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
//            }
//            //申请权限
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//
//        } else {
//
//            Bitmap bitmap = ViewUtil.compressImage(ViewUtil.getViewBitmap(linearLayout), Bitmap.CompressFormat.JPEG, 1000);//截取View的屏幕大小并压缩图片
//            ViewUtil.saveImage(this, Environment.getExternalStorageDirectory() + "/1.jpg", bitmap, Bitmap.CompressFormat.JPEG);//把获取到的Bitmap对象压缩图片保存到本地中sdcard
////            imageView.setImageBitmap(bitmap);//在把获取到的Bitmap对象在imageView中显示出来
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        getWindow().getDecorView().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                checkPermission();
//            }
//        }, 5000);
//        requestOverlayPermission();
    }

    // 动态请求悬浮窗权限
    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                String ACTION_MANAGE_OVERLAY_PERMISSION = "android.settings.action.MANAGE_OVERLAY_PERMISSION";
                Intent intent = new Intent(ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));

                startActivityForResult(intent, 555);
            } else {
                startService(new Intent(this, OverfloawService.class));
            }
        }
    }

    /**
     * Activity执行结果，回调函数
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 555) {        // 从应用权限设置界面返回
            if (resultCode == RESULT_OK) {
                // 设置标识为可显示悬浮窗
                startService(new Intent(this, OverfloawService.class));
            } else {
                Toast.makeText(this, "请开启浮窗权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
