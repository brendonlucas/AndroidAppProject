package com.example.prototipoapp.home.ui.funcionario.perfil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
//        GetUserDetailsRequests();

//        card_label_name = findViewById(R.id.card_name_label);
//        card_label_username = findViewById(R.id.card_label_username);
//        card_label_pass = findViewById(R.id.card_label_pass);
//        card_label_fone = findViewById(R.id.card_label_fone);
//        card_data_name = findViewById(R.id.card_data_name);
//        card_data_username = findViewById(R.id.card_data_username);
//        card_data_pass = findViewById(R.id.card_data_pass);
//        card_data_fone = findViewById(R.id.card_data_fone);
//
//        btn_name = findViewById(R.id.btn_change_name);
//        btn_username = findViewById(R.id.btn_change_username);
//        btn_pass = findViewById(R.id.btn_change_pass);
//        btn_fone = findViewById(R.id.btn_change_fone);



        img_user = findViewById(R.id.Image_profile_acc);
        img_user.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                getImageToProfile();
                return false;
            }
        });

//        card_label_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (card_data_name.getVisibility() == View.GONE) {
//                    showContent(card_data_name);
//                } else {
//                    closeContent(card_data_name);
//                }
//            }
//        });
//
//        card_label_username.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (card_data_username.getVisibility() == View.GONE) {
//                    showContent(card_data_username);
//                } else {
//                    closeContent(card_data_username);
//                }
//
//            }
//        });
//
//        card_label_pass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (card_data_pass.getVisibility() == View.GONE) {
//                    showContent(card_data_pass);
//                } else {
//                    closeContent(card_data_pass);
//                }
//
//            }
//        });
//
//        card_label_fone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (card_data_fone.getVisibility() == View.GONE) {
//                    showContent(card_data_fone);
//                } else {
//                    closeContent(card_data_fone);
//                }
//
//            }
//        });
//
//
//        btn_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogChangeName();
//            }
//        });
//
//        btn_username.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogChangeUsername();
//            }
//        });
//
//        btn_pass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogChangePass();
//            }
//        });
//
//        btn_fone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogChangeFone();
//            }
//        });
    }
    private void openDialog() {
        DialogDataInfos.display(getSupportFragmentManager());
    }

