package com.arcmedtek.e_puskesmas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.model.DoctorScheduleModel;

import java.util.ArrayList;
import java.util.List;

public class DoctorScheduleRecyclerViewAdapter extends RecyclerView.Adapter<DoctorScheduleRecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<DoctorScheduleModel> doctorScheduleList;

    public DoctorScheduleRecyclerViewAdapter(Context context, ArrayList<DoctorScheduleModel> doctorScheduleList) {
        this.context = context;
        this.doctorScheduleList = doctorScheduleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_doctor_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (doctorScheduleList != null && doctorScheduleList.size() > 0) {
            String fullname;

            DoctorScheduleModel model = doctorScheduleList.get(position);
            holder.scheduleDay.setText(model.getDay());
            if (model.getFrontDegree().equals("") && model.getLastDegree().equals("")) {
                if (model.getMiddleName().equals("") && model.getLastName().equals("")) {
                    fullname = model.getFirstName();
                } else if (model.getMiddleName().equals("")) {
                    fullname = model.getFirstName() + " " + model.getLastName();
                } else if (model.getLastName().equals("")) {
                    fullname = model.getFirstName() + " " + model.getMiddleName();
                } else {
                    fullname = model.getFirstName() + " " + model.getMiddleName() + " " + model.getLastName();
                }
            } else if (model.getFrontDegree().equals("")) {
                if (model.getMiddleName().equals("") && model.getLastName().equals("")) {
                    fullname = model.getFirstName() + ", " + model.getLastDegree();
                } else if (model.getMiddleName().equals("")) {
                    fullname = model.getFirstName() + " " + model.getLastName() + ", " + model.getLastDegree();
                } else if (model.getLastName().equals("")) {
                    fullname = model.getFirstName() + " " + model.getMiddleName() + ", " + model.getLastDegree();
                } else {
                    fullname = model.getFirstName() + " " + model.getMiddleName() + " " + model.getLastName() + ", " + model.getLastDegree();
                }
            } else if (model.getLastDegree().equals("")) {
                if (model.getMiddleName().equals("") && model.getLastName().equals("")) {
                    fullname = model.getFrontDegree() + ". " + model.getFirstName();
                } else if (model.getMiddleName().equals("")) {
                    fullname = model.getFrontDegree() + ". " + model.getFirstName() + " " + model.getLastName();
                } else if (model.getLastName().equals("")) {
                    fullname = model.getFrontDegree() + ". " + model.getFirstName() + " " + model.getMiddleName();
                } else {
                    fullname = model.getFrontDegree() + ". " + model.getFirstName() + " " + model.getMiddleName() + " " + model.getLastName();
                }
            } else {
                if (model.getMiddleName().equals("") && model.getLastName().equals("")) {
                    fullname = model.getFrontDegree() + ". " + model.getFirstName() + ", " + model.getLastDegree();
                } else if (model.getMiddleName().equals("")) {
                    fullname = model.getFrontDegree() + ". " + model.getFirstName() + " " + model.getLastName() + ", " + model.getLastDegree();
                } else if (model.getLastName().equals("")) {
                    fullname = model.getFrontDegree() + ". " + model.getFirstName() + " " + model.getMiddleName() + ", " + model.getLastDegree();
                } else {
                    fullname = model.getFrontDegree() + ". " + model.getFirstName() + " " + model.getMiddleName() + " " + model.getLastName() + ", " + model.getLastDegree();
                }
            }

            holder.scheduleDoctor.setText(fullname);
        } else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return doctorScheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView scheduleDay, scheduleDoctor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            scheduleDay = itemView.findViewById(R.id.schedule_day);
            scheduleDoctor = itemView.findViewById(R.id.schedule_doctor);
        }
    }
}
