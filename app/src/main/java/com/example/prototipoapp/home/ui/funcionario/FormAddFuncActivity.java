package com.example.prototipoapp.home.ui.funcionario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prototipoapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class FormAddFuncActivity extends AppCompatActivity {
    TextInputLayout layout_pass;
    TextInputEditText senha, username, full_name, name, sobrename;
    AutoCompleteTextView cargo_user;
    CheckBox checkSenha;
    Boolean senha_checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add_func);

        setTitle("Adicionar Funcionario");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String[] type = new String[]{"Gerente", "Secretario", "Motorista"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.drop_down_item_type_v, type);

        cargo_user = findViewById(R.id.form_add_cargo_user_data);
        cargo_user.setAdapter(adapter);

        senha = findViewById(R.id.form_add_password_user_data);
        username = findViewById(R.id.form_add_username_user_data);
        full_name = findViewById(R.id.form_add_fullname_user_data);
        name = findViewById(R.id.form_add_name_user_data);
        sobrename = findViewById(R.id.form_add_sobrenome_user_data);

        layout_pass = findViewById(R.id.form_add_password_user);
        layout_pass.setVisibility(View.GONE);

        checkSenha = findViewById(R.id.checkBoxTemSenha);

        checkSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSenha.isChecked()) {
                    layout_pass.setVisibility(View.VISIBLE);
                    senha_checked = true;
                } else {
                    senha.setText("");
                    layout_pass.setVisibility(View.GONE);
                    senha_checked = false;
                }

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_confirm, menu);
        MenuItem confirmOitem = menu.findItem(R.id.id_confirm_v);
        confirmOitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                postDataFunc();
                return false;
            }
        });
        return true;
    }

    private void postDataFunc() {
        String S_name, S_sobrenome, S_username, S_full_name, S_password, S_cargo;

        S_name = name.getText().toString();
        S_sobrenome = sobrename.getText().toString();
        S_username = username.getText().toString();
        S_full_name = full_name.getText().toString();

        S_cargo = cargo_user.getText().toString();

        if (senha_checked) {
            S_password = senha.getText().toString();
        } else {
            S_password = "null";
        }
        if (!S_name.equals("") && !S_sobrenome.equals("") && !S_username.equals("")
                && !S_full_name.equals("") && !S_cargo.equals("") && !S_password.equals("")) {
            PostDataUserVolley(S_name, S_sobrenome, S_username, S_password, S_full_name, S_cargo);


        }
    }


    public void PostDataUserVolley(String name, String sobrename, String username, String password,
                                   String fullname, String cargo) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.SHARED_PREFERENCES), Context.MODE_PRIVATE);
        int id_instituicao = sharedPreferences.getInt("INSTITUICAO_ID", 0);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        String url = getString(R.string.dominio) + id_instituicao + getString(R.string.CREATE_USER);
        StringRequest volleyMultipartRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Criado com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                Toast.makeText(getApplicationContext(), "Dados errados", Toast.LENGTH_SHORT).show();
                String body = "";
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // exception
                }
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(body);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("sobrename", sobrename);
                params.put("username", username);
                params.put("password", password);
                params.put("cargo", cargo);
                params.put("full_name", fullname);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + sharedPreferences.getString("TOKEN", "null"));
                return params;
            }

        };


        MyRequestQueue.add(volleyMultipartRequest);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}