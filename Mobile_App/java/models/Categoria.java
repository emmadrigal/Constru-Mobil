package models;

public class Categoria {
    public String Nombre;
    public String Descripcion;

    public void Categoria(){}
    public void Categoria(String nombre, String descripcion){
        this.Nombre = nombre;
        this.Descripcion = descripcion;
    }
    public void setNombre(String nombre){
        this.Nombre = nombre;
    }
    public void setDescripcion(String descripcion){
        this.Descripcion = descripcion;
    }
}
