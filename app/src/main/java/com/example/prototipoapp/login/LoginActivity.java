package com.example.prototipoapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prototipoapp.R;
import com.example.prototipoapp.home.LateralSlideOptionsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    public static String SHARED_PREFERENCES = "DadosUser";
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.IDUserName);
        password = findViewById(R.id.IDPassword);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        if (!Objects.equals(sharedPreferences.getString("TOKEN", "null"), "null")) {
            finish();
        }
    }

    public void logar(View view) throws IOException {
        String str_username = username.getText().toString();
        String str_password = password.getText().toString();
        if (!str_username.equals("") && !str_password.equals("")) {
            PostStringVolley(str_username, str_password);
        }else{
            Toast.makeText(this, "Preencha os dados", Toast.LENGTH_SHORT).show();
        }
//        PostStringVolley(str_username, str_password);

    }


    public void PostStringVolley(final String username, final String password) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String url = "https://brendonlulucas.pythonanywhere.com/api-token-auth/";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    String usuarioDataJson = new JSONObject(response).getString("dados");
                    String instDataJson = new JSONObject(response).getString("instituicao");
                    String userDataJson = new JSONObject(usuarioDataJson).getString("user");




                    String token = new JSONObject(response).getString("token");
                    String nome = new JSONObject(usuarioDataJson).getString("name");
                    int instituicao_id = new JSONObject(instDataJson).getInt("pk");
                    int pk_user = new JSONObject(usuarioDataJson).getInt("pk");

                    int cargo_user_id = new JSONObject(usuarioDataJson).getInt("cargo");
                    saveDados(token, nome, instituicao_id,usuarioDataJson,cargo_user_id,instDataJson,pk_user);

                    Toast.makeText(LoginActivity.this, nome + token, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                String body = "";
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // exception
                }
                if (statusCode.equals("400")) {
                    Toast.makeText(LoginActivity.this, "Usuário e/ou senha inválidos", Toast.LENGTH_SHORT).show();
                }

                System.out.println(body);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("username", username);
                MyData.put("password", password);
                return MyData;
            }


        };
        MyRequestQueue.add(MyStringRequest);
    }

    public void ChangeTela() {
        Intent intent = new Intent(this, LateralSlideOptionsActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveDados(String token, String nome, int instituicao_id, String usuarioDataJson,
                           int cargo_id,String instDataJson, int pk_user) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TOKEN", token);
        editor.putString("NAME", nome);
        editor.putString("DADOS_USER", usuarioDataJson);
        editor.putInt("INSTITUICAO_ID", instituicao_id);
        editor.putInt("PK_USER", pk_user);
        editor.putString("DADOS_INSTITUICAO", instDataJson);
        editor.putInt("CARGO_ID", cargo_id);
        editor.apply();
        ChangeTela();
    }
}