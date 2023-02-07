package com.example.prototipoapp.home.ui.ordem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prototipoapp.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class FormAddOrdemActivity extends AppCompatActivity {
    public static String SHARED_PREFERENCES = "DadosUser";
    TextInputEditText data_picker, time_picker, vagas_edit;
    AutoCompleteTextView data_picker_edit;
    TextInputLayout data_picker_layout, time_picker_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add_ordem);

        setTitle("Criar Ordem");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String[] type = new String[]{"Pessoas", "Material", "Outros"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.drop_down_item_type_v, type);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.data_tipo_fill);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Toast.makeText(FormAddOrdemActivity.this, "before tex changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                TextInputLayout vagas_layout_text = findViewById(R.id.input_vaga_v);
                if (charSequence.toString().equals("Pessoas")) {
                    vagas_layout_text.setVisibility(View.VISIBLE);
                } else {
                    vagas_layout_text.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Toast.makeText(FormAddOrdemActivity.this, "after text changed", Toast.LENGTH_SHORT).show();
            }
        });

        vagas_edit = findViewById(R.id.input_content_vaga_v);
        vagas_edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        vagas_edit.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

        data_picker = findViewById(R.id.data_text_date);
        data_picker.setInputType(InputType.TYPE_NULL);
        data_picker.setKeyListener(null);

        time_picker = findViewById(R.id.data_text_time);
        time_picker.setInputType(InputType.TYPE_NULL);
        time_picker.setKeyListener(null);

        data_picker_layout = findViewById(R.id.content_data_picker);
        time_picker_layout = findViewById(R.id.content_timer_picker);
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


    }

    private void showTimePickerDialog() {
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Escolha o Horario").build();
        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                Toast.makeText(FormAddOrdemActivity.this, "Pegouuu", Toast.LENGTH_SHORT).show();
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
        MenuItem confirmOitem = menu.findItem(R.id.id_confirm_v);
        confirmOitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                postDataOrdem();
                return false;
            }
        });
        return true;
    }

    private void postDataOrdem() {
        String carga, local_saida, local_destino, descricao,
                data_solicitada, hora_solicitada;
        AutoCompleteTextView type_T;
        TextInputEditText qtd, saida, destino, desc, d_s, h_s;

        type_T = findViewById(R.id.data_tipo_fill);
        qtd = findViewById(R.id.input_content_vaga_v);
        saida = findViewById(R.id.data_text_saida);
        destino = findViewById(R.id.data_text_destino);
        desc = findViewById(R.id.data_descricao_text);
        d_s = findViewById(R.id.data_text_date);
        h_s = findViewById(R.id.data_text_time);

        if (!type_T.getText().toString().equals("Pessoa")) {
            carga = "0";
        } else {
            carga = qtd.getText().toString();
        }
        local_saida = saida.getText().toString();
        local_destino = destino.getText().toString();
        descricao = desc.getText().toString();
        data_solicitada = d_s.getText().toString();
        hora_solicitada = h_s.getText().toString();


        if (!isNull(local_saida) && !isNull(local_destino) && !isNull(descricao) &&
                !isNull(data_solicitada) && !isNull(hora_solicitada)) {

            SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                String reformattedStr = myFormat.format(fromUser.parse(data_solicitada));
                PostDataOrdemVolley(local_saida, local_destino, carga, reformattedStr, hora_solicitada, descricao);
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            PostDataOrdemVolley(local_saida, local_destino, carga, reformattedStr, hora_solicitada, descricao);
        }
    }

    private boolean isNull(Object args) {
        if (args == null || (args + "").equals("")) {
            return true;
        }
        return false;
    }

    public void PostDataOrdemVolley(String saida, String destino, String carga, String data_s,
                                    String hora_s, String descricao) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_instituicao = sharedPreferences.getInt("INSTITUICAO_ID", 0);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        String url = getString(R.string.dominio) + id_instituicao + getString(R.string.CREATE_ORDEM);
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
                params.put("data_solicitado", data_s);
                params.put("horario_requirido", hora_s);
                params.put("descricao", descricao);
                params.put("qtd_espaco", carga);
                params.put("origem", saida);
                params.put("destino", destino);
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