package prueba.appprueba.models;

import java.util.LinkedList;
import java.util.List;

public class Usuario {
    public long Cedula;
    public String Nombre;
    public String Apellido;
    public String Lugar_de_Residencia;
    public String Fecha_de_Nacimiento;
    public int Telefono;

    public List<String> rol= new LinkedList<String>();

    public Usuario(){}
    public Usuario(long cedula, String nombre, String appelidos, String residencia, String date, int telefono){
        this.Cedula = cedula;
        this.Nombre = nombre;
        this.Apellido = appelidos;
        this.Lugar_de_Residencia = residencia;
        this.Fecha_de_Nacimiento = date;
        this.Telefono = telefono;
    }
};
