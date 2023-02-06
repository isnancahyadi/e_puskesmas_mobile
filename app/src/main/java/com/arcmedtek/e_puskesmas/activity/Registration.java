package com.arcmedtek.e_puskesmas.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.fragment.NewPatientSignUpFragment;
import com.arcmedtek.e_puskesmas.fragment.OldPatientSignUpFragment;

public class Registration extends AppCompatActivity {

    FrameLayout _fragmentPatientSignUp;
    CardView _oldPatient, _newPatient;
    RelativeLayout _signUpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        _signUpLayout = findViewById(R.id.sign_up_layout);
        _fragmentPatientSignUp = findViewById(R.id.fragment_patient_sign_up);
        _oldPatient = findViewById(R.id.old_patient);
        _newPatient = findViewById(R.id.new_patient);

        _oldPatient.setOnClickListener(view -> {
            //_signUpLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_patient_sign_up, OldPatientSignUpFragment.class, null).addToBackStack(null).commit();
        });

        _newPatient.setOnClickListener(view -> {
            //_signUpLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_patient_sign_up, NewPatientSignUpFragment.class, null).addToBackStack(null).commit();
        });
    }
}