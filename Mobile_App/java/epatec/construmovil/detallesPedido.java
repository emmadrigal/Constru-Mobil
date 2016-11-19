package epatec.construmovil;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Database.DBHandler;
import models.Contiene;
import models.Pedido;

public class detallesPedido extends AppCompatActivity {


    private static final List<String> contieneIDList = new ArrayList<>();
    private static final List<Map<String, String>> productosList = new ArrayList<>();

    private static SimpleAdapter productosAdapter;

    static Pedido pedido;
    static String pedidoID;
    static String contieneID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedido);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        pedidoID = getIntent().getStringExtra("id");

        DBHandler db = DBHandler.getSingletonInstance(this);

        pedido = db.getPedido(Long.parseLong(pedidoID));

        productosAdapter = new SimpleAdapter(this, productosList,
                android.R.layout.simple_list_item_2,
                new String[] {"name", "quantity"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});

        updateProductos();

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void onResume(){
        super.onResume();

        updateProductos();
        productosAdapter.notifyDataSetChanged();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0: return pedidoDetails.newInstance(position + 1);
                default: return productList.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Información del Pedido";
                case 1:
                    return "Productos";
            }
            return null;
        }
    }

    /**
     * A view containing a user's details
     */
    public static class pedidoDetails extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public pedidoDetails() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static pedidoDetails newInstance(int sectionNumber) {
            pedidoDetails fragment = new pedidoDetails();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * If a view of the project list is created this is called
         * @param inflater required by Android
         * @param container required by Android
         * @param savedInstanceState required by Android
         * @return View to be displayed
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.pedido_information, container, false);

            TextView sucursal =  (TextView) rootView.findViewById(R.id.sucursal);
            TextView clientID =  (TextView) rootView.findViewById(R.id.clientID);
            final TextView phoneNumber =  (TextView) rootView.findViewById(R.id.phoneNumber);
            TextView creationTime =  (TextView) rootView.findViewById(R.id.creationTime);

            sucursal.setText(Long.toString(pedido.Id_Sucursal));
            clientID.setText(Long.toString(pedido.Cedula_Cliente));
            creationTime.setText(pedido.Hora_de_Creación);

            phoneNumber.setText(Long.toString(pedido.Telefono_Preferido));

            View.OnClickListener updatePhone = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog d = new Dialog(getContext());
                    d.setContentView(R.layout.text_popup);
                    Button setValue = (Button) d.findViewById(R.id.set);
                    Button cancelAction = (Button) d.findViewById(R.id.cancel);

                    final EditText np = (EditText) d.findViewById(R.id.newValue);

                    setValue.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            DBHandler db = DBHandler.getSingletonInstance(null);//The DBHandler has already been created

                            db.updatePedido(pedido);
                            phoneNumber.setText(np.getText());
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
            phoneNumber.setOnClickListener(updatePhone);


            return rootView;
        }
    }

    /**
     * A view containing a list of all one users requests
     */
    public static class productList extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public productList() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static productList newInstance(int sectionNumber) {
            productList fragment = new productList();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * If a view of the project list is created this is called
         * @param inflater required by Android
         * @param container required by Android
         * @param savedInstanceState required by Android
         * @return View to be displayed
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.pager_view_list, container, false);

            final ListView list = (ListView) rootView.findViewById(R.id.List);

            list.setAdapter(productosAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) productosAdapter.getItem(position);
                    contieneID = contieneIDList.get(position);
                    String initialValue = (String) obj.get("quantity");

                    show(Integer.parseInt(initialValue));
                }
            });

            Button addNew = (Button) rootView.findViewById(R.id.create);
            addNew.setText("Add new Material");

            addNew.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), addProductToPedido.class);
                    intent.putExtra("id", pedidoID);
                    startActivity(intent);
                }
            });

            return rootView;
        }

        public void show(int initialValue)
        {
            final Dialog d = new Dialog(getContext());
            d.setContentView(R.layout.update_material);
            Button setValue = (Button) d.findViewById(R.id.set);
            Button delete   = (Button) d.findViewById(R.id.delete);
            Button cancelAction = (Button) d.findViewById(R.id.cancel);

            final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
            np.setMinValue(0);
            np.setMaxValue(Integer.MAX_VALUE);
            np.setValue(initialValue);
            np.setWrapSelectorWheel(false);

            delete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    deleteContiene();
                    // create intent to start another activity
                    d.dismiss();
                }
            });

            setValue.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    updateContiene(np.getValue());
                    // create intent to start another activity
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
    }

    public void delete(View view){
        DBHandler db = DBHandler.getSingletonInstance(null);//The DBHandler has already been created

        db.deletePedido(pedido.Id_Pedido);
        finish();
    }

    static void updateProductos(){
        productosList.clear();
        contieneIDList.clear();

        DBHandler db = DBHandler.getSingletonInstance(null);//The DBHandler has already been created
        pedido = db.getPedido(Long.parseLong(pedidoID));

        for(int i = 0; i < pedido.productos.size(); i++){
            contieneIDList.add(Long.toString(pedido.productos.get(i).id_Contiene));

            Map<String, String> producto = new HashMap<>(2);

            producto.put("name", pedido.productos.get(i).Nombre_Producto);
            producto.put("quantity", Integer.toString(pedido.productos.get(i).Cantidad));

            productosList.add(producto);
        }

        productosAdapter.notifyDataSetChanged();
    }


    public static void updateContiene(int value){
        DBHandler db = DBHandler.getSingletonInstance(null);//The DBHandler has already been created

        Contiene contiene = db.getContiene(contieneID);
        contiene.Cantidad = value;
        db.updateContiene(contiene, pedido);

        updateProductos();
    }

    public static void deleteContiene(){
        DBHandler db = DBHandler.getSingletonInstance(null);//The DBHandler has already been created

        db.deleteFromDB("CONTIENE", "id_Contiene", contieneID);

        updateProductos();
    }
}

