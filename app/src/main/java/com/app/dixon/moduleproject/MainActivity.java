package com.app.dixon.moduleproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.app.dixon.base.ARouterConstants;
import com.app.dixon.bus.SendEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 应用层 顶级模块
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);
    }

    public void startToSave(View view) {
        ARouter.getInstance().build(ARouterConstants.SAVE_ACTIVITY).navigation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallback(SendEvent event) {
        ((TextView) findViewById(R.id.tv)).setText("call back");
    }
}
