package com.zony.testhookclick.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.zony.testhookclick.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zony on 17-5-18.
 */

public class CommonUtil {

    /**
     * 获取所有apk信息
     *
     * @param
     * @author zony
     * @time 17-5-18 下午5:05
     */
    public static List<AppInfo> getAllApk(Context context) {
        List<AppInfo> appInfoList = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
        List<ResolveInfo> packageInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo reInfo : packageInfos) {
            String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
            String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
            String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
            Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
            ApplicationInfo applicationInfo = reInfo.activityInfo.applicationInfo;
            File file = new File(applicationInfo.sourceDir);
            int flags = applicationInfo.flags;

            // 为应用程序的启动Activity 准备Intent
            Intent launchIntent = new Intent();
            launchIntent.setComponent(new ComponentName(pkgName,
                    activityName));
            // 创建一个AppInfo对象，并赋值
            AppInfo appInfo = new AppInfo();
            appInfo.setAppLabel(appLabel);
            appInfo.setAppName(pkgName);
            appInfo.setAppIcon(icon);
            appInfo.setIntent(launchIntent);
            appInfo.setAppSize(((int) file.length())/1024/1024);
            appInfo.setAppPath(file.getAbsolutePath());
            //判断是否是属于系统的apk
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                appInfo.setAppStyle("SYSTEM APP");
            } else {
                appInfo.setAppStyle("SD APP");
            }
            appInfoList.add(appInfo); // 添加至列表中
        }
        return appInfoList;
    }
}
