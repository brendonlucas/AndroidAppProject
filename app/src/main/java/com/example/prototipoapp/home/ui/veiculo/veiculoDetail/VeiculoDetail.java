package com.example.prototipoapp.home.ui.veiculo.veiculoDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prototipoapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class VeiculoDetail extends AppCompatActivity {
    public static String SHARED_PREFERENCES = "DadosUser";
    TextView name_veiculo, placa_veiculo, tipo_veiculo;
    FloatingActionButton btn_openOV, btn_edit_v;
    ImageView imgCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculo_detail);
        Bundle bundle = getIntent().getExtras();


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        GetVeiculoDetailsRequests();

        Toast.makeText(this, "etalhes veiculo" + bundle.getInt("id_veiculo"), Toast.LENGTH_SHORT).show();

        btn_openOV = findViewById(R.id.btn_openOV);

        btn_edit_v = findViewById(R.id.btn_edit_v);
        btn_openOV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bundle != null) {
                    Bundle bundles = getIntent().getExtras();
                    int id_veiculo = bundles.getInt("id_veiculo");
                    Intent intent = new Intent(view.getContext(), OrdensVeiculo.class);
                    intent.putExtra("id_veiculo", id_veiculo);
                    startActivity(intent);
                }
            }
        });
        btn_edit_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bundle != null) {
                    Bundle bundles = getIntent().getExtras();
                    int id_veiculo = bundles.getInt("id_veiculo");
                    Intent intent = new Intent(view.getContext(), EditVeiculoActivity.class);
                    intent.putExtra("id_veiculo", id_veiculo);
                    startActivityForResult(intent,1);
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                GetVeiculoDetailsRequests();
                Toast.makeText(this, "coisouuuuuuuu", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void GetVeiculoDetailsRequests() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Bundle bundle = getIntent().getExtras();
        int id_veiculo = bundle.getInt("id_veiculo");
        Toast.makeText(this, " " + id_veiculo, Toast.LENGTH_SHORT).show();
        String url = "https://brendonlulucas.pythonanywhere.com/" + id_veiculo + "/API/APIveiculo_detail/";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    set_values_veiculo(responseJSON);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                System.out.println(body);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + sharedPreferences.getString("TOKEN", "null"));
                params.put("Content-Type", "application/json");
                return params;
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    // solution 1:
                    String jsonString = new String(response.data, "UTF-8");

                    return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(jsonArrayRequest);
    }

    private void set_values_veiculo(JSONObject response) {
        name_veiculo = findViewById(R.id.txt_data_name_veiculo);
        TextView name_car = findViewById(R.id.name_cars);
        placa_veiculo = findViewById(R.id.txt_data_placa_veiculo);
        tipo_veiculo = findViewById(R.id.txt_data_modelo_veiculo);
        imgCar = findViewById(R.id.imageCarID);


        try {
            Picasso.get().load(response.getString("image").toString()).into(imgCar);
            name_veiculo.setText(response.getString("name").toString());
            name_car.setText(response.getString("name").toString());
            placa_veiculo.setText(response.getString("placa").toString());
            int tipo_id = response.getInt("tipo");

            if(tipo_id == 1){
                tipo_veiculo.setText("Moto");
            }else if(tipo_id == 2){
                tipo_veiculo.setText("Carro");
            }else if(tipo_id == 3){
                tipo_veiculo.setText("Caminhão");
            }else if(tipo_id == 4){
                tipo_veiculo.setText("Ônibus");
            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }
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