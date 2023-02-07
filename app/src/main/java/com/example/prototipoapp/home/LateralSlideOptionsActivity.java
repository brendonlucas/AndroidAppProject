package com.example.prototipoapp.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.prototipoapp.R;

import com.example.prototipoapp.databinding.ActivityLateralSlideOptionsBinding;
import com.example.prototipoapp.home.ui.funcionario.perfil.PerfilActivity;
import com.example.prototipoapp.home.ui.instituicao.InstituicaoFragment;
import com.example.prototipoapp.home.ui.veiculo.VeiculoFragment;
import com.example.prototipoapp.login.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


public class LateralSlideOptionsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityLateralSlideOptionsBinding binding;
    public static String SHARED_PREFERENCES = "DadosUser";
    TextView name_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLateralSlideOptionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarLateralSlideOptions.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder( R.id.nav_ordem,
                R.id.nav_veiculo,R.id.funcionarios_id,R.id.nav_insti,R.id.nav_logoff_id)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_lateral_slide_options);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);

        View headerView = navigationView.getHeaderView(0);
        name_users = headerView.findViewById(R.id.name_user_ids);
        ImageView imageView = headerView.findViewById(R.id.image_profile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LateralSlideOptionsActivity.this, PerfilActivity.class);
                startActivity(intent);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

            }
        });
        if (name_users != null){
            name_users.setText(sharedPreferences.getString("NAME", "null"));

        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        navigationView.setNavigationItemSelectedListener(this);
//
//        getSupportFragmentManager().beginTransaction().replace(
//                R.id.nav_host_fragment_content_lateral_slide_options,new InstituicaoFragment()).commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.lateral_slide_options, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_sair) {
            Toast.makeText(this, "Saiu", Toast.LENGTH_SHORT).show();
            sair();
            return true;
        }

        return(super.onOptionsItemSelected(item));
    }

    @SuppressLint("NonConstantResourceId")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.nav_insti:
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.nav_host_fragment_content_lateral_slide_options,new InstituicaoFragment()).commit();
            case R.id.nav_ordem:
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.nav_host_fragment_content_lateral_slide_options,new InstituicaoFragment()).commit();
            case R.id.nav_veiculo:
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.nav_host_fragment_content_lateral_slide_options,new InstituicaoFragment()).commit();
            case R.id.funcionarios_id:
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.nav_host_fragment_content_lateral_slide_options,new VeiculoFragment()).commit();
            case R.id.nav_logoff_id:
                finish();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_lateral_slide_options);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    public void sair() {
        SharedPreferences sharedPreferences = getSharedPreferences("DadosUser", MODE_PRIVATE);
        System.out.println("--------------------------------------" + sharedPreferences.getString("TOKEN", "null"));
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



}