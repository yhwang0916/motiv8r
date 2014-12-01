package com.example.shreya.motiv8r;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;

import java.lang.ref.WeakReference;


public class MQTTService extends Service
{
    public MqttPublishSample mqttPublishSample = null;
    public class LocalBinder<S> extends Binder
    {
        private WeakReference<S> mService;

        public LocalBinder(S service)
        {
            mService = new WeakReference<S>(service);
        }
        public S getService()
        {
            return mService.get();
        }
        public void close()
        {
            mService = null;
        }
    }

    private LocalBinder<MQTTService> mBinder;

    @Override
    public void onCreate()
    {
        //note: Service not created because function was overrided
        super.onCreate();
        // reset status variable to initial state

        mBinder = new LocalBinder<MQTTService>(this);

    }


    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isOnline()){
                    mqttPublishSample = new MqttPublishSample();
                }

//                    handleStart(intent, startId);
            }
        }, "MQTTservice").start();

        // return START_NOT_STICKY - we want this Service to be left running
        //  unless explicitly stopped, and it's process is killed, we want it to
        //  be restarted
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isAvailable() &&
                cm.getActiveNetworkInfo().isConnected())
        {
            return true;
        }
        return false;
    }
}
