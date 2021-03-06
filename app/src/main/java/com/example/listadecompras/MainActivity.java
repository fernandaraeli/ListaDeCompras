package com.example.listadecompras;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

        produtos = (EditText) findViewById(R.id.inserirTexto);
        minhaLista = (ListView) findViewById(R.id.listaProdutos);
        incluir = (Button) findViewById(R.id.inserir);

        listaProdutos();

        minhaLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //excluirProduto(ids.get(position));
                alertaExcluirProduto(position);
                return false;
            }
        });

        minhaLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("compra", produto.get(position));
                startActivity(intent);
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
                Toast.makeText(MainActivity.this, "Insira uma compra", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "Compra "+novoProduto+" Inserida!!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(MainActivity.this, "Compra Removido", Toast.LENGTH_SHORT).show();
            listaProdutos();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void alertaExcluirProduto(Integer selecionado){
        String produtoSelecionado = produto.get(selecionado);
        final Integer numeroId = selecionado;

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Aviso").setMessage("Deseja apagar compra: "+produtoSelecionado+" ?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluirProduto(ids.get(numeroId));
                    }
                }).setNegativeButton("Não", null).show();
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
                    android.R.layout.simple_selectable_list_item,
                    android.R.id.text1, produto);

            minhaLista.setAdapter(itensAdaptador);

            cursor.moveToFirst();
            while (cursor != null) {
                Log.i("LogX:", "ID:" + cursor.getString(indiceColunaId) + "Compra: " + cursor.getString(indiceColunaTarefa));
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
