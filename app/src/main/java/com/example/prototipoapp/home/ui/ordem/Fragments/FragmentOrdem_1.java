package com.example.prototipoapp.home.ui.ordem.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.prototipoapp.home.ui.veiculo.veiculoDetail.OrdensVeiculo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentOrdem_1 extends Fragment {
    RecyclerView recyclerView;
    List<Ordem> ordemsItens1;
    public static RecycleVOrdemAdapter recycleVOrdemAdapterA1;
    public static String SHARED_PREFERENCES = "DadosUser";
    SwipeRefreshLayout refreshOrdem1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ordem_1, container, false);

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        recyclerView = view.findViewById(R.id.RecycleOrdem1);
        ordemsItens1 = new ArrayList<>();
        refreshOrdem1 = view.findViewById(R.id.refresh_ordem_1);

        int id_instituicao = sharedPreferences.getInt("INSTITUICAO_ID", 0);
        if (id_instituicao != 0){
            GetOrdem1Request();
            refreshOrdem1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (internetIsConnected()){
                        GetOrdem1Request();
                    }
                }
            });

        }else {
            // colocar informe de sem instituição
        }

        return view;
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    public void GetOrdem1Request() {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_instituicao = sharedPreferences.getInt("INSTITUICAO_ID", 0);

        String url = "https://brendonlulucas.pythonanywhere.com/"+ id_instituicao +"/API/APIGetAllOrdem/";

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest jsonArrayRequest = new StringRequest (Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ordemsItens1 = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i<jsonArray.length(); i++){
                        try {
                            Ordem ordemItem = new Ordem();
                            JSONObject ordemObject = jsonArray.getJSONObject(i);
                            JSONObject solicitanteDATA = ordemObject.getJSONObject("solicitante");
                            try {
                                JSONObject motoristaDATA = ordemObject.getJSONObject("motorista");
                                JSONObject veiculoDATA = ordemObject.getJSONObject("veiculo");
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            if (ordemObject.getString("status").toString().equals("3")){
                                ordemItem.setPk(ordemObject.getInt("pk"));
                                ordemItem.setSolicitante(solicitanteDATA.getString("name").toString());
                                ordemItem.setDescricao("Descrição aqui");
                                ordemItem.setStatus(ordemObject.getString("status").toString());
                                ordemItem.setData_solicitacao(ordemObject.getString("data_solicitacao").toString());
                                ordemsItens1.add(ordemItem);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                    refreshOrdem1.setRefreshing(false);
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
        }){
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

    private void initRecycleViewO() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recycleVOrdemAdapterA1 = new RecycleVOrdemAdapter(getActivity().getApplicationContext(), ordemsItens1);
        recyclerView.setAdapter(recycleVOrdemAdapterA1);
    }
}