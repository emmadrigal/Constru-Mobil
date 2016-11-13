package com.example.emmanuel.construmobil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import models.Categoria;

public class detallesCategoria extends AppCompatActivity {
    static Categoria category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_categoria);

        //TODO call
        category = new Categoria();
        category.nombre = "Linea Blanca";
        category.descripcion = "This is a placeholder object";

        TextView name = (TextView) findViewById(R.id.name);
        TextView descripcion = (TextView) findViewById(R.id.description);

        name.setText(category.nombre);
        descripcion.setText(category.descripcion);

        //TODO hide delete category if user isn't an admin
    }

}
