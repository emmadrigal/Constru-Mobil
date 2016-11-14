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

    public void setId_Pedido(long id){
        this.Id_Pedido = id;
    }

    public void setCedula_Cliente(long id){
        this.Cedula_Cliente = id;
    }

    public void setId_Sucursal(long id){
        this.Id_Sucursal = id;
    }

    public void setTelefono_Preferido(int telefono_Preferido){
        this.Telefono_Preferido = telefono_Preferido;
    }

    public void setHora_de_Creación(String hora_de_Creación){
        this.Hora_de_Creación = hora_de_Creación;
    }
}
