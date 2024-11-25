package com.example.monitorapp.Activities;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

public class MonitorVisualizarActivity extends AppCompatActivity {
    private final ComunicacaoServer cs = new ComunicacaoServer();
    private int idMonitor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_monitor_visualizar);
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
                Toast.makeText(MonitorVisualizarActivity.this, "Erro ao obter monitor", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonitorObtido(Monitor m) {
                // Atualizar a tela com os dados do monitor
                atualizarInfoMonitor(m);
            }
        });
    }


    public void atualizarInfoMonitor(Monitor m){
        TextView idTV = findViewById(R.id.textViewId);
        TextView nomeTV = findViewById(R.id.textViewNome);
        TextView tipoTV = findViewById(R.id.textViewTipo);
        TextView tamanhoTV = findViewById(R.id.textViewTamanho);
        TextView precoTV = findViewById(R.id.textViewPreco);

        idTV.setText(String.valueOf(m.getId()));
        nomeTV.setText(m.getNome());
        tipoTV.setText(m.getTipo());
        tamanhoTV.setText(String.valueOf(m.getTamanho()));
        precoTV.setText(String.valueOf(m.getPreco()));
    }


    public void menu(int id_padrao_selecionado){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(id_padrao_selecionado);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_home) {
                Intent intent = new Intent(MonitorVisualizarActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (id == R.id.menu_incluir) {
                Intent intent = new Intent(MonitorVisualizarActivity.this, MonitorInserirActivity.class);
                startActivity(intent);
            }  else if (id == R.id.menu_listar) {
                Intent intent = new Intent(MonitorVisualizarActivity.this, MonitorListarActivity.class);
                startActivity(intent);
            }
            return true;
        });
    }
}