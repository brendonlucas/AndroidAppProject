package com.example.prototipoapp.home.ui.ordem.GerenciaOrdem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prototipoapp.R;
import com.example.prototipoapp.home.ui.funcionario.User;
import com.example.prototipoapp.home.ui.funcionario.perfil.AdapterListItem;
import com.example.prototipoapp.home.ui.ordem.FormAddOrdemActivity;
import com.example.prototipoapp.home.ui.veiculo.VeiculoItem;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.shuhart.stepview.StepView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class ConfirmOrdem extends AppCompatActivity {
    StepView stepView;
    Button btn_nxt, btn_previs;
    ConstraintLayout box1, box2, box3, box4, box5;
    int index = 0;

    Spinner spinner,spinner_motorista;

    List<VeiculoItem> veiculoList;
    ArrayList<String> veiculoNamesList;
    List<User> motoristasList;
    List<String> motoristasNamesList;
    public static String SHARED_PREFERENCES = "DadosUser";

    TextInputEditText data_picker, time_picker;
    TextInputLayout data_picker_layout, time_picker_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ordem);
        setTitle("Confirm Ordem");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        spinner = findViewById(R.id.spinner_veiculo);
        spinner_motorista = findViewById(R.id.spinner_motorista);

        veiculoNamesList = new ArrayList<>();
        veiculoList = new ArrayList<>();
        motoristasList = new ArrayList<>();
        motoristasNamesList = new ArrayList<>();

        GetVeiculosRequest();
        GetMotoristasRequest();

        data_picker = findViewById(R.id.form_text_date);
        data_picker.setInputType(InputType.TYPE_NULL);
        data_picker.setKeyListener(null);

        time_picker = findViewById(R.id.form_text_time);
        time_picker.setInputType(InputType.TYPE_NULL);
        time_picker.setKeyListener(null);

        data_picker_layout = findViewById(R.id.content_data_picker_form);
        time_picker_layout = findViewById(R.id.content_timer_picker_form);
        data_picker_layout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDataPickerDialog();
            }
        });
        time_picker_layout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        box1 = findViewById(R.id.content_dados_inicial);
        box2 = findViewById(R.id.content_form_1);
        box3 = findViewById(R.id.content_form_2);
        box4 = findViewById(R.id.content_select_data);
        box5 = findViewById(R.id.content_resumo);

        box2.setVisibility(View.GONE);
        box3.setVisibility(View.GONE);
        box4.setVisibility(View.GONE);
        box5.setVisibility(View.GONE);


        stepView = findViewById(R.id.step_view);
        stepView.getState().animationType(StepView.ANIMATION_ALL)
                .stepsNumber(5)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .commit();


        btn_nxt = findViewById(R.id.next_btn);
        btn_previs = findViewById(R.id.previs_btn);

        btn_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < 5 && index < 4) {
                    index++;
                    stepView.go(index, true);

                    setGoneVisible(index);

                    Toast.makeText(getApplicationContext(), index + "", Toast.LENGTH_SHORT).show();
                    if (index == 4) {
                        btn_nxt.setVisibility(View.INVISIBLE);
                    }
                    if (index > 0) {
                        btn_previs.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btn_previs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < 5 && index >= 1) {
                    index--;
                    stepView.go(index, true);

                    setGoneVisible(index);

                    Toast.makeText(getApplicationContext(), index + "", Toast.LENGTH_SHORT).show();
                    if (index < 4) {
                        btn_nxt.setVisibility(View.VISIBLE);
                    }
                    if (index == 0) {
                        btn_previs.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

    }



    public void initSpinnerAdapter() {
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, veiculoNamesList));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView nome_car, placa_txt;
                ImageView img_car;
                ConstraintLayout ctnt_info_car;

                nome_car = findViewById(R.id.text_name_info_car);
                placa_txt = findViewById(R.id.text_placa_info_car);
                img_car = findViewById(R.id.img_info_car);
                ctnt_info_car = findViewById(R.id.ctnt_info_car);

                nome_car.setText(veiculoList.get(i).getName());
                placa_txt.setText(veiculoList.get(i).getPlaca());
                Picasso.get().load(veiculoList.get(i).getImageUrl()).into(img_car);
                ctnt_info_car.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void initSpinner2Adapter() {
        spinner_motorista.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, motoristasNamesList));
        spinner_motorista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView nome_moto, contato_txt;
                ImageView img_car;
                ConstraintLayout ctnt_info_moto;

                nome_moto = findViewById(R.id.text_name_info_motorista);
                contato_txt = findViewById(R.id.text_contato_info_motorista);
                img_car = findViewById(R.id.img_info_motorista);
                ctnt_info_moto = findViewById(R.id.ctnt_info_motorista);

                nome_moto.setText(motoristasList.get(i).getName());
                contato_txt.setText(motoristasList.get(i).getContato());
