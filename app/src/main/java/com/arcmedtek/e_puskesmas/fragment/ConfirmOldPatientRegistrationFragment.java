package com.arcmedtek.e_puskesmas.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.activity.Login;
import com.arcmedtek.e_puskesmas.service.EPuskesmasDataService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfirmOldPatientRegistrationFragment extends Fragment {

    TextInputLayout _inlayGender;
    TextInputEditText _inetNik, _inetFirstName, _inetMiddleName, _inetLastName, _inetBirthPlace, _inetBirthDate, _inetAddress;
    AutoCompleteTextView _tvGender;
    LinearLayout _takePicture, _pictureContainer;
    ImageView _pictureResult;
    Button _btnTakePictureAgain, _btnNextConfirm;

    String _nik, _firstName, _middleName, _lastName, _gender, _birthPlace, _birthDate, _address, _photoKtp;

    EPuskesmasDataService _ePuskesmasDataService;

    public ConfirmOldPatientRegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_old_patient_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            _nik = getArguments().getString("nik");
            _firstName = getArguments().getString("first_name");
            _middleName = getArguments().getString("middle_name");
            _lastName = getArguments().getString("last_name");
            _gender = getArguments().getString("gender");
            _birthPlace = getArguments().getString("birth_place");
            _birthDate = getArguments().getString("birth_date");
            _address = getArguments().getString("address");
            _photoKtp = getArguments().getString("photo_ktp");
        }

        _inetNik = view.findViewById(R.id.inet_nik);
        _inetFirstName = view.findViewById(R.id.inet_first_name);
        _inetMiddleName = view.findViewById(R.id.inet_middle_name);
        _inetLastName = view.findViewById(R.id.inet_last_name);
        _tvGender = view.findViewById(R.id.choose_gender);
        _inetBirthPlace = view.findViewById(R.id.inet_birth_place);
        _inetBirthDate = view.findViewById(R.id.inet_birth_date);
        _inetAddress = view.findViewById(R.id.inet_address);
        _takePicture = view.findViewById(R.id.take_picture);
        _pictureContainer = view.findViewById(R.id.picture_container);
        _pictureResult = view.findViewById(R.id.picture_result);
        _btnTakePictureAgain = view.findViewById(R.id.take_picture_again);
        _inlayGender = view.findViewById(R.id.inlay_gender);
        _btnNextConfirm = view.findViewById(R.id.next_confirm);

        _ePuskesmasDataService = new EPuskesmasDataService(getContext());

        showDataPatient(_nik, _firstName, _middleName, _lastName, _gender, _birthPlace, _birthDate, _address, _photoKtp);

        _btnNextConfirm.setOnClickListener(v -> createAccount(_nik));
    }

    private void createAccount(String txtNik) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
        View createAccountDialog = LayoutInflater.from(getContext()).inflate(R.layout.create_account, getView().findViewById(R.id.create_account));
        builder.setView(createAccountDialog);

        TextInputEditText inetNik = createAccountDialog.findViewById(R.id.inet_nik);
        TextInputEditText inetPass = createAccountDialog.findViewById(R.id.inet_pass);
        TextInputEditText inetRetypePass = createAccountDialog.findViewById(R.id.inet_retype_pass);
        TextInputLayout inlayRetypePass = createAccountDialog.findViewById(R.id.inlay_retype_pass);

        inetNik.setEnabled(false);
        inetNik.setText(txtNik);

        final AlertDialog alertDialog = builder.create();

        createAccountDialog.findViewById(R.id.btn_create_account).setOnClickListener(v -> {
            if (!inetRetypePass.getText().toString().equals(inetPass.getText().toString())) {
                inlayRetypePass.setError("Password tidak sama");
                inetRetypePass.setText("");
            } else {
                _ePuskesmasDataService.signUp(txtNik, inetPass.getText().toString(), new EPuskesmasDataService.SignUpListener() {
                    @Override
                    public void onResponse(String message) {
                        doneDialog("Data pasien berhasil teregistrasi");
                    }

                    @Override
                    public void onError(String message) {

                    }
                });

                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    private void showDataPatient(String nik, String firstName, String middleName, String lastName, String gender, String birthPlace, String birthDate, String address, String photoKtp) {
//        String fullname = "";
//        if (middleName.equals("") && lastName.equals("")) {
//            fullname = firstName;
//        } else if (middleName.equals("")) {
//            fullname = firstName + " " + lastName;
//        } else {
//            fullname = firstName + " " + middleName + " " + lastName;
//        }

        String gen = "";
        if (gender.equals("1")) {
            gen = "Pria";
        } else if (gender.equals("0")) {
            gen = "Wanita";
        }

        _takePicture.setVisibility(View.GONE);
        _pictureContainer.setVisibility(View.VISIBLE);
//        _tvGender.setInputType(InputType.TYPE_NULL);

        _inetNik.setText(nik);
        _inetFirstName.setText(firstName);
        _inetMiddleName.setText(middleName);
        _inetLastName.setText(lastName);
        _tvGender.setText(gen);
        _inetBirthPlace.setText(birthPlace);
        _inetBirthDate.setText(convertToAppDate(birthDate));
        _inetAddress.setText(address);
        Picasso.get().load(photoKtp).into(_pictureResult);

        editTextDisable(false);
    }

    private String convertToAppDate(String txtBirthDate) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(txtBirthDate);
            str = outputFormat.format(date);
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

            Intent intent = new Intent(getContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    private void editTextDisable(boolean disable) {
        _tvGender.setEnabled(disable);
        _inetNik.setEnabled(disable);
        _inetFirstName.setEnabled(disable);
        _inetMiddleName.setEnabled(disable);
        _inetLastName.setEnabled(disable);
        _inetBirthPlace.setEnabled(disable);
        _inetBirthDate.setEnabled(disable);
        _inetAddress.setEnabled(disable);
    }
}