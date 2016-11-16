package models;

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
