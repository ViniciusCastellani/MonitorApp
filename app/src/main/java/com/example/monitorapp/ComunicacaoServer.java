package com.example.monitorapp;

import android.content.Context;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComunicacaoServer {
    private String ipServer = "192.168.0.13";
    private String url = "http://" + ipServer + ":8080/api/monitor";
    private List<Monitor> monitores = new ArrayList<>();
    private Monitor monitor;


    // POST
    public void enviarMonitor(String jsonMonitor, Context context) {
        if (jsonMonitor.isEmpty()) {
            Toast.makeText(context, "JSON vazio", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            enviarVolleyJson(jsonMonitor, requestQueue, context);
        } catch (Exception ex) {
            Toast.makeText(context, "Erro ao enviar JSON", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    
    private void enviarVolleyJson(String jsonMonitor, RequestQueue requestQueue, Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, s -> tratarOKPOST(s, context),
                s -> tratarErroPOST(s, context)) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonMonitor.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        requestQueue.add(stringRequest);
    }

    
    private void tratarErroPOST(VolleyError s, Context context) {
        Toast.makeText(context, "Erro ao adicionar Monitor", Toast.LENGTH_SHORT).show();
    }

    
    private void tratarOKPOST(String json, Context context) {
        Toast.makeText(context, "Monitor adicionado com sucesso", Toast.LENGTH_SHORT).show();
    }


    public void listarTodosMonitores(Context context, MonitorCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        System.out.println("Server Response: " + response);
                        JSONArray jsonArray = new JSONArray(response);
                        this.monitores.clear();

                        System.out.println(jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Monitor m = new Monitor();
                            m.fromJSON(jsonObject.toString());
                            System.out.println(m.getId() + " " + m.getNome() + " " + m.getTipo() + " " + m.getTamanho() + " " + m.getPreco());

                            monitores.add(m);
                        }

                        System.out.println("Monitores after parsing: " + monitores.size());

                        for (Monitor m : monitores){
                            System.out.println(m.getId() + " " + m.getNome() + " " + m.getTipo() + " " + m.getTamanho() + " " + m.getPreco());
                        }

                        Toast.makeText(context, "Monitores carregados com sucesso", Toast.LENGTH_SHORT).show();

                        callback.onMonitoresObtidos(monitores); // Chamar o callback com a lista de monitores
                    } catch (JSONException e) {
                        Toast.makeText(context, "Erro ao listar monitores", Toast.LENGTH_SHORT).show();
                        callback.onError(null); // Chamar o callback com o erro (pode passar 'e' também)
                    }
                },
                error -> {
                    Toast.makeText(context, "Erro ao listar monitores", Toast.LENGTH_SHORT).show();
                    callback.onError(error); // Chamar o callback com o erro
                });

        requestQueue.add(stringRequest);
    }


    // GET com ID específico
    public void obterMonitorPorId(int idMonitor, Context context, MonitorCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String urlGetId = url + "/" + idMonitor; // URL para obter um monitor específico

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGetId,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        monitor = new Monitor();
                        monitor.fromJSON(jsonObject.toString());
                    } catch (JSONException e) {
                        Toast.makeText(context, "Erro ao obter monitor", Toast.LENGTH_SHORT).show();
                        callback.onError(null); // Chamar o callback com o erro (pode passar 'e' também)
                    }
                    callback.onMonitorObtido(monitor);
                },
                error -> {
                    Toast.makeText(context, "Erro ao obter monitor por ID", Toast.LENGTH_SHORT).show();
                    callback.onError(error); // Chamar o callback com o erro
                });

        requestQueue.add(stringRequest);
    }


    // PUT
    public void atualizarMonitor(String jsonMonitor, Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, s -> tratarOKPUT(s, context),
                s -> tratarErroPUT(s, context)) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonMonitor.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        requestQueue.add(stringRequest);
    }

    private void tratarErroPUT(VolleyError s, Context context) {
        Toast.makeText(context, "Erro ao atualizar Monitor", Toast.LENGTH_SHORT).show();
    }

    private void tratarOKPUT(String json, Context context) {
        Toast.makeText(context, "Monitor atualizado com sucesso", Toast.LENGTH_SHORT).show();
    }


    // DELETE
    public void deletarMonitor(int idMonitor, Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String urlDelete = url + "/" + idMonitor; // URL para deletar um monitor específico

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, urlDelete,
                response -> tratarOKDELETE(response, context),
                error -> tratarErroDELETE(error, context));

        requestQueue.add(stringRequest);
    }


    private void tratarErroDELETE(VolleyError error, Context context) {
        Toast.makeText(context, "Erro ao deletar Monitor", Toast.LENGTH_SHORT).show();
    }


    private void tratarOKDELETE(String response, Context context) {
        Toast.makeText(context, "Monitor deletado com sucesso", Toast.LENGTH_SHORT).show();
    }


}