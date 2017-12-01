package com.dustinbayer.macrohomelauncher;

/**
 * Created by dusti on 11/27/2017.
 */

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dusti on 11/20/2017.
 */

public class AppsListAdapter extends BaseAdapter {
    private MainActivity main;
    private ArrayList<AppModel> installedApps;
    private final LayoutInflater inflater;

    public AppsListAdapter (MainActivity main, ArrayList<AppModel> installedApps) {
        this.main = main;
        this.installedApps = new ArrayList<>(installedApps);
        inflater = LayoutInflater.from(main);
    }

    @Override
    public int getCount() {
        return installedApps.size();
    }

    @Override
    public AppModel getItem(int position) {
        return installedApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if(main.getSharedPref().getInt(main.getString(R.string.launch_orientation), R.layout.fragment_launch_right) == R.layout.fragment_launch_right)
            view = inflater.inflate(R.layout.item_appslist_right, parent, false);
        else
            view = inflater.inflate(R.layout.item_appslist_left, parent, false);

        final ViewHolder holder = new ViewHolder(main, view);
        final AppModel app = installedApps.get(position);
        ((ImageView)view.findViewById(R.id.icon)).setImageDrawable(app.getIcon());
        ((TextView)view.findViewById(R.id.text)).setText(app.getLabel());
        holder.setApp(app);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!main.getLaunchFragment().openApp(holder.getApp().getApplicationPackageName()))
                    main.getLaunchTools().runTool(app);
            }
        });

        final LinearLayout macroLayout = view.findViewById(R.id.macro);
        String macro = main.getLaunchFragment().getMacro(app.getApplicationPackageName());
        if(!macro.equals("")) {
            String[] keyArray = MacroTools.getMacroKeyArray(macro);
            if(keyArray.length > 0) {
                for (int i = 1; i < keyArray.length; i++) {
                    int id = MacroTools.getKeyLayoutId(keyArray[i]);
                    if(id != 0) {
                        View key = macroLayout.findViewById(id);
                        if (key != null) {
                            key.setAlpha(1);
                            switch (i) {
                                case 1:
                                    key.setBackground(ContextCompat.getDrawable(main, R.drawable.dot_white));
                                    break;
                                case 2:
                                    key.setBackground(ContextCompat.getDrawable(main, R.drawable.dot_gray));
                                    break;
                                case 3:
                                    key.setBackground(ContextCompat.getDrawable(main, R.drawable.dot_black));
                                    break;
                            }
                        }
                    }
                }
            }
        }

        macroLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.getLaunchFragment().editMacro(app, macroLayout);
            }
        });


        return view;
    }

    public static class ViewHolder extends View {
        public View view;

        private AppModel app;
        public AppModel getApp() { return app; }
        public void setApp(AppModel app) { this.app = app; }

        public ViewHolder(MainActivity main, View view) {
            super(main);
            this.view = view;
        }

    }

    public void cleanUp() {
        main = null;
        installedApps.clear();
        installedApps = null;
    }

}
