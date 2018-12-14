package com.smart.notebook;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.library.base.BaseAppManager;
import com.library.utils.TLog;
import com.smart.notebook.db.control.AppDbManager;
import com.smart.notebook.utils.DensityHelper;
import com.tencent.smtt.sdk.QbSdk;

import java.util.List;

public class BaseApplication extends Application {
    private final static String TAG = "BaseApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        TLog.d(TAG, "onCreate");
        if (!shouldInit()) {
            TLog.d(TAG, "return for not main process");
            return;
        }

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                TLog.d(TAG, " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
        new DensityHelper(this, 720).activate();
        AppDbManager.init(getApplicationContext());
    }

    @Override
    public void onLowMemory() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onLowMemory();
    }

    public void exitApp() {
        BaseAppManager.getInstance().clear();
        System.gc();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        TLog.d(TAG, "shouldInit myPid:" + myPid);
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
