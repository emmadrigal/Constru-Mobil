package com.example.emmanuel.construmobil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import Database.DBHandler;
import models.Categoria;

public class crearCategory extends AppCompatActivity {
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_category);

        userID = getIntent().getStringExtra("userID");
    }


    public void create(View view){
        EditText nombre = (EditText) findViewById(R.id.name);
        EditText description = (EditText) findViewById(R.id.description);

        DBHandler db = DBHandler.getSingletonInstance(null);

        Categoria categoria = new Categoria();
        categoria.Nombre = nombre.getText().toString();
        categoria.Descripcion = description.getText().toString();

        db.addCategoria(categoria);

        Intent intent = new Intent(this, detallesCategoria.class);
        intent.putExtra("userID", userID);
        intent.putExtra("id", categoria.Nombre);
        finish();
        startActivity(intent);
    }
}
