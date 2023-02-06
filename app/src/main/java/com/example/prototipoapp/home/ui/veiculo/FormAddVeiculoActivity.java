package com.example.prototipoapp.home.ui.veiculo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prototipoapp.R;
import com.example.prototipoapp.home.ui.Volley.VolleyMultipartRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class FormAddVeiculoActivity extends AppCompatActivity {
    public static String SHARED_PREFERENCES = "DadosUser";
    ImageView image_file;
    TextInputEditText data_name_v,data_placa_v,data_carga_v;
    Button browser;
    AutoCompleteTextView data_tipo_v,file_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add_veiculo);

        file_name = findViewById(R.id.input_text_file_v);
        browser = findViewById(R.id.btn_select_img);
        data_name_v = findViewById(R.id.input_content_name_v);
        data_placa_v = findViewById(R.id.input_content_placa_v);
        data_tipo_v = findViewById(R.id.fill_exposed);
        data_carga_v = findViewById(R.id.input_content_carga_v);
        image_file = findViewById(R.id.image_preview_v);

        setTitle("Criar veiculo");

        String[] type = new String[]{"Moto", "Carro", "Caminhão", "Ônibus"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.drop_down_item_type_v, type);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.fill_exposed);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(FormAddVeiculoActivity.this, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_confirm, menu);
        MenuItem confirmVitem = menu.findItem(R.id.id_confirm_v);
        confirmVitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                sendDataVeiculo();
//                Toast.makeText(FormAddVeiculoActivity.this, "Criado com sucesso", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return true;
    }

    byte[] imageEscolhida = null;

    public void onActivityResult(int requestscode, int resultCode, @Nullable Intent data) {
        if (requestscode == 1 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Toast.makeText(this, queryName(imageUri), Toast.LENGTH_SHORT).show();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                image_file.setImageBitmap(bitmap);
                imageEscolhida = getFileDataFromDrawable(bitmap);
                file_name.setText(queryName(imageUri));
                System.out.println(queryName(imageUri));

            } catch (Exception ex) {

            }
        }
        super.onActivityResult(requestscode, resultCode, data);
    }

    private String queryName(Uri uri) {
        Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        return returnCursor.getString(nameIndex);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    public void openImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Buscar image"), 1);

    }

    public void sendDataVeiculo() {
        String name_veiculo, placa, tipo, carga;
        name_veiculo = data_name_v.getText().toString();
        placa = data_placa_v.getText().toString();
        tipo = data_tipo_v.getText().toString();
        carga = data_carga_v.getText().toString();
        if (!name_veiculo.equals("") && !placa.equals("") && !tipo.equals("")
                && !carga.equals("")){
            if (imageEscolhida != null){
                PostVeiculoDataWithImage(name_veiculo,placa,tipo,carga);
            }else{
                PostVeiculoDataNoImage(name_veiculo,placa,tipo,carga);
            }
        }
        Toast.makeText(this, name_veiculo+" "+placa+" "+tipo+" "+carga, Toast.LENGTH_SHORT).show();
    }

    public void PostVeiculoDataWithImage(final String name_veiculo, final String placa,
                                final String tipo, final String carga) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_instituicao = sharedPreferences.getInt("INSTITUICAO_ID", 0);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String url = "https://brendonlulucas.pythonanywhere.com/" + id_instituicao + "/API/APIcreate_veiculo/";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), "Criado com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                Toast.makeText(FormAddVeiculoActivity.this, "Dados errados", Toast.LENGTH_SHORT).show();
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
                params.put("name", name_veiculo);
                params.put("placa", placa);
                String tipo_v = "0";
                if (tipo.equals("Moto")){
                    tipo_v = "1";
                }else if (tipo.equals("Carro")){
                    tipo_v = "2";
                }else if (tipo.equals("Caminhão")){
                    tipo_v = "3";
                }else if (tipo.equals("Ônibus")){
                    tipo_v = "4";
                }
                params.put("tipo", tipo_v);
                params.put("qtd_pessoas", carga);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + sharedPreferences.getString("TOKEN", "null"));
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new VolleyMultipartRequest.DataPart(imagename + ".png", imageEscolhida));
                return params;
            }

        };


        MyRequestQueue.add(volleyMultipartRequest);
    }


    public void PostVeiculoDataNoImage(final String name_veiculo, final String placa,
                                         final String tipo, final String carga) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_instituicao = sharedPreferences.getInt("INSTITUICAO_ID", 0);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String url = "https://brendonlulucas.pythonanywhere.com/" + id_instituicao + "/API/APIcreate_veiculo/";
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
                Toast.makeText(FormAddVeiculoActivity.this, "Dados errados", Toast.LENGTH_SHORT).show();
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
                params.put("name", name_veiculo);
                params.put("placa", placa);
                params.put("tipo", tipo);
                params.put("qtd_pessoas", carga);
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
}