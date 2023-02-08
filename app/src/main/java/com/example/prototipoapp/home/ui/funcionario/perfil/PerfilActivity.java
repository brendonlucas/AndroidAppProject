package com.example.prototipoapp.home.ui.funcionario.perfil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.example.prototipoapp.home.ui.Volley.VolleyMultipartRequest;
import com.example.prototipoapp.home.ui.veiculo.FormAddVeiculoActivity;
import com.example.prototipoapp.home.ui.veiculo.VeiculoItem;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {
    public static String SHARED_PREFERENCES = "DadosUser";
    CardView card_label_name, card_label_username, card_label_fone, card_label_pass;
    CardView card_data_name, card_data_username, card_data_fone, card_data_pass;
    ImageButton btn_name, btn_username, btn_pass, btn_fone;
    TextView name_user,txt_name_user, name_username, fone;
    ImageView image_file;
    ShapeableImageView img_user;
    private AlertDialog alerta_names, alerta_username, alerta_pass, alerta_fone;

    String optionsList[] = {"opt1","Notificações","Dados Pessoais","Preferências","opt5"};
    int icList[] = {R.drawable.ic_baseline_phone_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_edit_calendar_24,
            R.drawable.ic_baseline_garage_24, R.drawable.ic_baseline_settings_24,};
    ListView listView;
    boolean dialog_creat = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.custom_list_view);
        AdapterListItem adapterListItem = new AdapterListItem(getApplicationContext(),optionsList,icList);
        listView.setAdapter(adapterListItem);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = optionsList[i];
                switch (item) {
                    case "opt1":
                        Toast.makeText(PerfilActivity.this, "OPT1:" + item, Toast.LENGTH_SHORT).show();
                        break;
                    case "Notificações":
                        Toast.makeText(PerfilActivity.this, "Not:" + item, Toast.LENGTH_SHORT).show();
                        break;
                    case "Dados Pessoais":
                        openDialog();
                        Toast.makeText(PerfilActivity.this, "Dad:" + item, Toast.LENGTH_SHORT).show();
                        break;
                    case "Preferências":
                        Toast.makeText(PerfilActivity.this, "Pref:" + item, Toast.LENGTH_SHORT).show();
                        break;
                    case "opt5":
                        Toast.makeText(PerfilActivity.this, "Opt5:" + item, Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });


        img_user = findViewById(R.id.Image_profile_acc);
        img_user.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                getImageToProfile();
                return false;
            }
        });


    }
    private void openDialog() {
        DialogDataInfos.display(getSupportFragmentManager());
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

    public void set_values_user(JSONObject response) {
        txt_name_user = findViewById(R.id.txt_name_user);
        try {
            JSONObject moore_data_user = response.getJSONObject("user");
            name_user.setText(response.getString("name").toString());
            txt_name_user.setText(response.getString("name").toString());
            name_username.setText(moore_data_user.getString("username").toString());
            fone.setText(response.getString("telefone").toString());
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
    }

    public void GetUserDetailsRequests() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_user = sharedPreferences.getInt("PK_USER", 0);
        String url = "https://brendonlulucas.pythonanywhere.com/" + id_user + "/API/APIGetUserDetail/";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    set_values_user(responseJSON);
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
                System.out.println("Authorization:" + "Token " + sharedPreferences.getString("TOKEN", "null"));
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



    byte[] imageEscolhida = null;

    public void onActivityResult(int requestscode, int resultCode, @Nullable Intent data) {
        if (requestscode == 1 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Toast.makeText(this, queryName(imageUri), Toast.LENGTH_SHORT).show();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageEscolhida = getFileDataFromDrawable(bitmap);
                if (dialog_creat){
                    alerta_names.dismiss();
                    alertDD(bitmap);
//                    showDialogImage2(bitmap);
                }else{
                    alertDD(bitmap);
//                    showDialogImage2(bitmap);
                    dialog_creat = true;
                }

                System.out.println(queryName(imageUri));

            } catch (Exception ex) {

            }
        }
        super.onActivityResult(requestscode, resultCode, data);
    }

    private void showDialogImage2(Bitmap bitmap){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ImageView image = new ImageView(this);
        Button btn_new_img = new Button(this);
        LinearLayout container_img_btn = new LinearLayout(this);
        container_img_btn.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_new_img.setText("Escolher outra imagem");
        image.setImageBitmap(bitmap);
        image.setLayoutParams(lp);
        builder.setTitle("Selecionar imagem");
        container_img_btn.addView(image,lp);
        container_img_btn.addView(btn_new_img);
        builder.setView(container_img_btn);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                SendImageProfileData();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "negativo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });

        btn_new_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageToProfile();
            }
        });

        alerta_names = builder.create();
        alerta_names.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alerta_names.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);

        alerta_names.show();
    }

    @SuppressLint("MissingInflatedId")
    public void alertDD(Bitmap bitmap){
        LayoutInflater factory = LayoutInflater.from(this);
        View deleteDialogView = factory.inflate(R.layout.dialog_open_image, null);
        ImageView image;
        image = deleteDialogView.findViewById(R.id.img_getted);
        image.setImageBitmap(bitmap);

        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendImageProfileData();
                alerta_names.dismiss();
            }
        });
        deleteDialogView.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta_names.dismiss();
            }
        });
        deleteDialogView.findViewById(R.id.btn_get_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageToProfile();
            }
        });
        alerta_names = deleteDialog.create();
        alerta_names.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alerta_names.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        alerta_names.show();
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

    public void getImageToProfile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Buscar image"), 1);

    }

    public void SendImageProfileData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_user = sharedPreferences.getInt("PK_USER", 0);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String url = "https://brendonlulucas.pythonanywhere.com/" + id_user + "/API/APIGetUserDetail/";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.PUT, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + sharedPreferences.getString("TOKEN", "null"));
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("foto", new VolleyMultipartRequest.DataPart(imagename + ".png", imageEscolhida));
                return params;
            }
        };

        MyRequestQueue.add(volleyMultipartRequest);
    }
}

