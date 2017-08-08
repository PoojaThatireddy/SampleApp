package com.appit.AnimationsAndBottomSheet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class BottomSheetActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_bottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private BottomSheetAdapter bottomSheetAdapter;
    private SheetAdapter adapter;

    private Toolbar toolbar;
    int initialSelection=0;

    ArrayList<BottomSheetItemModel> bottomSheetItem;
    ListView lvBottomSheet;
    String[] timerList = new String[]{"15 secs",
            "1 min",
            "1 hour",
            "1 day",
            "1 week",
            "1 month",
           };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_bottomSheet=(Button)findViewById(R.id.btn_open_bottom_sheet);
        lvBottomSheet = (ListView) findViewById(R.id.lv_bottom_sheet);




        setSupportActionBar(toolbar);


        bottomSheetBehavior = BottomSheetBehavior.from(lvBottomSheet);

        getData();

//        bottomSheetAdapter = new BottomSheetAdapter(this, R.layout.item_bottom_sheet, timerList);
//        lvBottomSheet.setAdapter(bottomSheetAdapter);

        adapter = new SheetAdapter(this, bottomSheetItem);
        lvBottomSheet.setAdapter(adapter);



        lvBottomSheet.setTranslationY(getStatusBarHeight());
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            boolean first = true;

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.d("MainActivity", "onStateChanged: " + newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d("MainActivity", "onSlide: ");
                if (first) {
                    first = false;
                    bottomSheet.setTranslationY(0);
                }
            }
        });

        btn_bottomSheet.setOnClickListener(this);

        lvBottomSheet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                if(parent.isSelected()){
                    lvBottomSheet.setSelection(position);
                }else {
                    lvBottomSheet.setSelection(0);
                }
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

            }
        });

    }

    private void getData() {
        bottomSheetItem=new ArrayList<>();
        for(int i=0;i<timerList.length;i++){
        bottomSheetItem.add(new BottomSheetItemModel(timerList[i]));
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    @Override
    public void onClick(View view) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
