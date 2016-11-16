package com.example.emmanuel.construmobil;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import Database.DBHandler;
import models.Producto;

import static android.text.InputType.TYPE_CLASS_NUMBER;

public class productDetails extends AppCompatActivity {
    Producto producto;

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
        View.OnClickListener updateName = new View.OnClickListener() {
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
                        producto.Nombre_Producto = np.getText().toString();

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
        name.setOnClickListener(updateName);


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

        final TextView exento = (TextView) findViewById(R.id.exento);
        View.OnClickListener updateExento = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(v.getContext());
                //TODO only show sí & no options
                d.setContentView(R.layout.text_popup);
                Button setValue = (Button) d.findViewById(R.id.set);
                Button cancelAction = (Button) d.findViewById(R.id.cancel);

                final EditText np = (EditText) d.findViewById(R.id.newValue);
                np.setInputType(TYPE_CLASS_NUMBER);

                setValue.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        producto.exento = Integer.parseInt(np.getText().toString());

                        DBHandler db = DBHandler.getSingletonInstance(null);
                        db.updateProducto(producto);

                        exento.setText(np.getText());
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
        exento.setOnClickListener(updateExento);

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
        if(producto.exento == 1){
            exento.setText("Sí");
        } else{
            exento.setText("No");
        }
        quantity.setText(Long.toString(producto.cantidad));
        price.setText("$" + Integer.toString(producto.precio));

        Button deleteBTN = (Button)  findViewById(R.id.delete);


        if (userID.equals(producto.Cedula_Proveedor)){
            deleteBTN.setVisibility(View.VISIBLE);
        } else {
            deleteBTN.setVisibility(View.GONE);
        }
        //TODO set popup edit windows

    }

    public void delete(View view){

    }
}
