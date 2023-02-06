package com.arcmedtek.e_puskesmas.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.arcmedtek.e_puskesmas.fragment.HomeFragment;

public class QueueCountService extends Service {

    EPuskesmasDataService _ePuskesmasDataService;

    @Override
    public void onCreate() {
        super.onCreate();

        _ePuskesmasDataService = new EPuskesmasDataService(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String poly = intent.getStringExtra("poly_name");

        switch (poly) {
            case "umum":
                _ePuskesmasDataService.getQueueGeneralPoly(new EPuskesmasDataService.GetQueueGeneralPoly() {
                    @Override
                    public void onResponse(String queueNum) {
                        Bundle bundle = new Bundle();
                        bundle.putString("queue_num", queueNum);

                        HomeFragment homeFragment = new HomeFragment();
                        homeFragment.setArguments(bundle);

//                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, homeFragment, 0);

                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(QueueCountService.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "anak":

                break;
            case "kb":

                break;
            case "kia":

                break;
        }
//        startForeground(1);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
