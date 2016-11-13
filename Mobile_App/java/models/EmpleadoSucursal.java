package prueba.appprueba.models;

/**
 * Created by kquiros96 on 13/11/16.
 */

public class EmpleadoSucursal {
    public long Id_EmpleadoSucursal;
    public long id_empleado;
    public long id_sucursal;

    public EmpleadoSucursal(){}
    public EmpleadoSucursal(long idEmp, long idSuc){
        this.id_empleado = idEmp;
        this.id_sucursal = idSuc;
    }
}
