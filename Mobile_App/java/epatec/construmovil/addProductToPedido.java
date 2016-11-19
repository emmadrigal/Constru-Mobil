package epatec.construmovil;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;

import Database.DBHandler;
import models.Contiene;
import models.Pedido;
import models.Producto;

public class addProductToPedido extends AppCompatActivity {
    private ListView list;
    static private ArrayAdapter<String> arrayAdapter;

    static List<String> productosList = new ArrayList<>();

    private String pedidoID;

    static private String selectedMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_pedido);

        pedidoID = getIntent().getStringExtra("id");

        list = (ListView) findViewById(R.id.List);
        EditText filter = (EditText) findViewById(R.id.filter);

        list.setTextFilterEnabled(true);

        updateListaProductos();
        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                productosList);

        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedMaterial = (String) list.getItemAtPosition(position);
                show();
            }
        });

        list.setTextFilterEnabled(true);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                arrayAdapter.getFilter().filter(arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            @Override
            public void afterTextChanged(Editable arg0) {}
        });
    }

    /**
     * Call to show the popup window that allows to select the quantity of desired materials
     */
    private void show()
    {
        final Dialog d = new Dialog(addProductToPedido.this);
        d.setTitle("Choose the quantity");
        d.setContentView(R.layout.number_picker_dialog);
        Button setValue = (Button) d.findViewById(R.id.set);
        Button cancelAction = (Button) d.findViewById(R.id.cancel);

        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMinValue(1);
        np.setMaxValue(Integer.MAX_VALUE);
        np.setWrapSelectorWheel(false);

        setValue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                addProductoToPedido(np.getValue());

                d.dismiss();
                finish();
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

    public  void addProductoToPedido(int cantidad){
        DBHandler db = DBHandler.getSingletonInstance(null);

        Contiene contiene = new Contiene();
        contiene.Cantidad = cantidad;
        contiene.Nombre_Producto = selectedMaterial;
        contiene.Id_Pedido = Long.parseLong(pedidoID);

        Pedido pedido = db.getPedido(Long.parseLong(pedidoID));

        db.addContiene(contiene, pedido);
    }

    /**
     * Updates the products list from the data in the database and triggers a refresh on the DB
     */
    public static void updateListaProductos(){
        productosList.clear();

        DBHandler db = DBHandler.getSingletonInstance(null);//The DBHandler has already been created
        List<Producto> productos = db.getAllProductos();

        models.Producto producto;
        for(int i =0; i < productos.size(); i++){
            producto = productos.get(i);
            productosList.add(producto.Nombre_Producto);
        }
    }

}
