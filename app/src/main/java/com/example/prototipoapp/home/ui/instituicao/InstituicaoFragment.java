package com.example.prototipoapp.home.ui.instituicao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.prototipoapp.databinding.FragmentInstituicaoBinding;


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
        InstituicaoViewModel galleryViewModel =
                new ViewModelProvider(this).get(InstituicaoViewModel.class);

        binding = FragmentInstituicaoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fabAddInsId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PostStringVolley("lul", "1213");
                ShowDialogCreateInstituicao();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        System.out.println("*************************************************" + sharedPreferences.getInt("INSTITUICAO_ID", 0));

        if (sharedPreferences.getInt("CARGO_ID", 0) == 1 &&
                sharedPreferences.getInt("INSTITUICAO_ID", 0) == 0) {
            binding.containerInstId.setVisibility(View.INVISIBLE);
            binding.fabAddInsId.setVisibility(View.VISIBLE);

        } else if (sharedPreferences.getInt("INSTITUICAO_ID", 0) != 0) {
            binding.containerInstId.setVisibility(View.VISIBLE);
            binding.fabAddInsId.setVisibility(View.INVISIBLE);
            set_info_inst();
        }

//        if (sharedPreferences.getInt("INSTITUICAO_ID", 0) != 0) {
//            binding.containerInstId.setVisibility(View.VISIBLE);
//            binding.fabAddInsId.setVisibility(View.INVISIBLE);
//        } else {
//            binding.containerInstId.setVisibility(View.INVISIBLE);
//            binding.fabAddInsId.setVisibility(View.VISIBLE);
//        }


        final TextView textView = binding.textInstituicao;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    void set_info_inst() {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        try {
            String dataInst = sharedPreferences.getString("DADOS_INSTITUICAO", "null");
            String nameinst = new JSONObject(dataInst).getString("nome");
            binding.nameInstId.setText(nameinst);

        } catch (
                JSONException e) {
            e.printStackTrace();
        }


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
        alerta.show();
    }


    public void PostStringVolley(final String nameInst) {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(binding.getRoot().getContext());
        String url = "https://brendonlulucas.pythonanywhere.com/API/APICreateIntituicao/";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}