//                Picasso.get().load(motoristasList.get(i).getImageUrl()).into(img_car);
                ctnt_info_moto.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showTimePickerDialog() {
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Escolha o Horario").build();
        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Pegouuu", Toast.LENGTH_SHORT).show();
                time_picker.setText(materialTimePicker.getHour() + ":" + materialTimePicker.getMinute());
            }
        });

        materialTimePicker.show(getSupportFragmentManager(), "TIME_PICKER");
    }

    private void showDataPickerDialog() {
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Escolha a data").build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                utc.setTimeInMillis((Long) selection);
                String date = calendarToDate(getApplicationContext(), utc, "dd-MM-yyyy");
                data_picker.setText("" + date);
            }
        });
        materialDatePicker.show(getSupportFragmentManager(), "TAG");
    }

    public static String calendarToDate(Context context, Calendar calendar, String dateFormat) {
        if (calendar == null) {
            return null;
        }
        Locale locale = context.getResources().getConfiguration().locale;
        SimpleDateFormat df = new SimpleDateFormat(dateFormat, locale);
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        df.setTimeZone(timeZone);
        Date d = calendar.getTime();
        return df.format(d);
    }

    public void GetVeiculosRequest() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_instituicao = sharedPreferences.getInt("INSTITUICAO_ID", 0);
        String url = getString(R.string.dominio) + id_instituicao + getString(R.string.GET_VEICULOS);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray responseJSON = new JSONArray(response);
                    for (int i = 0; i < responseJSON.length(); i++) {
                        try {
                            JSONObject veiculoObject = responseJSON.getJSONObject(i);
                            VeiculoItem veiculoItem = new VeiculoItem();
                            veiculoItem.setPk(veiculoObject.getInt("pk"));
                            veiculoItem.setName(veiculoObject.getString("name").toString());
                            veiculoItem.setPlaca(veiculoObject.getString("placa").toString());
                            veiculoItem.setImageUrl(veiculoObject.getString("image").toString());
                            veiculoItem.setTipo(veiculoObject.getInt("tipo"));
                            veiculoList.add(veiculoItem);
                            veiculoNamesList.add(veiculoObject.getString("name").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    initSpinnerAdapter();

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
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, "UTF-8");
                    return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void GetMotoristasRequest() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_instituicao = sharedPreferences.getInt("INSTITUICAO_ID", 0);
        String url = getString(R.string.dominio) + id_instituicao + getString(R.string.GET_FUNCIONARIOS);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray responseJSON = new JSONArray(response);
                    for (int i = 0; i < responseJSON.length(); i++) {
                        try {
                            JSONObject funcObject = responseJSON.getJSONObject(i);
                            if (funcObject.getString("cargo").equals("4")){
                                User funcItem = new User();
                                funcItem.setPk(funcObject.getInt("pk"));
                                funcItem.setName(funcObject.getString("name").toString());
                                funcItem.setContato(funcObject.getString("telefone").toString());
                                funcItem.setCargo(funcObject.getString("cargo").toString());
                                motoristasList.add(funcItem);
                                motoristasNamesList.add(funcObject.getString("name").toString());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    initSpinner2Adapter();
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
                return params;
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, "UTF-8");
                    return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void setGoneVisible(int page) {
        switch (page) {
            case 0:
                box1.setVisibility(View.VISIBLE);
                box1.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));
                box2.setVisibility(View.GONE);
                box3.setVisibility(View.GONE);
                box4.setVisibility(View.GONE);
                box5.setVisibility(View.GONE);
                break;
            case 1:
                box2.setVisibility(View.VISIBLE);
                box2.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));
                box1.setVisibility(View.GONE);
                box3.setVisibility(View.GONE);
                box4.setVisibility(View.GONE);
                box5.setVisibility(View.GONE);
                break;
            case 2:
                box3.setVisibility(View.VISIBLE);
                box3.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));
                box1.setVisibility(View.GONE);
                box2.setVisibility(View.GONE);
                box4.setVisibility(View.GONE);
                box5.setVisibility(View.GONE);
                break;
            case 3:
                box4.setVisibility(View.VISIBLE);
                box4.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));
                box1.setVisibility(View.GONE);
                box2.setVisibility(View.GONE);
                box3.setVisibility(View.GONE);
                box5.setVisibility(View.GONE);
                break;
            case 4:
                box5.setVisibility(View.VISIBLE);
                box5.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));
                box1.setVisibility(View.GONE);
                box2.setVisibility(View.GONE);
                box3.setVisibility(View.GONE);
                box4.setVisibility(View.GONE);
                break;
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