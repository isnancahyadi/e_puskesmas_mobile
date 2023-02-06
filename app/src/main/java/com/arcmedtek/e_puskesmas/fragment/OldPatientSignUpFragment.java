package com.arcmedtek.e_puskesmas.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.model.PatientModel;
import com.arcmedtek.e_puskesmas.service.EPuskesmasDataService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class OldPatientSignUpFragment extends Fragment {

    TextInputEditText _searchNik;
    Button _btnSearchNik;

    EPuskesmasDataService _ePuskesmasDataService;

    public OldPatientSignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_old_patient_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _searchNik = view.findViewById(R.id.search_nik);
        _btnSearchNik = view.findViewById(R.id.btn_search_nik);

        _ePuskesmasDataService = new EPuskesmasDataService(getContext());

        _btnSearchNik.setOnClickListener(v -> {
            _ePuskesmasDataService.searchPatient(String.valueOf(_searchNik.getText()), new EPuskesmasDataService.SearchPatient() {
                @Override
                public void onResponse(ArrayList<PatientModel> dataPatient) {
                    if (dataPatient.isEmpty()) {
                        Toast.makeText(getContext(), "Data tidak tersedia", Toast.LENGTH_SHORT).show();
                    } else {
//                        ConfirmOldPatientRegistrationFragment confirmOldPatientRegistrationFragment = new ConfirmOldPatientRegistrationFragment();
//                        FragmentManager fragmentManager = getFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id);

//                        getFragmentManager().beginTransaction().replace(R.id.fragment_patient_sign_up, ConfirmOldPatientRegistrationFragment.class, null).addToBackStack(null).commit();

                        Fragment fragment = new ConfirmOldPatientRegistrationFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        Bundle bundle = new Bundle();
                        bundle.putString("nik", String.valueOf(_searchNik.getText()));
                        bundle.putString("first_name", dataPatient.get(0).getFirstName());
                        bundle.putString("middle_name", dataPatient.get(0).getMiddleName());
                        bundle.putString("last_name", dataPatient.get(0).getLastName());
                        bundle.putString("gender", dataPatient.get(0).getGender());
                        bundle.putString("birth_place", dataPatient.get(0).getBirthPlace());
                        bundle.putString("birth_date", dataPatient.get(0).getBirthDate());
                        bundle.putString("address", dataPatient.get(0).getAddress());
                        bundle.putString("photo_ktp", dataPatient.get(0).getPhotoKtp());

                        fragment.setArguments(bundle);
                        transaction.replace(R.id.fragment_patient_sign_up, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}