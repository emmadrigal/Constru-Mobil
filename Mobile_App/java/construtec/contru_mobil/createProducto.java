package com.example.emmanuel.construmobil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import Database.DBHandler;
import models.Producto;

public class createProducto extends AppCompatActivity {
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_producto);

        userID = getIntent().getStringExtra("userID");

        //TODO set category as a spinner? or give options
        //TODO give only si & no options on exento
    }

    public void Create(View view) {
        EditText name = (EditText) findViewById(R.id.userName);
        EditText description = (EditText) findViewById(R.id.description);
        EditText category = (EditText) findViewById(R.id.category);
        EditText exento = (EditText) findViewById(R.id.exento);
        EditText quantity = (EditText) findViewById(R.id.quantity);
        EditText price = (EditText) findViewById(R.id.price);

        Producto producto = new Producto();
        producto.Nombre_Producto = name.getText().toString();
        producto.descripcion = description.getText().toString();
        producto.Nombre_Categoria = category.getText().toString();
        producto.exento = Integer.parseInt(exento.getText().toString());
        producto.cantidad = Integer.parseInt(quantity.getText().toString());
        producto.precio = Integer.parseInt(price.getText().toString());



        DBHandler db = DBHandler.getSingletonInstance(this);
        db.addProducto(producto);

        Intent intent = new Intent(this, productDetails.class);
        intent.putExtra("userID", userID);
        intent.putExtra("id", producto.Nombre_Producto);
        finish();
        startActivity(intent);
    }
}
