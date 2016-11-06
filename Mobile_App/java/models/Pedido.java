package models;

import java.util.LinkedList;
import java.util.List;

public class Pedido {
    public long Id_Pedido;
    public long cedula;
    public long id_sucursal;
    public int telefono;
    public String hora;

    public List<Producto> productos = new LinkedList<Producto>();
}
