package com.dustinbayer.macrohomelauncher;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

/**
 * Created by dusti on 11/27/2017.
 */

public class AppsListFragment extends Fragment  implements LoaderManager.LoaderCallbacks<ArrayList<AppModel>>{

    private MainActivity main;
    private View view;

    private ListView listView;
    public ListView getRecyclerView() { return listView; }

    private AppsListAdapter appsListAdapter;
    public AppsListAdapter getAppsListAdapter() { return appsListAdapter; }

    private ArrayList<AppModel> installedApps;
    public ArrayList<AppModel> getInstalledApps() { return installedApps; }

    private RecyclerView.LayoutManager layoutManager;

    private AppsLoader loader;
    private Boolean listShown = false;
    private View progressView;
    static final int PROGRESS_ID = 0x00ff0002;

    public static AppsListFragment newInstance() { return new AppsListFragment(); }

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
        view = inflater.inflate(R.layout.fragment_appslist, container, false);
        listView = (ListView) view.findViewById(R.id.apps_list_view);
        progressView = createProgressView();
        setListShown(false,true);

        getLoaderManager().initLoader(0, null, this);
        return view;
    }

    private View createProgressView() {
        LinearLayout pframe = new LinearLayout(main);
        pframe.setId(PROGRESS_ID);
        pframe.setOrientation(LinearLayout.VERTICAL);
        pframe.setVisibility(View.GONE);
        pframe.setGravity(Gravity.CENTER);

        ProgressBar progress = new ProgressBar(main, null,
                android.R.attr.progressBarStyleLarge);
        progress.setBackground(ContextCompat.getDrawable(main, R.drawable.circle_clear));
        pframe.addView(progress, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return pframe;
    }

    @Override
    public Loader<ArrayList<AppModel>> onCreateLoader(int id, Bundle bundle) {
        return new AppsLoader(main);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<AppModel>> loader, ArrayList<AppModel> apps) {
        appsListAdapter = new AppsListAdapter(main, apps);
        listView.setAdapter(appsListAdapter);

        if (isResumed()) {
            setListShown(true, true);
        } else {
            setListShown(true, false);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<AppModel>> loader) {

    }

    public void setListShown(boolean shown, boolean animate) {

        if (progressView == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        }
        if (listShown == shown) {
            return;
        }
        listShown = shown;
        if (shown) {
            if (animate) {
                progressView.startAnimation(AnimationUtils.loadAnimation(
                        main, android.R.anim.fade_out));
                listView.startAnimation(AnimationUtils.loadAnimation(
                        main, android.R.anim.fade_in));
            } else {
                progressView.clearAnimation();
                listView.clearAnimation();
            }
            progressView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                progressView.startAnimation(AnimationUtils.loadAnimation(
                        main, android.R.anim.fade_in));
                listView.startAnimation(AnimationUtils.loadAnimation(
                        main, android.R.anim.fade_out));
            } else {
                progressView.clearAnimation();
                listView.clearAnimation();
            }
            progressView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

    }

    public void cleanUp() {
        main = null;
        view = null;
        listView = null;
        progressView = null;
        if(appsListAdapter != null) {
            appsListAdapter.cleanUp();
            appsListAdapter = null;
        }
        if(installedApps != null) {
            installedApps.clear();
            installedApps = null;
        }
        if(loader != null) {
            loader.cleanUp();
            loader = null;
        }
    }
}
