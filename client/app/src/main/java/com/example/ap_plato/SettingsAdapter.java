package com.example.ap_plato;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class SettingsAdapter  extends ArrayAdapter<SettingsInfo> {
    private Context mContext;
    private int mResource;
    public static View view;
    private static class ViewHolder {
        TextView titleView;
    }

    public SettingsAdapter(Context context, int resource, List<SettingsInfo> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SettingsInfo settingsInfo = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.mContext);
            convertView = inflater.inflate(this.mResource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleView = convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.titleView.setText(Objects.requireNonNull(settingsInfo).getTitle());
        viewHolder.titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0){
                    mContext.startActivity(new Intent(mContext , About.class));
                    ((Activity)mContext).overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
                }
                else if (position == 1){
                    mContext.startActivity(new Intent(mContext , Login.class));
                    ((Activity)mContext).overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
                }
            }
        });
        return convertView;
    }

}
