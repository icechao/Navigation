package com.icechao.navigation;

import com.didichuxing.doraemonkit.DoraemonKit;

public class  Application  extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DoraemonKit.install(this,"pId");
    }
}
