package com.app.dixon.save;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.app.dixon.base.ARouterConstants;
import com.app.dixon.base.GetActInterface;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

/**
 * 全路径：com.app.dixon.save
 * 类描述：
 * 创建人：dixon.xu
 * 创建时间：2019/5/29 7:21 PM
 */

//无差别拦截 启动别的模块也会调用 所以应该判断调用链
//这里仍然推荐将 Interceptor 放在子 module 中，这样当 module 移除时，对应的拦截器也将移除。带来的问题是：
//1.拦截器顺序带来的影响：数字越小越先执行，且多个拦截器会依次执行（onContinue）。只要保证子Interceptor优先级可高于或低于BaseInterceptor即可，即Base要位于中位。
//2.拦截器优先级相同带来的影响：相同会启动（ARouter初始化）Crash。
//3.多模块可能使用相同拦截器：Base提供一个通用拦截器但不[注解]调用，由子Module继承调用，这样子Module移除时对应拦截器也会移除。（记得一定判断路由 ）

//测试 无差别拦截（判断路由）、priority（不能相同）  权限使用（AndPermission）
@Interceptor(priority = 100, name = "save interceptor")
public class SaveInterceptor implements IInterceptor {

    private Context context;

    @Override
    public void process(final Postcard postcard, final InterceptorCallback callback) {
        Log.e("Interceptor", "拦截...");

        //当前在子线程 不能直接弹窗

        //无需拦截 直接跳转
        if (!postcard.getPath().equals(ARouterConstants.SAVE_ACTIVITY)) {
            callback.onContinue(postcard);
            return;
        }

        AndPermission.with(context)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        callback.onContinue(postcard);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        callback.onInterrupt(new RuntimeException("你拒绝了跳转"));
                    }
                })
                .rationale(new Rationale() {
                    @Override
                    public void showRationale(final Context context, List<String> permissions, final RequestExecutor executor) {

                        Log.e("Interceptor", Thread.currentThread().getName());
                        //回到主线程弹窗才能显示
                        ((GetActInterface) context).getTopActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(((GetActInterface) context).getTopActivity());
                                builder.setTitle("title")
                                        .setMessage("message")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                executor.execute();
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                callback.onInterrupt(new RuntimeException("你拒绝了跳转"));
                                            }
                                        });
                                builder.create().show();
                            }
                        });

                    }
                })
                .start();

        Log.e("Interceptor", "拦截结束");
    }

    @Override
    public void init(Context context) {
        //context 是 application 对象，不能用来弹窗
        this.context = context;
    }
}
