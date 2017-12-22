package com.dustinbayer.macrohomelauncher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Map;

/**
 * Created by dusti on 11/25/2017.
 */

public class LaunchFragment extends Fragment {

    private MainActivity main;
    private View view;

    private DrawerLayout drawerLayout;

    private AppsListFragment appsListFragment;
    public AppsListFragment getAppsListFragment() { return appsListFragment; }

    private MacroLayout macroLayout;
    private View editBlur;
    private boolean editMacro;
    private AppModel editApp;

    public static LaunchFragment newInstance() { return new LaunchFragment(); }

    /**
     * - 3+ combo macros
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }

        main = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_launch, container, false);
        appsListFragment = AppsListFragment.newInstance();
        getChildFragmentManager().beginTransaction().add(R.id.apps_list, appsListFragment).commit();
        editBlur = view.findViewById(R.id.edit_blur);
        editBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBlur.setVisibility(View.GONE);
                editMacro = false;
                editApp = null;
            }
        });

        macroLayout = view.findViewById(R.id.macro_layout);
//        patternView = view.findViewById(R.id.patternView);
//        patternView.setOnPatternCellAddedListener(new PatternView.OnPatternCellAddedListener() {
//            @Override
//            public void onPatternCellAdded() {
//                List<Cell> cellList = patternView.getPattern();
//                cellsAdded++;
//                switch (cellsAdded) {
//                    case 1:
//                        view.findViewById(MacroTools.getCellButtonId(cellList.get(0))).setBackground(ContextCompat.getDrawable(main, R.drawable.circle_white));
//                        break;
//                    case 2:
//                        view.findViewById(MacroTools.getCellButtonId(cellList.get(1))).setBackground(ContextCompat.getDrawable(main, R.drawable.circle_gray));
//                        break;
//                    case 3:
//                        view.findViewById(MacroTools.getCellButtonId(cellList.get(2))).setBackground(ContextCompat.getDrawable(main, R.drawable.circle_black));
//                        break;
//                }
//            }
//        });
//        patternView.setOnPatternDetectedListener(new PatternView.OnPatternDetectedListener() {
//            @Override
//            public void onPatternDetected() {
//                if(patternView.getPattern().size() == MacroTools.MACRO_SIZE) {
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            checkKeys(patternView.getPattern());
//                        }
//                    }, 100);
//                } else {
//                    clearPattern();
//                }
//
//                cellsAdded = 0;
//            }
//        });

        return view;
    }

    public void reloadApps() {
        appsListFragment.cleanUp();
        appsListFragment = AppsListFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.apps_list, appsListFragment).commit();
    }

    private void clearPattern() {
        if(view == null)
            return;

//        for(int i = 0; i < 9; i ++)
//            view.findViewById(MacroTools.getButtonId(i)).setBackground(ContextCompat.getDrawable(main, R.drawable.circle_clear));
        //patternView.clearPattern();
    }

//    private void checkKeys(List<Cell> cells) {
//        if(main.getSharedPref().getBoolean(main.getString(R.string.first_time), true)) {
//            appsListFragment.getAppsListAdapter().setMacroLayout(main.getString(R.string.launchtool_appdrawer), cells);
//            SharedPreferences.Editor editor = main.getSharedPref().edit();
//            editor.putBoolean(main.getString(R.string.first_time), false);
//            editor.putString(editApp.getApplicationPackageName(), MacroTools.cellsToKey(cells));
//            editor.commit();
//            editBlur.setVisibility(View.GONE);
//            clearPattern();
//            editMacro = false;
//            return;
//        }
//
//        String key = MacroTools.cellsToKey(cells);
//        if(cells.size() == MacroTools.MACRO_SIZE) {
//            if (editMacro) {
//                if(!macroExists(key)) {
//                    appsListFragment.getAppsListAdapter().setMacroLayout(editApp.getApplicationPackageName(), cells);
//                    SharedPreferences.Editor editor = main.getSharedPref().edit();
//                    editor.putString(editApp.getApplicationPackageName(), MacroTools.cellsToKey(cells));
//                    editor.commit();
//                    editBlur.setVisibility(View.GONE);
//                    editMacro = false;
//                }
//            } else {
//                openApp(getApp(key));
//            }
//        }
//        clearPattern();
//    }

    public void editMacro(AppModel app) {
        editMacro = true;
        editApp = app;
        editBlur.setVisibility(View.VISIBLE);
    }

    public boolean macroExists(String macro) {
        Map<String,?> appMap = main.getSharedPref().getAll();
        for(String app : appMap.keySet()){
            if(appMap.get(app).equals(macro)){
                return true;
            }
        }
        return false;
    }

    public String getApp(String macro) {
        Map<String,?> appMap = main.getSharedPref().getAll();
        for(String app : appMap.keySet()){
            if(appMap.get(app).equals(macro)){
                return app; //return the first found
            }
        }
        return null;
    }

    public String getMacro(String app) {
        return main.getSharedPref().getString(app, "");
    }

    public boolean openApp(String val) {
        if(val == null || main.getLaunchTools().runTool(val))
            return false;

        Intent intent = main.getPackageManager().getLaunchIntentForPackage(val);

        if (intent != null) {
            main.startActivity(intent);
            return true;
        }
        return false;
    }

    public void cleanUp() {
        main = null;
        view = null;
        if(appsListFragment != null) {
            appsListFragment.cleanUp();
            appsListFragment = null;
        }
        if(macroLayout != null) {
            macroLayout.cleanUp();
            macroLayout = null;
        }
    }
}
