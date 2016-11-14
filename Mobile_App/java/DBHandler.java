import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "costrumovil";
    private static final int DB_SCHEME_VERSION = 1;

    private static final String SQL_CREATE_TABLE_USUARIO = "CREATE TABLE USUARIO" + "(" +
            "Cedula bigint NOT NULL PRIMARY KEY, " +
            "Nombre varchar(max) NOT NULL, " +
            "Apellido varchar(max) NOT NULL, " +
            "Lugar_de_Residencia varchar(max), " +
            "Fecha_de_Nacimiento date, " +
            "Telefono int" +
            ");";

    private static final String SQL_CREATE_TABLE_PRODUCTO = "CREATE TABLE PRODUCTO" + "(" +
            "Nombre_Producto varchar(30) NOT NULL PRIMARY KEY, " +
            "Id_Sucursal bigint FOREIGN KEY REFERENCES SUCURSAL(Id_Sucursal), " +
            "Cedula_Provedor bigint FOREIGN KEY REFERENCES USUARIO(Cedula), " +
            "Nombre_Categoría varchar(50) FOREIGN KEY REFERENCES CATEGORIA(Nombre), " +
            "Descripción varchar(max), " +
            "Exento bit, " +
            "Cantidad_Disponible int NOT NULL, " +
            "Precio int);";

    private static final String SQL_CREATE_TABLE_CATEGORIA = "CREATE TABLE CATEGORIA" + "(" +
            "Nombre varchar (50) NOT NULL PRIMARY KEY, " +
            "Descripcion varchar(max)" +
            ");";

    private static final String SQL_CREATE_TABLE_PEDIDO = "CREATE TABLE PEDIDO" + "(" +
            "Id_Pedido bigint NOT NULL AUTOINCREMENT PRIMARY KEY, " +
            "Cedula_Cliente bigint FOREIGN KEY REFERENCES USUARIO(Cedula), " +
            "Id_Sucursal bigint FOREIGN KEY REFERENCES SUCURSAL(Id_Sucursal), " +
            "Telefono_Preferido int NOT NULL, " +
            "Hora_de_Creación datetime NOT NULL" +
            ");";

    private static final String SQL_CREATE_TABLE_SUCURSAL = "CREATE TABLE SUCURSAL" + "("
            + "Id_Sucursal LONG PRIMARY KEY NOT NULL"
            + ");";

    private static final String SQL_CREATE_TABLE_ROL_USUARIO = "CREATE TABLE ROL_USUARIO( " +
            "id_Rol_Usuario bigint AUTOINCREMENT PRIMARY KEY, " +
            "usuario bigint FOREIGN KEY REFERENCES USUARIO(Cedula), " +
            "rol bigint FOREIGN KEY REFERENCES ROL(Id_rol));";

    private static final String SQL_CREATE_TABLE_EMPLEADOSUCURSAL = "CREATE TABLE EmpleadoSucursal(" +
            "Id_EmpleadoSucursal bigint NOT NULL AUTOINCREMENT PRIMARY KEY, " +
            "id_empleado bigint FOREIGN KEY REFERENCES USUARIO(Cedula), " +
            "id_sucursal bigint FOREIGN KEY REFERENCES SUCURSAL(Id_Sucursal));";

    private static final String SQL_CREATE_TABLE_ROL = "CREATE TABLE ROL (" +
            "Id_rol bigint PRIMARY KEY AUTOINCREMENT, " +
            "nombre varchar(max));";

    private static final String SQL_CREATE_TABLE_CONTIENE = "CREATE TABLE CONTIENE (" +
            "id_Contiene bigint PRIMARY KEY AUTOINCREMENT, " +
            "Nombre_Producto varchar(30) FOREIGN KEY REFERENCES PRODUCTO(Nombre_Producto), " +
            "Id_Pedido bigint FOREIGN KEY REFERENCES PEDIDO(Id_Pedido), " +
            "Cantidad int);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USUARIO);
        db.execSQL(SQL_CREATE_TABLE_PRODUCTO);
        db.execSQL(SQL_CREATE_TABLE_CATEGORIA);
        db.execSQL(SQL_CREATE_TABLE_PEDIDO);
        db.execSQL(SQL_CREATE_TABLE_SUCURSAL);
        db.execSQL(SQL_CREATE_TABLE_ROL_USUARIO);
        db.execSQL(SQL_CREATE_TABLE_ROL);
        db.execSQL(SQL_CREATE_TABLE_CONTIENE);
        db.execSQL(SQL_CREATE_TABLE_EMPLEADOSUCURSAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

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
    }

    public void deleteUsuario(long id){
        deleteFromDB("USUARIO", "Cedula", String.valueOf(id));
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

        Usuario usuario = new Usuario();
        usuario.setCedula(id);
        usuario.setApellido(cursor.getString(cursor.getColumnIndex("Apellido")));
        usuario.setNombre(cursor.getString(cursor.getColumnIndex("Nombre")));
        usuario.setLugar_de_Residencia(cursor.getString(cursor.getColumnIndex("Lugar_de_Residencia")));
        usuario.setFecha_de_Nacimiento(cursor.getString(cursor.getColumnIndex("Fecha_de_Nacimiento")));
        usuario.setTelefono(cursor.getInt(cursor.getColumnIndex("Telefono")));

        return usuario;
    }

    //Métodos para la tabla producto
    public void addProducto(Producto prod){
        ContentValues values = new ContentValues();

        values.put("Nombre_Producto", prod.Nombre_Producto);
        values.put("Id_Sucursal", prod.Id_Sucursal);
        values.put("Cedula_Proveedor", prod.Cedula_Proveedor);
        values.put("Nombre_Categoria", prod.Nombre_Categoria);
        values.put("descripcion", prod.descripcion);
        values.put("exento", prod.exento);
        values.put("cantidad", prod.cantidad);
        values.put("precio", prod.precio);

        insertInDB("PRODUCTO", values);
    }

    public void deleteProducto(String nombre){
        deleteFromDB("PRODUCTO", "Nombre_Producto", nombre);
    }

    public void updateProducto(Producto prod){
        ContentValues values = new ContentValues();

        values.put("Id_Sucursal", prod.Id_Sucursal);
        values.put("Cedula_Proveedor", prod.Cedula_Proveedor);
        values.put("Nombre_Categoria", prod.Nombre_Categoria);
        values.put("descripcion", prod.descripcion);
        values.put("exento", prod.exento);
        values.put("cantidad", prod.cantidad);
        values.put("precio", prod.precio);

        updateFromDB("PRODUCTO", values, "Nombre_Producto="+ prod.Nombre_Producto);
    }

    public Producto getProducto(String nombre){
        Cursor cursor = getRowFromDB("PRODUCTO", "Nombre_Producto", nombre);

        Producto producto = new Producto();
        producto.setNombre_Producto(nombre);
        producto.setId_Sucursal(cursor.getLong(cursor.getColumnIndex("Id_Sucursal")));
        producto.setCedula_Proveedor(cursor.getLong(cursor.getColumnIndex("Cedula_Proveedor")));
        producto.setNombre_Categoria(cursor.getString(cursor.getColumnIndex("Nombre_Categoria")));
        producto.setDescripcion(cursor.getString(cursor.getColumnIndex("descripcion")));
        producto.setExento(cursor.getInt(cursor.getColumnIndex("exento")));
        producto.setCantidad(cursor.getInt(cursor.getColumnIndex("cantidad")));
        producto.setPrecio(cursor.getInt(cursor.getColumnIndex("precio")));

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
        deleteFromDB("CATEGORIA", "Nombre", nombre);
    }

    public void updateCategoria(Categoria categoria){
        ContentValues values = new ContentValues();

        values.put("Descripcion", categoria.Descripcion);

        updateFromDB("CATEGORIA", values, "Nombre="+categoria.Nombre);
    }
    
    private Categoria getCategoria(String nombre){
        Cursor cursor = getRowFromDB("CATEGORIA", "Nombre", nombre);
        
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setDescripcion(cursor.getString(cursor.getColumnIndex("Descripcion")));
        
        return categoria;
    }

    //Métodos para tabla pedido
    public void addPedido(Pedido pedido){
        ContentValues values = new ContentValues();

        values.put("Id_Pedido", pedido.Id_Pedido);
        values.put("Cedula_Cliente", pedido.Cedula_Cliente);
        values.put("Id_Sucursal", pedido.Id_Sucursal);
        values.put("Telefono_Preferido", pedido.Telefono_Preferido);
        values.put("Hora_de_Creación", pedido.Hora_de_Creación);

        insertInDB("PEDIDO", values);
    }

    public void deletePedido(long id){
        deleteFromDB("PEDIDO", "Id_Pedido", String.valueOf(id));
    }

    public void updatePedido(Pedido pedido){
        ContentValues values = new ContentValues();

        values.put("Cedula_Cliente", pedido.Cedula_Cliente);
        values.put("Id_Sucursal", pedido.Id_Sucursal);
        values.put("Telefono_Preferido", pedido.Telefono_Preferido);
        values.put("Hora_de_Creación", pedido.Hora_de_Creación);

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

        String selectQuery = "SELECT  * FROM " + table + " WHERE "
                + row + " = " + id;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {cursor.moveToFirst();}

        return cursor;
    }
}
