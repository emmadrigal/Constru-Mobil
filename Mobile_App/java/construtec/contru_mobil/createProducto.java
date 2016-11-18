package construtec.contru_mobil;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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
    }

    public void Create(View view) {
        EditText name = (EditText) findViewById(R.id.userName);
        EditText description = (EditText) findViewById(R.id.description);
        EditText category = (EditText) findViewById(R.id.category);
        CheckBox exento = (CheckBox) findViewById(R.id.exento);
        EditText quantity = (EditText) findViewById(R.id.quantity);
        EditText price = (EditText) findViewById(R.id.price);

        Producto producto = new Producto();
        if(!name.getText().toString().isEmpty())
            producto.Nombre_Producto = name.getText().toString();
        else {
            name.setError("El nombre del producto es necesario");
            return;
        }

        producto.descripcion = description.getText().toString();
        producto.Nombre_Categoria = category.getText().toString();

        if(exento.isChecked())
            producto.exento = 1;
        else
            producto.exento = 0;

        if(!quantity.getText().toString().isEmpty())
            producto.cantidad = Integer.parseInt(quantity.getText().toString());
        else
            producto.cantidad = 0;

        if(!price.getText().toString().isEmpty())
            producto.precio = Integer.parseInt(price.getText().toString());
        else{
            price.setError("Debe de colocar un precio");
            return;
        }

        DBHandler db = DBHandler.getSingletonInstance(this);
        db.addProducto(producto);

        Intent intent = new Intent(this, productDetails.class);
        intent.putExtra("userID", userID);
        intent.putExtra("id", producto.Nombre_Producto);
        finish();
        startActivity(intent);
    }
}
