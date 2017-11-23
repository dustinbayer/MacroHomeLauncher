package com.dustinbayer.macrohomelauncher;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.eftimoff.patternview.PatternView;
import com.eftimoff.patternview.cells.Cell;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private Boolean editMacro = false;
    private AppModel editApp;
    private TextView macroText;

    private DrawerLayout mDrawerLayout;
    private AppsGridFragment gridFragment;
    private PatternView patternView;
    private ImageButton homeButton;
    //private ImageButton notesButton;
    private ActionBarDrawerToggle mDrawerToggle;
    private View mDrawerView;
    //private View mNotesView;
    private View editBlur;
    private boolean refreshed = false;

    public final static int MACRO_SIZE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        ((ImageView)findViewById(R.id.wallpaper)).setImageDrawable(wallpaperDrawable);
        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!mDrawerLayout.isDrawerOpen(mDrawerView))
            gridFragment.refresh();
    }

    private void setup() {
        homeButton = findViewById(R.id.apps_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDrawerLayout.isDrawerOpen(mDrawerView)
                        && !refreshed && !listIsAtTop()) {
                    refreshed = true;
                    gridFragment.refresh();
                } else {
                    toggleDrawer(mDrawerView);
                }
            }
        });

//        notesButton = findViewById(R.id.notes_button);
//        notesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggleDrawer(mNotesView);
//            }
//        });

        gridFragment = (AppsGridFragment) getFragmentManager().findFragmentById(R.id.grid_frag);

        if(mDrawerLayout == null || mDrawerView == null || mDrawerToggle == null) {
            // Configure navigation drawer
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerView = findViewById(R.id.apps_grid);
            //mNotesView = findViewById(R.id.notes_drawer);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View drawerView) {
                    mDrawerToggle.syncState();
                    homeButton.setRotation(0);
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    mDrawerToggle.syncState();
                    homeButton.setRotation(180);
                }

            };

            mDrawerLayout.addDrawerListener(mDrawerToggle); // Set the drawer toggle as the DrawerListener
        }

        editBlur = findViewById(R.id.edit_blur);
        editBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBlur.setVisibility(View.GONE);
                macroText.setText(getMacro(editApp.getApplicationPackageName()));
                editMacro = false;
                patternView.clearPattern();
            }
        });

        patternView = findViewById(R.id.patternView);
        patternView.setOnPatternDetectedListener(new PatternView.OnPatternDetectedListener() {
            @Override
            public void onPatternDetected() {
                if(patternView.getPattern().size() == MACRO_SIZE) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            convertKeys(patternView.getPattern());
                        }
                    }, 100);
                } else {
                    patternView.clearPattern();
                }
            }
        });
        patternView.clearPattern();

    }

    private boolean listIsAtTop()   {
       // if(gridFragment.lv.getChildCount() == 0) return true;
        return gridFragment.lv.getChildAt(0).getTop() == 0;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void convertKeys(List<Cell> cells) {
       if(cells.size() == MACRO_SIZE) {

           String key = "";
           for (int i = 0; i < MACRO_SIZE; i++) {
               Cell cell = cells.get(i);
               Log.d("PATTERN", cell.getId());
               switch (cell.getId()) {
                   case "000-000":
                       key = key + "Q";
                       break;
                   case "000-001":
                       key = key + "W";
                       break;
                   case "000-002":
                       key = key + "E";
                       break;
                   case "001-000":
                       key = key + "R";
                       break;
                   case "001-001":
                       key = key + "T";
                       break;
                   case "001-002":
                       key = key + "Y";
                       break;
                   case "002-000":
                       key = key + "U";
                       break;
                   case "002-001":
                       key = key + "I";
                       break;
                   case "002-002":
                       key = key + "O";
                       break;
               }

           }

           checkKeys(key);
       }
    }

    private void checkKeys(String macro) {
        if(editMacro) {
            if(!macroExists(macro))
            {
                setMacro(editApp.getApplicationPackageName(), macro);
                editApp.setMacro(macro);
                macroText.setText(macro);
            } else {
                patternView.clearPattern();
                macroText.setText("");
            }
        } else {
            String val = getApp(macro);

            if(val == null) {
                Log.e("KEY ERROR", "Macro '" + macro + "' not valid");
            } else {
                openApp(val);
            }
        }

        patternView.clearPattern();
    }

    public void setMacro(String app, String macro) {
        editMacro = false;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(app, macro);
        editor.commit();
        editBlur.setVisibility(View.GONE);
    }

    public void editMacro(AppModel app, TextView macro) {
        editApp = app;
        macroText = macro;
        editMacro = true;
        patternView.clearPattern();
        editBlur.setVisibility(View.VISIBLE);
    }

    public boolean macroExists(String macro) {
        Map<String,?> appMap = sharedPref.getAll();
        for(String app : appMap.keySet()){
            if(appMap.get(app).equals(macro)){
                return true;
            }
        }
        return false;
    }

    public String getMacro(String app) {
        return sharedPref.getString(app, "***");
    }

    public String getApp(String macro) {
        Map<String,?> appMap = sharedPref.getAll();
        for(String app : appMap.keySet()){
            if(appMap.get(app).equals(macro)){
                return app; //return the first found
            }
        }
        return null;
    }

    private void toggleDrawer(View drawer) {
        refreshed = false;
        if(mDrawerLayout.isDrawerOpen(drawer)) {
            spinButton(drawer, false);
            mDrawerLayout.closeDrawer(drawer, true);
        } else {
            spinButton(drawer, true);
            mDrawerLayout.openDrawer(drawer, true);
        }
    }

    private void spinButton(View drawer, Boolean open) {
        if(drawer == mDrawerView){
            if(open)
                homeButton.animate().rotation(180).start();
            else
                homeButton.animate().rotation(0).start();
        }
//        else if (drawer == mNotesView) {
//            if(open)
//                notesButton.animate().rotation(0).start();
//            else
//                notesButton.animate().rotation(180).start();
//        }

    }

    public void openApp(String val) {
        if(val.equals("com.dustinbayer.macrohomelauncher.home")) {
            toggleDrawer(mDrawerView);
        } else {

            Intent intent = getPackageManager().getLaunchIntentForPackage(val);

            if (intent != null) {
                startActivity(intent);
            } else {
                Log.e("VALUE ERROR", "Value '" + val + "' not valid");
            }
       }
    }

}


