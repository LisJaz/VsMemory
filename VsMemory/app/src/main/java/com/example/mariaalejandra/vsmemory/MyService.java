package com.example.mariaalejandra.vsmemory;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    private final String TAG = "MyService";
    private Thread t;
    private final int seconds = 3;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
        Log.v("TAG", "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        Log.v("TAG", "onStart");

        final int currentId = startId;

        Runnable r = new Runnable() {
            public void run() {

                Log.d(TAG, "Service " + currentId + " started");

                for (int i = 0; i < 5; i++)
                {
                    //For our sample, we just sleep for 5 seconds
                    long endTime = System.currentTimeMillis() + seconds*1000;
                    while (System.currentTimeMillis() < endTime) {
                        synchronized (this) {
                            try {
                                wait(endTime -
                                        System.currentTimeMillis());
                            } catch (Exception e) {
                            }
                        }
                    }
                    Log.d(TAG, "Service " + currentId+"   cnt: "+ (i+1));
                    //Log.d(TAG, "Service running, id:" + currentId +"cnt: ");
                }
                Log.d(TAG, "Service " + currentId + " finishing");
                stopSelf();

            }
        };

        t = new Thread(r);
        t.start();

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        Log.v("TAG", "onDestroy");
        super.onDestroy();
    }
}
