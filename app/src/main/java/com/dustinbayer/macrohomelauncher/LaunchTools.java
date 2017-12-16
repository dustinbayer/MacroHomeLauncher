package com.dustinbayer.macrohomelauncher;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dusti on 11/26/2017.
 */

public class LaunchTools {

    private MainActivity main;

    private List<AppModel> launchToolsList;
    public List<AppModel> getLaunchToolsList() { return launchToolsList; }

    private LaunchTools(MainActivity main) {
        this.main = main;
        launchToolsList = new ArrayList<>();

        //Toggle app drawer
        ApplicationInfo appDrawerInfo = new ApplicationInfo();
        appDrawerInfo.packageName =  main.getString(R.string.launchtool_appdrawer);
        AppModel appDrawerTool = AppModel.newInstance(main, appDrawerInfo);
        //appDrawerTool.setIcon(main.getDrawable(R.mipmap.ic_appdrawer_round));
        launchToolsList.add(appDrawerTool);

    }

    public static LaunchTools newInstance(MainActivity main) { return new LaunchTools(main); }

    public boolean runTool(String name) {
        //Toggle app drawer
        if (name.equals(main.getString(R.string.launchtool_appdrawer))) {
            main.getLaunchFragment().toggleAppDrawer();
            return true;
        }

        return false;
    }

    public void cleanUp() {
        main = null;
        launchToolsList.clear();
        launchToolsList = null;
    }
}
