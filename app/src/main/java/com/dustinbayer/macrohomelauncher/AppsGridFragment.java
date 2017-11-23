package com.dustinbayer.macrohomelauncher;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;

import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by dusti on 11/20/2017.
 */

public class AppsGridFragment extends GridFragment implements LoaderManager.LoaderCallbacks<ArrayList<AppModel>> {

    AppListAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No Applications");

        mAdapter = new AppListAdapter(getActivity());
        setGridAdapter(mAdapter);

        // till the data is loaded display a spinner
        setGridShown(false);

        // create the loader to load the apps list in background
        getLoaderManager().initLoader(0, null, this);
    }

    public void refresh() {
        setGridShown(true, true);
       getLoaderManager().destroyLoader(0);
        // create the loader to load the apps list in background
        getLoaderManager().initLoader(0, null, this);
        setGridShown(false);
    }

    @Override
    public Loader<ArrayList<AppModel>> onCreateLoader(int id, Bundle bundle) {
        return new AppsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<AppModel>> loader, ArrayList<AppModel> apps) {
        mAdapter.setData(apps);

        if (isResumed()) {
            setGridShown(true);
        } else {
            setGridShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<AppModel>> loader) {
        mAdapter.setData(null);
    }

    @Override
    public void onGridItemClick(GridView g, View v, int position, long id) {
        AppModel app = (AppModel) getGridAdapter().getItem(position);
        if (app != null) {

            if(app.getApplicationPackageName().equals("com.dustinbayer.macrohomelauncher.home"))
                ((MainActivity) getActivity()).openApp(app.getApplicationPackageName());
            else if(app.getApplicationPackageName().equals("com.dustinbayer.macrohomelauncher.colorpicker"))
                ((MainActivity) getActivity()).openColorPicker();
            else if(app.getApplicationPackageName().equals("com.dustinbayer.macrohomelauncher.handness"))
                ((MainActivity) getActivity()).setHandness();
            else {
                Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(app.getApplicationPackageName());

                if (intent != null) {
                    startActivity(intent);
                }
            }
        }
    }
}



