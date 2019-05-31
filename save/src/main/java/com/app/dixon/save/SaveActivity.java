package com.app.dixon.save;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.app.dixon.base.ARouterConstants;
import com.app.dixon.bus.SendEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 子模块 组件层 中间模块
 */

@Route(path = ARouterConstants.SAVE_ACTIVITY)
public class SaveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        EventBus.getDefault().post(new SendEvent());
    }
}
