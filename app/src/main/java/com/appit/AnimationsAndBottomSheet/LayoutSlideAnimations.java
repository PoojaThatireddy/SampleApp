package com.appit.AnimationsAndBottomSheet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class LayoutSlideAnimations extends AppCompatActivity {

    Toolbar toolbar;
    Button btn_slideTopBottom,btn_slideLeftRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_slide_animations);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        btn_slideTopBottom=(Button)findViewById(R.id.btn_slide_top_bottom);
        btn_slideLeftRight=(Button)findViewById(R.id.btn_slide_left_right);


        btn_slideTopBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(LayoutSlideAnimations.this, SlideLayout.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_bottom,R.anim.slide_no_move);

            }
        });
        btn_slideLeftRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(LayoutSlideAnimations.this, SlideLayout.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_no_move);
            }
        });
    }

}
