package com.example.monitorapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.VolleyError;
import com.example.monitorapp.ComunicacaoServer;
import com.example.monitorapp.Monitor;
import com.example.monitorapp.MonitorCallback;
import com.example.monitorapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.util.List;

public class MonitorAlterarActivity extends AppCompatActivity {
    private final ComunicacaoServer cs = new ComunicacaoServer();
    private int idMonitor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_monitor_alterar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        idMonitor = getIntent().getIntExtra("monitorId", -1);
        obterMonitor(idMonitor);
        menu(R.id.menu_listar);
    }


    public void obterMonitor(int idMonitor) {
        cs.obterMonitorPorId(idMonitor, this, new MonitorCallback() {

            @Override
            public void onMonitoresObtidos(List<Monitor> monitores) {
                return;
            }

            @Override
            public void onError(VolleyError error) {
                // Tratar o erro
                Toast.makeText(MonitorAlterarActivity.this, "Erro ao obter monitor", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonitorObtido(Monitor m) {
                // Atualizar a tela com os dados do monitor
                atualizarInfoMonitor(m);
            }
        });
    }


    public void atualizarInfoMonitor(Monitor m){
        EditText idET = findViewById(R.id.editTextId);
        EditText nomeET = findViewById(R.id.editTextNome);
        EditText tipoET = findViewById(R.id.editTextTipo);
        EditText tamanhoET = findViewById(R.id.editTextTamanho);
        EditText precoET = findViewById(R.id.editTextPreco);

        idET.setText(String.valueOf(m.getId()));
        nomeET.setText(m.getNome());
        tipoET.setText(m.getTipo());
        tamanhoET.setText(String.valueOf(m.getTamanho()));
        precoET.setText(String.valueOf(m.getPreco()));
    }


    // FUNCAO CENTRAL ALTERAR MONITOR
    public void alterarMonitor(View v) {
        if (validarCampos()) {
            Monitor monitor = criarMonitor();
            String jsonMonitor = converterMonitorParaJSON(monitor);
            if (jsonMonitor != null) {
                cs.atualizarMonitor(jsonMonitor, this);
                Intent intent = new Intent(MonitorAlterarActivity.this, MonitorListarActivity.class);
                startActivity(intent);
            }
        }
    }


    // FUNCAO PARA VALIDAR CAMPOS
    private boolean validarCampos() {
        EditText idET = findViewById(R.id.editTextId);
        EditText nomeET = findViewById(R.id.editTextNome);
        EditText tipoET = findViewById(R.id.editTextTipo);
        EditText tamanhoET = findViewById(R.id.editTextTamanho);
        EditText precoET = findViewById(R.id.editTextPreco);

        if (isCampoVazio(nomeET) || isCampoVazio(tipoET) || isCampoVazio(tamanhoET) || isCampoVazio(precoET) || isCampoVazio(idET)) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    // FUNCAO PARA VERIFICAR SE O CAMPO ESTA VAZIO
    private boolean isCampoVazio(EditText campo) {
        return campo.getText().toString().trim().isEmpty();
    }


    // FUNCAO PARA CRIAR UM OBJETO MONITOR
    private Monitor criarMonitor() {
        EditText idET = findViewById(R.id.editTextId);
        EditText nomeET = findViewById(R.id.editTextNome);
        EditText tipoET = findViewById(R.id.editTextTipo);
        EditText tamanhoET = findViewById(R.id.editTextTamanho);
        EditText precoET = findViewById(R.id.editTextPreco);


        int idMonitor = Integer.parseInt(idET.getText().toString().trim());
        String nomeMonitor = nomeET.getText().toString().trim();
        String tipoMonitor = tipoET.getText().toString().trim();
        double tamanhoMonitor = Double.parseDouble(tamanhoET.getText().toString().trim());
        double precoMonitor = Double.parseDouble(precoET.getText().toString().trim());

        return new Monitor(idMonitor, nomeMonitor, tipoMonitor, tamanhoMonitor, precoMonitor);
    }


    // FUNCAO PARA CONVERTER UM OBJETO MONITOR PARA JSON
    private String converterMonitorParaJSON(Monitor monitor) {
        try {
            return String.valueOf(monitor.toJSON());
        } catch (JSONException e) {
            Toast.makeText(this, "Erro ao converter para JSON", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    // FUNCAO CONFIGURACAO MENU DAS TELAS
    public void menu(int id_padrao_selecionado){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(id_padrao_selecionado);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_home) {
                Intent intent = new Intent(MonitorAlterarActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (id == R.id.menu_incluir) {
                Intent intent = new Intent(MonitorAlterarActivity.this, MonitorInserirActivity.class);
                startActivity(intent);
            } else if (id == R.id.menu_listar) {
                Intent intent = new Intent(MonitorAlterarActivity.this, MonitorListarActivity.class);
                startActivity(intent);
            }
            return true;
        });
    }


}