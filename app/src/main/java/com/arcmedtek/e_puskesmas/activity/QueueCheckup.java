package com.arcmedtek.e_puskesmas.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.adapter.QueueListAdapter;
import com.arcmedtek.e_puskesmas.model.QueueModel;
import com.arcmedtek.e_puskesmas.service.EPuskesmasDataService;

import java.util.ArrayList;

public class QueueCheckup extends AppCompatActivity {

    RecyclerView _queueRecycler;
    ImageView _btnBack;

    QueueListAdapter _queueListAdapter;

    EPuskesmasDataService _ePuskesmasDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_checkup);

        _queueRecycler = findViewById(R.id.list_queue);
        _btnBack = findViewById(R.id.btn_back);

        _ePuskesmasDataService = new EPuskesmasDataService(this);

        _ePuskesmasDataService.getPatientQueue(new EPuskesmasDataService.GetPatientQueue() {
            @Override
            public void onResponse(ArrayList<QueueModel> dataPatientQueue) {
                setPatientQueueRecycler(dataPatientQueue);
            }

            @Override
            public void onError(String message) {

            }
        });

        _btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void setPatientQueueRecycler(ArrayList<QueueModel> dataPatientQueue) {
        _queueListAdapter = new QueueListAdapter(dataPatientQueue, QueueCheckup.this);
        _queueRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        _queueRecycler.setAdapter(_queueListAdapter);
    }
}