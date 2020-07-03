package com.padmavathy.mylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.padmavathy.mylibrary.activity.BottomNavActivity;

public class MyThread extends Thread{
    public boolean bRun = true;
    Context context;

    public MyThread(Context context){
        this.context = context;
    }

    @Override
    public void run()
    {
        try
        {
            sleep(3200);
            if (bRun)
            {
                Intent i1 = new Intent (context, BottomNavActivity.class);
                context.startActivity(i1);
//                startActivity(new Intent(getApplicationContext(), BottomNavActivity.class));
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
