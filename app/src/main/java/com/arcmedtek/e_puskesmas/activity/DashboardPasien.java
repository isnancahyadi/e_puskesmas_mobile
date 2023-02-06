package com.arcmedtek.e_puskesmas.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.fragment.HistoryFragment;
import com.arcmedtek.e_puskesmas.fragment.HomeFragment;
import com.arcmedtek.e_puskesmas.fragment.ProfileFragment;
import com.arcmedtek.e_puskesmas.model.PatientModel;
import com.arcmedtek.e_puskesmas.service.EPuskesmasDataService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class DashboardPasien extends AppCompatActivity {

    BottomNavigationView _bottomNavMenu;
//    NavController navController;

    EPuskesmasDataService _ePuskesmasDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_pasien);

        _bottomNavMenu = findViewById(R.id.bottom_nav_dashboard_patient);
//        navController = Navigation.findNavController(this, R.id.fragment_container_view);

        _ePuskesmasDataService = new EPuskesmasDataService(DashboardPasien.this);

        _ePuskesmasDataService.getDataPatient(new EPuskesmasDataService.GetDataPatient() {
            @Override
            public void onResponse(ArrayList<PatientModel> dataPatient) {
                patientProfile(dataPatient.get(0).getNik(), dataPatient.get(0).getFirstName(), dataPatient.get(0).getMiddleName(), dataPatient.get(0).getLastName(), dataPatient.get(0).getGender(), dataPatient.get(0).getBirthPlace(), dataPatient.get(0).getBirthDate(), dataPatient.get(0).getAddress(), dataPatient.get(0).getPhotoKtp());
            }

            @Override
            public void onError(String message) {

            }
        });

//        NavigationUI.setupWithNavController(_bottomNavMenu, navController);
    }

    private void patientProfile(String nik, String firstName, String middleName, String lastName, String gender, String birthPlace, String birthDate, String address, String photoKtp) {
        Bundle bundle = new Bundle();
        bundle.putString("NIK", nik);
        bundle.putString("First_Name", firstName);
        bundle.putString("Middle_Name", middleName);
        bundle.putString("Last_Name", lastName);
        bundle.putString("Gender", gender);
        bundle.putString("Birth_Place", birthPlace);
        bundle.putString("Birth_Date", birthDate);
        bundle.putString("Address", address);
        bundle.putString("Photo_KTP", photoKtp);

        replaceFragment(new HomeFragment(), bundle);

        _bottomNavMenu.setSelectedItemId(R.id.home_fragment);

        _bottomNavMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_fragment:
                        replaceFragment(new HomeFragment(), bundle);
                        break;
                    case R.id.history_fagment:
                        replaceFragment(new HistoryFragment(), bundle);
                        break;
                    case R.id.profile_fragment:
                        replaceFragment(new ProfileFragment(), bundle);
                        break;
                }

                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment, Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragment.setArguments(bundle);
//        if (reload) {
//            getSupportFragmentManager().popBackStack();
//        }
        transaction.replace(R.id.body_container, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragment.setArguments(bundle);
//        fragmentManager.popBackStack();
//        fragmentTransaction.replace(R.id.body_container, fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }

    public void refreshFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
//        getSupportFragmentManager().beginTransaction().attach(fragment).commit();
    }
}