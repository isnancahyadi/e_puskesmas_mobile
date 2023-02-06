package com.arcmedtek.e_puskesmas.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcmedtek.e_puskesmas.R;
import com.arcmedtek.e_puskesmas.adapter.HistoryListAdapter;
import com.arcmedtek.e_puskesmas.model.HistoryModel;
import com.arcmedtek.e_puskesmas.service.EPuskesmasDataService;

import java.util.ArrayList;
import java.util.Objects;

public class HistoryFragment extends Fragment {

    RecyclerView _historyRecycler;

    HistoryListAdapter _historyListAdapter;

    EPuskesmasDataService _ePuskesmasDataService;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _historyRecycler = view.findViewById(R.id.list_history);

        _ePuskesmasDataService = new EPuskesmasDataService(getContext());

        _ePuskesmasDataService.getHistoryPatient(new EPuskesmasDataService.GetHistoryPatient() {
            @Override
            public void onResponse(ArrayList<HistoryModel> historyPatient) {
                setHistoryPatientRecycler(historyPatient);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void setHistoryPatientRecycler(ArrayList<HistoryModel> historyPatient) {
        _historyListAdapter = new HistoryListAdapter(historyPatient, getContext());
        _historyRecycler.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));

        _historyRecycler.setAdapter(_historyListAdapter);
    }
}