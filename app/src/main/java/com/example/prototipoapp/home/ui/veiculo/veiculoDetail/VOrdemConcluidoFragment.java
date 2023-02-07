package com.example.prototipoapp.home.ui.veiculo.veiculoDetail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.prototipoapp.home.ui.ordem.Ordem;
import com.example.prototipoapp.home.ui.veiculo.veiculoDetail.AdapterV.RecycleVOrdemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VOrdemConcluidoFragment extends Fragment {
    public static String SHARED_PREFERENCES = "DadosUser";
    RecyclerView recyclerView;
    List<Ordem> ordemsitens;
    public static RecycleVOrdemAdapter recycleVOrdemAdapterFinal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_v_ordem_concluido, container, false);

        recyclerView = view.findViewById(R.id.recycle_view_ordens_2);
        ordemsitens = new ArrayList<>();

        GetOrdemRequest();
        return view;
    }


    public void GetOrdemRequest() {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        OrdensVeiculo activity = (OrdensVeiculo) getActivity();
        int id_veiculo = activity.getIdVeiculo();

        String url = "https://brendonlulucas.pythonanywhere.com/" + id_veiculo + "/API/APIget_ordem_veiculo/";

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            Ordem ordemItem = new Ordem();
                            JSONObject ordemObject = jsonArray.getJSONObject(i);
                            JSONObject solicitanteDATA = ordemObject.getJSONObject("solicitante");
                            JSONObject motoristaDATA = ordemObject.getJSONObject("motorista");
                            JSONObject veiculoDATA = ordemObject.getJSONObject("veiculo");

                            if (ordemObject.getString("status").toString().equals("1")) {
                                ordemItem.setPk(ordemObject.getInt("pk"));
                                ordemItem.setSolicitante(solicitanteDATA.getString("name").toString());
                                ordemItem.setDescricao("Descrição aqui");
                                ordemItem.setStatus(ordemObject.getString("status").toString());
                                ordemItem.setData_solicitacao(ordemObject.getString("data_solicitacao").toString());
                                ordemsitens.add(ordemItem);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initRecycleViewO();
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


    private void initRecycleViewO() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recycleVOrdemAdapterFinal = new RecycleVOrdemAdapter(getActivity().getApplicationContext(), ordemsitens);
        recyclerView.setAdapter(recycleVOrdemAdapterFinal);
    }
}