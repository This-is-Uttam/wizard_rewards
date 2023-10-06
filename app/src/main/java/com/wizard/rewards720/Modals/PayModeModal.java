package com.wizard.rewards720.Modals;

import android.graphics.drawable.Drawable;

public class PayModeModal {
    Drawable appicon;
    String appName;
    String appPackageName;
    boolean isChecked = false;

    public PayModeModal(Drawable appicon, String appName, String appPackageName) {
        this.appicon = appicon;
        this.appName = appName;
        this.appPackageName = appPackageName;
    }

    public Drawable getAppicon() {
        return appicon;
    }

    public void setAppicon(Drawable appicon) {
        this.appicon = appicon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
