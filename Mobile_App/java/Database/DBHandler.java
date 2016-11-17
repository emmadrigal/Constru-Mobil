package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import models.*;

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

    private static final String SQL_CREATE_TABLE_REGISTRO = "CREATE TABLE REGISTRO(" +
            "id_Registro INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "tipo VARCHAR(30), " +
            "json TEXT" +
            ");";

    private static DBHandler DBHandler;
    private Context context;

    private DBHandler(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
        this.context = context;
    }

    public static DBHandler getSingletonInstance(Context context) {
        if (DBHandler == null){
            DBHandler = new DBHandler(context);
        }
        else{
            System.out.println("No se puede crear el objeto porque ya existe un objeto de la clase DBHanlder");
        }

        return DBHandler;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USUARIO);
        db.execSQL(SQL_CREATE_TABLE_CATEGORIA);
        db.execSQL(SQL_CREATE_TABLE_SUCURSAL);
        db.execSQL(SQL_CREATE_TABLE_PRODUCTO);
        db.execSQL(SQL_CREATE_TABLE_PEDIDO);
        db.execSQL(SQL_CREATE_TABLE_ROL_USUARIO);
        db.execSQL(SQL_CREATE_TABLE_ROL);
        db.execSQL(SQL_CREATE_TABLE_CONTIENE);
        db.execSQL(SQL_CREATE_TABLE_EMPLEADOSUCURSAL);
        db.execSQL(SQL_CREATE_TABLE_REGISTRO);

        ContentValues values = new ContentValues();
        values.put("nombre", "Proveedor");
        db.insert("ROL", null, values);

        values = new ContentValues();
        values.put("nombre", "Cliente");
        db.insert("ROL", null, values);


        values = new ContentValues();
        values.put("nombre", "Vendedor");
        db.insert("ROL", null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    //Métodos para la tabla Usuario
    public void addUsuario(Usuario usuario){
        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();

        values.put("Cedula", usuario.Cedula);
        values.put("Nombre", usuario.Nombre);
        values.put("Apellido", usuario.Apellido);
        values.put("Lugar_de_Residencia", usuario.Lugar_de_Residencia);
        values.put("Fecha_de_Nacimiento", usuario.Fecha_de_Nacimiento);
        values.put("Telefono", usuario.Telefono);

        insertInDB("USUARIO", values);

        //Crea el string para el json que se almacena en el registro de queries
        String json = "/Cliente/{Cedula_Cliente:"+usuario.Cedula+", " +
                "Nombre:"+usuario.Nombre+", " +
                "Apellidos"+usuario.Apellido+", " +
                "Residencia:"+usuario.Lugar_de_Residencia+", " +
                "Nacimiento:"+usuario.Fecha_de_Nacimiento+", " +
                "Telefono:"+usuario.Telefono+"}";

        values2.put("tipo", "POST");
        values2.put("json", json);

        //almacena los datos en el registro y agrega que se añadió un usuario
        insertInDB("REGISTRO", values2);
    }

    public void addRol(long userID, String rol) {
        //Log.i("rol-usuario", Long.toString(userID) + rol);
        ContentValues values = new ContentValues();

        values.put("usuario", userID);
        switch (rol){
            case "Cliente" :{
                values.put("rol", 2);
                break;
            }
            case "Proveedor":{
                values.put("rol", 1);
                break;
            }
        }

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("ROL_USUARIO", null, values);
        db.close();
    }

    public void deleteUsuario(long id){
        deleteFromDB("USUARIO", "Cedula", String.valueOf(id));

        //Agrega al registro que se eliminto el usuario
        ContentValues values = new ContentValues();
        values.put("tipo", "DELETE");
        values.put("json", "/Cliente/{"+id+"}");
        insertInDB("REGISTRO", values);
    }

    public void updateUsuario(Usuario usuario){
        ContentValues values = new ContentValues();

        values.put("Nombre", usuario.Nombre);
        values.put("Apellido", usuario.Apellido);
        values.put("Lugar_de_Residencia", usuario.Lugar_de_Residencia);
        values.put("Fecha_de_Nacimiento", usuario.Fecha_de_Nacimiento);
        values.put("Telefono", usuario.Telefono);

        updateFromDB("USUARIO", values, "Cedula="+usuario.Cedula);
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
        values.put("Exento", prod.exento);
        values.put("Cantidad_Disponible", prod.cantidad);
        values.put("Precio", prod.precio);

        insertInDB("PRODUCTO", values);
    }

    public void deleteProducto(String nombre){
        deleteFromDB("PRODUCTO", "Nombre_Producto", "'"+ nombre+ "'");
    }

    public void updateProducto(Producto prod){
        ContentValues values = new ContentValues();

        values.put("Descripción", prod.descripcion);
        values.put("Exento", prod.exento);
        values.put("Cantidad_Disponible", prod.cantidad);
        values.put("Precio", prod.precio);

        updateFromDB("PRODUCTO", values, "Nombre_Producto = '"+ prod.Nombre_Producto+ "'");
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
    }

    public void deleteCategoria(String nombre){
        deleteFromDB("CATEGORIA", "Nombre", "'" + nombre + "'");
    }

    public void updateCategoria(Categoria categoria){
        ContentValues values = new ContentValues();

        values.put("Descripcion", categoria.Descripcion);

        updateFromDB("CATEGORIA", values, "Nombre= '"+categoria.Nombre + "'");
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
    }

    public void addContiene(Contiene contiene){
        ContentValues values = new ContentValues();

        values.put("Cantidad", contiene.Cantidad);
        values.put("Nombre_Producto", contiene.Nombre_Producto);
        values.put("Id_Pedido", contiene.Id_Pedido);

        insertInDB("CONTIENE", values);
    }

    public void deletePedido(long id){
        deleteFromDB("PEDIDO", "Id_Pedido", String.valueOf(id));
    }

    public void updatePedido(Pedido pedido){
        ContentValues values = new ContentValues();

        values.put("Telefono_Preferido", pedido.Telefono_Preferido);

        updateFromDB("PEDIDO", values, "Id_Pedido="+ pedido.Id_Pedido);
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

    public void clearDatabase(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        db.execSQL(clearDBQuery);
    }

    //Método inserta una nueva fila en la tabla deseada
    public void insertInDB(String table, ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(table, null, values);
        db.close();
    }

    //Método elimina un registro de la tabla deseada
    public void deleteFromDB(String table, String propiedad, String value){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, propiedad+"="+value, null);
        db.close();
    }

    public void updateFromDB(String table, ContentValues values, String upFilter){
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(table, values, upFilter, null);
        db.close();
    }

    public Cursor getRowFromDB(String table, String row, String id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + table + " WHERE " + row + " = " + id;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {cursor.moveToFirst();}

        db.close();

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

        db.close();
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

        db.close();
        return categorias;
    }

    public List<String> getRolesUsuario(long userID){
        List<String> roles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //String selectQuery = "SELECT R.nombre as name FROM ((ROL as R JOIN ROL_USUARIO as RU ON R.Id_rol = RU.rol) " +
        //        "JOIN USUARIO as U ON U.Cedula = RU.usuario) WHERE U.Cedula =" + Long.toString(userID) + ";";

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

        db.close();
        return roles;
    }

    public void SyncDB(){
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM REGISTRO";
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //Revisa que tipo de query es
                if(cursor.getString(cursor.getColumnIndex("tipo")) == "DELETE"){
                    //HACE UN DELETE
                    //Acá llama al restful api para actualizar la DB con el json de
                    // String json = cursor.getString(cursor.getColumnIndex("json"));
                }
                else if(cursor.getString(cursor.getColumnIndex("tipo")) == "POST"){
                    //HACE UN POST
                }
                else{
                    //HACE UN PUT
                }
                //Elimina el query del registro porque ya se actualizó en la DB
                deleteFromDB("REGISTRO", "id_Registro", cursor.getString(cursor.getColumnIndex("id_Registro")));
            } while (cursor.moveToNext());
        }

        db.close();
    }
}
