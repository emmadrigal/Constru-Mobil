package models;

import java.util.LinkedList;
import java.util.List;

public class Usuario {
    public long cedula;
    public String nombre;
    public String apellidos;
    public short penalizacion;//Deberiamos implementar esto o que solo lo haga EPATEC?
    public String residencia;
    public String date;
    public int Telefono;

    public List<String> rol= new LinkedList<String>();
}
