package com.example.emmanuel.construmobil;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Database.DBHandler;
import models.Pedido;
import models.Producto;

public class detallesPedido extends AppCompatActivity {

    private static final List<String> productosList = new ArrayList<>();
    private static ArrayAdapter productosAdapter;

    static Pedido pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedido);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        String pedidoID = getIntent().getStringExtra("id");

        DBHandler db = DBHandler.getSingletonInstance(this);

        pedido = db.getPedido(Long.parseLong(pedidoID));

        productosAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                productosList );
        updateProductos();

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //TODO hide delete pedido if user isn't the owner?
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
                    String data = (String) list.getItemAtPosition(position);

                    //TODO create pop-up to change number of materials or delete
                }
            });

            return rootView;
        }
    }

    public void delete(View view){
        DBHandler db = DBHandler.getSingletonInstance(null);//The DBHandler has already been created

        db.deletePedido(pedido.Id_Pedido);
        finish();
    }

    void updateProductos(){
        productosList.clear();

        for(int i = 0; i < pedido.productos.size(); i++){
            productosList.add(pedido.productos.get(i).Nombre_Producto);
        }

        productosAdapter.notifyDataSetChanged();
    }
}
