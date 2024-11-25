package com.example.monitorapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class MonitorListarActivity extends AppCompatActivity {
    private final ComunicacaoServer cs = new ComunicacaoServer();
    private List<Monitor> monitores = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_monitor_listar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        obterMonitores();
        menu(R.id.menu_listar);
    }


    public void obterMonitores() {
        cs.listarTodosMonitores(this, new MonitorCallback() {
            @Override
            public void onMonitoresObtidos(List<Monitor> monitores) {
                // Atualizar a tabela com a lista de monitores
                atualizarTabela(monitores);
            }

            @Override
            public void onError(VolleyError error) {
                // Tratar o erro
                Toast.makeText(MonitorListarActivity.this, "Erro ao obter monitores", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonitorObtido(Monitor m) {
                return;
            }
        });
    }


    private void atualizarTabela(List<Monitor> monitores) {
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.removeAllViews();

        // Cabeçalho da tabela
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(getResources().getColor(R.color.black));

        TextView idHeader = new TextView(this);
        idHeader.setText("ID");
        idHeader.setGravity(Gravity.CENTER);
        idHeader.setTextColor(Color.WHITE);
        headerRow.addView(idHeader);

        TextView nomeHeader = new TextView(this);
        nomeHeader.setText("Nome");
        nomeHeader.setGravity(Gravity.CENTER);
        nomeHeader.setTextColor(Color.WHITE);
        headerRow.addView(nomeHeader);

        TextView tipoHeader = new TextView(this);
        tipoHeader.setText("Tipo");
        tipoHeader.setGravity(Gravity.CENTER);
        tipoHeader.setTextColor(Color.WHITE);
        headerRow.addView(tipoHeader);

        TextView tamanhoHeader = new TextView(this);
        tamanhoHeader.setText("Tamanho");
        tamanhoHeader.setGravity(Gravity.CENTER);
        tamanhoHeader.setTextColor(Color.WHITE);
        headerRow.addView(tamanhoHeader);

        TextView precoHeader = new TextView(this);
        precoHeader.setText("Preço");
        precoHeader.setGravity(Gravity.CENTER);
        precoHeader.setTextColor(Color.WHITE);
        headerRow.addView(precoHeader);

        TextView acoesHeader = new TextView(this);
        acoesHeader.setText("Ações");
        acoesHeader.setGravity(Gravity.CENTER);
        acoesHeader.setTextColor(Color.WHITE);
        headerRow.addView(acoesHeader);

        tableLayout.addView(headerRow);

        // Dados dos Monitores
        for (Monitor monitor : monitores) {
            TableRow row = new TableRow(this);
            row.setBackgroundColor(getResources().getColor(R.color.colorLightGray));

            addDataCell(row, String.valueOf(monitor.getId()));
            addDataCell(row, monitor.getNome());
            addDataCell(row, monitor.getTipo());
            addDataCell(row, String.valueOf(monitor.getTamanho()));
            addDataCell(row, String.format("%.2f", monitor.getPreco()));

            // Botão de Ações
            Button acaoButton = new Button(this);
            acaoButton.setText("Ações");
            acaoButton.setBackgroundColor(getResources().getColor(R.color.whine));
            acaoButton.setTextColor(Color.WHITE);
            acaoButton.setOnClickListener(view -> mostrarOpcoesAcoes(monitor));
            row.addView(acaoButton);

            tableLayout.addView(row);
        }
    }


    private void addDataCell(TableRow row, String data) {
        TextView textView = new TextView(this);
        textView.setText(data);
        textView.setTextColor(Color.BLACK);
        textView.setPadding(8, 8, 8, 8);
        row.addView(textView);
    }


    private void mostrarOpcoesAcoes(Monitor monitor) {
        // Criando um pop-up (Dialog) com as opções de ações
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Escolha uma ação");

        // Adicionando as opções no pop-up
        builder.setItems(new CharSequence[]{"Visualizar", "Alterar", "Deletar"},
                (dialog, which) -> {
                    switch (which) {
                        case 0: // Visualizar
                            Intent intentVisualizar = new Intent(MonitorListarActivity.this, MonitorVisualizarActivity.class);
                            intentVisualizar.putExtra("monitorId", monitor.getId());
                            startActivity(intentVisualizar);
                            break;
                        case 1: // Alterar
                            Intent intentAlterar = new Intent(MonitorListarActivity.this, MonitorAlterarActivity.class);
                            intentAlterar.putExtra("monitorId", monitor.getId());
                            startActivity(intentAlterar);
                            break;
                        case 2: // Deletar
                            confirmarDelecao(monitor); // Chama o diálogo de confirmação
                            break;
                    }
                });
        builder.create().show();
    }

    private void confirmarDelecao(Monitor monitor) {
        // Criar o diálogo de confirmação
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Confirmar exclusão");
        builder.setMessage("Tem certeza que deseja excluir o monitor '" + monitor.getNome() + "'?");

        builder.setPositiveButton("Sim", (dialog, which) -> {
            // Ação de exclusão
            cs.deletarMonitor(monitor.getId(), this);
            Toast.makeText(this, "Monitor excluído com sucesso!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MonitorListarActivity.this, MonitorListarActivity.class);
            startActivity(intent);
        });

        builder.setNegativeButton("Não", (dialog, which) -> {
            // Apenas fecha o diálogo
            dialog.dismiss();
        });

        builder.create().show();
    }


    public void menu(int id_padrao_selecionado){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(id_padrao_selecionado);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_home) {
                Intent intent = new Intent(MonitorListarActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (id == R.id.menu_incluir) {
                Intent intent = new Intent(MonitorListarActivity.this, MonitorInserirActivity.class);
                startActivity(intent);
            }
            return true;
        });
    }
}