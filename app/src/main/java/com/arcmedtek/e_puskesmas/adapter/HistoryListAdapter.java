package com.arcmedtek.e_puskesmas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.PrecomputedText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.model.HistoryModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.HistoryListHolder> {

    ArrayList<HistoryModel> _historyModels;
    Context _context;

    public HistoryListAdapter(ArrayList<HistoryModel> _historyModels, Context _context) {
        this._historyModels = _historyModels;
        this._context = _context;
    }

    @NonNull
    @Override
    public HistoryListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.list_item_history_patient, parent, false);

        return new HistoryListHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryListHolder holder, int position) {
        HistoryModel model = _historyModels.get(position);

        String doctorFullname;

        if (model.getDokFrontDegree().isEmpty() && model.getDokLastDegree().isEmpty()) {
            if (model.getDokMiddleName().isEmpty() && model.getDokLastName().isEmpty()) {
                doctorFullname = model.getDokFirstName();
            } else if (model.getDokMiddleName().isEmpty()) {
                doctorFullname = model.getDokFirstName() + " " + model.getDokLastName();
            } else if (model.getDokLastName().isEmpty()) {
                doctorFullname = model.getDokFirstName() + " " + model.getDokMiddleName();
            } else {
                doctorFullname = model.getDokFirstName() + " " + model.getDokMiddleName() + " " + model.getDokLastName();
            }
        } else if (model.getDokFrontDegree().isEmpty()) {
            if (model.getDokMiddleName().isEmpty() && model.getDokLastName().isEmpty()) {
                doctorFullname = model.getDokFirstName() + ", " + model.getDokLastDegree();
            } else if (model.getDokMiddleName().isEmpty()) {
                doctorFullname = model.getDokFirstName() + " " + model.getDokLastName() + ", " + model.getDokLastDegree();
            } else if (model.getDokLastName().isEmpty()) {
                doctorFullname = model.getDokFirstName() + " " + model.getDokMiddleName() + ", " + model.getDokLastDegree();
            } else {
                doctorFullname = model.getDokFirstName() + " " + model.getDokMiddleName() + " " + model.getDokLastName() + ", " + model.getDokLastDegree();
            }
        } else if (model.getDokLastDegree().isEmpty()) {
            if (model.getDokMiddleName().isEmpty() && model.getDokLastName().isEmpty()) {
                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName();
            } else if (model.getDokMiddleName().isEmpty()) {
                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName() + " " + model.getDokLastName();
            } else if (model.getDokLastName().isEmpty()) {
                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName() + " " + model.getDokMiddleName();
            } else {
                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName() + " " + model.getDokMiddleName() + " " + model.getDokLastName();
            }
        } else {
            if (model.getDokMiddleName().isEmpty() && model.getDokLastName().isEmpty()) {
                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName() + ", " + model.getDokLastDegree();
            } else if (model.getDokMiddleName().isEmpty()) {
                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName() + " " + model.getDokLastName() + ", " + model.getDokLastDegree();
            } else if (model.getDokLastName().isEmpty()) {
                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName() + " " + model.getDokMiddleName() + ", " + model.getDokLastDegree();
            } else {
                doctorFullname = model.getDokFrontDegree() + ". " + model.getDokFirstName() + " " + model.getDokMiddleName() + " " + model.getDokLastName() + ", " + model.getDokLastDegree();
            }
        }

        holder._dayDate.setText(model.getDay() + ", " + convertToAppDate(model.getDate()));
        holder._poly.setText("Poli " + model.getPoly());
        holder._doctor.setText(doctorFullname);
        holder._complaint.setText(model.getComplaint());
        holder._diagnosis.setText(model.getDiagnosis());
        holder._medicine.setText(model.getMedicine());
        holder._note.setText(model.getNote());

        boolean isExpand = _historyModels.get(position).isExpand();
        if (isExpand) {
            holder._expandHistory.setVisibility(View.VISIBLE);
            holder._icExpand.setImageResource(R.drawable.ic_arrow_up_32);
        } else {
            holder._expandHistory.setVisibility(View.GONE);
            holder._icExpand.setImageResource(R.drawable.ic_arrow_down_32);
        }
    }

    @Override
    public int getItemCount() {
        return _historyModels.size();
    }

    public class HistoryListHolder extends RecyclerView.ViewHolder {

        TextView _dayDate, _poly, _doctor, _complaint, _diagnosis, _medicine, _note;
        ImageView _icExpand;
        RelativeLayout _historyTitle;
        LinearLayout _expandHistory;

        public HistoryListHolder(@NonNull View itemView) {
            super(itemView);

            _dayDate = itemView.findViewById(R.id.date_time_history);
            _poly = itemView.findViewById(R.id.poly_history);
            _doctor = itemView.findViewById(R.id.txt_doctor);
            _complaint = itemView.findViewById(R.id.txt_complaint);
            _diagnosis = itemView.findViewById(R.id.txt_diagnosis);
            _medicine = itemView.findViewById(R.id.txt_medicine);
            _note = itemView.findViewById(R.id.txt_note);
            _icExpand = itemView.findViewById(R.id.icon_expand);
            _historyTitle = itemView.findViewById(R.id.history_content);
            _expandHistory = itemView.findViewById(R.id.expandable_history_content);

            _historyTitle.setOnClickListener(v -> {
                HistoryModel model = _historyModels.get(getAdapterPosition());
                model.setExpand(!model.isExpand());
                notifyItemChanged(getAdapterPosition());
            });
        }
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
