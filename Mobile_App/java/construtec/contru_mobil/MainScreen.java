package com.example.emmanuel.construmobil;

import android.app.Dialog;
import android.app.DatePickerDialog;
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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import models.Pedido;
import models.Usuario;
import android.widget.DatePicker;

public class MainScreen extends AppCompatActivity {

    private static models.Usuario usuario;
    private static final List<String> pedidosList = new ArrayList<>();
    private static final List<String> productosList = new ArrayList<>();
    private static final List<String> categoriasList = new ArrayList<>();

    private static ArrayAdapter pedidosAdapter;
    private static ArrayAdapter productosAdapter;
    private static ArrayAdapter categoriasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        pedidosAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                pedidosList );
        updateListaPedidos();

        productosAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                productosList);
        updateListaProductos();

        categoriasAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                categoriasList);
        updateListaCategorias();

        //TODO get this information from the DB
        usuario = new Usuario();
        usuario.nombre = "Emmanuel";
        usuario.apellidos = "Madrigal";
        usuario.cedula = 304960478;
        usuario.date = "6-8-1996";
        usuario.residencia = "Cartago, Costa Rica";
        usuario.Telefono = 87947188;
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
                case 0: return userDetails.newInstance(position + 1);
                case 1: return pedidos.newInstance(position + 1);
                case 2: return productos.newInstance(position + 1);
                case 3: return categorias.newInstance(position + 1);
                default: return opciones.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            return 5;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Información del Usuario";
                case 1:
                    return "Mis Pedidos";
                case 2:
                    return "Productos";
                case 3:
                    return "Categorías";
                case 4:
                    return "Opciones";
            }
            return null;
        }
    }


    /**
     * A view containing a user's details
     */
    public static class userDetails extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public userDetails() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static userDetails newInstance(int sectionNumber) {
            userDetails fragment = new userDetails();
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
            View rootView = inflater.inflate(R.layout.user_details, container, false);

            final TextView name =  (TextView) rootView.findViewById(R.id.userName);
            View.OnClickListener updateName = new View.OnClickListener() {
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
                            //TODO make call the the DBhandler
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

            final TextView lastName =  (TextView) rootView.findViewById(R.id.lastName);
            View.OnClickListener updateLastName = new View.OnClickListener() {
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
                            //TODO make call the the DBhandler
                            lastName.setText(np.getText());
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
            lastName.setOnClickListener(updateLastName);

            final TextView cedula =  (TextView) rootView.findViewById(R.id.userId);

            Date fecha = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(fecha);

            final int dia = cal.get(Calendar.DAY_OF_MONTH);
            final int mes = cal.get(Calendar.MONTH);
            final int año = cal.get(Calendar.YEAR);

            final TextView date =  (TextView) rootView.findViewById(R.id.birthDate);
            View.OnClickListener updateDate = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            selectedmonth = selectedmonth + 1;
                            String newDate = Integer.toString(selectedyear) +"-" +   Integer.toString(selectedmonth) +"-" + Integer.toString(selectedday);
                            //TODO make call to DB Handler
                            date.setText(newDate);
                        }

                    },
                            año, mes, dia);
                    mDatePicker.show();  }
            };
            date.setOnClickListener(updateDate);


            final TextView address =  (TextView) rootView.findViewById(R.id.residence);
            View.OnClickListener updateAddress = new View.OnClickListener() {
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
                            //TODO make call the the DBhandler
                            address.setText(np.getText());
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
            address.setOnClickListener(updateAddress);


            final TextView phone =  (TextView) rootView.findViewById(R.id.phoneNumber);
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
                            //TODO make call the the DBhandler
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
            phone.setOnClickListener(updatePhone);


            name.setText(usuario.nombre);
            lastName.setText(usuario.apellidos);
            cedula.setText(Long.toString(usuario.cedula));
            date.setText(usuario.date);
            address.setText(usuario.residencia);
            phone.setText(Long.toString(usuario.Telefono));




            return rootView;
        }
    }

    /**
     * A view containing a list of all one users requests
     */
    public static class pedidos extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public pedidos() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static pedidos newInstance(int sectionNumber) {
            pedidos fragment = new pedidos();
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

            list.setAdapter(MainScreen.pedidosAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String data = (String) list.getItemAtPosition(position);

                    //Calls an activity with the corresponding data
                    Intent intent = new Intent(view.getContext(), detallesPedido.class);
                    intent.putExtra("id", data);
                    startActivity(intent);
                }
            });



            return rootView;
        }
    }

    /**
     * A view containing a list of all the categories
     */
    public static class categorias extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public categorias() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static categorias newInstance(int sectionNumber) {
            categorias fragment = new categorias();
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

            list.setAdapter(MainScreen.categoriasAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String data = (String) list.getItemAtPosition(position);

                    //Calls an activity with the corresponding data
                    Intent intent = new Intent(view.getContext(), detallesCategoria.class);
                    intent.putExtra("id", data);
                    startActivity(intent);
                }
            });


            return rootView;
        }
    }

    /**
     * A view containing a list of all the projects for the logged in user
     */
    public static class productos extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public productos() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static productos newInstance(int sectionNumber) {
            productos fragment = new productos();
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

            list.setAdapter(MainScreen.productosAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String data = (String) list.getItemAtPosition(position);

                    //TODO add a way to filtrate the items

                    //Calls an activity with the corresponding data
                    Intent intent = new Intent(view.getContext(), productDetails.class);
                    intent.putExtra("id", data);
                    startActivity(intent);
                }
            });
            return rootView;
        }
    }

    /**
     * A view containing extra options
     */
    public static class opciones extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public opciones() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static opciones newInstance(int sectionNumber) {
            opciones fragment = new opciones();
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
            final View rootView = inflater.inflate(R.layout.options, container, false);

            Button newUser = (Button) rootView.findViewById(R.id.create_New_User);
            Button newSupplier = (Button) rootView.findViewById(R.id.create_New_Supplier);
            Button Sync = (Button) rootView.findViewById(R.id.sync);

            View.OnClickListener createNewUser = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(rootView.getContext(), RegisterUser.class);
                    intent.putExtra("origin", "newUser");
                    startActivity(intent);
                }
            };
            newUser.setOnClickListener(createNewUser);

            View.OnClickListener createNewSupplier = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(rootView.getContext(), RegisterUser.class);
                    intent.putExtra("origin", "newSupplier");
                    startActivity(intent);
                }
            };
            newSupplier.setOnClickListener(createNewSupplier);

            View.OnClickListener sync = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO force sincronization with EPATEC
                }
            };
            Sync.setOnClickListener(sync);

            return rootView;
        }
    }


    /**
     * Deletes the user and its information, but prevents deletions from sellers
     * @param v view calling this function
     */
    public void delete(View v){
        //TODO avoid delete from vendedores
    }

    /**
     * Updates the orders list from the data in the database and triggers a refresh on the DB
     */
    public void updateListaPedidos(){
        pedidosList.clear();

        //TODO take this information from the DB
        List<models.Pedido> pedidos = new LinkedList<>();

        Pedido pedido = new Pedido();
        pedido.Id_Pedido = 1;
        pedidos.add(pedido);

        pedido = new Pedido();
        pedido.Id_Pedido = 2;
        pedidos.add(pedido);

        pedido = new Pedido();
        pedido.Id_Pedido = 3;
        pedidos.add(pedido);

        pedido = new Pedido();
        pedido.Id_Pedido = 4;
        pedidos.add(pedido);

        for(int i =0; i < pedidos.size(); i++){
            pedido = pedidos.get(i);
            pedidosList.add(Long.toString(pedido.Id_Pedido));
        }
        pedidosAdapter.notifyDataSetChanged();
    }

    /**
     * Updates the products list from the data in the database and triggers a refresh on the DB
     */
    public void updateListaProductos(){
        //TODO take this information from the DB
        List<models.Producto> productos = new LinkedList<>();

        models.Producto producto = new models.Producto();
        producto.Nombre_Producto = "Carro";
        productos.add(producto);

        producto = new models.Producto();
        producto.Nombre_Producto = "Refrigeradora";
        productos.add(producto);

        producto = new models.Producto();
        producto.Nombre_Producto = "Clavo";
        productos.add(producto);

        producto = new models.Producto();
        producto.Nombre_Producto = "Tornillo";
        productos.add(producto);



        for(int i =0; i < productos.size(); i++){
            producto = productos.get(i);
            productosList.add(producto.Nombre_Producto);
        }
    }

    /**
     * Updates the categories list from the data in the database and triggers a refresh on the DB
     */
    public void updateListaCategorias(){
        //TODO take this information from the DB
        List<models.Categoria> categorias = new LinkedList<>();

        models.Categoria categoria = new models.Categoria();
        categoria.nombre = "Linea Blanca";
        categorias.add(categoria);

        categoria = new models.Categoria();
        categoria.nombre = "Cosntruccion";
        categorias.add(categoria);

        categoria = new models.Categoria();
        categoria.nombre = "Hogar";
        categorias.add(categoria);



        for(int i =0; i < categorias.size(); i++){
            categoria = categorias.get(i);
            categoriasList.add(categoria.nombre);
        }
    }
}
