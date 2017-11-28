package com.dustinbayer.macrohomelauncher;

/**
 * Created by dusti on 11/27/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dusti on 11/20/2017.
 */

public class AppsListAdapter extends  RecyclerView.Adapter<AppsListAdapter.ViewHolder> {
    private MainActivity main;
    private ArrayList<AppModel> installedApps;
    private final LayoutInflater inflater;

    public AppsListAdapter (MainActivity main, ArrayList<AppModel> installedApps) {
        this.main = main;
        this.installedApps = new ArrayList<>(installedApps);
        inflater = LayoutInflater.from(main);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        private AppModel app;
        public AppModel getApp() { return app; }
        public void setApp(AppModel app) { this.app = app; }

        public ViewHolder(View view) {
            super(view);
            this.view = view;
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public AppsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if(main.getSharedPref().getInt(main.getString(R.string.launch_orientation), R.layout.fragment_launch_right) == R.layout.fragment_launch_right)
            view = inflater.inflate(R.layout.item_appslist_right, parent, false);
        else
            view = inflater.inflate(R.layout.item_appslist_left, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppModel app = installedApps.get(position);
        ((ImageView)holder.view.findViewById(R.id.icon)).setImageDrawable(app.getIcon());
        ((TextView)holder.view.findViewById(R.id.text)).setText(app.getLabel());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return installedApps.size();
    }

    public void cleanUp() {
        main = null;
        installedApps.clear();
        installedApps = null;
    }

}
