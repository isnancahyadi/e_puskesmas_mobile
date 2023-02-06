package com.arcmedtek.e_puskesmas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.activity.DashboardPasien;
import com.arcmedtek.e_puskesmas.model.QueueModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QueueListAdapter extends RecyclerView.Adapter<QueueListAdapter.QueueListHolder> {

    ArrayList<QueueModel> _queueModels;
    Context _context;

    public QueueListAdapter(ArrayList<QueueModel> _queueModels, Context _context) {
        this._queueModels = _queueModels;
        this._context = _context;
    }

    @NonNull
    @Override
    public QueueListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.list_item_queue_checkup, parent, false);

        return new QueueListHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull QueueListHolder holder, int position) {
        QueueModel model = _queueModels.get(position);
        String doctorFullname, patientFullName, date;

//        if (model.getDokFrontDegree().isEmpty() && model.getDokLastDegree().isEmpty()) {
//            if (model.getDokMiddleName().isEmpty() && model.getDokLastName().isEmpty()) {
//                doctorFullname = model.getDokFirstName();
//            } else if (model.getDokMiddleName().isEmpty()) {
//                doctorFullname = model.getDokFirstName() + " " + model.getDokLastName();
//            } else if (model.getDokLastName().isEmpty()) {
//                doctorFullname = model.getDokFirstName() + " " + model.getDokMiddleName();
//            } else {
//                doctorFullname = model.getDokFirstName() + " " + model.getDokMiddleName() + " " + model.getDokLastName();
//            }
//        } else if (model.getDokFrontDegree().isEmpty()) {
//            if (model.getDokMiddleName().isEmpty() && model.getDokLastName().isEmpty()) {
//                doctorFullname = model.getDokFirstName() + ", " + model.getDokLastDegree();
//            } else if (model.getDokMiddleName().isEmpty()) {
//                doctorFullname = model.getDokFirstName() + " " + model.getDokLastName() + ", " + model.getDokLastDegree();
//            } else if (model.getDokLastName().isEmpty()) {
//                doctorFullname = model.getDokFirstName() + " " + model.getDokMiddleName() + ", " + model.getDokLastDegree();
//            } else {
//                doctorFullname = model.getDokFirstName() + " " + model.getDokMiddleName() + " " + model.getDokLastName() + ", " + model.getDokLastDegree();
//            }
//        } else if (model.getDokLastDegree().isEmpty()) {
//            if (model.getDokMiddleName().isEmpty() && model.getDokLastName().isEmpty()) {
//                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName();
//            } else if (model.getDokMiddleName().isEmpty()) {
//                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName() + " " + model.getDokLastName();
//            } else if (model.getDokLastName().isEmpty()) {
//                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName() + " " + model.getDokMiddleName();
//            } else {
//                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName() + " " + model.getDokMiddleName() + " " + model.getDokLastName();
//            }
//        } else {
//            if (model.getDokMiddleName().isEmpty() && model.getDokLastName().isEmpty()) {
//                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName() + ", " + model.getDokLastDegree();
//            } else if (model.getDokMiddleName().isEmpty()) {
//                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName() + " " + model.getDokLastName() + ", " + model.getDokLastDegree();
//            } else if (model.getDokLastName().isEmpty()) {
//                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName() + " " + model.getDokMiddleName() + ", " + model.getDokLastDegree();
//            } else {
//                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName() + " " + model.getDokMiddleName() + " " + model.getDokLastName() + ", " + model.getDokLastDegree();
//            }
//        }

        if (model.getPasMiddleName().isEmpty() && model.getPasLastName().isEmpty()) {
            patientFullName = model.getPasFirstName();
        } else if (model.getPasMiddleName().isEmpty()) {
            patientFullName = model.getPasFirstName() + " " + model.getPasLastName();
        } else if (model.getPasLastName().isEmpty()) {
            patientFullName = model.getPasFirstName() + " " + model.getPasMiddleName();
        } else {
            patientFullName = model.getPasFirstName() + " " + model.getPasMiddleName() + " " + model.getPasLastName();
        }

        date = model.getDay() + ", " + convertToAppDate(model.getDate());

        holder._dateTime.setText(date);
        holder._poly.setText("Poli " + model.getPoly());
        holder.itemView.setOnClickListener(v -> {
            queueNum(v, patientFullName, date, model.getPatientType(), model.getQueueNum(), model.getPoly());
        });
    }

    @Override
    public int getItemCount() {
        return _queueModels.size();
    }

    public class QueueListHolder extends RecyclerView.ViewHolder {

        TextView _dateTime, _poly;

        public QueueListHolder(@NonNull View itemView) {
            super(itemView);

            _dateTime = itemView.findViewById(R.id.date_time_checkup);
            _poly = itemView.findViewById(R.id.poly_checkup);
        }
    }

    @SuppressLint("SetTextI18n")
    private void queueNum(View v, String patientFullName, String date, String patientType, String queueNum, String polyName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(_context, R.style.AlertDialogStyle);
        View queueDialog = LayoutInflater.from(_context).inflate(R.layout.queue_dialog, v.findViewById(R.id.queue_dialog));
        builder.setView(queueDialog);

        TextView ticketDate = queueDialog.findViewById(R.id.ticket_date);
        TextView ticketPatientStatus = queueDialog.findViewById(R.id.ticket_patient_status);
        TextView ticketPatientName = queueDialog.findViewById(R.id.ticket_patient_name);
        TextView ticketQueue = queueDialog.findViewById(R.id.ticket_queue);
        TextView ticketPolyName = queueDialog.findViewById(R.id.ticket_poly_name);
//        TextView ticketDoctorName = queueDialog.findViewById(R.id.ticket_doctor_name);

        String txtStatus = "";

        switch (patientType) {
            case "0":
                txtStatus = "Non-BPJS";
                break;
            case "1":
                txtStatus = "BPJS";
                break;
        }

        ticketDate.setText(date);
        ticketPatientStatus.setText("Pasien " + txtStatus);
        ticketPatientName.setText(patientFullName);
        ticketQueue.setText(queueNum);
        ticketPolyName.setText("Poli " + polyName);
//        ticketDoctorName.setText(doctorFullname);

        final AlertDialog alertDialog = builder.create();

        queueDialog.findViewById(R.id.btn_oke).setOnClickListener(v2 -> {
            alertDialog.dismiss();
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    private String convertToAppDate(String dateTime) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(dateTime);
            str = outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
