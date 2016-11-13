package com.example.emmanuel.construmobil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import models.Producto;

public class productDetails extends AppCompatActivity {

    Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        //TODO get this information from the DB Handler
        producto = new Producto();

        producto.Nombre_Producto = "Carro";
        producto.cantidad = 1203;
        producto.Cedula_Proveedor = 123;
        producto.descripcion = "Es un carro";
        producto.exento = true;
        producto.precio = 123;
        producto.id_Sucursal = 2;
        producto.Nombre_Categoria = "Linea Blanca";

        TextView name = (TextView) findViewById(R.id.userName);
        TextView description = (TextView) findViewById(R.id.description);
        TextView sucursal = (TextView) findViewById(R.id.sucursal);
        TextView supplier = (TextView) findViewById(R.id.supplier);
        TextView category = (TextView) findViewById(R.id.category);
        TextView exento = (TextView) findViewById(R.id.exento);
        TextView quantity = (TextView) findViewById(R.id.quantity);
        TextView price = (TextView) findViewById(R.id.price);

        name.setText(producto.Nombre_Producto);
        description.setText(producto.descripcion);
        sucursal.setText(Long.toString(producto.id_Sucursal));
        supplier.setText(Long.toString(producto.Cedula_Proveedor));
        category.setText(producto.Nombre_Categoria);
        if(producto.exento){
            exento.setText("Sí");
        } else{
            exento.setText("No");
        }
        quantity.setText(Long.toString(producto.cantidad));
        price.setText("$" + Integer.toString(producto.precio));


        //TODO set popup edit windows

    }
}
