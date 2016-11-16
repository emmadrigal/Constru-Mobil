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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import Database.DBHandler;
import models.Pedido;
import models.Usuario;
import android.widget.DatePicker;

import static android.text.InputType.TYPE_CLASS_NUMBER;

public class MainScreen extends AppCompatActivity {

    private static List<String> roles;
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

        productosAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                productosList);

        categoriasAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                categoriasList);

        long userId = getIntent().getLongExtra("id", 0);

        DBHandler db = DBHandler.getSingletonInstance(this);

        roles = db.getRolesUsuario(userId);
        for(int i = 0; i < roles.size(); i++){
            Log.i("roles!!!", roles.get(i));
        }
        usuario =  db.getUsuario(userId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Updates the information on the lists
        updateListaPedidos();
        updateListaProductos();
        updateListaCategorias();
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
                            usuario.Nombre = np.getText().toString();

                            DBHandler db = DBHandler.getSingletonInstance(null);
                            db.updateUsuario(usuario);

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
                            usuario.Apellido = np.getText().toString();

                            DBHandler db = DBHandler.getSingletonInstance(null);
                            db.updateUsuario(usuario);

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

                            usuario.Fecha_de_Nacimiento = newDate;

                            DBHandler db = DBHandler.getSingletonInstance(null);
                            db.updateUsuario(usuario);

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
                            usuario.Lugar_de_Residencia = np.getText().toString();

                            DBHandler db = DBHandler.getSingletonInstance(null);
                            db.updateUsuario(usuario);

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
                            usuario.Telefono = Integer.parseInt(np.getText().toString());

                            DBHandler db = DBHandler.getSingletonInstance(null);
                            db.updateUsuario(usuario);

                            phone.setText(np.getText());
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


            name.setText(usuario.Nombre);
            lastName.setText(usuario.Apellido);
            cedula.setText(Long.toString(usuario.Cedula));
            date.setText(usuario.Fecha_de_Nacimiento);
            address.setText(usuario.Lugar_de_Residencia);
            phone.setText(Long.toString(usuario.Telefono));

            Button delete = (Button) rootView.findViewById(R.id.delete);
            if(hasRol("Vendedor")){
                delete.setVisibility(View.GONE);
            } else {
                delete.setVisibility(View.VISIBLE);
            }

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
                    intent.putExtra("userID", usuario.Cedula);
                    startActivity(intent);
                }
            });

            Button create = (Button) rootView.findViewById(R.id.create);
            create.setText("Crear nuevo pedido");

            create.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    final Dialog d = new Dialog(getContext());
                    d.setContentView(R.layout.text_popup);
                    Button setValue = (Button) d.findViewById(R.id.set);
                    setValue.setText("Crear");

                    Button cancelAction = (Button) d.findViewById(R.id.cancel);

                    final EditText np = (EditText) d.findViewById(R.id.newValue);
                    np.setHint("Teléfono Preferido");
                    np.setInputType(TYPE_CLASS_NUMBER);

                    setValue.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            Pedido pedido = new Pedido();

                            pedido.Cedula_Cliente = usuario.Cedula;
                            pedido.Id_Sucursal = 0;
                            pedido.Telefono_Preferido = Integer.parseInt(np.getText().toString());

                            Calendar cal = Calendar.getInstance();

                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


                            //TODO give correct format to this date
                            pedido.Hora_de_Creación = dateFormat.format(cal.getTime());

                            DBHandler db = DBHandler.getSingletonInstance(getContext());
                            db.addPedido(pedido);

                            updateListaPedidos();

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

            //TODO add a way to filtrate the items

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String data = (String) list.getItemAtPosition(position);

                    //Calls an activity with the corresponding data
                    Intent intent = new Intent(view.getContext(), detallesCategoria.class);
                    intent.putExtra("id", data);
                    intent.putExtra("userID", usuario.Cedula);
                    startActivity(intent);
                }
            });

            Button create = (Button) rootView.findViewById(R.id.create);
            create.setText("Crear nueva Categoria");

            create.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), crearCategory.class);
                    intent.putExtra("userID", usuario.Cedula);
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

            //TODO add a way to filtrate the items

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String data = (String) list.getItemAtPosition(position);

                    //Calls an activity with the corresponding data
                    Intent intent = new Intent(view.getContext(), productDetails.class);
                    intent.putExtra("id", data);
                    intent.putExtra("userID", usuario.Cedula);
                    startActivity(intent);
                }
            });

            Button create = (Button) rootView.findViewById(R.id.create);

            if(hasRol("Proveedor")){
                create.setVisibility(View.VISIBLE);
                create.setText("Crear nuevo Producto");

                create.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), createProducto.class);
                        intent.putExtra("userID", Long.toString(usuario.Cedula));
                        startActivity(intent);
                    }
                });
            } else {
                create.setVisibility(View.GONE);
            }


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
        DBHandler db = DBHandler.getSingletonInstance(null);//The DBHandler has already been created

        db.deleteUsuario(usuario.Cedula);
        finish();
    }

    /**
     * Updates the orders list from the data in the database and triggers a refresh on the DB
     */
    public static void updateListaPedidos(){
        pedidosList.clear();

        DBHandler db = DBHandler.getSingletonInstance(null);//The DBHandler has already been created

        List<models.Pedido> pedidos = db.getPedidosCliente(usuario.Cedula);

        Pedido pedido;

        for(int i =0; i < pedidos.size(); i++){
            pedido = pedidos.get(i);
            pedidosList.add(Long.toString(pedido.Id_Pedido));
        }
        pedidosAdapter.notifyDataSetChanged();
    }

    /**
     * Updates the products list from the data in the database and triggers a refresh on the DB
     */
    public static void updateListaProductos(){
        productosList.clear();

        DBHandler db = DBHandler.getSingletonInstance(null);//The DBHandler has already been created
        List<models.Producto> productos = db.getAllProductos();

        models.Producto producto;
        for(int i =0; i < productos.size(); i++){
            producto = productos.get(i);
            productosList.add(producto.Nombre_Producto);
        }
        productosAdapter.notifyDataSetChanged();
    }

    /**
     * Updates the categories list from the data in the database and triggers a refresh on the DB
     */
    public static void updateListaCategorias(){
        categoriasList.clear();
        DBHandler db = DBHandler.getSingletonInstance(null);//The DBHandler has already been created
        List<models.Categoria> categorias = db.getAllCategorias();

        models.Categoria categoria;
        for(int i =0; i < categorias.size(); i++){
            categoria = categorias.get(i);
            categoriasList.add(categoria.Nombre);
        }
        categoriasAdapter.notifyDataSetChanged();
    }

    public static boolean hasRol(String rol){
        for (int i = 0; i < roles.size(); i++){
            if(rol.equals(roles.get(i))){
                return true;
            }
        }
        return false;
    }
}
