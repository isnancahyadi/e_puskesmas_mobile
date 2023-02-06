package com.arcmedtek.e_puskesmas.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.activity.PolyRegistration;
import com.arcmedtek.e_puskesmas.activity.QueueCheckup;
import com.arcmedtek.e_puskesmas.model.QueueModel;
import com.arcmedtek.e_puskesmas.service.EPuskesmasDataService;
import com.arcmedtek.e_puskesmas.service.QueueCountService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    TextView _patientName, _nik, _queueNumMine, _queueNumNow, _queuePolyName;
    ImageView _btnGeneralPoly, _btnKbPoly, _btnKiaPoly, _btnChildPoly, _btnNotification, _dotNotification, _bannerPoly;
    //    LinearLayout _dashboardQueueContainer;
    CardView _queueCountContainer;

    String nik, firstName, middleName, lastName;
    String queueNumNow;

    NotificationManagerCompat notificationManager;

    EPuskesmasDataService _ePuskesmasDataService;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _patientName = view.findViewById(R.id.patient_name);
        _nik = view.findViewById(R.id.nik);
//        _dashboardQueueContainer = view.findViewById(R.id.dashboard_queue_container);
        _btnGeneralPoly = view.findViewById(R.id.general_poly);
        _btnKbPoly = view.findViewById(R.id.kb_poly);
        _btnKiaPoly = view.findViewById(R.id.kia_poly);
        _btnChildPoly = view.findViewById(R.id.child_poly);
        _btnNotification = view.findViewById(R.id.btn_notification);
        _dotNotification = view.findViewById(R.id.dot_notification);
        _queueNumMine = view.findViewById(R.id.queue_num_mine);
        _queueNumNow = view.findViewById(R.id.queue_num_now);
        _queuePolyName = view.findViewById(R.id.queue_poly_name);
        _bannerPoly = view.findViewById(R.id.banner_poly);
        _queueCountContainer = view.findViewById(R.id.queue_count_container);

        notificationManager = NotificationManagerCompat.from(getContext());

        _ePuskesmasDataService = new EPuskesmasDataService(getContext());

        if (getArguments() != null) {
            nik = getArguments().getString("NIK");
            firstName = getArguments().getString("First_Name");
            middleName = getArguments().getString("Middle_Name");
            lastName = getArguments().getString("Last_Name");
        }

        showProfile(nik, firstName, middleName, lastName);

        _btnGeneralPoly.setOnClickListener(v -> polyRegistration("umum"));
        _btnKbPoly.setOnClickListener(v -> polyRegistration("kb"));
        _btnKiaPoly.setOnClickListener(v -> polyRegistration("kia"));
        _btnChildPoly.setOnClickListener(v -> polyRegistration("anak"));

        _ePuskesmasDataService.getPatientQueue(new EPuskesmasDataService.GetPatientQueue() {
            @Override
            public void onResponse(ArrayList<QueueModel> dataPatientQueue) {
                if (!dataPatientQueue.isEmpty()) {
                    _dotNotification.setVisibility(View.VISIBLE);
//                    _dashboardQueueContainer.setVisibility(View.VISIBLE);
//                    activeNotification();
                    showQueueCount(dataPatientQueue);
                }
            }

            @Override
            public void onError(String message) {

            }
        });

        _btnNotification.setOnClickListener(v -> queueCheckup());
    }

    private void activeNotification() {

    }

    @SuppressLint("SetTextI18n")
    private void showQueueCount(ArrayList<QueueModel> dataPatientQueue) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = sdf.format(new Date());
        String poly = "";

//        for (int i = 0; i < dataPatientQueue.size(); i++) {
        if (dataPatientQueue.get(0).getDate().equals(currentDateTime)) {
            _bannerPoly.setVisibility(View.GONE);
            _queueCountContainer.setVisibility(View.VISIBLE);
            _queueNumMine.setText(dataPatientQueue.get(0).getQueueNum());
            _queuePolyName.setText("Poli " + dataPatientQueue.get(0).getPoly());
            poly = dataPatientQueue.get(0).getPoly();
            new QueueCountTask().execute(poly.toLowerCase());
            new GetQueueNow() {
                @Override
                public void onGet(String queueNow) {
                    Toast.makeText(getContext(), "Antrian saat ini : " + queueNow, Toast.LENGTH_SHORT).show();
                }
            };
//            Intent queueCountService = new Intent(getContext(), QueueCountService.class);
//            queueCountService.putExtra("poly_name", poly.toLowerCase());
//            ContextCompat.startForegroundService(getContext(), queueCountService);
//            Toast.makeText(getContext(), "Antrian saat ini : " + queueNumNow, Toast.LENGTH_SHORT).show();
        }

//        String queueNumNow = "";
//
//        if (getArguments() != null) {
//            queueNumNow = getArguments().getString("queue_num");
//        }
//
//        _queueNumNow.setText(queueNumNow);
//        }

//        switch (poly.toLowerCase()) {
//            case "umum":
//                queueCountUmum();
//                break;
//            case "anak":
//                queueCountAnak();
//                break;
//            case "kb":
//                queueCountKb();
//                break;
//            case "kia":
//                queueCountKia();
//                break;
//        }

        String queueNumMine = _queueNumMine.getText().toString();

