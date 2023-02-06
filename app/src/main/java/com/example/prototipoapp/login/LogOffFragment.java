package com.example.prototipoapp.login;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prototipoapp.R;


public class LogOffFragment extends Fragment {
    public static String SHARED_PREFERENCES = "DadosUser";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sair();

        return inflater.inflate(R.layout.fragment_log_off, container, false);
    }


    public void sair() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DadosUser", MODE_PRIVATE);
        System.out.println("--------------------------------------" + sharedPreferences.getString("TOKEN", "null"));
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}