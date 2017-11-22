package com.dustinbayer.macrohomelauncher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by dusti on 11/20/2017.
 */

public class AppModel {

    private final Context mContext;
    private final ApplicationInfo mInfo;

    private String mAppLabel;
    public Drawable mIcon;

    private boolean mMounted;
    private final File mApkFile;

    private String macro = "***";

    public AppModel(Context context, ApplicationInfo info) {
        mContext = context;
        mInfo = info;

        if(info.sourceDir != null)
            mApkFile = new File(info.sourceDir);
        else
            mApkFile = null;
    }

    public ApplicationInfo getAppInfo() {
        return mInfo;
    }

    public String getApplicationPackageName() {
        return getAppInfo().packageName;
    }

    public String getLabel() {
        return mAppLabel;
    }

    public Drawable getIcon() {
        if(mIcon != null) {
            return mIcon;
        } else if (mIcon == null) {
            if (mApkFile.exists()) {
                mIcon = mInfo.loadIcon(mContext.getPackageManager());
                return mIcon;
            } else {
                mMounted = false;
            }
        } else if (!mMounted) {
            // If the app wasn't mounted but is now mounted, reload
            // its icon.
            if (mApkFile.exists()) {
                mMounted = true;
                mIcon = mInfo.loadIcon(mContext.getPackageManager());
                return mIcon;
            }
        } else {
            return mIcon;
        }

        return mContext.getResources().getDrawable(android.R.drawable.sym_def_app_icon);
    }


    void loadLabel(Context context) {
        if (mAppLabel == null || !mMounted) {
            if (!mApkFile.exists()) {
                mMounted = false;
                mAppLabel = mInfo.packageName;
            } else {
                mMounted = true;
                CharSequence label = mInfo.loadLabel(context.getPackageManager());
                mAppLabel = label != null ? label.toString() : mInfo.packageName;
            }
        }
    }

    public void setMacro(String val) {
        macro = val;
    }

    public String getMacro() {
        return macro;
    }
}
