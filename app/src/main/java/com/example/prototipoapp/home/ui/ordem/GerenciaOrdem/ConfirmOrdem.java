package com.example.prototipoapp.home.ui.ordem.GerenciaOrdem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import java.text.DateFormat;
import java.text.ParseException;
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
    Button btn_nxt, btn_previs, btn_send_form_confirm;
    ConstraintLayout box1, box2, box3, box4, box5;
    int index = 0;

    CustomSearchableSpinner spinner, spinner_motorista;

    List<VeiculoItem> veiculoList;
    ArrayList<String> veiculoNamesList;
    List<User> motoristasList;
    List<String> motoristasNamesList;
    User motoristaSelecionado;
    VeiculoItem veiculoSelecionado;

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

        spinner = (CustomSearchableSpinner) findViewById(R.id.spinner_veiculo);
        spinner_motorista = (CustomSearchableSpinner) findViewById(R.id.spinner_motorista);

        veiculoNamesList = new ArrayList<>();
        veiculoList = new ArrayList<>();
        motoristasList = new ArrayList<>();
        motoristasNamesList = new ArrayList<>();

        GetOrdemRequest();
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

        btn_send_form_confirm = findViewById(R.id.btn_send_form_confirm);
        btn_send_form_confirm.setVisibility(View.GONE);

        btn_send_form_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_veiculo, id_motorista, data_1, data_2;
                id_veiculo = Integer.toString(veiculoSelecionado.getPk());
                id_motorista = Integer.toString(motoristaSelecionado.getPk());
                TextInputEditText edt_data_1 = findViewById(R.id.form_text_date);
                TextInputEditText edt_data_2 = findViewById(R.id.form_text_time);
                data_1 = edt_data_1.getText().toString();
                data_2 = edt_data_2.getText().toString();
                if (!id_veiculo.equals("") && !id_motorista.equals("") && !data_1.equals("") &&
                        !data_2.equals("")) {
                    PostDataConfirmVolley(id_motorista, id_veiculo, data_1, data_2);

                }
            }
        });

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
                    boolean pode_passar = false;
                    switch (index) {
                        case 0:
                            pode_passar = true;
                            break;
                        case 1:
                            if (veiculoSelecionado != null) {
                                pode_passar = true;
                            }
                            break;
                        case 2:
                            if (motoristaSelecionado != null) {
                                pode_passar = true;
                            }
                            break;
                        case 3:
                            TextInputEditText edt_data_1 = findViewById(R.id.form_text_date);
                            TextInputEditText edt_data_2 = findViewById(R.id.form_text_time);
                            String data_1 = edt_data_1.getText().toString();
                            String data_2 = edt_data_2.getText().toString();
                            if (!data_1.equals("") && !data_2.equals("")) {
                                pode_passar = true;
                                setDadosResumoFinal();
                            }
                            break;
                    }
                    if (pode_passar) {
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
        spinner.setTitle("Selecione");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner.isCountriesSpinnerOpen = false;
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
                veiculoSelecionado = veiculoList.get(i);
                Toast.makeText(ConfirmOrdem.this, veiculoSelecionado.getName(), Toast.LENGTH_SHORT).show();
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
                motoristaSelecionado = motoristasList.get(i);
                Toast.makeText(ConfirmOrdem.this, motoristaSelecionado.getName(), Toast.LENGTH_SHORT).show();
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
                            if (funcObject.getString("cargo").equals("4")) {
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
                btn_send_form_confirm.setVisibility(View.GONE);
                break;
            case 1:
                box2.setVisibility(View.VISIBLE);
                box2.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));
                box1.setVisibility(View.GONE);
                box3.setVisibility(View.GONE);
                box4.setVisibility(View.GONE);
                box5.setVisibility(View.GONE);
                btn_send_form_confirm.setVisibility(View.GONE);
                break;
            case 2:
                box3.setVisibility(View.VISIBLE);
                box3.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));
                box1.setVisibility(View.GONE);
                box2.setVisibility(View.GONE);
                box4.setVisibility(View.GONE);
                box5.setVisibility(View.GONE);
                btn_send_form_confirm.setVisibility(View.GONE);
                break;
            case 3:
                box4.setVisibility(View.VISIBLE);
                box4.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));
                box1.setVisibility(View.GONE);
                box2.setVisibility(View.GONE);
                box3.setVisibility(View.GONE);
                box5.setVisibility(View.GONE);
                btn_send_form_confirm.setVisibility(View.GONE);
                break;
            case 4:
                box5.setVisibility(View.VISIBLE);
                box5.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));
                box1.setVisibility(View.VISIBLE);
                box2.setVisibility(View.GONE);
                box3.setVisibility(View.GONE);
                box4.setVisibility(View.GONE);
                btn_send_form_confirm.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void GetOrdemRequest() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        int id_ordem = bundle.getInt("id_ordem");
        String url = getString(R.string.dominio) + id_ordem + getString(R.string.GET_DETAILS_ORDEM);
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

    public void setValuesInTextView(JSONObject responseJSON) {
        TextView name_soli, data_em, data_para, tipo_soli, qtd_vaga_soli, descri_soli;
        ConstraintLayout ctnt_carga;
        name_soli = findViewById(R.id.txt_data_solicitante_info);
        data_em = findViewById(R.id.txt_data_SE_info);
        data_para = findViewById(R.id.txt_data_SP_info);
        tipo_soli = findViewById(R.id.txt_data_tipo_info);
        qtd_vaga_soli = findViewById(R.id.txt_data_vaga_info);
        descri_soli = findViewById(R.id.txt_data_descricao_info);
        ctnt_carga = findViewById(R.id.d4);

        try {
            JSONObject solicitanteDATA = responseJSON.getJSONObject("solicitante");
            name_soli.setText(solicitanteDATA.getString("name").toString());
            data_em.setText(convertDate(responseJSON.getString("data_solicitacao"), 1));
            data_para.setText(convertDate(responseJSON.getString("data_solicitado"), 1));
            descri_soli.setText(responseJSON.getString("descricao"));
        } catch (JSONException e) {
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

    public String convertDate(String data_to_converte, int type) {
        if (type == 1) {
            DateFormat formatUS = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = formatUS.parse(data_to_converte);
                DateFormat formatBR = new SimpleDateFormat("dd-MM-yyyy");
                return formatBR.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (type == 2) {
            DateFormat formatUS = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = formatUS.parse(data_to_converte);
                DateFormat formatBR = new SimpleDateFormat("yyyy-MM-dd");
                return formatBR.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return data_to_converte;
    }

    public void PostDataConfirmVolley(String motorista_id, String veiculo_id, String data_1, String data2) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        int id_ordem = bundle.getInt("id_ordem");
        String url = getString(R.string.dominio) + id_ordem + getString(R.string.CONFIRM_ORDEM);
        StringRequest MyStringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                final String statusCode = String.valueOf(error.networkResponse.statusCode);
//                String body = "";
//                try {
//                    body = new String(error.networkResponse.data, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    // exception
//                }
//                if (statusCode.equals("400")) {
//                    Toast.makeText(binding.getRoot().getContext(), body, Toast.LENGTH_SHORT).show();
//                }
//
//                System.out.println(body);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("motorista", motorista_id);
                MyData.put("veiculo", veiculo_id);
                MyData.put("data_marcada", convertDate(data_1, 2));
                MyData.put("horario_marcado", data2);
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


    @Override
    protected void onPause() {
        dismissSpinner();
        super.onPause();
    }

    private void dismissSpinner() {
        Fragment searchableSpinnerDialog = getFragmentManager().findFragmentByTag("TAG");
        if (searchableSpinnerDialog != null && searchableSpinnerDialog.isAdded()) {
            getFragmentManager().beginTransaction().remove(searchableSpinnerDialog).commit();
        }
    }

    private void setDadosResumoFinal() {
        TextView name_v, tipo_v, fabrica_v, name_m, contato_m, status_m , data_m, hora_m;
        ImageView img_v, img_m;
        TextInputEditText edt_data_1 = findViewById(R.id.form_text_date);
        TextInputEditText edt_time_2 = findViewById(R.id.form_text_time);

        name_v = findViewById(R.id.txt_RF_NV);
        tipo_v = findViewById(R.id.txt_RF_TV);
        fabrica_v = findViewById(R.id.txt_RF_FV);
        img_v = findViewById(R.id.img_RF_IV);

        name_m = findViewById(R.id.txt_RF_NM);
        contato_m = findViewById(R.id.txt_RF_CM);
        status_m = findViewById(R.id.txt_RF_SM);
        img_m = findViewById(R.id.img_RF_IM);

        data_m = findViewById(R.id.txt_RF_DM);
        hora_m = findViewById(R.id.txt_RF_HM);

        name_v.setText(veiculoSelecionado.getName().toString());
        tipo_v.setText(veiculoSelecionado.getTipo()+"");
        fabrica_v.setText("Ainda por na Api");
        Picasso.get().load(veiculoSelecionado.getImageUrl()).into(img_v);

        name_m.setText(motoristaSelecionado.getName().toString());
        contato_m.setText(motoristaSelecionado.getContato().toString());
        status_m.setText("Ainda Por na Api");
//        Picasso.get().load(motoristaSelecionado.getImageUrl()).into(img_m); # ainda por na api

        data_m.setText(edt_data_1.getText());
        hora_m.setText(edt_time_2.getText());

    }
}