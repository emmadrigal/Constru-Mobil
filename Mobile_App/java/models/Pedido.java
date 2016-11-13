package prueba.appprueba.models;

import java.util.LinkedList;
import java.util.List;

public class Pedido {
    public long Id_Pedido;
    public long Cedula_Cliente;
    public long Id_Sucursal;
    public int Telefono_Preferido;
    public String Hora_de_Creación;

    public List<Producto> productos = new LinkedList<Producto>();
}
