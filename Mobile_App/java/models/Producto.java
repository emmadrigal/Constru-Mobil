package prueba.appprueba.models;


public class Producto {
    public String Nombre_Producto;
    public long Id_Sucursal;
    public long Cedula_Proveedor;
    public String Nombre_Categoria;
    public String descripcion;
    public boolean exento;
    public int cantidad;
    public int precio;

    public Producto(){}

    public Producto(String nombre, long idSuc, long cedProv, String nCat, String descr, boolean exento, int cant, int precio){
        this.Nombre_Producto = nombre;
        this.Id_Sucursal = idSuc;
        this.Cedula_Proveedor = cedProv;
        this.Nombre_Categoria = nCat;
        this.descripcion = descr;
        this.exento = exento;
        this.cantidad = cant;
        this.precio = precio;
    }
}
