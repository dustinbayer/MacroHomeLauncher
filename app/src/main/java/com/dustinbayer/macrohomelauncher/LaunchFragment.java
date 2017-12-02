package com.dustinbayer.macrohomelauncher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.eftimoff.patternview.PatternView;
import com.eftimoff.patternview.cells.Cell;

import java.util.List;
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

    private PatternView patternView;
    private int cellsAdded = 0;
    private View editBlur;
    private boolean editMacro;
    private AppModel editApp;
    private LinearLayout macroLayout;

    public static LaunchFragment newInstance() { return new LaunchFragment(); }

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

        view = inflater.inflate(main.getSharedPref().getInt(main.getString(R.string.launch_orientation), R.layout.fragment_launch_right), container, false);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        appsListFragment = AppsListFragment.newInstance();
        getChildFragmentManager().beginTransaction().add(R.id.apps_list, appsListFragment).commit();
        editBlur = view.findViewById(R.id.edit_blur);
        editBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!main.getSharedPref().getBoolean(main.getString(R.string.first_time), true)) {
                    editBlur.setVisibility(View.GONE);
                }
            }
        });
        if(main.getSharedPref().getBoolean(main.getString(R.string.first_time), true)) {
            drawerLayout.openDrawer(view.findViewById(R.id.apps_list));
            editBlur.setVisibility(View.VISIBLE);
        }
        
        patternView = view.findViewById(R.id.patternView);
        patternView.setOnPatternCellAddedListener(new PatternView.OnPatternCellAddedListener() {
            @Override
            public void onPatternCellAdded() {
                List<Cell> cellList = patternView.getPattern();
                cellsAdded++;
                switch (cellsAdded) {
                    case 1:
                        view.findViewById(MacroTools.getCellButtonId(cellList.get(0))).setBackground(ContextCompat.getDrawable(main, R.drawable.circle_white));
                        break;
                    case 2:
                        view.findViewById(MacroTools.getCellButtonId(cellList.get(1))).setBackground(ContextCompat.getDrawable(main, R.drawable.circle_gray));
                        break;
                    case 3:
                        view.findViewById(MacroTools.getCellButtonId(cellList.get(2))).setBackground(ContextCompat.getDrawable(main, R.drawable.circle_black));
                        break;
                }
            }
        });
        patternView.setOnPatternDetectedListener(new PatternView.OnPatternDetectedListener() {
            @Override
            public void onPatternDetected() {
                if(patternView.getPattern().size() == MacroTools.MACRO_SIZE) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkKeys(patternView.getPattern());
                        }
                    }, 100);
                } else {
                    clearPattern();
                }

                cellsAdded = 0;
            }
        });

        return view;
    }

    private void clearPattern() {
        if(view == null)
            return;

        for(int i = 0; i < 9; i ++)
            view.findViewById(MacroTools.getButtonId(i)).setBackground(ContextCompat.getDrawable(main, R.drawable.circle_clear));
        patternView.clearPattern();
    }

    private void checkKeys(List<Cell> cells) {
        if(main.getSharedPref().getBoolean(main.getString(R.string.first_time), true)) {
            setMacro(main.getString(R.string.launchtool_appdrawer), cells);
            SharedPreferences.Editor editor = main.getSharedPref().edit();
            editor.putBoolean(main.getString(R.string.first_time), false);
            editor.commit();
            clearPattern();
            return;
        }

        String key = MacroTools.cellsToKey(cells);
        if(cells.size() == MacroTools.MACRO_SIZE) {
            if (editMacro) {
                if(!macroExists(key))
                    setMacro(editApp.getApplicationPackageName(), cells);
            } else {
                openApp(getApp(key));
            }
        }
        clearPattern();
    }

    public void setMacro(String app, List<Cell> cells) {

        for(int i = 0; i < cells.size(); i++) {
            View view = macroLayout.findViewById(MacroTools.getCellLayoutId(cells.get(i)));
            view.setAlpha(1);
            switch (i) {
                case 0:
                    view.setBackground(ContextCompat.getDrawable(main, R.drawable.dot_white));
                    break;
                case 1:
                    view.setBackground(ContextCompat.getDrawable(main, R.drawable.dot_gray));
                    break;
                case 2:
                    view.setBackground(ContextCompat.getDrawable(main, R.drawable.dot_black));
                    break;
            }
        }

        SharedPreferences.Editor editor = main.getSharedPref().edit();
        editor.putString(app, MacroTools.cellsToKey(cells));
        editor.commit();
        editBlur.setVisibility(View.GONE);
        editMacro = false;
    }

    public void editMacro(AppModel app, LinearLayout macro) {
        editApp = app;
        macroLayout = macro;
        editMacro = true;
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

    public void toggleAppDrawer() {
        View drawerView = view.findViewById(R.id.apps_list);
        if(drawerLayout.isDrawerOpen(drawerView)) {
            drawerLayout.closeDrawer(drawerView, true);
        } else {
            drawerLayout.openDrawer(drawerView, true);
        }
    }

    public boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(view.findViewById(R.id.apps_list));
    }

    public void cleanUp() {
        main = null;
        view = null;
        if(appsListFragment != null) {
            appsListFragment.cleanUp();
            appsListFragment = null;
        }
    }
}
