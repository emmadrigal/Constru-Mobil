package com.example.emmanuel.construmobil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import Database.DBHandler;
import models.Categoria;

public class detallesCategoria extends AppCompatActivity {
    static Categoria category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_categoria);

        String categoryID = getIntent().getStringExtra("id");
        String userID = getIntent().getStringExtra("userID");

        DBHandler db = DBHandler.getSingletonInstance(this);

        category = db.getCategoria(categoryID);

        TextView name = (TextView) findViewById(R.id.name);
        TextView descripcion = (TextView) findViewById(R.id.description);
        Button deleteBTN = (Button)  findViewById(R.id.delete);

        //TODO hide delete category if user isn't an admin

        name.setText(category.Nombre);
        descripcion.setText(category.Descripcion);

    }

    public void delete(View view){
        DBHandler db = DBHandler.getSingletonInstance(this);
        db.deleteCategoria(category.Nombre);

        finish();
    }

}
