package com.arcmedtek.e_puskesmas.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.config.SessionManager;
import com.arcmedtek.e_puskesmas.fragment.BpjsRegistrationFragment;
import com.arcmedtek.e_puskesmas.fragment.PolyRegistrationFragment;
import com.arcmedtek.e_puskesmas.model.BpjsModel;
import com.arcmedtek.e_puskesmas.service.EPuskesmasDataService;

import java.util.ArrayList;
import java.util.HashMap;

public class PolyRegistration extends AppCompatActivity {

    ImageView _btnBack;
    CardView _btnNonBpjs, _btnBpjs;

    String _polyName;

    EPuskesmasDataService _ePuskesmasDataService;

    HashMap<String, String> _userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poly_registration);

        _polyName = getIntent().getStringExtra("poly_name");

        _btnBack = findViewById(R.id.btn_back);
        _btnNonBpjs = findViewById(R.id.btn_non_bpjs);
        _btnBpjs = findViewById(R.id.btn_bpjs);

        _ePuskesmasDataService = new EPuskesmasDataService(this);

        _userKey = new SessionManager(this).getUserDetail();

        _btnBack.setOnClickListener(v -> onBackPressed());

        polyRegistration();
    }

    private void polyRegistration() {
        Bundle bundle = new Bundle();
        bundle.putString("Poly_Name", _polyName);
        bundle.putString("nik", _userKey.get(SessionManager.USERNAME));
//        bundle.putString("Bpjs", "bpjs");
//        bundle.putString("Non_Bpjs", "non_bpjs");

        _btnBpjs.setOnClickListener(v -> {
            bundle.putString("Patient_Type", "bpjs");
            _ePuskesmasDataService.getPatientBpjs(new EPuskesmasDataService.GetPatientBpjs() {
                @Override
                public void onResponse(ArrayList<BpjsModel> dataPatientBpjs) {
                    if (dataPatientBpjs.isEmpty()) {
                        Fragment fragment = new BpjsRegistrationFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        fragment.setArguments(bundle);
                        transaction.replace(R.id.fragment_poly_registration, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        replaceFragment(bundle);
                    }
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(PolyRegistration.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        });

        _btnNonBpjs.setOnClickListener(v -> {
            bundle.putString("Patient_Type", "non_bpjs");
            replaceFragment(bundle);
        });
    }

    private void replaceFragment(Bundle bundle) {
        Fragment fragment = new PolyRegistrationFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        fragment.setArguments(bundle);
        transaction.replace(R.id.fragment_poly_registration, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}