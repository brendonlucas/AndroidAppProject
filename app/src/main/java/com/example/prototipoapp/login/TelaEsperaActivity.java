package com.example.prototipoapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.prototipoapp.MainActivity;
import com.example.prototipoapp.R;
import com.example.prototipoapp.home.LateralSlideOptionsActivity;

import java.util.Objects;

public class TelaEsperaActivity extends AppCompatActivity {
    public static String SHARED_PREFERENCES = "DadosUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_espera);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        if (Objects.equals(sharedPreferences.getString("TOKEN", "null"), "null")){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this, LateralSlideOptionsActivity.class);
            startActivity(intent);
            finish();
        }
    }
}