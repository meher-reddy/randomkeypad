package com.app.randomkeypad;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnLongClickListener {


    Button[] buttons = new Button[10];
    List<Integer> l = Arrays.asList(0,1,2,3,4,5,6,7,8,9);
    TextView text;
    Vibrator v;
    ProgressDialog loading;
    ImageButton accessibleb;
    boolean accessiblebool = false, master = true, headset = false;
    BroadcastReceiver broadcastReceiver;
    MediaPlayer a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, back, disable, enable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState );
        setContentView(R.layout.activity_main);

        a0 = MediaPlayer.create(this, R.raw.a0);
        a1 = MediaPlayer.create(this, R.raw.a1);
        a2 = MediaPlayer.create(this, R.raw.a2);
        a3 = MediaPlayer.create(this, R.raw.a3);
        a4 = MediaPlayer.create(this, R.raw.a4);
        a5 = MediaPlayer.create(this, R.raw.a5);
        a6 = MediaPlayer.create(this, R.raw.a6);
        a7 = MediaPlayer.create(this, R.raw.a7);
        a8 = MediaPlayer.create(this, R.raw.a8);
        a9 = MediaPlayer.create(this, R.raw.a9);
        back = MediaPlayer.create(this, R.raw.backspace);
        disable = MediaPlayer.create(this, R.raw.disable);
        enable = MediaPlayer.create(this, R.raw.enable);

        accessibleb = findViewById(R.id.b11);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        text = findViewById(R.id.text);
        buttons[0] = findViewById(R.id.b0);
        buttons[1] = findViewById(R.id.b1);
        buttons[2] = findViewById(R.id.b2);
        buttons[3] = findViewById(R.id.b3);
        buttons[4] = findViewById(R.id.b4);
        buttons[5] = findViewById(R.id.b5);
        buttons[6] = findViewById(R.id.b6);
        buttons[7] = findViewById(R.id.b7);
        buttons[8] = findViewById(R.id.b8);
        buttons[9] = findViewById(R.id.b9);
        for(int i = 0;i<10;i++){
            buttons[i].setOnClickListener(MainActivity.this);
            buttons[i].setOnLongClickListener(MainActivity.this);
        }
        fill();


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                int iii;
                if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
                    iii = intent.getIntExtra("state", -1);
                    if (iii == 0) {
                        headset = false;
                    }
                    if (iii == 1) {
                        headset = true;
                    }
                }
            }
        };
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(broadcastReceiver, receiverFilter);


    }

    void fill(){
        loading = ProgressDialog.show(this, "Please Wait", "Randomizing keyBoard layout...");
        CountDownTimer c = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Collections.shuffle(l);
                for(int i =0;i<l.size();i++){
                    buttons[i].setText(String.valueOf(l.get(i)));
                }
                loading.dismiss();
            }
        };
        c.start();
    }

    public void refresh(View view){
        fill();
    }






    public void backspace(View view){
        if(accessiblebool) vibrate(50);
        if(!text.getText().toString().equals("")){
            if(headset) back.start();
            text.setText(text.getText().toString().substring(0, text.getText().toString().length()-1));
        }
    }


    public void accessible(View view) {

        if(accessibleb.getTag().toString().equals("off")){
            loading = ProgressDialog.show(this, "Please Wait", "Turning on accessibility mode...");
            CountDownTimer c = new CountDownTimer(1000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    vibrate(200);
                }

                @Override
                public void onFinish() {
                    enable.start();
                    accessibleb.setTag("on");
                    accessibleb.setImageResource(R.drawable.accessible);
                    accessiblebool = true;
                    loading.dismiss();
                }
            };
            c.start();
        }
        else if(accessibleb.getTag().toString().equals("on")){
            loading = ProgressDialog.show(this, "Please Wait", "Turning off accessibility mode...");
            CountDownTimer c = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    vibrate(500);
                }

                @Override
                public void onFinish() {
                    disable.start();
                    accessibleb.setTag("off");
                    accessibleb.setImageResource(R.drawable.not_accessible);
                    accessiblebool = false;
                    loading.dismiss();
                }
            };
            c.start();
        }

    }

    void vibrate(int sec){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(sec, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(sec);
        }
    }



    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {

        if (master) {
            if (!accessiblebool) {
                switch (v.getId()) {
                    case R.id.b0: text.setText(text.getText().toString() + "" + buttons[0].getText().toString()); break;
                    case R.id.b1: text.setText(text.getText().toString() + "" + buttons[1].getText().toString()); break;
                    case R.id.b2: text.setText(text.getText().toString() + "" + buttons[2].getText().toString()); break;
                    case R.id.b3: text.setText(text.getText().toString() + "" + buttons[3].getText().toString()); break;
                    case R.id.b4: text.setText(text.getText().toString() + "" + buttons[4].getText().toString()); break;
                    case R.id.b5: text.setText(text.getText().toString() + "" + buttons[5].getText().toString()); break;
                    case R.id.b6: text.setText(text.getText().toString() + "" + buttons[6].getText().toString()); break;
                    case R.id.b7: text.setText(text.getText().toString() + "" + buttons[7].getText().toString()); break;
                    case R.id.b8: text.setText(text.getText().toString() + "" + buttons[8].getText().toString()); break;
                    case R.id.b9: text.setText(text.getText().toString() + "" + buttons[9].getText().toString()); break;
                }
            }
            else if (accessiblebool) {
                master = false;
                for(int i = 0;i<10;i++){
                    if(buttons[i].getId()==v.getId()){

                        int finalI = i;
                        int t = (!buttons[i].getText().toString().equals("0"))?(Integer.parseInt(buttons[i].getText().toString())*130):130;
                        CountDownTimer c = new CountDownTimer(t, 130) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if(buttons[finalI].getText().toString().equals("0"))
                                    vibrate(300);
                                else
                                    vibrate(100);
                            }

                            @Override
                            public void onFinish() {
                                master = true;
                            }
                        };c.start();
                        break;
                    }
                }
                if(headset){
                    master = false;
                    for(int i = 0;i<10;i++){
                        if(buttons[i].getId()==v.getId()){
                            switch (buttons[i].getText().toString()) {
                                case "0" : a0.start(); break;
                                case "1" : a1.start(); break;
                                case "2" : a2.start(); break;
                                case "3" : a3.start(); break;
                                case "4" : a4.start(); break;
                                case "5" : a5.start(); break;
                                case "6" : a6.start(); break;
                                case "7" : a7.start(); break;
                                case "8" : a8.start(); break;
                                case "9" : a9.start(); break;
                            }
                            break;
                        }
                    }
                    master = true;
                }
            }


        }
    }


    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    public boolean onLongClick(View v) {

        if(master){
            switch (v.getId()) {
                case R.id.b0: text.setText(text.getText().toString() + "" + buttons[0].getText().toString()); break;
                case R.id.b1: text.setText(text.getText().toString() + "" + buttons[1].getText().toString()); break;
                case R.id.b2: text.setText(text.getText().toString() + "" + buttons[2].getText().toString()); break;
                case R.id.b3: text.setText(text.getText().toString() + "" + buttons[3].getText().toString()); break;
                case R.id.b4: text.setText(text.getText().toString() + "" + buttons[4].getText().toString()); break;
                case R.id.b5: text.setText(text.getText().toString() + "" + buttons[5].getText().toString()); break;
                case R.id.b6: text.setText(text.getText().toString() + "" + buttons[6].getText().toString()); break;
                case R.id.b7: text.setText(text.getText().toString() + "" + buttons[7].getText().toString()); break;
                case R.id.b8: text.setText(text.getText().toString() + "" + buttons[8].getText().toString()); break;
                case R.id.b9: text.setText(text.getText().toString() + "" + buttons[9].getText().toString()); break;
            }
            return true;
        }
        else return false;
    }
}