package com.example.listadecompras;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private EditText produtos;
    private ListView minhaLista;
    private Button incluir;

    private SQLiteDatabase bd;

    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<Integer> ids;
    private ArrayList<String> produto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        produtos = (EditText) findViewById(R.id.editText);
        minhaLista = (ListView) findViewById(R.id.listView);
        incluir = (Button) findViewById(R.id.button);

        listaProdutos();


        minhaLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                excluirProduto(ids.get(position));
                return false;
            }
        });

        incluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adicionarProduto(produtos.getText().toString());

            }
        });
    }

    private void adicionarProduto(String novoProduto) {
        try{
            if(novoProduto.equals("")){
                Toast.makeText(MainActivity.this, "Insira um produto", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "Produto "+novoProduto+" Inserido", Toast.LENGTH_SHORT).show();
                produtos.setText("");
                bd.execSQL("INSERT INTO minhalista(produto) VALUES ('"+novoProduto+"')");
                listaProdutos();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void excluirProduto(Integer id) {
        try{
            bd.execSQL("DELETE FROM minhaLista WHERE id="+id);
            Toast.makeText(MainActivity.this, "Produto Removido", Toast.LENGTH_SHORT).show();
            listaProdutos();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void listaProdutos() {
        try {
            bd = openOrCreateDatabase("listaSupermercado", MODE_PRIVATE, null);
            bd.execSQL("CREATE TABLE IF NOT EXISTS minhalista (id INTEGER PRIMARY KEY AUTOINCREMENT, produto VARCHAR)");

            //String novoProduto = produtos.getText().toString();
            //bd.execSQL("INSERT INTO minhalista(produto) VALUES ('"+novoProduto+"')");

            //Cursor cursor = bd.rawQuery("SELECT * FROM minhalista", null);
            Cursor cursor = bd.rawQuery("SELECT * FROM minhalista ORDER BY id DESC", null);

            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaTarefa = cursor.getColumnIndex("produto");

            ids = new ArrayList<Integer>();
            produto = new ArrayList<String>();


            itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2,
                    android.R.id.text1, produto);

            minhaLista.setAdapter(itensAdaptador);

            cursor.moveToFirst();
            while (cursor != null) {
                Log.i("LogX:", "ID:" + cursor.getString(indiceColunaId) + "Produto: " + cursor.getString(indiceColunaTarefa));
                produto.add(cursor.getString(indiceColunaTarefa));
                ids.add(Integer.parseInt(cursor.getString(indiceColunaId)));
                cursor.moveToNext();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }








}
