package com.icechao.navigation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.navigation
 * @FileName     : ViewUtil.java
 * @Author       : chao
 * @Date         : 2019-10-28
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
public class ViewUtil {

    /**
     * 截取scrollview的屏幕
     *
     * @param viewGroup
     * @return
     */
    public static Bitmap getViewBitmap(ViewGroup viewGroup) {
        int h = 0;
        Bitmap bitmap = null;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            h += viewGroup.getChildAt(i).getHeight();//获取scrollView的屏幕高度
            viewGroup.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"));
        }
        //如果传的参数不是NestedScrollView，则不需要循环遍历高度
//        h += viewGroup.getHeight();//获取scrollView的屏幕高度
//        viewGroup.setBackgroundColor(
//                    Color.parseColor("#ffffff"));
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(viewGroup.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);//把创建的bitmap放到画布中去
        viewGroup.draw(canvas);//绘制canvas 画布
        return bitmap;
    }

    /**
     * 压缩图片
     *
     * @param image
     * @param comressType
     * @param imageSize
     * @return
     */
    public static Bitmap compressImage(Bitmap image, Bitmap.CompressFormat comressType, int imageSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(comressType, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > imageSize) {
            // 重置baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(comressType, options, baos);
            // 每次都减少10
            options -= 10;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * 保存到sdcard并返回路径地址
     *
     * @param activity
     * @param fileName
     * @param bitmap
     * @param compressType
     */
    public static void saveImage(Activity activity, String fileName, Bitmap bitmap, Bitmap.CompressFormat compressType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",
                Locale.US);
        File file = Environment.getExternalStorageDirectory();
        // 如果文件不存在，则创建一个新文件
        if (!file.isDirectory()) {
            try {
                file.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File outFile = new File(fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outFile);//获取FileOutputStream对象
            /**
             * 压缩图片
             * 第一个参数：要压缩成的图片格式
             * 第二个参数：压缩率
             * 第三个参数：压缩到指定位置
             */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(compressType, 100, baos);
            fos.write(baos.toByteArray());
            Toast.makeText(activity, "保存成功", Toast.LENGTH_SHORT).show();
            //通知图库更新
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outFile));
            activity.sendBroadcast(intent);
            fos.flush();
            fos.close();//最后关闭此文件输出流并释放与此流相关联的任何系统资源。

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            fos = null;
        } catch (IOException e) {
            e.printStackTrace();
            fos = null;
        }
    }
}

