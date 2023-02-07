package com.example.prototipoapp.home.ui.instituicao;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SearchView;
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
import com.example.prototipoapp.databinding.FragmentInstituicaoBinding;
import com.example.prototipoapp.home.ui.veiculo.FormAddVeiculoActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class InstituicaoFragment extends Fragment {

    private FragmentInstituicaoBinding binding;
    public static String SHARED_PREFERENCES = "DadosUser";
    private AlertDialog alerta;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInstituicaoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setHasOptionsMenu(true);

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences.getInt("CARGO_ID", 0) == 1 &&
                sharedPreferences.getInt("INSTITUICAO_ID", 0) == 0) {
            binding.Contente555.setVisibility(View.INVISIBLE);
        } else if (sharedPreferences.getInt("INSTITUICAO_ID", 0) != 0) {
            binding.Contente555.setVisibility(View.VISIBLE);
            if (internetIsConnected()){

                GetInstituicaoDetailsRequests();
            }

        }
        int id_user = sharedPreferences.getInt("INSTITUICAO_ID", 0);
        Toast.makeText(getContext(), id_user+"", Toast.LENGTH_SHORT).show();

        return root;
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    void set_info_inst(JSONObject responseJSON) {
        try {
            binding.nameInstId.setText(responseJSON.getString("nome").toString());
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        int id_instituicao = sharedPreferences.getInt("INSTITUICAO_ID", 0);
        if (id_instituicao == 0){
            inflater.inflate(R.menu.menu_add_veiculo, menu);
            MenuItem createVitem = menu.findItem(R.id.id_create_v);
            createVitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    ShowDialogCreateInstituicao();
//                    Intent intent = new Intent(getContext(), FormAddVeiculoActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    Toast.makeText(getContext(), "Criar olha so", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }else {
            inflater.inflate(R.menu.menu_trash, menu);
            MenuItem createVitem = menu.findItem(R.id.id_remove_item);
            createVitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    showPopMenuConfirmDelete();
                    return false;
                }
            });

        }
    }

    public void GetInstituicaoDetailsRequests() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_inst = sharedPreferences.getInt("INSTITUICAO_ID", 0);
        String url = "https://brendonlulucas.pythonanywhere.com/" + id_inst + "/API/APIGIntituicao/";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    set_info_inst(responseJSON);
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

    public void PostStringVolley(final String nameInst) {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(binding.getRoot().getContext());
        String url = "https://brendonlulucas.pythonanywhere.com/API/APICreateIntituicao/";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("INSTITUICAO_ID", responseJSON.getInt("pk"));
                    editor.apply();
                    getActivity().recreate();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(binding.getRoot().getContext(), response, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(binding.getRoot().getContext(), body, Toast.LENGTH_SHORT).show();
                }

                System.out.println(body);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("nome", nameInst);
                return MyData;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + sharedPreferences.getString("TOKEN", "null"));
                System.out.println("Authorization:" + "Token " + sharedPreferences.getString("TOKEN", "null"));
                return params;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }

    public void DeleteInstituicao() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_inst = sharedPreferences.getInt("INSTITUICAO_ID", 0);
        String url = "https://brendonlulucas.pythonanywhere.com/" + id_inst + "/API/APIGIntituicao/";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.DELETE, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("INSTITUICAO_ID", 0);
                editor.apply();
                getActivity().recreate();
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
    private void showPopMenuConfirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Remover Instituição?");
        builder.setMessage("Tudo será apagado e não poderá ser recuperado!");
        builder.setIcon(R.drawable.ic_baseline_warning_24);
        builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                DeleteInstituicao();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alerta = builder.create();
        alerta.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        alerta.show();
    }

    private void ShowDialogCreateInstituicao() {
        final EditText inputname = new EditText(getActivity());
        inputname.setInputType(InputType.TYPE_CLASS_TEXT);
        inputname.setHint("Nome Instituição");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Adicionar Instituição");
        builder.setMessage("Nome");
        builder.setView(inputname);
        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                String nameInst = inputname.getText().toString();
                if (!nameInst.equals("")) {
                    Toast.makeText(getActivity(), nameInst + arg1, Toast.LENGTH_SHORT).show();
                    String url = "";
                    PostStringVolley(nameInst);
                } else {
                    Toast.makeText(getActivity(), "Valor invalido", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), "negativo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        alerta = builder.create();
        alerta.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        alerta.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}