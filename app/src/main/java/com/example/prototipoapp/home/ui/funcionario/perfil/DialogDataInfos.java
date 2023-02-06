package com.example.prototipoapp.home.ui.funcionario.perfil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prototipoapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class DialogDataInfos extends DialogFragment {
    public static String SHARED_PREFERENCES = "DadosUser";
    public static final String TAG = "example_dialog";
    private Toolbar toolbar;
    private AlertDialog alerta_name, alerta_username, alerta_pass, alerta_fone;

    String optionsUserList[] = {"Nome", "Usuario", "Senha", "Telefone"};
    String[] dataUserList = new String[4];
    int icUserList[] = {R.drawable.ic_baseline_phone_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_edit_calendar_24,
            R.drawable.ic_baseline_garage_24};
    ListView listViewData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_full_v1, container, false);
        toolbar = view.findViewById(R.id.toolbar);

        GetUserDetailsRequests(view);


        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    public static DialogDataInfos display(FragmentManager fragmentManager) {
        DialogDataInfos exampleDialog = new DialogDataInfos();
        exampleDialog.show(fragmentManager, TAG);
        return exampleDialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle("Dados");
        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.inflateMenu(R.menu.dialog_full_v1);
        toolbar.setOnMenuItemClickListener(item -> {
            dismiss();
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);

        }
    }

    private void set_values_user(JSONObject response, View view) {

        try {
            JSONObject moore_data_user = response.getJSONObject("user");
            dataUserList[0] = response.getString("name").toString();
            dataUserList[1] = moore_data_user.getString("username").toString();
            dataUserList[2] = "*******";
            dataUserList[3] = response.getString("telefone").toString();
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

        listViewData = view.findViewById(R.id.List_options_data_user);
        AdapterListItemDetail adapterListItem = new AdapterListItemDetail(getContext(), optionsUserList, icUserList, dataUserList);

        listViewData.setDivider(new ColorDrawable(this.getResources().getColor(R.color.colorPrimary)));
        listViewData.setDividerHeight(1);
        listViewData.setAdapter(adapterListItem);
        listViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = optionsUserList[i];
                switch (item) {
                    case "Nome":
                        DialogChangeName();
                        Toast.makeText(getContext(), "OPT1:" + item, Toast.LENGTH_SHORT).show();
                        break;
                    case "Usuario":
                        DialogChangeUsername();
                        Toast.makeText(getContext(), "Not:" + item, Toast.LENGTH_SHORT).show();
                        break;
                    case "Senha":
                        DialogChangePass();
                        Toast.makeText(getContext(), "Dad:" + item, Toast.LENGTH_SHORT).show();
                        break;
                    case "Telefone":
                        DialogChangeFone();
                        Toast.makeText(getContext(), "Pref:" + item, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    public void GetUserDetailsRequests(View view) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_user = sharedPreferences.getInt("PK_USER", 0);
        String url = "https://brendonlulucas.pythonanywhere.com/" + id_user + "/API/APIGetUserDetail/";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    set_values_user(responseJSON, view);
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
        };
        queue.add(jsonArrayRequest);
    }


    private void DialogChangeName() {
        final EditText input = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0, 50, 0);
        input.setGravity(android.view.Gravity.TOP | android.view.Gravity.LEFT);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setLines(1);
        input.setMaxLines(1);
        input.setLayoutParams(lp);
        input.setHint("Nomes");
        builder.setTitle("Modificar nome");
        container.addView(input, lp);
        builder.setView(container);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                String name = input.getText().toString();
                if (!name.equals("")) {
//                    PutDataUserVolley("name", name);
                    Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Valor invalido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getContext(), "negativo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        alerta_name = builder.create();
        alerta_name.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alerta_name.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        alerta_name.show();
    }

    private void DialogChangeUsername() {
        final EditText input = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0, 50, 0);
        input.setGravity(android.view.Gravity.TOP | android.view.Gravity.LEFT);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setLines(1);
        input.setMaxLines(1);
        input.setLayoutParams(lp);
        input.setHint("Nomes");
        builder.setTitle("Modificar nome");
        container.addView(input, lp);
        builder.setView(container);
        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                String nameUsername = input.getText().toString();
                if (!nameUsername.equals("")) {
                    PutDataUserVolley("username", nameUsername);
                    Toast.makeText(getContext(), nameUsername + arg1, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Valor invalido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getContext(), "negativo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        alerta_username = builder.create();
        alerta_username.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alerta_username.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        alerta_username.show();

    }

    private void DialogChangePass() {
        final EditText input_p_1 = new EditText(getContext());
        final EditText input_p_2 = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LinearLayout container_p_1 = new LinearLayout(getContext());
        LinearLayout container_p_2 = new LinearLayout(getContext());

        container_p_1.setOrientation(LinearLayout.VERTICAL);
        container_p_2.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0, 50, 0);

        input_p_1.setGravity(android.view.Gravity.TOP | android.view.Gravity.LEFT);
        input_p_1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input_p_1.setLines(1);
        input_p_1.setMaxLines(1);
        input_p_1.setLayoutParams(lp);
        input_p_1.setHint("Senha");

        input_p_2.setGravity(android.view.Gravity.TOP | android.view.Gravity.LEFT);
        input_p_2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input_p_2.setLines(1);
        input_p_2.setMaxLines(1);
        input_p_2.setLayoutParams(lp);
        input_p_2.setHint("Confirmar Senha");
        container_p_1.addView(input_p_1, lp);
        container_p_1.addView(input_p_2, lp);

        builder.setTitle("Modificar senha");
        builder.setView(container_p_1);


        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                String pass_1 = input_p_1.getText().toString();
                String pass_2 = input_p_2.getText().toString();
                if (!pass_1.equals("") && !pass_2.equals("") && pass_1.equals(pass_2)) {
                    Toast.makeText(getContext(), pass_1, Toast.LENGTH_SHORT).show();
                    PutDataUserVolley("password", pass_1);
                } else {
                    Toast.makeText(getContext(), "Valor invalido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getContext(), "negativo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        alerta_pass = builder.create();
        alerta_pass.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alerta_pass.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        alerta_pass.show();
    }

    private void DialogChangeFone() {
        final EditText input = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0, 50, 0);
        input.setGravity(android.view.Gravity.TOP | android.view.Gravity.LEFT);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setLines(1);
        input.setMaxLines(1);
        input.setLayoutParams(lp);
        input.setHint("Telefone");
        builder.setTitle("Modificar Telefone");
        container.addView(input, lp);
        builder.setView(container);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                String fone = input.getText().toString();
                if (!fone.equals("")) {
                    PutDataUserVolley("telefone", fone);
                    Toast.makeText(getContext(), fone, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Valor invalido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        alerta_fone = builder.create();
        alerta_fone.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alerta_fone.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        alerta_fone.show();

    }


    public void PutDataUserVolley(String tipo, String value) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_user = sharedPreferences.getInt("PK_USER", 0);
        String url = "https://brendonlulucas.pythonanywhere.com/" + id_user + "/API/APIGetUserDetail/";
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest MyStringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GetUserDetailsRequests(getView());
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
                    Toast.makeText(getContext(), "valor inválido ou não disponivel", Toast.LENGTH_SHORT).show();
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
}