//        if (queueNumNow.equals(queueNumMine)) {
//            Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
//                    .setSmallIcon(R.drawable.logo_puskesmas_kabupaten_bengkulu)
//                    .setContentTitle("Panggilan Antrian")
//                    .setContentText("Nomor antrian anda telah dipanggil")
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setCategory(NotificationCompat.CATEGORY_EVENT)
//                    .build();
//
//            notificationManager.notify(1, notification);
//        }

//        refresh(1000, poly.toLowerCase());
    }

//    private void queueCountKia() {
//        _ePuskesmasDataService.getQueueKiaPoly(new EPuskesmasDataService.GetQueueKiaPoly() {
//            @Override
//            public void onResponse(String queueNum) {
//                _queueNumNow.setText(queueNum);
//                queueNumNow = _queueNumNow.getText().toString();
//                refresh(1000, "kia");
//            }
//
//            @Override
//            public void onError(String message) {
//                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void queueCountKb() {
//        _ePuskesmasDataService.getQueueKbPoly(new EPuskesmasDataService.GetQueueKbPoly() {
//            @Override
//            public void onResponse(String queueNum) {
//                _queueNumNow.setText(queueNum);
//                queueNumNow = _queueNumNow.getText().toString();
//                refresh(1000, "kb");
//            }
//
//            @Override
//            public void onError(String message) {
//                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void queueCountAnak() {
//        _ePuskesmasDataService.getQueueChildPoly(new EPuskesmasDataService.GetQueueChildPoly() {
//            @Override
//            public void onResponse(String queueNum) {
//                _queueNumNow.setText(queueNum);
//                queueNumNow = _queueNumNow.getText().toString();
//                refresh(1000, "anak");
//            }
//
//            @Override
//            public void onError(String message) {
//                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void queueCountUmum() {
//        _ePuskesmasDataService.getQueueGeneralPoly(new EPuskesmasDataService.GetQueueGeneralPoly() {
//            @Override
//            public void onResponse(String queueNum) {
////                _queueNumNow.setText(queueNum);
////                queueNumNow = _queueNumNow.getText().toString();
////                refresh(1000, "umum");
////                new QueueCountTask().execute(queueNum);
//            }
//
//            @Override
//            public void onError(String message) {
//                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void queueCheckup() {
        Intent intent = new Intent(getContext(), QueueCheckup.class);
        startActivity(intent);
    }

    private void showProfile(String nik, String firstName, String middleName, String lastName) {
        String fullname;

        if (middleName.equals("")) {
            fullname = firstName + " " + lastName;
        } else {
            fullname = firstName + " " + middleName + " " + lastName;
        }

        _patientName.setText(fullname);
        _nik.setText(nik);
    }

    private void polyRegistration(String polyName) {
        Intent intent = new Intent(getContext(), PolyRegistration.class);
        intent.putExtra("poly_name", polyName);
        startActivity(intent);
    }

//    private void refresh(int milliseconds, String poly) {
//        final Handler handler = new Handler();
//
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                switch (poly) {
//                    case "umum":
//                        queueCountUmum();
//                        break;
//                    case "anak":
//                        queueCountAnak();
//                        break;
//                    case "kb":
//                        queueCountKb();
//                        break;
//                    case "kia":
//                        queueCountKia();
//                        break;
//                }
////                activeNotification();
//            }
//        };
//
//        handler.postDelayed(runnable, milliseconds);
//    }

    public interface GetQueueNow {
        void onGet(String queueNow);
    }

    class QueueCountTask extends AsyncTask<String, String, Void> {
//        String countNow;

        GetQueueNow getQueueNow;

        @Override
        protected Void doInBackground(String... strings) {
            Looper.prepare();
            switch (strings[0]) {
                case "umum":

                    Handler handler = new Handler();

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            _ePuskesmasDataService.getQueueGeneralPoly(new EPuskesmasDataService.GetQueueGeneralPoly() {
                                @Override
                                public void onResponse(String queueNum) {
//                                    Toast.makeText(getContext(), "Antrian : " + queueNum, Toast.LENGTH_SHORT).show();
//                                    countNow = queueNum;
                                    publishProgress(queueNum);
                                    getQueueNow.onGet(queueNum);
                                }

                                @Override
                                public void onError(String message) {
                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            });
                            handler.postDelayed(this, 1000);
                        }
                    };
                    handler.post(runnable);
                    break;
                case "anak":
//                    queueCountAnak();
                    break;
                case "kb":
//                    queueCountKb();
                    break;
                case "kia":
//                    queueCountKia();
                    break;
            }

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            Looper.loop();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            _queueNumNow.setText(values[0]);
//            Toast.makeText(getContext(), "Antrian saat ini : " + _queueNumNow.getText(), Toast.LENGTH_SHORT).show();
//            queueNumNow = _queueNumNow.getText().toString();
//            super.onProgressUpdate(values);
        }

//        @Override
//        protected void onPostExecute(String unused) {
//            Toast.makeText(getContext(), "Antrian saat ini : " + unused, Toast.LENGTH_SHORT).show();
//        }
    }
}