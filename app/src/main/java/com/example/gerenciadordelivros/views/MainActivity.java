package com.example.gerenciadordelivros.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.gerenciadordelivros.R;
import com.example.gerenciadordelivros.adapter.LivroAdapter;
import com.example.gerenciadordelivros.data.LivroDAO;
import com.example.gerenciadordelivros.dialogs.DeleteDialog;
import com.example.gerenciadordelivros.dominio.Livro;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LivroAdapter.OnLivroListener, DeleteDialog.OnDeleteListener {

    private LivroDAO livroDAO;
    LivroAdapter livroAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));





        livroDAO = LivroDAO.getInstance(this);

        List<Livro> listaLivros = livroDAO.list();

        livroAdapter = new LivroAdapter(listaLivros, this, this);

        recyclerView.setAdapter(livroAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_adicionar:
                Intent intent = new Intent(getApplicationContext(),EditarLivroActivity.class);
                startActivityForResult(intent,100);
                return true;
            case R.id.action_sair:
                 finish();
                 return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

     @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK){
            atualizaListaLivros();
        }

         if (requestCode == 101 && resultCode == RESULT_OK){
             atualizaListaLivros();
         }
    }

    public void atualizaListaLivros(){
        List<Livro> livros = livroDAO.list();
        livroAdapter.setItems(livros);
        livroAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLivroClick(int pos) {

        Intent intent = new Intent(getApplicationContext(),EditarLivroActivity.class);
        intent.putExtra("livro",livroAdapter.getItem(pos));
        startActivityForResult(intent, 101);


    }

    @Override
    public void onLivroLongClick(int pos) {

        Livro livro = livroAdapter.getItem(pos);

        DeleteDialog dialog = new DeleteDialog();
        dialog.setLivro(livro);
        dialog.show(getSupportFragmentManager(),"deleteDialog");

    }

    @Override
    public void onDelete(Livro livro) {
        livroDAO.delete(livro);
        atualizaListaLivros();

        Toast.makeText(this, "Livro excluido com sucesso!", Toast.LENGTH_SHORT).show();
    }
}