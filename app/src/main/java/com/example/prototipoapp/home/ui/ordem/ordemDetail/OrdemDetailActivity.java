package com.example.prototipoapp.home.ui.ordem.ordemDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prototipoapp.R;
import com.example.prototipoapp.home.ui.ordem.Ordem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class OrdemDetailActivity extends AppCompatActivity {
    public static String SHARED_PREFERENCES = "DadosUser";
    CardView cardVeiculo,cardMotorista;
    TextView name, data_s, data_s_2, descri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordem_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.text_o_solicitante);
        data_s = findViewById(R.id.text_o_data_solicitacao);
        data_s_2 = findViewById(R.id.text_o_data_solicitado_para);
        descri = findViewById(R.id.text_o_descricao);



//        cardMotorista.setVisibility(View.GONE);
//        cardVeiculo.setVisibility(View.GONE);
        cardVeiculo = findViewById(R.id.card_veiculo_ordem);
        cardMotorista = findViewById(R.id.card_motorista_ordem);

        cardVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OrdemDetailActivity.this, "Abrir um popup menu com dados", Toast.LENGTH_SHORT).show();
            }
        });

        GetOrdemRequest();
    }
    private void setValuesVeiculo(JSONObject response) {

    }

    private void setValuesInTextView(JSONObject response) {
        try {
            JSONObject solicitanteDATA = response.getJSONObject("solicitante");
            name.setText(solicitanteDATA.getString("name").toString());
            data_s.setText(response.getString("data_solicitacao").toString());
            data_s_2.setText(response.getString("data_solicitado").toString());
            descri.setText(response.getString("descricao"));

            try {
                ImageView image_veiculo, image_motorista;
                TextView name_veiculo, tipo_veiculo, name_motorista;

                JSONObject motoristaDATA = response.getJSONObject("motorista");
                JSONObject veiculoDATA = response.getJSONObject("veiculo");

                name_veiculo = findViewById(R.id.text_o_name_veiculo);
                tipo_veiculo = findViewById(R.id.text_o_t_veiculo);
                name_motorista = findViewById(R.id.text_o_name_motorista);
                image_veiculo = findViewById(R.id.o_img_veiculo);

                name_motorista.setText(motoristaDATA.getString("name").toString());


                name_veiculo.setText("Nome: "+ veiculoDATA.getString("name").toString());
                tipo_veiculo.setText(veiculoDATA.getString("tipo").toString());
                Picasso.get().load(veiculoDATA.getString("image").toString()).into(image_veiculo);
                cardVeiculo.setVisibility(View.VISIBLE);
                cardMotorista.setVisibility(View.VISIBLE);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void GetOrdemRequest() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        int id_ordem = bundle.getInt("id_ordem");
        String url = getString(R.string.dominio) + id_ordem +  getString(R.string.GET_DETAILS_ORDEM) ;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    setValuesInTextView(responseJSON);
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
                params.put("Content-Type", "application/json; charset=UTF-8");
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

    @SuppressLint("NonConstantResourceId")
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