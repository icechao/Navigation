package com.icechao.navigation;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.Locale;

public class MainActivity extends Activity implements View.OnClickListener, TextToSpeech.OnInitListener {

    private PanelView panelView;
    private CircleProgress circleProgress;
    private HorizontalProgress horzizentalProgress;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        TextView textView = findViewById(R.id.text_view_html);
        textView.setText(null);

        circleProgress = findViewById(R.id.circle_progress);
        horzizentalProgress = findViewById(R.id.progress_horizontal);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                TextView textView = new TextView(getBaseContext());
                textView.setText("position" + position);
                container.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return textView;
            }
        });

        findViewById(R.id.text_view_0).setOnClickListener(this);
        findViewById(R.id.text_view_1).setOnClickListener(this);
        findViewById(R.id.text_view_2).setOnClickListener(this);
        findViewById(R.id.text_view_3).setOnClickListener(this);
        findViewById(R.id.text_view_4).setOnClickListener(this);

        panelView = findViewById(R.id.panel_view);

        textToSpeech = new TextToSpeech(this, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_view_0:

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

                ComponentName cName = new ComponentName("com.huochat.im", "com.huochat.im.activity.SplashActivity");
                Intent intent = new Intent("shareToHuoChat");
                intent.setComponent(cName);
                intent.putExtra("shareData",
                        "{\n" +
                                "\"shareType\":\"1\",\n" +
                                "\"shareTo\":\"1\",\n" +
                                "\"title\":\"分享\",\n" +
                                "\"summary\":\"分享内容\",\n" +
                                "\"imageUrl\":\"http://baidu.com\",\n" +
                                "\"url\":\"http://baidu.com\",\n" +
                                "\"source\":\"其他app\",\n" +
                                "\"sourceIcon\":\"http://baidu.com\"\n" +
                                "}");
                intent.setType("text/plain");
                intent.putExtra("imageBitmap", bitmap);
                startActivity(intent);


                panelView.setCurrent(0.1f);
                circleProgress.setProgress(10);
                horzizentalProgress.setProgress(10);


                if (textToSpeech != null && !textToSpeech.isSpeaking()) {
                    // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                    textToSpeech.setPitch(1.0f);
                    //设定语速 ，默认1.0正常语速
                    textToSpeech.setSpeechRate(1.0f);
                    //朗读，注意这里三个参数的added in API level 4   四个参数的added in API level 21
                    textToSpeech.speak("ATB到账100W!", TextToSpeech.QUEUE_FLUSH, null);
                }

                break;
            case R.id.text_view_1:
                panelView.setCurrent(90);
                circleProgress.setProgress(40);
                horzizentalProgress.setProgress(40);
                break;
            case R.id.text_view_2:
                panelView.setCurrent(180);
                circleProgress.setProgress(50);
                horzizentalProgress.setProgress(50);
                break;
            case R.id.text_view_3:
                panelView.setCurrent(60);
                circleProgress.setProgress(75);
                horzizentalProgress.setProgress(75);
                break;
            case R.id.text_view_4:
                panelView.setCurrent(120);
                circleProgress.setProgress(99);
                horzizentalProgress.setProgress(99);

//无用
//                Dialog dialog = new Dialog(this, R.style.LevelDialog);
//                View inflate = LayoutInflater.from(this).inflate(R.layout.view_dialog_level_logo, null);
//                Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_level_main);
//                animation.setInterpolator(new OvershootInterpolator(2));
//                View relativeLayout = inflate.findViewById(R.id.relative_layout);
//                relativeLayout.startAnimation(animation);
//
//                View imageViewBigBg = inflate.findViewById(R.id.image_view_big_bg);
//                View imageViewSmallBg = inflate.findViewById(R.id.image_view_small_bg);
//
//                animation = AnimationUtils.loadAnimation(this, R.anim.anim_level_big_bg);
//                imageViewBigBg.startAnimation(animation);
//
//                animation = AnimationUtils.loadAnimation(this, R.anim.anim_level_small_bg);
//                imageViewSmallBg.startAnimation(animation);
//                dialog.setContentView(inflate);
////        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                dialog.show();


                break;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(MainActivity.this, "数据丢失/不支持" + result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
