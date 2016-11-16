package com.example.emmanuel.construmobil;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import Database.DBHandler;
import models.Producto;

import static android.text.InputType.TYPE_CLASS_NUMBER;

public class productDetails extends AppCompatActivity {
    static Producto producto;

    TextView name;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        String productoID = getIntent().getStringExtra("id");
        String userID = getIntent().getStringExtra("userID");

        DBHandler db = DBHandler.getSingletonInstance(this);

        producto = db.getProducto(productoID);

        name = (TextView) findViewById(R.id.productName);

        description = (TextView) findViewById(R.id.description);
        View.OnClickListener updateDescription = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(v.getContext());
                d.setContentView(R.layout.text_popup);
                Button setValue = (Button) d.findViewById(R.id.set);
                Button cancelAction = (Button) d.findViewById(R.id.cancel);

                final EditText np = (EditText) d.findViewById(R.id.newValue);

                setValue.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        producto.descripcion = np.getText().toString();

                        DBHandler db = DBHandler.getSingletonInstance(null);
                        db.updateProducto(producto);

                        description.setText(np.getText());
                        d.dismiss();
                    }
                });
                cancelAction.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.show();
            }
        };
        description.setOnClickListener(updateDescription);

        TextView sucursal = (TextView) findViewById(R.id.sucursal);

        TextView supplier = (TextView) findViewById(R.id.supplier);

        TextView category = (TextView) findViewById(R.id.category);

        final CheckBox exento = (CheckBox) findViewById(R.id.exento);
        exento.setChecked(producto.exento == 1);

        final TextView quantity = (TextView) findViewById(R.id.quantity);
        View.OnClickListener updateQuantity = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(v.getContext());
                d.setContentView(R.layout.text_popup);
                Button setValue = (Button) d.findViewById(R.id.set);
                Button cancelAction = (Button) d.findViewById(R.id.cancel);

                final EditText np = (EditText) d.findViewById(R.id.newValue);
                np.setInputType(TYPE_CLASS_NUMBER);

                setValue.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        producto.cantidad = Integer.parseInt(np.getText().toString());

                        DBHandler db = DBHandler.getSingletonInstance(null);
                        db.updateProducto(producto);

                        quantity.setText(np.getText());
                        d.dismiss();
                    }
                });
                cancelAction.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.show();
            }
        };
        quantity.setOnClickListener(updateQuantity);

        TextView price = (TextView) findViewById(R.id.price);
        View.OnClickListener updatePrice = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(v.getContext());
                d.setContentView(R.layout.text_popup);
                Button setValue = (Button) d.findViewById(R.id.set);
                Button cancelAction = (Button) d.findViewById(R.id.cancel);

                final EditText np = (EditText) d.findViewById(R.id.newValue);
                np.setInputType(TYPE_CLASS_NUMBER);

                setValue.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        producto.precio = Integer.parseInt(np.getText().toString());

                        DBHandler db = DBHandler.getSingletonInstance(null);
                        db.updateProducto(producto);

                        name.setText(np.getText());
                        d.dismiss();
                    }
                });
                cancelAction.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.show();
            }
        };
        price.setOnClickListener(updatePrice);


        name.setText(producto.Nombre_Producto);
        description.setText(producto.descripcion);
        sucursal.setText(Long.toString(producto.Id_Sucursal));
        supplier.setText(Long.toString(producto.Cedula_Proveedor));
        category.setText(producto.Nombre_Categoria);

        quantity.setText(Long.toString(producto.cantidad));
        price.setText("$" + Integer.toString(producto.precio));

        Button deleteBTN = (Button)  findViewById(R.id.delete);

/*
        if (userID.equals(producto.Cedula_Proveedor)){
            deleteBTN.setVisibility(View.VISIBLE);
        } else {
            deleteBTN.setVisibility(View.GONE);
        }
        */

    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.exento:
                if (checked)
                    producto.exento = 1;
                else
                    producto.exento = 0;
                break;
        }
        DBHandler db = DBHandler.getSingletonInstance(null);
        db.updateProducto(producto);

    }

    public void delete(View view){
        DBHandler db = DBHandler.getSingletonInstance(null);//The DBHandler has already been created

        db.deleteProducto(producto.Nombre_Producto);
        finish();
    }
}
