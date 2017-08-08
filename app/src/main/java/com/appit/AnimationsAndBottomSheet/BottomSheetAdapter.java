package com.appit.AnimationsAndBottomSheet;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Appit on 8/2/2017.
 */

class BottomSheetAdapter extends ArrayAdapter<String>{
    private Activity activity;

    private int selectedItem;

    public BottomSheetAdapter(BottomSheetActivity context, int resource, String[] timerList) {
        super(context, resource, timerList);
        this.activity = context;
    }

    public BottomSheetAdapter(ActivtyChat activtyChat, int resource, String[] timerList) {
        super(activtyChat, resource, timerList);
        this.activity=activtyChat;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(R.layout.item_bottom_sheet, parent, false);

            holder = new ViewHolder();
            holder.textView = (TextView) row.findViewById(R.id.tv_timer);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.textView.setText(getItem(position));
        return row;
    }


    private static class ViewHolder {
        TextView textView;
    }
}
