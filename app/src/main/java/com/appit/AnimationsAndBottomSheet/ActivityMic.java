package com.appit.AnimationsAndBottomSheet;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ActivityMic extends AppCompatActivity implements View.OnClickListener {

    Button buttonStart = null,
            buttonPlayLastRecordAudio = null,
            buttonStopPlayingRecording = null;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder = null;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer = null;


    ArrayList<BottomSheetItemModel> bottomSheetItem;
    TextView timer;
    Timer t;

    View micLayout;
    ImageView stop, send,mic;
    TextView cancel, tvTimer;

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
        setContentView(R.layout.activity_mic);

        buttonStart = (Button) findViewById(R.id.btn_record);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.btn_play);
        buttonStopPlayingRecording = (Button) findViewById(R.id.btn_stop_playing);
        tvTimer=(TextView)findViewById(R.id.timer_id);
        mic=(ImageView)findViewById(R.id.mic_id);

        micLayout = findViewById(R.id.mic_layout);
        stop = (ImageView) micLayout.findViewById(R.id.iv_stop_record);
        send = (ImageView) micLayout.findViewById(R.id.iv_send);
        cancel = (TextView) micLayout.findViewById(R.id.tv_cancel);
        timer = (TextView) micLayout.findViewById(R.id.ch_record_timer);

        micLayout.setVisibility(View.INVISIBLE);
        random = new Random();


        lvBottomSheet = (ListView) findViewById(R.id.lv_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(lvBottomSheet);
        bottomSheetItem=new ArrayList<>();
        for(int i=0;i<timerList.length;i++) {
            bottomSheetItem.add(new BottomSheetItemModel(timerList[i]));
        }

        adapter = new SheetAdapter(this, bottomSheetItem);
        lvBottomSheet.setAdapter(adapter);

        mic.setOnClickListener(this);
        tvTimer.setOnClickListener(this);

        buttonStart.setOnClickListener(this);
        stop.setOnClickListener(this);
        cancel.setOnClickListener(this);
        buttonPlayLastRecordAudio.setOnClickListener(this);
        buttonStopPlayingRecording.setOnClickListener(this);

        lvBottomSheet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });

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
                           /* minute = 0;
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

        ActivityCompat.requestPermissions(ActivityMic.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {

                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {

                        Toast.makeText(ActivityMic.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ActivityMic.this, "Permission Denied", Toast.LENGTH_LONG).show();

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
        int id=view.getId();
        switch (id){
            case R.id.btn_record:
                startRecording();
                break;
            case R.id.iv_stop_record:
                stopRecording();
                break;
            case R.id.tv_cancel:
                cancelRecording();
                break;
            case R.id.btn_play:
                playRecordedAudio();
                break;
            case R.id.btn_stop_playing:
                stopAudioPlaying();
                break;
            case R.id.timer_id:
                openTimerBottomSheet();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                break;
            case R.id.mic_id:
                openTimerBottomSheet();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        }
    }

    private void openTimerBottomSheet() {
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

    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void startRecording() {
        if (checkPermission()) {

            AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CreateRandomAudioFileName(5) + "AudioRecording.3gp";

            MediaRecorderReady();
            micLayout.setVisibility(View.VISIBLE);
            micLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            send.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.VISIBLE);


            try {
                startTimer();
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            buttonStart.setEnabled(false);
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
            mediaRecorder=null;
            micLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            send.setVisibility(View.VISIBLE);
            stop.setVisibility(View.INVISIBLE);

            stop.setVisibility(View.INVISIBLE);
            send.setVisibility(View.VISIBLE);
            buttonPlayLastRecordAudio.setEnabled(true);
            buttonStart.setEnabled(true);
            buttonStopPlayingRecording.setEnabled(false);

        }
    }
    private void cancelRecording() {
        if(mediaRecorder!=null) {
            mediaRecorder.release();
            if(t!=null){
                t.cancel();
                t=null;
            }
        }
        micLayout.setVisibility(View.INVISIBLE);
        buttonStart.setEnabled(true);

    }
    private void playRecordedAudio() {
        buttonStart.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(true);

        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(AudioSavePathInDevice);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
        micLayout.setVisibility(View.VISIBLE);

        Toast.makeText(ActivityMic.this, "Recording Playing", Toast.LENGTH_LONG).show();

    }
    private void stopAudioPlaying() {
        buttonStart.setEnabled(true);
        buttonStopPlayingRecording.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(true);

        if (mediaPlayer != null) {

            mediaPlayer.stop();
            mediaPlayer.release();

            MediaRecorderReady();

        }
    }


}
