package com.appit.AnimationsAndBottomSheet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class SlideLayout extends AppCompatActivity {
    Button btn_backTopBottom,btn_backLeftRight;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        btn_backTopBottom=(Button)findViewById(R.id.btn_back_from_up);
        btn_backLeftRight=(Button)findViewById(R.id.btn_back_from_down);


        btn_backTopBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_no_move,R.anim.slide_out_top);
            }
        });
        btn_backLeftRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_no_move,R.anim.slide_out_left);
            }
        });
    }
}
