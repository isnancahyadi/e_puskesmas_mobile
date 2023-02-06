package com.arcmedtek.e_puskesmas.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.activity.DashboardPasien;
import com.arcmedtek.e_puskesmas.adapter.DoctorScheduleRecyclerViewAdapter;
import com.arcmedtek.e_puskesmas.config.DateValidatorWeekdays;
import com.arcmedtek.e_puskesmas.model.DoctorScheduleModel;
import com.arcmedtek.e_puskesmas.service.EPuskesmasDataService;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class PolyRegistrationFragment extends Fragment {

    TextView _txtPolyName;
    TextInputEditText _inetMedicalDate;
    RecyclerView _doctorScheduleRecyclerView;
    DoctorScheduleRecyclerViewAdapter _doctorScheduleAdapter;
    Button _btnRegistPoly;
    ImageView _btnBack;

//    DatePickerDialog.OnDateSetListener tgl;
//    DatePickerDialog datePickerDialog;
//    Calendar myCalendar;

    MaterialDatePicker materialDatePicker;
    MaterialDatePicker.Builder builder;

    EPuskesmasDataService _ePuskesmasDataService;

    String polyName, patientType;

    public PolyRegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poly_registration, container, false);
    }

    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _txtPolyName = view.findViewById(R.id.txt_poly);
        _inetMedicalDate = view.findViewById(R.id.inet_medical_date);
        _doctorScheduleRecyclerView = view.findViewById(R.id.doctor_schedule_recyclerview);
        _btnRegistPoly = view.findViewById(R.id.btn_regist_poly);
        _btnBack = view.findViewById(R.id.btn_back_poly_registration);

        _ePuskesmasDataService = new EPuskesmasDataService(getContext());

        if (getArguments() != null) {
            polyName = getArguments().getString("Poly_Name");
            patientType = getArguments().getString("Patient_Type");
        }

        switch (polyName) {
            case "umum":
                _txtPolyName.setText("Poli Umum");
                break;
            case "kb":
                _txtPolyName.setText("Poli KB");
                break;
            case "kia":
                _txtPolyName.setText("Poli KIA");
                break;
            case "anak":
                _txtPolyName.setText("Poli Anak");
                break;
        }

//        myCalendar = Calendar.getInstance();
//
//        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//                String myFormat = "EEEE, dd MMMM yyyy";
//                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("id", "ID"));
//                _inetMedicalDate.setText(sdf.format(myCalendar.getTime()));
//            }
//        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

//        Calendar weekDays = Calendar.getInstance();
////        int[] setWeekDays = new int[6];
////        setWeekDays[0] = Calendar.MONDAY;
//        int dayOfWeek = weekDays.get(Calendar.DAY_OF_WEEK);
//
//        Toast.makeText(getContext(), "day : " + dayOfWeek, Toast.LENGTH_SHORT).show();

//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
//        myCalendar.add(Calendar.DAY_OF_WEEK, 7);
//        datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
//        datePickerDialog.getDatePicker().setSe

//        Calendar calendar = Calendar.getInstance();
//
////        calendar.set(Calendar.DAY_OF_WEEK, 0);
////        long start = calendar.getTimeInMillis();
//
//        calendar.setTimeInMillis(MaterialDatePicker.todayInUtcMilliseconds());
//        calendar.set(Calendar.DATE, 7);
//        long end = calendar.getTimeInMillis();

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
//        constraintBuilder.setStart(MaterialDatePicker.todayInUtcMilliseconds());
//        constraintBuilder.setEnd(end);
        constraintBuilder.setValidator(new DateValidatorWeekdays());
////        constraintBuilder.setValidator(DateValidatorPointForward.from(start));
////        constraintBuilder.setValidator(DateValidatorPointBackward.before(end));

        builder = MaterialDatePicker.Builder.datePicker();
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        builder.setCalendarConstraints(constraintBuilder.build());

        materialDatePicker = builder.build();

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                cal.setTimeInMillis(selection);

                String myFormat = "EEEE, dd MMMM yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("id", "ID"));
                _inetMedicalDate.setText(sdf.format(cal.getTime()));
            }
        });

        _inetMedicalDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
//                datePickerDialog.show();
                materialDatePicker.show(getParentFragmentManager(), "DATE_PICKER");
            }
        });

        _inetMedicalDate.setOnClickListener(v -> {
//            datePickerDialog.show();
            materialDatePicker.show(getParentFragmentManager(), "DATE_PICKER");
        });

        _ePuskesmasDataService.getDoctorSchedule(polyName, new EPuskesmasDataService.GetDoctorSchedule() {
            @Override
            public void onResponse(ArrayList<DoctorScheduleModel> dataDoctorSchedule) {
                setDoctorScheduleRecyclerView(dataDoctorSchedule);
            }

            @Override
            public void onError(String message) {

            }
        });
        
        _btnRegistPoly.setOnClickListener(v -> regist());

        _btnBack.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void regist() {
        String medicalDate = String.valueOf(_inetMedicalDate.getText());
        String[] parts = medicalDate.split(", ");
        String _patientType = patientType.equals("non_bpjs") ? "0" : "1";
        String dayMed = parts[0].toLowerCase();
        String dateMed = convertToDBDate(parts[1]);

        _ePuskesmasDataService.postPolyRegistration(_patientType, polyName, dayMed, dateMed, new EPuskesmasDataService.PostPolyRegistration() {
            @Override
            public void onResponse(String message) {
                doneDialog(message);
            }

            @Override
            public void onError(String message) {
                if (message.equals("403")) {
                    forbiddenDialog("Kuota penuh\nSilahkan datang langsung di hari tersebut");
                } else if (message.equals("409")) {
                    forbiddenDialog("Pasien telah terdaftar sebelumnya");
                } else {
                    forbiddenDialog("Pasien gagal terdaftar");
                }
            }
        });
    }

    private void setDoctorScheduleRecyclerView(ArrayList<DoctorScheduleModel> dataDoctorSchedule) {
        _doctorScheduleRecyclerView.setHasFixedSize(true);
        _doctorScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _doctorScheduleAdapter = new DoctorScheduleRecyclerViewAdapter(getContext(), dataDoctorSchedule);
        _doctorScheduleRecyclerView.setAdapter(_doctorScheduleAdapter);
    }

    private String convertToDBDate(String txtBirthDate) {
        String inputPattern = "dd MMMM yyyy";
        String outputPattern = "yyyy-MM-dd";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, new Locale("id", "ID"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(txtBirthDate);
            str = outputFormat.format(Objects.requireNonNull(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private void doneDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
        View doneDialog = LayoutInflater.from(getContext()).inflate(R.layout.custom_done_dialog, getView().findViewById(R.id.confirm_done_dialog));
        builder.setView(doneDialog);

        TextView txtMessage = doneDialog.findViewById(R.id.done_message);
        txtMessage.setText(message);

        final AlertDialog alertDialog = builder.create();

        doneDialog.findViewById(R.id.btn_confirm_done).setOnClickListener(v -> {
            alertDialog.dismiss();

            Intent intent = new Intent(getContext(), DashboardPasien.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    private void forbiddenDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
        View forbiddenDialog = LayoutInflater.from(getContext()).inflate(R.layout.custom_forbidden_dialog, getView().findViewById(R.id.confirm_forbidden_dialog));
        builder.setView(forbiddenDialog);

        TextView txtMessage = forbiddenDialog.findViewById(R.id.forbidden_message);
        txtMessage.setText(message);

        final AlertDialog alertDialog = builder.create();

        forbiddenDialog.findViewById(R.id.btn_confirm_forbidden).setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }
}