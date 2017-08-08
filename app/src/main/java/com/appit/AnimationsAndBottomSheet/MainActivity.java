package com.appit.AnimationsAndBottomSheet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button slideFromBottom,bottomSheet,chatLayout;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        slideFromBottom=(Button) findViewById(R.id.btn_slide);
        bottomSheet=(Button)findViewById(R.id.btn_bottom_sheet);
        chatLayout=(Button)findViewById(R.id.btn_chat_layout);

        slideFromBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LayoutSlideAnimations.class);
                startActivity(intent);
            }
        });
        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BottomSheetActivity.class);
                startActivity(intent);
            }
        });

        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivtyChat.class);
                startActivity(intent);
            }
        });





    }
}
