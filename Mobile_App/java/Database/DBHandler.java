package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import models.*;
import httpConnection.*;

import static android.os.SystemClock.sleep;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "costrumovil";
    private static final int DB_SCHEME_VERSION = 1;

    private static final String SQL_CREATE_TABLE_USUARIO = "CREATE TABLE USUARIO(" +
            "Cedula bigint NOT NULL PRIMARY KEY, " +
            "Nombre TEXT NOT NULL, " +
            "Apellido TEXT NOT NULL, " +
            "Lugar_de_Residencia TEXT, " +
            "Fecha_de_Nacimiento TEXT, " +
            "Telefono int" +
            ");";

    private static final String SQL_CREATE_TABLE_PRODUCTO = "CREATE TABLE PRODUCTO(" +
            "Nombre_Producto VARCHAR(30) NOT NULL PRIMARY KEY, " +
            "Id_Sucursal BIGINT NOT NULL," +
            "Cedula_Provedor BIGINT NOT NULL, " +
            "Nombre_Categoría VARCHAR(50) NOT NULL," +
            "Descripción TEXT, " +
            "Exento TINYINT, " +
            "Cantidad_Disponible INT NOT NULL, " +
            "Precio INT, " +
            "FOREIGN KEY(Cedula_Provedor) REFERENCES USUARIO(Cedula), " +
            "FOREIGN KEY(Id_Sucursal) REFERENCES SUCURSAL(Id_Sucursal), " +
            "FOREIGN KEY(Nombre_Categoría) REFERENCES CATEGORIA(Nombre)" +
            ");";

    private static final String SQL_CREATE_TABLE_CATEGORIA = "CREATE TABLE CATEGORIA(" +
            "Nombre VARCHAR(50) NOT NULL PRIMARY KEY, " +
            "Descripcion TEXT" +
            ");";

    private static final String SQL_CREATE_TABLE_PEDIDO = "CREATE TABLE PEDIDO(" +
            "Id_Pedido INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Id_Sucursal BIGINT NOT NULL, " +
            "Cedula_Cliente BIGINT NOT NULL, " +
            "Telefono_Preferido INT NOT NULL, " +
            "Hora_de_Creación TEXT NOT NULL, " +
            "FOREIGN KEY(Cedula_Cliente) REFERENCES USUARIO(Cedula), " +
            "FOREIGN KEY(Id_Sucursal) REFERENCES SUCURSAL(Id_Sucursal)" +
            ");";

    private static final String SQL_CREATE_TABLE_SUCURSAL = "CREATE TABLE SUCURSAL("
            + "Id_Sucursal BIGINT PRIMARY KEY NOT NULL"
            + ");";

    private static final String SQL_CREATE_TABLE_ROL_USUARIO = "CREATE TABLE ROL_USUARIO( " +
            "id_Rol_Usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "usuario BIGINT NOT NULL, " +
            "rol BIGINT NOT NULL, " +
            "FOREIGN KEY(usuario) REFERENCES USUARIO(Cedula), " +
            "FOREIGN KEY(rol) REFERENCES ROL(Id_rol)" +
            ");";

    private static final String SQL_CREATE_TABLE_EMPLEADOSUCURSAL = "CREATE TABLE EmpleadoSucursal(" +
            "Id_EmpleadoSucursal INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_empleado BIGINT NOT NULL, " +
            "id_sucursal BIGINT NOT NULL, " +
            "FOREIGN KEY(id_empleado) REFERENCES USUARIO(Cedula), " +
            "FOREIGN KEY(id_sucursal) REFERENCES SUCURSAL(Id_Sucursal)" +
            ");";

    private static final String SQL_CREATE_TABLE_ROL = "CREATE TABLE ROL(" +
            "Id_rol INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre TEXT);";

    private static final String SQL_CREATE_TABLE_CONTIENE = "CREATE TABLE CONTIENE(" +
            "id_Contiene INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Nombre_Producto TEXT NOT NULL, " +
            "Id_Pedido INTEGER NOT NULL, " +
            "Cantidad INTEGER, " +
            "FOREIGN KEY(Id_Pedido) REFERENCES PEDIDO(Id_Pedido), " +
            "FOREIGN KEY(Nombre_Producto) REFERENCES PRODUCTO(Nombre_Producto)" +
            ");";

    private static final String SQL_CREATE_TABLE_LOG = "CREATE TABLE LOG(" +
            "id_log INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "tipo varchar(6) NOT NULL, " +
            "url text NOT NULL, " +
            "json text " +
            ");";

    private static DBHandler DBHanlder;
    private Context context;
    //Indica si se está sincronizando con la base de datos en este momento
    private static boolean syncing;

    private static boolean updatingDB;

    private DBHandler(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
        this.context = context;
    }

    public static DBHandler getSingletonInstance(Context context) {
        if (DBHanlder == null){
            DBHanlder = new DBHandler(context);
        }

        return DBHanlder;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USUARIO);//
        db.execSQL(SQL_CREATE_TABLE_CATEGORIA);//
        db.execSQL(SQL_CREATE_TABLE_SUCURSAL);//
        db.execSQL(SQL_CREATE_TABLE_PRODUCTO);//
        db.execSQL(SQL_CREATE_TABLE_PEDIDO);//
        db.execSQL(SQL_CREATE_TABLE_CONTIENE);//
        db.execSQL(SQL_CREATE_TABLE_ROL_USUARIO);
        db.execSQL(SQL_CREATE_TABLE_ROL);
        db.execSQL(SQL_CREATE_TABLE_EMPLEADOSUCURSAL);
        db.execSQL(SQL_CREATE_TABLE_LOG);

        ContentValues values = new ContentValues();
        values.put("Id_rol", 1);
        values.put("nombre", "Proveedor");
        db.insert("ROL", null, values);

        values = new ContentValues();
        values.put("Id_rol", 2);
        values.put("nombre", "Cliente");
        db.insert("ROL", null, values);


        values = new ContentValues();
        values.put("Id_rol", 3);
        values.put("nombre", "Vendedor");
        db.insert("ROL", null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public class TalkToServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        /*
         *    do things before doInBackground() code runs
         *    such as preparing and showing a Dialog or ProgressBar
        */
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        /*
         *    updating data
         *    such a Dialog or ProgressBar
        */

        }

        @Override
        protected Void doInBackground(Void... params) {
            syncing = true;
            httpConnection conexion = httpConnection.getConnection();

            //Se envian todos los cambios hechos en la aplicación al Web Service
            String selectQuery = "SELECT * FROM LOG";
            Cursor cursor = DBHanlder.getReadableDatabase().rawQuery(selectQuery, null);

            Registro registro;
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    registro = new Registro();
                    registro.Id_Log = cursor.getLong(cursor.getColumnIndex("id_log"));
                    registro.type = cursor.getString(cursor.getColumnIndex("tipo"));
                    registro.url = cursor.getString(cursor.getColumnIndex("url"));
                    registro.json = cursor.getString(cursor.getColumnIndex("json"));

                    switch (registro.type) {
                        case "POST":
                            if(conexion.sendPost(registro.url, registro.json))
                                deleteFromDB("LOG", "id_log", Long.toString(registro.Id_Log));
                            break;
                        case "DELETE":
                            if(registro.url.equals("Contiene")) {
                                if (conexion.sendDeleteWithBody(registro.url, registro.json))
                                    deleteFromDB("LOG", "id_log", Long.toString(registro.Id_Log));
                            }
                            else if(conexion.sendDelete(registro.url))
                                deleteFromDB("LOG", "id_log", Long.toString(registro.Id_Log));
                            break;
                        case "PUT":
                            if(conexion.sendPut(registro.url))
                                deleteFromDB("LOG", "id_log", Long.toString(registro.Id_Log));
                            break;
                    }
                } while (cursor.moveToNext());
            }

            //Si se logró enviar todos los cambios se envian se solicitan todos los cambios realizados
            //en el WebService
            String usersJson = conexion.sendGet("getAllUsuario");
            String rolJson = conexion.sendGet("getAllRol_Usuario");
            String categoriaJson = conexion.sendGet("getAllCategories");
            String productoJson = conexion.sendGet("getAllProducto");
            String pedidoJson = conexion.sendGet("getAllPedido");



            //Si no se logró obtener todos los datos de la base de datos no hacer nada
            if((usersJson != null) && (rolJson != null) && (categoriaJson != null) && (productoJson != null) && (pedidoJson != null) ){
                //Se colocan los nuevos datos sobre la base de datos
                Usuario user;
                Categoria categoria;
                Producto producto;
                Pedido pedido;
                Contiene contiene;

                try {
                    JSONArray usersArray = new JSONArray(usersJson);
                    JSONArray rolArray = new JSONArray(rolJson);
                    JSONArray CategoryArray = new JSONArray(categoriaJson);
                    JSONArray productArray = new JSONArray(productoJson);
                    JSONArray pedidoArray = new JSONArray(pedidoJson);

                    updatingDB = true;
                    //Aquí si limpia la base de datos empotrada
                    clearDatabase();

                    //USUARIO
                    //Iterate the jsonArray and print the info of JSONObjects
                    for(int i=0; i < usersArray.length(); i++){
                        JSONObject jsonObject = usersArray.getJSONObject(i);

                        ContentValues values = new ContentValues();

                        values.put("Cedula", jsonObject.getLong("Id_usuario"));
                        values.put("Nombre", jsonObject.getString("Nombre"));
                        values.put("Apellido", jsonObject.getString("Apellido"));
                        values.put("Lugar_de_Residencia", jsonObject.getString("Residencia"));
                        values.put("Fecha_de_Nacimiento", jsonObject.getString("Fecha_de_Nacimiento"));
                        values.put("Telefono", jsonObject.getInt("Telefono"));

                        insertInDB("USUARIO", values);
                    }


                    //ROL
                    //Iterate the jsonArray and print the info of JSONObjects
                    for(int i=0; i < rolArray.length(); i++){
                        JSONObject jsonObject = rolArray.getJSONObject(i);

                        ContentValues values = new ContentValues();

                        values.put("usuario", jsonObject.getLong("Id_usuario"));
                        values.put("rol", jsonObject.getLong("Id_rol"));

                        SQLiteDatabase db = DBHanlder.getWritableDatabase();
                        db.insert("ROL_USUARIO", null, values);
                    }

                    //Categoria
                    //Iterate the jsonArray and print the info of JSONObjects
                    for(int i=0; i < CategoryArray.length(); i++){
                        JSONObject jsonObject = CategoryArray.getJSONObject(i);

                        ContentValues values = new ContentValues();
                        values.put("Nombre", jsonObject.getString("Nombre"));
                        values.put("Descripcion", jsonObject.getString("Descripcion"));

                        insertInDB("CATEGORIA", values);
                    }

                    //Producto
                    //Iterate the jsonArray and print the info of JSONObjects
                    for(int i=0; i < productArray.length(); i++){
                        JSONObject jsonObject = productArray.getJSONObject(i);

                        ContentValues values = new ContentValues();

                        values.put("Nombre_Producto", jsonObject.getString("nombre"));
                        values.put("Id_Sucursal", jsonObject.getLong("id_Sucursal"));
                        values.put("Cedula_Provedor", jsonObject.getLong("Cedula_Provedor"));
                        values.put("Nombre_Categoría", jsonObject.getString("categoria"));
                        values.put("Descripción", jsonObject.getString("Descripcion"));
                        if(jsonObject.getBoolean("Exento"))
                            values.put("Exento", "true");
                        else
                            values.put("Exento", "false");
                        values.put("Cantidad_Disponible", jsonObject.getInt("Cantidad_Disponible"));
                        values.put("Precio", jsonObject.getInt("Precio"));

                        insertInDB("PRODUCTO", values);
                    }

                    Long idPedido;
                    //Pedido
                    //Iterate the jsonArray and print the info of JSONObjects
                    for(int i=0; i < pedidoArray.length(); i++){
                        JSONObject jsonObject = pedidoArray.getJSONObject(i);

                        ContentValues values = new ContentValues();

                        idPedido = jsonObject.getLong("id_Pedido");
                        values.put("Id_Pedido", idPedido);
                        values.put("Cedula_Cliente", jsonObject.getLong("Cedula_Cliente"));

                        values.put("Id_Sucursal", jsonObject.getLong("id_Sucursal"));
                        values.put("Telefono_Preferido", jsonObject.getInt("Telefono"));
                        values.put("Hora_de_Creación", jsonObject.getString("Hora"));

                        insertInDB("PEDIDO", values);

                        JSONArray listaContiene = jsonObject.getJSONArray("productos");


                        for(int j = 0; j < listaContiene.length(); j++){
                            JSONObject jsonObject2;
                            jsonObject2 = listaContiene.getJSONObject(j);

                            ContentValues valuesContiene = new ContentValues();

                            valuesContiene.put("Cantidad", jsonObject2.getInt("Quantity"));
                            valuesContiene.put("Nombre_Producto", jsonObject2.getString("nombre"));
                            valuesContiene.put("Id_Pedido", idPedido);

                            insertInDB("CONTIENE", valuesContiene);
                        }
                        updatingDB = false;
                    }

                }
                catch (JSONException e) {
                    Log.i("!!!!!!!!!!!!!!!!!!!!!", "Failed to get all arrays");
                    Log.i("error", e.getMessage());
                }
            }
            DBHanlder.getWritableDatabase().close();
            syncing = false;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
        /*
         *    do something with data here
         *    display it or send to mainactivity
         *    close any dialogs/ProgressBars/etc...
        */
        }
    }



    public void SyncDB(){
        new TalkToServer().execute();
    }

    //Métodos para la tabla Usuario
    public void addUsuario(Usuario usuario){
        ContentValues values = new ContentValues();

        values.put("Cedula", usuario.Cedula);
        values.put("Nombre", usuario.Nombre);
        values.put("Apellido", usuario.Apellido);
        values.put("Lugar_de_Residencia", usuario.Lugar_de_Residencia);
        values.put("Fecha_de_Nacimiento", usuario.Fecha_de_Nacimiento);
        values.put("Telefono", usuario.Telefono);

        insertInDB("USUARIO", values);

        JSONObject json = new JSONObject();
        try {
            json.put("Id_usuario", usuario.Cedula);
            json.put("Nombre", usuario.Nombre);
            json.put("Apellido", usuario.Apellido);
            json.put("Residencia", usuario.Lugar_de_Residencia);
            json.put("Fecha_de_Nacimiento", usuario.Fecha_de_Nacimiento);
            json.put("Telefono", usuario.Telefono);

            ContentValues logvalues = new ContentValues();
            logvalues.put("tipo", "POST");
            logvalues.put("url", "Usuario");
            logvalues.put("json", json.toString());

            insertInDB("LOG", logvalues);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Asocia un usuario a un rol
     * @param userID id del usuario
     * @param rol rol a asociar
     */
    public void addRol(long userID, String rol) {
        ContentValues values = new ContentValues();

        values.put("usuario", userID);
        switch (rol){
            case "Cliente" :{
                values.put("rol", 2);//Estos valores dependen de los roles creados
                break;
            }
            case "Proveedor":{
                values.put("rol", 1);//Estos valores dependen de los roles creados
                break;
            }
            case "Vendedor":{
                values.put("rol", 3);//Estos valores dependen de los roles creados
                break;
            }
        }

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("ROL_USUARIO", null, values);
        if(!syncing){
            db.close();
        }

        JSONObject json = new JSONObject();
        try {
            json.put("Id_usuario", userID);
            json.put("Id_rol", rol);

            ContentValues logvalues = new ContentValues();
            logvalues.put("tipo", "POST");
            logvalues.put("url", "Rol_Usuario");
            logvalues.put("json", json.toString());

            insertInDB("LOG", logvalues);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteUsuario(long id){
        deleteFromDB("USUARIO", "Cedula", String.valueOf(id));

        ContentValues logvalues = new ContentValues();
        logvalues.put("tipo", "Delete");
        logvalues.put("url", "Usuario/" + Long.toString(id));

        insertInDB("LOG", logvalues);
    }

    public void updateUsuario(Usuario usuario){
        ContentValues values = new ContentValues();

        values.put("Nombre", usuario.Nombre);
        values.put("Apellido", usuario.Apellido);
        values.put("Lugar_de_Residencia", usuario.Lugar_de_Residencia);
        values.put("Fecha_de_Nacimiento", usuario.Fecha_de_Nacimiento);
        values.put("Telefono", usuario.Telefono);

        updateFromDB("USUARIO", values, "Cedula="+usuario.Cedula);

        if(!syncing){
            ContentValues logvalues = new ContentValues();
            logvalues.put("tipo", "PUT");
            logvalues.put("url", "Usuario/" + Long.toString(usuario.Cedula) + "/" + "Nombre" + "/" + usuario.Nombre);

            insertInDB("LOG", logvalues);

            logvalues = new ContentValues();
            logvalues.put("tipo", "PUT");
            logvalues.put("url", "Usuario/" + Long.toString(usuario.Cedula) + "/" + "Apellido" + "/" + usuario.Apellido);

            insertInDB("LOG", logvalues);

            logvalues = new ContentValues();
            logvalues.put("tipo", "PUT");
            logvalues.put("url", "Usuario/" + Long.toString(usuario.Cedula) + "/" + "Lugar_de_Residencia" + "/" + usuario.Lugar_de_Residencia);

            insertInDB("LOG", logvalues);

            logvalues = new ContentValues();
            logvalues.put("tipo", "PUT");
            logvalues.put("url", "Usuario/" + Long.toString(usuario.Cedula) + "/" + "Fecha_de_Nacimiento" + "/" + usuario.Fecha_de_Nacimiento);

            insertInDB("LOG", logvalues);

            logvalues = new ContentValues();
            logvalues.put("tipo", "PUT");
            logvalues.put("url", "Usuario/" + Long.toString(usuario.Cedula) + "/" + "Telefono" + "/" + usuario.Telefono);

            insertInDB("LOG", logvalues);
        }
    }

    public Usuario getUsuario(long id){
        Cursor cursor = getRowFromDB("USUARIO", "Cedula", String.valueOf(id));

        if(cursor.getCount() > 0){
            Usuario usuario = new Usuario();
            usuario.setCedula(id);
            usuario.setApellido(cursor.getString(cursor.getColumnIndex("Apellido")));
            usuario.setNombre(cursor.getString(cursor.getColumnIndex("Nombre")));
            usuario.setLugar_de_Residencia(cursor.getString(cursor.getColumnIndex("Lugar_de_Residencia")));
            usuario.setFecha_de_Nacimiento(cursor.getString(cursor.getColumnIndex("Fecha_de_Nacimiento")));
            usuario.setTelefono(cursor.getInt(cursor.getColumnIndex("Telefono")));

            return usuario;
        }else{
            return null;
        }

    }

    //Métodos para la tabla producto
    public void addProducto(Producto prod){
        ContentValues values = new ContentValues();

        values.put("Nombre_Producto", prod.Nombre_Producto);
        values.put("Id_Sucursal", prod.Id_Sucursal);
        values.put("Cedula_Provedor", prod.Cedula_Proveedor);
        values.put("Nombre_Categoría", prod.Nombre_Categoria);
        values.put("Descripción", prod.descripcion);
        if(prod.exento == 1)
            values.put("Exento", "true");
        else
            values.put("Exento", "false");
        values.put("Cantidad_Disponible", prod.cantidad);
        values.put("Precio", prod.precio);

        insertInDB("PRODUCTO", values);

        JSONObject json = new JSONObject();
        try {
            json.put("nombre", prod.Nombre_Producto);
            json.put("id_Sucursal", prod.Id_Sucursal);
            json.put("Cedula_Provedor", prod.Cedula_Proveedor);
            json.put("categoria", prod.Nombre_Categoria);
            json.put("Descripcion", prod.descripcion);
            if(prod.exento == 0)
                json.put("Exento", "false");
            else
                json.put("Exento", "true");
            json.put("Cantidad_Disponible", prod.cantidad);
            json.put("Precio", prod.precio);

            ContentValues logvalues = new ContentValues();
            logvalues.put("tipo", "POST");
            logvalues.put("url", "Producto");
            logvalues.put("json", json.toString());

            insertInDB("LOG", logvalues);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteProducto(String nombre){
        deleteFromDB("PRODUCTO", "Nombre_Producto", "'"+ nombre+ "'");

        ContentValues logvalues = new ContentValues();
        logvalues.put("tipo", "DELETE");
        logvalues.put("url", "Producto/" + nombre);

        insertInDB("LOG", logvalues);
    }

    public void updateProducto(Producto prod){
        ContentValues values = new ContentValues();

        values.put("Descripción", prod.descripcion);
        values.put("Exento", prod.exento);
        values.put("Cantidad_Disponible", prod.cantidad);
        values.put("Precio", prod.precio);

        updateFromDB("PRODUCTO", values, "Nombre_Producto = '"+ prod.Nombre_Producto+ "'");

        if(!syncing){
            ContentValues logvalues = new ContentValues();
            logvalues.put("tipo", "PUT");
            logvalues.put("url", "Producto/" + prod.Nombre_Producto + "/" + "Descripción" + "/" + prod.descripcion);

            insertInDB("LOG", logvalues);

            logvalues = new ContentValues();
            logvalues.put("tipo", "PUT");
            logvalues.put("url", "Producto/" + prod.Nombre_Producto + "/" + "Exento" + "/" + Integer.toString(prod.exento));

            insertInDB("LOG", logvalues);

            logvalues = new ContentValues();
            logvalues.put("tipo", "PUT");
            logvalues.put("url", "Producto/" + prod.Nombre_Producto + "/" + "Precio" + "/" + Integer.toString(prod.precio));

            insertInDB("LOG", logvalues);
        }
    }

    public Producto getProducto(String nombre){
        Cursor cursor = getRowFromDB("PRODUCTO", "Nombre_Producto", "'"+ nombre+ "'");

        Producto producto = new Producto();
        producto.setNombre_Producto(cursor.getString(cursor.getColumnIndex("Nombre_Producto")));
        producto.setId_Sucursal(cursor.getLong(cursor.getColumnIndex("Id_Sucursal")));
        producto.setCedula_Proveedor(cursor.getLong(cursor.getColumnIndex("Cedula_Provedor")));
        producto.setNombre_Categoria(cursor.getString(cursor.getColumnIndex("Nombre_Categoría")));
        producto.setDescripcion(cursor.getString(cursor.getColumnIndex("Descripción")));
        producto.setExento(cursor.getInt(cursor.getColumnIndex("Exento")));
        producto.setCantidad(cursor.getInt(cursor.getColumnIndex("Cantidad_Disponible")));
        producto.setPrecio(cursor.getInt(cursor.getColumnIndex("Precio")));

        return producto;
    }

    //Métodos para tabla categoría
    public void addCategoria(Categoria categoria){
        ContentValues values = new ContentValues();

        values.put("Nombre", categoria.Nombre);
        values.put("Descripcion", categoria.Descripcion);

        insertInDB("CATEGORIA", values);

        JSONObject json = new JSONObject();
        try {
            json.put("Nombre", categoria.Nombre);
            json.put("Descripcion", categoria.Descripcion);

            ContentValues logvalues = new ContentValues();
            logvalues.put("tipo", "POST");
            logvalues.put("url", "Categoria");
            logvalues.put("json", json.toString());

            insertInDB("LOG", logvalues);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategoria(String nombre){
        deleteFromDB("CATEGORIA", "Nombre", "'" + nombre + "'");

        ContentValues logvalues = new ContentValues();
        logvalues.put("tipo", "DELETE");
        logvalues.put("url", "Categoria/" + nombre);

        insertInDB("LOG", logvalues);
    }

    public void updateCategoria(Categoria categoria){
        ContentValues values = new ContentValues();

        values.put("Descripcion", categoria.Descripcion);

        updateFromDB("CATEGORIA", values, "Nombre= '"+categoria.Nombre + "'");

        ContentValues logvalues = new ContentValues();
        logvalues.put("tipo", "PUT");
        logvalues.put("url", "Categoria/" + categoria.Nombre + "/" + "Descripción" + "/" + categoria.Descripcion);

        insertInDB("LOG", logvalues);
    }

    public Categoria getCategoria(String nombre){
        Cursor cursor = getRowFromDB("CATEGORIA", "Nombre", "'" + nombre + "'");

        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setDescripcion(cursor.getString(cursor.getColumnIndex("Descripcion")));

        return categoria;
    }

    //Métodos para tabla pedido
    public void addPedido(Pedido pedido){
        ContentValues values = new ContentValues();

        values.put("Cedula_Cliente", pedido.Cedula_Cliente);
        values.put("Id_Sucursal", pedido.Id_Sucursal);
        values.put("Telefono_Preferido", pedido.Telefono_Preferido);
        values.put("Hora_de_Creación", pedido.Hora_de_Creación);

        insertInDB("PEDIDO", values);

        JSONObject json = new JSONObject();
        try {
            json.put("Cedula_Cliente", pedido.Cedula_Cliente);
            json.put("Id_Sucursal", pedido.Id_Sucursal);
            json.put("Telefono", pedido.Telefono_Preferido);
            json.put("Hora", pedido.Hora_de_Creación);

            ContentValues logvalues = new ContentValues();
            logvalues.put("tipo", "POST");
            logvalues.put("url", "Pedido");
            logvalues.put("json", json.toString());

            insertInDB("LOG", logvalues);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addPedidoConID(Pedido pedido){
        ContentValues values = new ContentValues();

        values.put("Id_Pedido", pedido.Id_Pedido);
        values.put("Cedula_Cliente", pedido.Cedula_Cliente);
        values.put("Id_Sucursal", pedido.Id_Sucursal);
        values.put("Telefono_Preferido", pedido.Telefono_Preferido);
        values.put("Hora_de_Creación", pedido.Hora_de_Creación);

        insertInDB("PEDIDO", values);

        if(!syncing){
            JSONObject json = new JSONObject();
            try {
                json.put("Cedula_Cliente", pedido.Cedula_Cliente);
                json.put("Id_Sucursal", pedido.Id_Sucursal);
                json.put("Telefono", pedido.Telefono_Preferido);
                json.put("Hora", pedido.Hora_de_Creación);

                ContentValues logvalues = new ContentValues();
                logvalues.put("tipo", "POST");
                logvalues.put("url", "Pedido");
                logvalues.put("json", json.toString());

                insertInDB("LOG", logvalues);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Contiene getContiene(String idContiene){
        Cursor cursor = getRowFromDB("CONTIENE", "id_Contiene", idContiene);

        Contiene contiene = new Contiene();
        contiene.id_Contiene = cursor.getLong(cursor.getColumnIndex("id_Contiene"));
        contiene.Nombre_Producto = cursor.getString(cursor.getColumnIndex("Nombre_Producto"));
        contiene.Id_Pedido = cursor.getLong(cursor.getColumnIndex("Id_Pedido"));
        contiene.Cantidad = cursor.getInt(cursor.getColumnIndex("Cantidad"));

        return contiene;
    }

    public void addContiene(Contiene contiene, Pedido pedido){
        ContentValues values = new ContentValues();

        values.put("Cantidad", contiene.Cantidad);
        values.put("Nombre_Producto", contiene.Nombre_Producto);
        values.put("Id_Pedido", contiene.Id_Pedido);

        insertInDB("CONTIENE", values);

        JSONObject json = new JSONObject();
        try {
            json.put("userID", pedido.Cedula_Cliente);
            json.put("time", pedido.Hora_de_Creación);
            json.put("producto", contiene.Nombre_Producto);
            json.put("cantidad", contiene.Cantidad);


            ContentValues logvalues = new ContentValues();
            logvalues.put("tipo", "POST");
            logvalues.put("url", "Contiene");
            logvalues.put("json", json.toString());

            insertInDB("LOG", logvalues);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //TODO make delete work
    public void updateContiene(Contiene contiene, Pedido pedido){
        ContentValues values = new ContentValues();

        values.put("Cantidad", contiene.Cantidad);

        updateFromDB("CONTIENE", values, "id_Contiene="+ contiene.id_Contiene);

        if(!syncing){
            ContentValues logvalues = new ContentValues();
            logvalues.put("tipo", "PUT");
            logvalues.put("url", "Contiene");

            JSONObject json = new JSONObject();
            try {
                json.put("userID", Long.toString(pedido.Cedula_Cliente));
                json.put("time", pedido.Hora_de_Creación);
                json.put("producto", contiene.Nombre_Producto);
                json.put("cantidad", contiene.Cantidad);


                logvalues.put("tipo", "POST");
                logvalues.put("url", "Contiene");
                logvalues.put("json", json.toString());

                insertInDB("LOG", logvalues);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteContiene(String id_Contiene, Pedido pedido){
        Contiene contiene = getContiene(id_Contiene);

        deleteFromDB("CONTIENE", "id_Contiene", id_Contiene);

        if(!syncing){
            ContentValues logvalues = new ContentValues();
            logvalues.put("tipo", "DELETE");
            logvalues.put("url", "Contiene");

            JSONObject json = new JSONObject();
            try {
                json.put("userID", Long.toString(pedido.Cedula_Cliente));
                json.put("time", pedido.Hora_de_Creación);
                json.put("producto", contiene.Nombre_Producto);
                json.put("cantidad", contiene.Cantidad);

                logvalues.put("json", json.toString());

                insertInDB("LOG", logvalues);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void deletePedido(long id){
        deleteFromDB("PEDIDO", "Id_Pedido", String.valueOf(id));

        //TODO this doesn't work since the id inside of the app isn't the same as in the webService!!!
        //This could be changed by selecting in EPATEC acording to userID and creationYime
        ContentValues logvalues = new ContentValues();
        logvalues.put("tipo", "DELETE");
        logvalues.put("url", "Pedido/" + Long.toString(id));

        insertInDB("LOG", logvalues);
    }

    public void updatePedido(Pedido pedido){
        ContentValues values = new ContentValues();

        values.put("Telefono_Preferido", pedido.Telefono_Preferido);

        updateFromDB("PEDIDO", values, "Id_Pedido="+ pedido.Id_Pedido);

        //This could be changed by selecting in EPATEC acording to userID and creationYime
        if(!syncing){
            ContentValues logvalues = new ContentValues();
            logvalues.put("tipo", "PUT");
            logvalues.put("url", "Pedido/" + Long.toString(pedido.Id_Pedido)+ "/" + "Telefono_Preferido" + "/" + pedido.Telefono_Preferido);

            logvalues.put("url", "Pedido/" + Long.toString(pedido.Cedula_Cliente) + "/"  +
                    pedido.Hora_de_Creación + "/"  + "/" + "Telefono_Preferido" + "/" + pedido.Telefono_Preferido);

            insertInDB("LOG", logvalues);
        }


    }

    public Pedido getPedido(long id){
        Cursor cursor = getRowFromDB("PEDIDO", "Id_Pedido", String.valueOf(id));

        Pedido pedido = new Pedido();
        pedido.setId_Pedido(id);
        pedido.setCedula_Cliente(cursor.getLong(cursor.getColumnIndex("Cedula_Cliente")));
        pedido.setId_Sucursal(cursor.getLong(cursor.getColumnIndex("Id_Sucursal")));
        pedido.setTelefono_Preferido(cursor.getInt(cursor.getColumnIndex("Telefono_Preferido")));
        pedido.setHora_de_Creación(cursor.getString(cursor.getColumnIndex("Hora_de_Creación")));
        pedido.setProductos(getProductosPedido(id));

        return pedido;
    }

    //Métodos para tabla sucursal
    public void addSucursal(long id){
        ContentValues values = new ContentValues();

        values.put("id_Sucursal", id);

        insertInDB("SUCURSAL", values);
    }

    public void deleteSurcursal(long id){
        deleteFromDB("SUCURSAL", "id_sucursal", String.valueOf(id));
    }

    public List<Sucursal> getAllSucursales() {
        List<Sucursal> sucursales = new ArrayList<Sucursal>();
        String selectQuery = "SELECT  * FROM " + "SUCURSAL";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Sucursal sucursal = new Sucursal();
                sucursal.setID(Long.parseLong(cursor.getString(0)));

                // Adding contact to list
                sucursales.add(sucursal);
            } while (cursor.moveToNext());
        }

        return sucursales;
    }

    /**
     * Deletes all the rows in a table
     * @param TABLE_NAME that is going to be emptied
     */
    public void clearTable(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+ TABLE_NAME;
        db.execSQL(clearDBQuery);
    }

    /**
     * Deletes all the rows in all the tables in the Database
     */
    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM CONTIENE; ";
        db.execSQL(clearDBQuery);

        clearDBQuery = "DELETE FROM PEDIDO;";
        db = this.getWritableDatabase();
        db.execSQL(clearDBQuery);

        clearDBQuery = "DELETE FROM PRODUCTO;";
        db = this.getWritableDatabase();
        db.execSQL(clearDBQuery);

        clearDBQuery = "DELETE FROM CATEGORIA;";
        db = this.getWritableDatabase();
        db.execSQL(clearDBQuery);

        clearDBQuery = "DELETE FROM ROL_USUARIO;";
        db = this.getWritableDatabase();
        db.execSQL(clearDBQuery);

        clearDBQuery = "DELETE FROM USUARIO;";
        db = this.getWritableDatabase();
        db.execSQL(clearDBQuery);
    }

    //Método inserta una nueva fila en la tabla deseada
    public void insertInDB(String table, ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(table, null, values);
        if(!syncing){
            db.close();
        }
    }

    //Método elimina un registro de la tabla deseada
    public void deleteFromDB(String table, String propiedad, String value){
        //TODO check that deletion is being performed correctly
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, propiedad+"="+value, null);
        if(!syncing){
            db.close();
        }
    }

    public void updateFromDB(String table, ContentValues values, String upFilter){
        //TODO, check that update is being logged
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(table, values, upFilter, null);
        if(!syncing){
            db.close();
        }
    }

    public Cursor getRowFromDB(String table, String row, String id){
        //Asegura que no se envien datos sucios
        while(updatingDB){
            sleep(50);
        }
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + table + " WHERE " + row + " = " + id;

        Log.i("selectQuery", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {cursor.moveToFirst();}

        if(!syncing){
            db.close();
        }
        
        return cursor;
    }

    public List<Pedido> getPedidosCliente(long id){
        List<Pedido> pedidos = new ArrayList<Pedido>();
        Cursor cursor = getRowFromDB("PEDIDO", "Cedula_Cliente", String.valueOf(id));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Pedido pedido = new Pedido();
                pedido.setId_Pedido(cursor.getLong(cursor.getColumnIndex("Id_Pedido")));
                pedido.setCedula_Cliente(id);
                pedido.setId_Sucursal(cursor.getLong(cursor.getColumnIndex("Id_Sucursal")));
                pedido.setTelefono_Preferido(cursor.getInt(cursor.getColumnIndex("Telefono_Preferido")));
                pedido.setHora_de_Creación(cursor.getString(cursor.getColumnIndex("Hora_de_Creación")));

                // Adding contact to list
                pedidos.add(pedido);
            } while (cursor.moveToNext());
        }

        return pedidos;
    }
    
    public List<Producto> getProductosVendedor(long id){
        List<Producto> productos = new ArrayList<Producto>();
        Cursor cursor = getRowFromDB("PRODUCTO", "Cedula_Proveedor", String.valueOf(id));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Producto producto = new Producto();
                producto.setNombre_Producto(cursor.getString(cursor.getColumnIndex("Nombre_Producto")));
                producto.setId_Sucursal(cursor.getLong(cursor.getColumnIndex("Id_Sucursal")));
                producto.setCedula_Proveedor(id);
                producto.setNombre_Categoria(cursor.getString(cursor.getColumnIndex("Nombre_Categoria")));
                producto.setDescripcion(cursor.getString(cursor.getColumnIndex("descripcion")));
                producto.setExento(cursor.getInt(cursor.getColumnIndex("exento")));
                producto.setCantidad(cursor.getInt(cursor.getColumnIndex("cantidad")));
                producto.setPrecio(cursor.getInt(cursor.getColumnIndex("precio")));

                // Adding contact to list
                productos.add(producto);
            } while (cursor.moveToNext());
        }
        return productos;
    }

    public List<Contiene> getProductosPedido(long id){
        List<Contiene> productos = new ArrayList<Contiene>();

        Cursor cursor = getRowFromDB("CONTIENE", "Id_Pedido", String.valueOf(id));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contiene contiene = new Contiene();
                contiene.setId_Contiene(cursor.getLong(cursor.getColumnIndex("id_Contiene")));
                contiene.setNombre_Producto(cursor.getString(cursor.getColumnIndex("Nombre_Producto")));
                contiene.setId_Pedido(id);
                contiene.setCantidad(cursor.getInt(cursor.getColumnIndex("Cantidad")));

                // Adding contact to list
                productos.add(contiene);
            } while (cursor.moveToNext());
        }
        return productos;
    }
    
    public List<Producto> getAllProductos(){
        List<Producto> productos = new ArrayList<Producto>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM PRODUCTO";
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Producto producto = new Producto();
                producto.setNombre_Producto(cursor.getString(cursor.getColumnIndex("Nombre_Producto")));
                producto.setId_Sucursal(cursor.getLong(cursor.getColumnIndex("Id_Sucursal")));
                producto.setCedula_Proveedor(cursor.getLong(cursor.getColumnIndex("Cedula_Provedor")));
                producto.setNombre_Categoria(cursor.getString(cursor.getColumnIndex("Nombre_Categoría")));
                producto.setDescripcion(cursor.getString(cursor.getColumnIndex("Descripción")));
                producto.setExento(cursor.getInt(cursor.getColumnIndex("Exento")));
                producto.setCantidad(cursor.getInt(cursor.getColumnIndex("Cantidad_Disponible")));
                producto.setPrecio(cursor.getInt(cursor.getColumnIndex("Precio")));

                // Adding contact to list
                productos.add(producto);
            } while (cursor.moveToNext());
        }

        if(!syncing){
            db.close();
        }
        return productos;
    }

    public List<Categoria> getAllCategorias(){
        List<Categoria> categorias = new ArrayList<Categoria>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM CATEGORIA";
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Categoria categoria = new Categoria();
                categoria.setNombre(cursor.getString(cursor.getColumnIndex("Nombre")));
                categoria.setDescripcion(cursor.getString(cursor.getColumnIndex("Descripcion")));

                // Adding contact to list
                categorias.add(categoria);
            } while (cursor.moveToNext());
        }

        if(!syncing){
            db.close();
        }
        return categorias;
    }

    public List<String> getRolesUsuario(long userID){
        List<String> roles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        String selectQuery = "SELECT R.nombre as nombre, U.Nombre as usuario FROM ROL R " +
                "JOIN ROL_USUARIO RU ON R.Id_rol = RU.rol " +
                "JOIN USUARIO U ON U.Cedula = RU.usuario WHERE U.Cedula = " + Long.toString(userID);

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String rol = cursor.getString(cursor.getColumnIndex("nombre"));

                // Adding contact to list
                roles.add(rol);
            } while (cursor.moveToNext());
        }

        if(!syncing){
            db.close();
        }
        return roles;
    }
    /*
 qué pasa si se trata de hacer un get de un objeto que no existe? se cae?
 Y no estoy seguro si esta, pero necesito roles por usuario
 Los nombres en si, no los ID*/
}
