package com.dustinbayer.macrohomelauncher;

/**
 * Created by dusti on 11/27/2017.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import java.util.ArrayList;

/**
 * Created by dusti on 11/20/2017.
 */

public class AppsListAdapter extends BaseAdapter {
    private MainActivity main;
    private ArrayList<AppModel> installedApps;
    private ArrayList<LinearLayout> macroList;
    private final LayoutInflater inflater;

    public AppsListAdapter (MainActivity main, ArrayList<AppModel> installedApps) {
        this.main = main;
        this.installedApps = new ArrayList<>(installedApps);
        inflater = LayoutInflater.from(main);
        macroList = new ArrayList<>();
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

        view = inflater.inflate(R.layout.item_appslist, parent, false);
//
//        final ViewHolder holder = new ViewHolder(main, view);
//        final AppModel app = installedApps.get(position);
//        ((ImageView)view.findViewById(R.id.icon)).setImageDrawable(app.getIcon());
//        ((TextView)view.findViewById(R.id.text)).setText(app.getLabel());
//        holder.setApp(app);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                main.getLaunchFragment().openApp(holder.getApp().getApplicationPackageName());
//            }
//        });
//
//        final LinearLayout macroLayout = view.findViewById(R.id.macro);
//        macroList.add(macroLayout);
//        String macro = main.getLaunchFragment().getMacro(app.getApplicationPackageName());
//        if(!macro.equals("")) {
//            String[] keyArray = MacroTools.getMacroKeyArray(macro);
//            if(keyArray.length > 0) {
//                for (int i = 1; i < keyArray.length; i++) {
//                    int id = MacroTools.getKeyLayoutId(keyArray[i]);
//                    if(id != 0) {
//                        View key = macroLayout.findViewById(id);
//                        if (key != null) {
//                            switch (i) {
//                                case 1:
//                                    key.setBackground(ContextCompat.getDrawable(main, R.drawable.dot_white));
//                                    break;
//                                case 2:
//                                    key.setBackground(ContextCompat.getDrawable(main, R.drawable.dot_gray));
//                                    break;
//                                case 3:
//                                    key.setBackground(ContextCompat.getDrawable(main, R.drawable.dot_black));
//                                    break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        macroLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                main.getLaunchFragment().editMacro(app);
//            }
//        });
//
//        if(holder.getApp().getApplicationPackageName().equals(main.getString(R.string.launchtool_appdrawer))) {
//            if (main.getSharedPref().getBoolean(main.getString(R.string.first_time), true)) {
//                main.getLaunchFragment().editMacro(app);
//            }
//        }

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

    public LinearLayout getMacroLayout(String app) {
        for(int i = 0; i < installedApps.size(); i++) {
            if (app.equals(installedApps.get(i).getApplicationPackageName())) {
                return macroList.get(i);
            }
        }
        return null;
    }

//    public void setMacroLayout(String app, List<Cell> cells) {
//        clearMacroLayout(app);
//        for (int i = 0; i < cells.size(); i++) {
//            View view = getMacroLayout(app);
//            if(view != null) {
//                View key = view.findViewById(MacroTools.getCellLayoutId(cells.get(i)));
//                switch (i) {
//                    case 0:
//                        key.setBackground(ContextCompat.getDrawable(main, R.drawable.dot_white));
//                        break;
//                    case 1:
//                        key.setBackground(ContextCompat.getDrawable(main, R.drawable.dot_gray));
//                        break;
//                    case 2:
//                        key.setBackground(ContextCompat.getDrawable(main, R.drawable.dot_black));
//                        break;
//                }
//            }
//        }
//    }

//    private void clearMacroLayout(String app){
//        LinearLayout macroLayout = getMacroLayout(app);
//        String macro = main.getLaunchFragment().getMacro(app);
//        if(!macro.equals("")) {
//            String[] keyArray = MacroTools.getMacroKeyArray(macro);
//            if(keyArray.length > 0) {
//                for (int i = 1; i < keyArray.length; i++) {
//                    int id = MacroTools.getKeyLayoutId(keyArray[i]);
//                    if(id != 0) {
//                        View key = macroLayout.findViewById(id);
//                        if (key != null) {
//                            key.setBackground(ContextCompat.getDrawable(main, R.drawable.dot));
//                        }
//                    }
//                }
//            }
//        }
//    }

    public void cleanUp() {
        main = null;
        installedApps.clear();
        installedApps = null;
    }

}
