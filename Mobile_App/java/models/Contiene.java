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
}
