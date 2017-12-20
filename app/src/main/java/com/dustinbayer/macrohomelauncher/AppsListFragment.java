package com.dustinbayer.macrohomelauncher;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by dusti on 11/27/2017.
 */

public class AppsListFragment extends Fragment  implements LoaderManager.LoaderCallbacks<ArrayList<AppModel>>{

    private MainActivity main;
    private View view;

    private ListView listView;
    public ListView getListView() { return listView; }

    private AppsListAdapter appsListAdapter;
    public AppsListAdapter getAppsListAdapter() { return appsListAdapter; }

    private ArrayList<AppModel> installedApps;
    public ArrayList<AppModel> getInstalledApps() { return installedApps; }

    private LinearLayout appsListBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private RelativeLayout bottomSheetPeek;
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
        view = inflater.inflate(R.layout.bottomsheet_appslist, container, false);
        appsListBottomSheet = view.findViewById(R.id.appslist_bottomsheet);
        bottomSheetPeek = view.findViewById(R.id.bottom_sheet_peek);
        bottomSheetPeek.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                    toggleBottomSheet();
                    return true;
                }

                return false;
            }
        });
        bottomSheetBehavior = BottomSheetBehavior.from(appsListBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if(newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    main.getLaunchFragment().reloadApps();
                } else if(newState == BottomSheetBehavior.STATE_EXPANDED) {

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        listView = (ListView) view.findViewById(R.id.apps_list_view);
        listView.setFastScrollEnabled(true);
        progressView = createProgressView();
        ((FrameLayout)view.findViewById(R.id.progress_frame)).addView(progressView);
        setListShown(false,true);

        getLoaderManager().initLoader(0, null, this);
        return view;
    }


    private View createProgressView() {
        LinearLayout pframe = new LinearLayout(main);
        pframe.setId(PROGRESS_ID);
        pframe.setOrientation(LinearLayout.VERTICAL);
        pframe.setGravity(Gravity.CENTER);

        ProgressBar progress = new ProgressBar(main, null,
                android.R.attr.progressBarStyleLarge);
        progress.setBackgroundColor(Color.WHITE);
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
        appsListBottomSheet  = null;
        bottomSheetPeek = null;
        bottomSheetBehavior = null;
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

    public void toggleBottomSheet() {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            ViewGroup.LayoutParams params = bottomSheetPeek.getLayoutParams();
            params.height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
            bottomSheetPeek.setLayoutParams(params);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            ViewGroup.LayoutParams params = bottomSheetPeek.getLayoutParams();
            params.height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
            bottomSheetPeek.setLayoutParams(params);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

}
