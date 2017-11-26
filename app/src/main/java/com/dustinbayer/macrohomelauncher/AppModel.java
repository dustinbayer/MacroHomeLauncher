package com.dustinbayer.macrohomelauncher;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import java.io.File;

/**
 * Created by dusti on 11/26/2017.
 */

public class AppModel {

    private final MainActivity main;
    private final ApplicationInfo mInfo;
    public ApplicationInfo getAppInfo() { return mInfo; }
    public String getApplicationPackageName() { return getAppInfo().packageName; }

    private String mAppLabel;
    public String getLabel() { return mAppLabel; }

    private Drawable mIcon;
    public Drawable getIcon() {
        if(mIcon != null) {
            return mIcon;
        } else if (mIcon == null) {
            if (mApkFile.exists()) {
                mIcon = mInfo.loadIcon(main.getPackageManager());
                return mIcon;
            } else {
                mMounted = false;
            }
        } else if (!mMounted) {
            // If the app wasn't mounted but is now mounted, reload
            // its icon.
            if (mApkFile.exists()) {
                mMounted = true;
                mIcon = mInfo.loadIcon(main.getPackageManager());
                return mIcon;
            }
        } else {
            return mIcon;
        }

        return main.getResources().getDrawable(android.R.drawable.sym_def_app_icon);
    }
    public void setIcon(Drawable icon) { mIcon = icon; }

    private boolean mMounted;
    private final File mApkFile;

    private AppModel(MainActivity main, ApplicationInfo info) {
        this.main = main;
        mInfo = info;

        if(info.sourceDir != null)
            mApkFile = new File(info.sourceDir);
        else
            mApkFile = null;
    }
    public static AppModel newInstance(MainActivity main, ApplicationInfo info) { return new AppModel(main, info); }

    void loadLabel() {
        if (mAppLabel == null || !mMounted) {
            if (!mApkFile.exists()) {
                mMounted = false;
                mAppLabel = mInfo.packageName;
            } else {
                mMounted = true;
                CharSequence label = mInfo.loadLabel(main.getPackageManager());
                mAppLabel = label != null ? label.toString() : mInfo.packageName;
            }
        }
    }
}