//    private void DialogChangeUsername() {
//        final EditText input = new EditText(this);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        LinearLayout container = new LinearLayout(this);
//        container.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(50, 0, 50,0);
//        input.setGravity(android.view.Gravity.TOP|android.view.Gravity.LEFT);
//        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//        input.setLines(1);
//        input.setMaxLines(1);
//        input.setLayoutParams(lp);
//        input.setHint("Nomes");
//        builder.setTitle("Modificar nome");
//        container.addView(input,lp);
//        builder.setView(container);
//        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface arg0, int arg1) {
//                String nameUsername = input.getText().toString();
//                if (!nameUsername.equals("")) {
//                    PutDataUserVolley("username", nameUsername);
//                    Toast.makeText(getApplicationContext(), nameUsername + arg1, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Valor invalido", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface arg0, int arg1) {
//                Toast.makeText(getApplicationContext(), "negativo=" + arg1, Toast.LENGTH_SHORT).show();
//            }
//        });
//        alerta_username = builder.create();
//        alerta_username.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alerta_username.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
//        alerta_username.show();
//
//    }
//
//    private void DialogChangePass() {
//        final EditText input_p_1 = new EditText(this);
//        final EditText input_p_2 = new EditText(this);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        LinearLayout container_p_1 = new LinearLayout(this);
//        LinearLayout container_p_2 = new LinearLayout(this);
//
//        container_p_1.setOrientation(LinearLayout.VERTICAL);
//        container_p_2.setOrientation(LinearLayout.VERTICAL);
//
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(50, 0, 50,0);
//
//        input_p_1.setGravity(android.view.Gravity.TOP|android.view.Gravity.LEFT);
//        input_p_1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        input_p_1.setLines(1);
//        input_p_1.setMaxLines(1);
//        input_p_1.setLayoutParams(lp);
//        input_p_1.setHint("Senha");
//
//        input_p_2.setGravity(android.view.Gravity.TOP|android.view.Gravity.LEFT);
//        input_p_2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        input_p_2.setLines(1);
//        input_p_2.setMaxLines(1);
//        input_p_2.setLayoutParams(lp);
//        input_p_2.setHint("Confirmar Senha");
//        container_p_1.addView(input_p_1,lp);
//        container_p_1.addView(input_p_2,lp);
//
//        builder.setTitle("Modificar senha");
//        builder.setView(container_p_1);
//
//
//        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface arg0, int arg1) {
//                String pass_1 = input_p_1.getText().toString();
//                String pass_2 = input_p_2.getText().toString();
//                if (!pass_1.equals("") && !pass_2.equals("") && pass_1.equals(pass_2)) {
//                    Toast.makeText(getApplicationContext(), pass_1, Toast.LENGTH_SHORT).show();
//                    PutDataUserVolley("password", pass_1);
//                } else {
//                    Toast.makeText(getApplicationContext(), "Valor invalido", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface arg0, int arg1) {
//                Toast.makeText(getApplicationContext(), "negativo=" + arg1, Toast.LENGTH_SHORT).show();
//            }
//        });
//        alerta_pass = builder.create();
//        alerta_pass.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alerta_pass.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
//        alerta_pass.show();
//    }
//
//    private void DialogChangeFone() {
//        final EditText input = new EditText(this);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        LinearLayout container = new LinearLayout(this);
//        container.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(50, 0, 50,0);
//        input.setGravity(android.view.Gravity.TOP|android.view.Gravity.LEFT);
//        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        input.setLines(1);
//        input.setMaxLines(1);
//        input.setLayoutParams(lp);
//        input.setHint("Telefone");
//        builder.setTitle("Modificar Telefone");
//        container.addView(input,lp);
//        builder.setView(container);
//
//        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface arg0, int arg1) {
//                String fone = input.getText().toString();
//                if (!fone.equals("")) {
//                    PutDataUserVolley("telefone", fone);
//                    Toast.makeText(getApplicationContext(), fone, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Valor invalido", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface arg0, int arg1) {
//
//            }
//        });
//        alerta_fone = builder.create();
//        alerta_fone.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alerta_fone.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
//        alerta_fone.show();
//
//    }
//
//    private void DialogChangeName() {
//        final EditText input = new EditText(this);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        LinearLayout container = new LinearLayout(this);
//        container.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(50, 0, 50,0);
//        input.setGravity(android.view.Gravity.TOP|android.view.Gravity.LEFT);
//        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//        input.setLines(1);
//        input.setMaxLines(1);
//        input.setLayoutParams(lp);
//        input.setHint("Nomes");
//        builder.setTitle("Modificar nome");
//        container.addView(input,lp);
//        builder.setView(container);
//
//        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface arg0, int arg1) {
//                String name = input.getText().toString();
//                if (!name.equals("")) {
//                    PutDataUserVolley("name", name);
//                    Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Valor invalido", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface arg0, int arg1) {
//                Toast.makeText(getApplicationContext(), "negativo=" + arg1, Toast.LENGTH_SHORT).show();
//            }
//        });
//        alerta_name = builder.create();
//        alerta_name.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alerta_name.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
//        alerta_name.show();
//    }
//
//    public void showContent(CardView card) {
//        card.setVisibility(View.VISIBLE);
//        TranslateAnimation animate = new TranslateAnimation(0, 0, card.getHeight(), 0);
//        animate.setDuration(200);
//        animate.setFillAfter(true);
//        card.startAnimation(animate);
//    }
//
//    public void closeContent(CardView card) {
//        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, card.getHeight());
//        animate.setDuration(200);
//        animate.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                card.setVisibility(View.GONE);
//                Toast.makeText(PerfilActivity.this, "alksdjlak", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        card.startAnimation(animate);
//    }

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
//        name_user = findViewById(R.id.txt_data_user);
        txt_name_user = findViewById(R.id.txt_name_user);
//        name_username = findViewById(R.id.txt_data_username);
//        fone = findViewById(R.id.txt_data_fone);

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
        };
        queue.add(jsonArrayRequest);
    }

    public void PutDataUserVolley(String tipo, String value) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_user = sharedPreferences.getInt("PK_USER", 0);
        String url = "https://brendonlulucas.pythonanywhere.com/" + id_user + "/API/APIGetUserDetail/";
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GetUserDetailsRequests();
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
                    Toast.makeText(getApplicationContext(), "valor inválido ou não disponivel", Toast.LENGTH_SHORT).show();
                }

                System.out.println(body);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put(tipo, value);
                return MyData;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + sharedPreferences.getString("TOKEN", "null"));
                return params;
            }
        };
        MyRequestQueue.add(MyStringRequest);
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
                    showDialogImage2(bitmap);
                }else{
                    showDialogImage2(bitmap);
                    dialog_creat = true;
                }

                System.out.println(queryName(imageUri));

            } catch (Exception ex) {

            }
        }
        super.onActivityResult(requestscode, resultCode, data);
    }

    private void showDialogImage2(Bitmap bitmap){

        AlertDialog alerta_name;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ImageView image = new ImageView(this);
        Button btn_new_img = new Button(this);
        LinearLayout container_img_btn = new LinearLayout(this);
        container_img_btn.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(50, 0, 50,0);

        btn_new_img.setText("Escolher outra imagem");
        image.setImageBitmap(bitmap);

        builder.setTitle("Selecionar imagem");
        container_img_btn.addView(image,lp);
        container_img_btn.addView(btn_new_img,lp);

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

