package com.appit.AnimationsAndBottomSheet;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appit.AnimationsAndBottomSheet.databinding.ActivtyChatBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ActivtyChat extends AppCompatActivity implements View.OnClickListener {

    ActivtyChatBinding mBinding;

    TextView tvTimer, cancel, recordState;
    ImageView mic, stopRec, send;
    LinearLayout msgBlockLayout;
    View micLayout;

    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder = null;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer = null;
    Timer t;
    Context context;
    TextView timer;

    ArrayList<BottomSheetItemModel> bottomSheetItem;
    BottomSheetBehavior bottomSheetBehavior;
    private SheetAdapter adapter;

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
        setContentView(R.layout.activty_chat);

        random = new Random();

        micLayout = (View) findViewById(R.id.mic_layout);

        tvTimer = (TextView) findViewById(R.id.timer_id);

        recordState = (TextView) micLayout.findViewById(R.id.tv_recording_state);
        cancel = (TextView) micLayout.findViewById(R.id.tv_cancel);
        stopRec = (ImageView) micLayout.findViewById(R.id.iv_stop_record);
        send = (ImageView) micLayout.findViewById(R.id.iv_send);
        msgBlockLayout = (LinearLayout) findViewById(R.id.message_block_id);
        mic = (ImageView) findViewById(R.id.mic_id);

        timer = (TextView) micLayout.findViewById(R.id.ch_record_timer);

        lvBottomSheet = (ListView) findViewById(R.id.lv_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(lvBottomSheet);
        bottomSheetItem = new ArrayList<>();
        for (int i = 0; i < timerList.length; i++) {
            bottomSheetItem.add(new BottomSheetItemModel(timerList[i]));
        }

        adapter = new SheetAdapter(this, bottomSheetItem);
        lvBottomSheet.setAdapter(adapter);

        timer.setOnClickListener(this);
        tvTimer.setOnClickListener(this);
        lvBottomSheet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                adapter.notifyDataSetChanged();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        micLayout.setVisibility(View.INVISIBLE);
        mic.setOnClickListener(this);
        cancel.setOnClickListener(this);
        stopRec.setOnClickListener(this);

    }

    private void startTimer() {

        t = new Timer();
        t.schedule(new TimerTask() {

            int minute = 0, seconds = 0;

            @Override
            public void run() {

                timer.post(new Runnable() {

                    public void run() {

                        seconds++;
                        if (seconds == 60) {
                            seconds = 0;
                            minute++;
                        }
                        if (minute == 1) {

                            stopRecording();
                          /*  minute = 0;
                            hour++;*/
                        }
                        timer.setText(""

                                + (minute > 9 ? minute : ("0" + minute))
                                + " : "
                                + (seconds > 9 ? seconds : "0" + seconds));

                    }
                });

            }
        }, 1000, 1000);


    }

    public void MediaRecorderReady() {

        mediaRecorder = new MediaRecorder();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

        mediaRecorder.setOutputFile(AudioSavePathInDevice);

    }

    public String CreateRandomAudioFileName(int string) {

        StringBuilder stringBuilder = new StringBuilder(string);

        int i = 0;
        while (i < string) {

            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(ActivtyChat.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {

                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {

                        Toast.makeText(ActivtyChat.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ActivtyChat.this, "Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    public boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.mic_id:
                startRecording();
                break;
            case R.id.iv_stop_record:
                stopRecording();
                break;
            case R.id.tv_cancel:
                cancelRecording();
                break;
            case R.id.timer_id:
                openTimerBottomSheet();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

        }
    }

    private void openTimerBottomSheet() {
//        lvBottomSheet.setTranslationY(getStatusBarHeight());
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

    }

    private void startRecording() {

        if (checkPermission()) {

            AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CreateRandomAudioFileName(10) + "AudioRecording.3gp";

            MediaRecorderReady();
            micLayout.setVisibility(View.VISIBLE);
            micLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            send.setVisibility(View.INVISIBLE);
            stopRec.setVisibility(View.VISIBLE);

            try {
                mediaRecorder.prepare();

                mediaRecorder.start();
                startTimer();


            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            micLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
            timer.setText("00:00");

        } else {
            requestPermission();
        }

    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            t.cancel();
            mediaRecorder = null;
            micLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            send.setVisibility(View.VISIBLE);
            stopRec.setVisibility(View.INVISIBLE);

            stopRec.setVisibility(View.INVISIBLE);
            send.setVisibility(View.VISIBLE);

        }
    }

    private void cancelRecording() {

        if (mediaRecorder != null) {
            mediaRecorder.release();
            if (t != null) {
                t.cancel();
                t = null;
            }

        }
        micLayout.setVisibility(View.INVISIBLE);

    }
}
