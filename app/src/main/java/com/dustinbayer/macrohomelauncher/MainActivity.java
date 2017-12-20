package com.dustinbayer.macrohomelauncher;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private LaunchFragment launchFragment;
    public LaunchFragment getLaunchFragment() { return launchFragment; }

    private SharedPreferences sharedPref;
    public SharedPreferences getSharedPref() { return sharedPref; }

    private LaunchTools launchTools;
    public LaunchTools getLaunchTools() { return launchTools; }

    private WallpaperManager wallpaperManager;
    private boolean didStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            return;
        }

        wallpaperManager = WallpaperManager.getInstance(this);
        ((ImageView)findViewById(R.id.wallpaper)).setImageDrawable(wallpaperManager.getDrawable());

        launchFragment = LaunchFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, launchFragment).commit();

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        launchTools = LaunchTools.newInstance(this);

        didStart = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if(didStart) {
            ((ImageView)findViewById(R.id.wallpaper)).setImageDrawable(wallpaperManager.getDrawable());
            reloadLaunchFragment();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        launchFragment.cleanUp();
        launchTools.cleanUp();
        sharedPref = null;
    }

    @Override
    public void onBackPressed() {
    }

    public void reloadLaunchFragment() {
        launchFragment.cleanUp();
        launchFragment = LaunchFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, launchFragment);

        transaction.commitAllowingStateLoss();
    }
}


