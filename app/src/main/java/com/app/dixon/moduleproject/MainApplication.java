package com.app.dixon.moduleproject;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.app.dixon.base.BaseApplication;
import com.app.dixon.base.GetActInterface;

/**
 * 全路径：com.app.dixon.moduleproject
 * 类描述：
 * 创建人：dixon.xu
 * 创建时间：2019/5/29 6:06 PM
 */

public class MainApplication extends BaseApplication implements GetActInterface {

    private static Activity sTopActivity;

    @Override
    public void onCreate() {
        super.onCreate();

//        if (isDebug()) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
//            ARouter.openLog();     // Print log
//            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
//        }
        //路由初始化
        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                sTopActivity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                //根据生命周期 A.resume > B.destroy 所以不会导致内存泄漏
                sTopActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    @Override
    public Activity getTopActivity() {
        return sTopActivity;
    }
}
