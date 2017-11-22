package com.dustinbayer.macrohomelauncher;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dusti on 11/20/2017.
 */

public class AppListAdapter extends ArrayAdapter<AppModel> {
    private final LayoutInflater mInflater;
    private Context context;

    public AppListAdapter (Context context) {
        super(context, android.R.layout.simple_list_item_2);
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<AppModel> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void addAll(Collection<? extends AppModel> items) {
        //If the platform supports it, use addAll, otherwise add in loop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.addAll(items);
        }else{
            for(AppModel item: items){
                super.add(item);
            }
        }
    }

    /**
     * Populate new items in the list.
     */
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final TextView macro;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_item_icon_text, parent, false);
        } else {
            view = convertView;
        }

        final AppModel item = getItem(position);
        ((ImageView)view.findViewById(R.id.icon)).setImageDrawable(item.getIcon());
        ((TextView)view.findViewById(R.id.text)).setText(item.getLabel());
        macro = (TextView)view.findViewById(R.id.macro);
        //macro.setText(item.getMacro());
        String key = ((MainActivity)context).getMacro(item.getApplicationPackageName());
        item.setMacro(key);
        macro.setText(key);
        macro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                macro.setText("");
                ((MainActivity)context).editMacro(item, macro);
            }
        });

        return view;
    }
}
