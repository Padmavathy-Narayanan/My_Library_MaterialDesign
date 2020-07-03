package com.padmavathy.mylibrary.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.utils.MyThread;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;
    ImageView img;
    TextView a, slogan;
    Button b;
    //Animations
    Animation topAnimantion, bottomAnimation, middleAnimation;
    MyThread thread;
    boolean firstRun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        final SharedPreferences settings = getSharedPreferences("prefs", 0);

        firstRun = settings.getBoolean("firstRun", true);

        /*Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);*/

        transparentStatusAndNavigation();
       /* thread = new MyThread(this);
        thread.start();*/

        a = findViewById(R.id.tv1);
        slogan = findViewById(R.id.tv2);
        img = findViewById(R.id.splash);
        b = findViewById(R.id.btn);

        if(firstRun) {

            SharedPreferences.Editor editor = settings.edit();
            firstRun =  false;
            editor.putBoolean("firstRun",firstRun);
            editor.commit();
            //Animation Calls
            topAnimantion = AnimationUtils.loadAnimation(this, R.anim.top_animantion);
            bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animantion);
            middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);

            //-----------Setting Animations to the elements of Splash
            img.setAnimation(topAnimantion);
            a.setAnimation(middleAnimation);
            slogan.setAnimation(middleAnimation);
            b.setAnimation(bottomAnimation);


        /*//Splash Screen Code to call new Activity after some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Calling new Activity
                Intent intent = new Intent(MainActivity.this, BottomNavActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);*/

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("firstRun", false);
                    editor.commit();
               /* if (thread != null && thread.isAlive()) {
                    thread.bRun = false;
                }
                Intent intent = new Intent(MainActivity.this, BottomNavActivity.class);
                startActivity(intent);
                finish();*/
                    finish();

                    Intent intent = new Intent(MainActivity.this,
                            BottomNavActivity.class);
                    startActivity(intent);

                }
            });
        }
        else {
            Intent intent = new Intent(MainActivity.this,
                    BottomNavActivity.class);
            startActivity(intent);
        }
    }

    private void transparentStatusAndNavigation() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


   /* private class SplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
              //  Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            finish();

            Intent intent = new Intent(MainActivity.this,
                    BottomNavActivity.class);
            startActivity(intent);

        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        boolean firstRun = settings.getBoolean("firstRun", true);
        if (!firstRun) {
            Intent intent = new Intent(this, BottomNavActivity.class);
            startActivity(intent);
            Log.d("TAG1", "firstRun(false): " + Boolean.valueOf(firstRun).toString());
        } else {
            Log.d("TAG1", "firstRun(true): " + Boolean.valueOf(firstRun).toString());
        }
    }
}
