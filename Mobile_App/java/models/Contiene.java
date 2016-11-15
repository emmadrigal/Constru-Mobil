package prueba.appprueba.models;

/**
 * Created by kquiros96 on 13/11/16.
 */

public class Contiene {
    public long id_Contiene;
    public String Nombre_Producto;
    public long Id_Pedido;
    public int Cantidad;

    public Contiene(){}
    public Contiene(String nProducto, long idPedido, int cant){
        this.Nombre_Producto = nProducto;
        this.Id_Pedido = idPedido;
        this.Cantidad = cant;
    }

    public void setId_Contiene (long id_contiene){
        this.id_Contiene = id_contiene;
    }

    public void setNombre_Producto(String nombre_producto){
        this.Nombre_Producto = nombre_producto;
    }

    public void setId_Pedido(long id_pedido){
        this.Id_Pedido = id_pedido;
    }

    public void setCantidad(int cantidad){
        this.Cantidad = cantidad;
    }

    public String getNombre_Producto(){
        return Nombre_Producto;
    }

    public long getId_Pedido(){
        return Id_Pedido;
    }

    public int getCantidad(){
        return Cantidad;
    }
}
