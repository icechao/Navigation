package com.icechao.navigation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.navigation
 * @FileName     : FlowCoinManager.java
 * @Author       : chao
 * @Date         : 2019-10-30
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
public class FlowCoinManager {

    private OverfloawService service;

    private static FlowCoinManager instance;

    private Map<String, View> symbolViews = new ConcurrentHashMap();
    private Map<String, TextView> priceViews = new ConcurrentHashMap();

    private FlowCoinManager() {
    }

    public static FlowCoinManager getInstance() {
        if (null == instance) {
            synchronized (FlowCoinManager.class) {
                if (null == instance) {
                    instance = new FlowCoinManager();
//                    context.startService(new Intent(context, OverfloawService.class));
                }
            }
        }
        return instance;
    }

    public void bindService(OverfloawService service) {
        this.service = service;
    }


    /**
     * 添加币种浮窗View
     *
     * @param left
     * @param right
     */
    public void addCoinFlow(Context context, String left, String right) {
        if (null == service) {
            context.startService(new Intent(context, OverfloawService.class));
        }
        View view = LayoutInflater.from(service.getApplication()).inflate(R.layout.view_market_flow, null);
        final TextView textViewCoinSymbol = view.findViewById(R.id.text_view_coin_symbol);
        final TextView textViewCoinPrice = view.findViewById(R.id.text_view_coin_price);
        service.addNewFlow(view);
        textViewCoinSymbol.setText(left + "/" + right);

        symbolViews.put(left + right, view);
        priceViews.put(left + right, textViewCoinPrice);

        testChange(view, textViewCoinPrice);

    }

    private void testChange(final View view, final TextView price) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                price.setText(String.format("%.04f", new Random().nextFloat() * 10000));
                testChange(view, price);
            }
        }, 1000);
    }

    /**
     * 删除币种浮窗View
     *
     * @param left
     * @param right
     */
    public void removeCoinFlow(String left, String right) {
        if (null != service) {
            service.removeFlow(symbolViews.get(left + right));
            if (symbolViews.size() == 0) {
                service.stopSelf();
                service = null;
            }
        }
    }

}
