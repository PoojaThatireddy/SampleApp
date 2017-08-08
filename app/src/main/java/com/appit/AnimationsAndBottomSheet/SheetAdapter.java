package com.appit.AnimationsAndBottomSheet;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Appit on 8/3/2017.
 */

class SheetAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<BottomSheetItemModel> bottomSheetList;

    public SheetAdapter(BottomSheetActivity bottomSheetActivity, ArrayList<BottomSheetItemModel> bottomSheetItem) {
        this.activity=bottomSheetActivity;
        this.bottomSheetList=bottomSheetItem;
    }

    public SheetAdapter(ActivityMic activityMic, ArrayList<BottomSheetItemModel> bottomSheetItem) {
        this.activity=activityMic;
        this.bottomSheetList=bottomSheetItem;
    }

    public SheetAdapter(ActivtyChat activtyChat, ArrayList<BottomSheetItemModel> bottomSheetItem) {
        this.activity=activtyChat;
        this.bottomSheetList=bottomSheetItem;
    }

    @Override
    public int getCount() {
        return bottomSheetList.size();
    }

    @Override
    public Object getItem(int position) {
        return bottomSheetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        SheetAdapter.ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(R.layout.item_bottom_sheet, parent, false);

            holder = new SheetAdapter.ViewHolder();
            holder.textView = (TextView) row.findViewById(R.id.tv_timer);
            row.setTag(holder);
        } else {
            holder = (SheetAdapter.ViewHolder) row.getTag();
        }

        holder.textView.setText(bottomSheetList.get(position).getTime_count());
        if(bottomSheetList.get(position).isSelected()) {
            holder.textView.setTextColor(ContextCompat.getColorStateList(activity, R.color.text_color_selector));
        }


        return row;
    }


    private static class ViewHolder {
        TextView textView;
    }


}
