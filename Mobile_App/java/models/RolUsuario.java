package prueba.appprueba.models;

/**
 * Created by kquiros96 on 13/11/16.
 */

public class RolUsuario {
    public long id_Rol_Usuario;
    public long usuario;
    public long rol;

    public RolUsuario(){}

    public RolUsuario(long idRol, long idUsuario, long rol){
        this.id_Rol_Usuario = idRol;
        this.usuario = idUsuario;
        this.rol = rol;
    }
}
