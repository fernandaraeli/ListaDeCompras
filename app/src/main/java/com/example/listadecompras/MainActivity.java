package com.example.listadecompras;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private EditText txtNovaLista;
    private ListView listLista;
    private Button btnNovaLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNovaLista = (EditText) findViewById(R.id.txtNovaLista);
        listLista = (ListView) findViewById(R.id.listLista);
        btnNovaLista = (Button) findViewById(R.id.btnNovaLista);

        btnNovaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bancoDados = openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
                bancoDados.execSQL("CREATE TABLE IF NOT EXISTS listacompras()");


            }
        });
    }
}
