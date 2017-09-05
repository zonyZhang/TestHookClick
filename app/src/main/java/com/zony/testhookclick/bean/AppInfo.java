package com.zony.testhookclick.bean;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zony on 17-5-18.
 */

public class AppInfo implements Serializable, Parcelable {

    private String appLabel;//应用程序标签
    private Drawable appIcon;//应用程序图像
    private Intent intent;//启动应用程序的Intent，一般是Action为Main和Category为Lancher的Activity
    private String appName;//应用程序所对应的包名
    private String appPath;//包路径
    private int appSize;//包大小

    private String appStyle;//系统应用orsd应用

    public AppInfo() {
    }

    public String getAppStyle() {
        return appStyle;
    }

    public void setAppStyle(String appStyle) {
        this.appStyle = appStyle;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public int getAppSize() {
        return appSize;
    }

    public void setAppSize(int appSize) {
        this.appSize = appSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appLabel);
        Bitmap bitmap = ((BitmapDrawable) appIcon).getBitmap();
        if (bitmap != null) {
            dest.writeParcelable(bitmap, flags);
        } else {
            dest.writeParcelable(null, flags);
        }
        dest.writeParcelable(this.intent, flags);
        dest.writeString(this.appName);
        dest.writeString(this.appPath);
        dest.writeInt(this.appSize);
        dest.writeString(this.appStyle);
    }

    protected AppInfo(Parcel in) {
        this.appLabel = in.readString();
        Bitmap bitmap = in.readParcelable(getClass().getClassLoader());
        if (bitmap != null) {
            appIcon = new BitmapDrawable(bitmap);
        } else {
            appIcon = null;
        }
        this.appIcon = in.readParcelable(Drawable.class.getClassLoader());
        this.intent = in.readParcelable(Intent.class.getClassLoader());
        this.appName = in.readString();
        this.appPath = in.readString();
        this.appSize = in.readInt();
        this.appStyle = in.readString();
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel source) {
            return new AppInfo(source);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    @Override
    public String toString() {
        return "AppInfo{"
                + "appName=" + appName
                + ", appLabel=" + appLabel
                + ", appSize=" + appSize
                + ", appPath=" + appPath
                + "}";
    }
}
