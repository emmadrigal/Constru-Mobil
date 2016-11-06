using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace EPATEC.Models
{
    public class EmpleadoSucursal
    {
        public long Id_EmpleadoSucursal { get; set; }
        public long Id_Empleado { get; set; }
        public long Id_Sucursal { get; set; }
    }
}