package prueba.appprueba.models;


public class Producto {
    public String Nombre_Producto;
    public long Id_Sucursal;
    public long Cedula_Proveedor;
    public String Nombre_Categoria;
    public String descripcion;
    public int exento;
    public int cantidad;
    public int precio;

    public Producto(){}

    public Producto(String nombre, long idSuc, long cedProv, String nCat, String descr, int exento, int cant, int precio){
        this.Nombre_Producto = nombre;
        this.Id_Sucursal = idSuc;
        this.Cedula_Proveedor = cedProv;
        this.Nombre_Categoria = nCat;
        this.descripcion = descr;
        this.exento = exento;
        this.cantidad = cant;
        this.precio = precio;
    }

    public void setNombre_Producto(String nombre){
        this.Nombre_Producto = nombre;
    }

    public void setId_Sucursal(long id_Sucursal){
        this.Id_Sucursal = id_Sucursal;
    }

    public void setCedula_Proveedor(long cedula_Proveedor){
        this.Cedula_Proveedor = cedula_Proveedor;
    }

    public void setNombre_Categoria(String nombre_Categoria){
        this.Nombre_Categoria = nombre_Categoria;
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    public void setExento(int exento){
        this.exento = exento;
    }

    public void setCantidad(int cantidad){
        this.cantidad = cantidad;
    }

    public void setPrecio(int precio){
        this.precio = precio;
    }
}
