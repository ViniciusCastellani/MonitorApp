package com.example.monitorapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.monitorapp.ComunicacaoServer;
import com.example.monitorapp.Monitor;
import com.example.monitorapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

public class MonitorInserirActivity extends AppCompatActivity {
    private final ComunicacaoServer cs = new ComunicacaoServer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_monitor_inserir);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        menu(R.id.menu_incluir);
    }


    // FUNCAO CENTRAL CADASTRO MONITOR
    public void cadastrarMonitor(View v) {
        if (validarCampos()) {
            Monitor monitor = criarMonitor();
            String jsonMonitor = converterMonitorParaJSON(monitor);
            if (jsonMonitor != null) {
                cs.enviarMonitor(jsonMonitor, this);
                Intent intent = new Intent(MonitorInserirActivity.this, MonitorListarActivity.class);
                startActivity(intent);
            }
        }
    }


    // FUNCAO PARA VALIDAR CAMPOS
    private boolean validarCampos() {
        EditText nomeET = findViewById(R.id.editTextNome);
        EditText tipoET = findViewById(R.id.editTextTipo);
        EditText tamanhoET = findViewById(R.id.editTextTamanho);
        EditText precoET = findViewById(R.id.editTextPreco);

        if (isCampoVazio(nomeET) || isCampoVazio(tipoET) || isCampoVazio(tamanhoET) || isCampoVazio(precoET)) {
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
        EditText nomeET = findViewById(R.id.editTextNome);
        EditText tipoET = findViewById(R.id.editTextTipo);
        EditText tamanhoET = findViewById(R.id.editTextTamanho);
        EditText precoET = findViewById(R.id.editTextPreco);

        String nomeMonitor = nomeET.getText().toString().trim();
        String tipoMonitor = tipoET.getText().toString().trim();
        double tamanhoMonitor = Double.parseDouble(tamanhoET.getText().toString().trim());
        double precoMonitor = Double.parseDouble(precoET.getText().toString().trim());

        return new Monitor(0, nomeMonitor, tipoMonitor, tamanhoMonitor, precoMonitor);
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
                Intent intent = new Intent(MonitorInserirActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (id == R.id.menu_listar) {
                 Intent intent = new Intent(MonitorInserirActivity.this, MonitorListarActivity.class);
                 startActivity(intent);
            }
            return true;
        });
    }
}