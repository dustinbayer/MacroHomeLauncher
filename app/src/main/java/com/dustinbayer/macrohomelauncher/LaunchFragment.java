package com.dustinbayer.macrohomelauncher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dusti on 11/25/2017.
 */

public class LaunchFragment extends Fragment {

    private MainActivity main;
    private View view;

    private DrawerLayout drawerLayout;

    private AppsListFragment appsListFragment;
    public AppsListFragment getAppsListFragment() { return appsListFragment; }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(main.getSharedPref().getInt(main.getString(R.string.launch_orientation), R.layout.fragment_launch_right), container, false);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        appsListFragment = AppsListFragment.newInstance();
        getChildFragmentManager().beginTransaction().add(R.id.apps_list, appsListFragment).commit();
        return view;
    }

    public void toggleAppDrawer() {

    }

    public void changeUiColor() {

    }

    public void cleanUp() {
        main = null;
        view = null;
        appsListFragment.cleanUp();
    }
}
