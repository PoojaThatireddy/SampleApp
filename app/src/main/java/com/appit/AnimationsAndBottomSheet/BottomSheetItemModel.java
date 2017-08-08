package com.appit.AnimationsAndBottomSheet;

/**
 * Created by Appit on 8/1/2017.
 */

public class BottomSheetItemModel {
    String time_count;
    boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public BottomSheetItemModel(String time) {
        this.time_count=time;
    }


    public String getTime_count() {
        return time_count;
    }

    public void setTime_count(String time_count) {
        this.time_count = time_count;
    }
}
