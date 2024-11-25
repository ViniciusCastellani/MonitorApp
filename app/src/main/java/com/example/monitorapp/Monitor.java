package com.example.monitorapp;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Monitor {
    private int id;
    private String nome;
    private String tipo;
    private double tamanho;
    private double preco;


    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("nome", nome);
        json.put("tipo", tipo);
        json.put("tamanho", tamanho);
        json.put("preco", preco);
        return json;
    }

    public void  fromJSON(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        this.id = jsonObject.optInt("id", 0);
        this.nome = jsonObject.optString("nome", "Desconhecido");
        this.tipo = jsonObject.optString("tipo", "Desconhecido");
        this.tamanho = jsonObject.optInt("tamanho", 0);
        this.preco = jsonObject.optDouble("preco", 0.0);


        // Log para verificar se os dados foram atribuídos corretamente
        System.out.println("Monitor criado: ID=" + id + ", Nome=" + nome + ", Tipo=" + tipo + ", Tamanho=" + tamanho + ", Preço=" + preco);
    }
}